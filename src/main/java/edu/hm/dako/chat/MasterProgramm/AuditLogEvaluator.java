package edu.hm.dako.chat.MasterProgramm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Wertet alle AuditLog-Saetze aus dem Protokoll aus.
 * 
 * @author OMMS
 */
public class AuditLogEvaluator {
	
	//Enthaelt alle Zeilen des AuditLogs
	private ArrayList<String> auditLogLineList = new ArrayList<String>();
	
	//Enthaelt alle Usernamen zur Zaehlung
	private ArrayList<String> usernames = new ArrayList<String>();
	
	//Ausgewertete Daten
	private int countAllPDU;
	private int countLoginEvents;
	private int countChatEvents;
	private int countLogoutEvents;
	private int countClients;
	private Date firstDate;
	private Date lastDate;
	
	/**
	 * Erstellt ein Klasse mit den auszuwertenden AuditLog-Saetzen.
	 * 
	 * @param auditLogLineList
	 */
	public AuditLogEvaluator() {
		usernames = new ArrayList<String>();
		
		countAllPDU = 0;
		countLoginEvents = 0;
		countChatEvents = 0;
		countLogoutEvents = 0;
		countClients = 0;
	}
	
	/**
	 * Updatet die auszuwertenden AuditLog-Saetze.
	 * 
	 * @param auditLogLineList
	 */
	public void update(ArrayList<String> auditLogLineList) {
		this.auditLogLineList = auditLogLineList;
		usernames = new ArrayList<String>();
		
		countAllPDU = 0;
		countLoginEvents = 0;
		countChatEvents = 0;
		countLogoutEvents = 0;
		countClients = 0;
	}
	
	/**
	 * Übernimmt die Auswertung.
	 */
	public void evaluate() {
		//For-schleife die jeden Datensatz durchgeht und auswertet
		for (String line : auditLogLineList) {
			
			countAllPDU = countAllPDU + 1;
			
			String[] lineAsList = line.split(";");
			
			String event = lineAsList[0];
			
			if (event.equalsIgnoreCase("login ")) {
				countLoginEvents = countLoginEvents + 1;
			} else if (event.equalsIgnoreCase("logout")) {
				countLogoutEvents = countLogoutEvents + 1;
			} else if (event.equalsIgnoreCase("chat  ")) {
				countChatEvents = countChatEvents + 1;
			}
			
			String user = lineAsList[1];
			
			if (!usernames.contains(user)) {
				countClients = countClients + 1;
				usernames.add(user);
			}
			
			String dateAsString = lineAsList[4];
			
			SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
			Date date = null;
			
			try {
				date = format.parse(dateAsString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if (firstDate != null) {
				if (firstDate.after(date)) {
					firstDate = date;
				}
			} else {
				firstDate = date;
			}
			
			if (lastDate != null) {
				if (lastDate.before(date)) {
					lastDate = date;
				}
			} else {
				lastDate = date;
			}
		}
	}
	
	/*
	 * GETTER
	 */
	public int getCountAllPDU() {
		return countAllPDU;
	}

	public int getCountLoginEvents() {
		return countLoginEvents;
	}
	
	public int getCountChatEvents() {
		return countChatEvents;
	}

	public int getCountLogoutEvents() {
		return countLogoutEvents;
	}

	public int getCountClients() {
		return countClients;
	}

	public Date getLastDate() {
		return lastDate;
	}

	public Date getFirstDate() {
		return firstDate;
	}
}
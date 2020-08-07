package edu.hm.dako.chat.AuditLogServer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import edu.hm.dako.chat.common.AuditLogPDU;

/**
 * Hilfklasse fuer das Protokollieren der AuditLogPDUs.
 * 
 * @author OMMS
 */

public class AuditLogFileWriter {
	
	//Protokoll-Datei
	private static String file = "AuditLogFile.txt";
	
	/**
	 * Schreibt einen String in das Protokoll.
	 * 
	 * @param auditLog
	 */
	private static void write(String auditLog) {
	    PrintWriter pWriter = null;
	    try {
	        pWriter = new PrintWriter(new FileWriter(file, true), true);
	        pWriter.println(auditLog);     
	    } catch (IOException ioe) {
	        ioe.printStackTrace();
	    } finally {
	        if (pWriter != null){
	            pWriter.close();
	        }
	    }
    }
	
	/**
	 * Formt eine AuditLogPDU in einen String und protokolliert diesen.
	 * 
	 * 1 = Event
	 * 2 = User
	 * 3 = ClientThread
	 * 4 = ServerThread
	 * 5 = Datum
	 * 6 = Message
	 * 
	 * @param pdu
	 */
	public static void writeAuditLogPDU(AuditLogPDU pdu) {
		Date dateAndTime = new Date(pdu.getAuditTime());
		
		String s = "" + pdu.getPduType() + ";" + pdu.getUserName() + ";" + pdu.getClientThreadName()
			+ ";" + pdu.getServerThreadName() + ";" + dateAndTime.toString() + ";" + pdu.getMessage();
		write(s);
	}
}
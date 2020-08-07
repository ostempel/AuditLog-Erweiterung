package edu.hm.dako.chat.MasterProgramm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Hilfsklasse zum lesen des AuditLogs.
 * 
 * @author OMMS
 */
public class AuditLogFileReader {
	
	//Protokoll-Datei
	private static String file = "AuditLogFile.txt";
	
	/**
	 * Liest alle Zeilen der Protokolldatei ein und gib sie in einer Liste zurueck.
	 * 
	 * @return ArrayList<String> mit allen Zeilen
	 */
	public static ArrayList<String> getAllLinesAsList() {
		BufferedReader br;
		String line;
		
		if (!new File(file).exists()) {
			return new ArrayList<String>();
		}
		
		ArrayList<String> list = new ArrayList<String>();
		try {
			br = new BufferedReader (new FileReader (file));
			while((line = br.readLine()) != null) {
			    list.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
}
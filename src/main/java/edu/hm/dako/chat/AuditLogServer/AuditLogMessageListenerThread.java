package edu.hm.dako.chat.AuditLogServer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.common.AuditLogPDU;
import edu.hm.dako.chat.common.ExceptionHandler;
import edu.hm.dako.chat.connection.Connection;
import edu.hm.dako.chat.connection.ConnectionTimeoutException;
import edu.hm.dako.chat.connection.EndOfFileException;

/**
 * Thread der sich um die ankommenden AuditLogPDUs kuemmert und diese an den @see {@link AuditLogFileWriter} weiterleitert.
 * 
 * @author OMMS
 */
public class AuditLogMessageListenerThread extends Thread {
	
	private static Log log = LogFactory.getLog(AuditLogMessageListenerThread.class);
	
	private boolean finished;
	private Connection connection;
	
	public AuditLogMessageListenerThread(Connection con) {
		System.out.println("Thread wurde gestartet!");
		connection = con;
		finished = false;
	}
	
	@Override
	public void run() {
		while (finished == false) {
			AuditLogPDU receivedPdu = null;
			// Nach einer Minute wird geprueft, ob Client noch eingeloggt ist
			final int RECEIVE_TIMEOUT = 1200000;
	
			try {
				
				//PDU empfangen
				receivedPdu = (AuditLogPDU) connection.receive(RECEIVE_TIMEOUT);
				
			} catch (ConnectionTimeoutException e) {
				// Wartezeit beim Empfang abgelaufen, pruefen, ob der Client
				// ueberhaupt noch etwas sendet
				log.debug(
						"Timeout beim Empfangen, " + RECEIVE_TIMEOUT + " ms ohne Nachricht vom ChatServer!");
				finished = true;
				return;
				
			} catch (EndOfFileException e) {
				log.debug("End of File beim Empfang, vermutlich Verbindungsabbau des Partners!");
				finished = true;
				return;
	
			} catch (java.net.SocketException e) {
				log.error("Verbindungsabbruch beim Empfang der naechsten Nachricht vom ChatServer "
						+ getName());
				finished = true;
				return;
	
			} catch (Exception e) {
				log.error(
						"Empfang einer Nachricht fehlgeschlagen");
				ExceptionHandler.logException(e);
				e.printStackTrace();
			}
			
			if (receivedPdu == null) {
				return;
			}
			
			try {
				//Protokollieren der AuditLogPDU
				AuditLogFileWriter.writeAuditLogPDU(receivedPdu);
			} catch (Exception e) {
				log.error("Exception bei der Nachrichtenverarbeitung");
				e.printStackTrace();
				ExceptionHandler.logExceptionAndTerminate(e);
			}
		}
	}
}
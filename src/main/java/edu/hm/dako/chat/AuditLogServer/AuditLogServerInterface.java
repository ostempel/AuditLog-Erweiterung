package edu.hm.dako.chat.AuditLogServer;

/**
 * Einheitliche Schnittstelle f�r den AuditLog-Server.
 * 
 * @author OMMS
 */
public interface AuditLogServerInterface {

	/**
	 * Startet den Server.
	 */
	void start();

	/**
	 * Stoppt den Server.
	 *
	 * @throws Exception
	 */
	void stop() throws Exception;
}

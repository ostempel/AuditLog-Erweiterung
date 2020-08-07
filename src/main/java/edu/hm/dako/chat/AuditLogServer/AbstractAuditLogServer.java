package edu.hm.dako.chat.AuditLogServer;

import edu.hm.dako.chat.connection.Connection;
import edu.hm.dako.chat.connection.ServerSocketInterface;
/**
 * Abstrakte Oberklasse der unterschiedlichen Implementierungstypen.
 * 
 * @author OMMS
 */
public abstract class AbstractAuditLogServer implements AuditLogServerInterface {
	
	protected ServerSocketInterface socket;

	protected Connection connection;
	
	protected AuditLogMessageListenerThread messageListenerThread;
}

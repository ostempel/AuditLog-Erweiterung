package edu.hm.dako.chat.AuditLogServer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.udp.UdpServerSocket;

/**
 * UDP-Implementierung eines AuditLog-Servers.
 * 
 * @author OMMS
 */
public class AuditLogServerUdpImpl extends AbstractAuditLogServer {
	
	private static Log log = LogFactory.getLog(AuditLogServerUdpImpl.class);
	
	public AuditLogServerUdpImpl(UdpServerSocket socket) {
		super.socket = socket;
	}
	
	@Override
	public void start() {
		try {
			connection = socket.accept();
			System.out.println("AuditLogServer(UDP) wartet auf ankommende Nachrichten...");
			System.out.println(connection.getClass());
			socket.getClass();
			
			//Server starten
			messageListenerThread = new AuditLogMessageListenerThread(connection);
			messageListenerThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() throws Exception {
		Thread.currentThread().interrupt();
		
		// Serversocket schliessen
		socket.close();
		log.debug("Listen-Socket geschlossen");
		
		System.out.println("AuditLogServer(UDP) beendet sich");
	}
}
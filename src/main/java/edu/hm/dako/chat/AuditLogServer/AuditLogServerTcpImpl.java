package edu.hm.dako.chat.AuditLogServer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.common.ExceptionHandler;
import edu.hm.dako.chat.tcp.TcpServerSocket;
import javafx.concurrent.Task;

/**
 * TCP-Implementierung eines AuditLog-Servers.
 * 
 * @author OMMS
 */
public class AuditLogServerTcpImpl extends AbstractAuditLogServer {
	
	private static Log log = LogFactory.getLog(AuditLogServerTcpImpl.class);
	
	public AuditLogServerTcpImpl(TcpServerSocket socket) {
		super.socket = socket;
	}

	@Override
	public void start() {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				while (!Thread.currentThread().isInterrupted() && !socket.isClosed()) {
					try {
						// Auf ankommende Verbindungsaufbauwuensche warten
						System.out.println(
								"AuditLogServer(TCP) wartet auf Verbindungsanfragen von Clients...");
						
						connection = socket.accept();
						System.out.println("Neuer Verbindungsaufbauwunsch empfangen");
						
						//Server starten
						messageListenerThread = new AuditLogMessageListenerThread(connection);
						messageListenerThread.start();
						
					} catch (Exception e) {
						if (socket.isClosed()) {
							log.debug("Socket wurde geschlossen");
						} else {
							log.error(
									"Exception beim Entgegennehmen von Verbindungsaufbauwuenschen: " + e);
							ExceptionHandler.logException(e);
						}
					}
				}
				return null;
			}
		};
		
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
	}
	
	@Override
	public void stop() throws Exception {
		Thread.currentThread().interrupt();
		
		// Serversocket schliessen
		socket.close();
		log.debug("Listen-Socket geschlossen");
		
		System.out.println("AuditLogServer(TCP) beendet sich");
	}
}
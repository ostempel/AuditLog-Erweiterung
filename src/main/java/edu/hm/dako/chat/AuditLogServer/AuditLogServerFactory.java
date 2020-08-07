package edu.hm.dako.chat.AuditLogServer;

import edu.hm.dako.chat.tcp.TcpServerSocket;
import edu.hm.dako.chat.udp.UdpServerSocket;

/**
 * Uebernimmt die Konfiguration und Erzeugung von TCP-/UDP-Server.
 * 
 * @author OMMS
 */
public class AuditLogServerFactory {
	
	// Standardadresse
	static final String DEFAULT_AUDITLOGSERVER_NAME="localhost";
	static final int DEFAULT_AUDITLOGSERVER_PORT = 40001;

	// Standard-und Maximal-Puffergroessen in Byte
	static final int DEFAULT_SENDBUFFER_SIZE = 300000;
	static final int DEFAULT_RECEIVEBUFFER_SIZE = 300000;
	
	/**
	 * Erzeugt einen Server mit einem Tcp-Socket und gibt ihn zurueck.
	 * 
	 * @return AuditLogServerTcpImpl
	 * @throws Exception 
	 */
	public static AuditLogServerInterface getTcpServer() throws Exception {
		try {
			System.out.println("AuditLogServer (TCP) wird gestartet, Listen-Port: " + DEFAULT_AUDITLOGSERVER_PORT + ", Sendepuffer: "
					+ DEFAULT_SENDBUFFER_SIZE + ", Empfangspuffer: " + DEFAULT_RECEIVEBUFFER_SIZE);
			
			TcpServerSocket tcpServerSocket = new TcpServerSocket(DEFAULT_AUDITLOGSERVER_PORT, DEFAULT_SENDBUFFER_SIZE,
					DEFAULT_RECEIVEBUFFER_SIZE);
			return new AuditLogServerTcpImpl(tcpServerSocket);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	/**
	 * Erzeugt einen Server mit einem Udp-Socket und gibt ihn zurueck.
	 * 
	 * @return AuditLogServerUdpImpl
	 * @throws Exception 
	 */
	public static AuditLogServerInterface getUdpServer() throws Exception {
		System.out.println("AuditLogServer (UDP) wird gestartet, Listen-Port: " + DEFAULT_AUDITLOGSERVER_PORT + ", Sendepuffer: "
				+ DEFAULT_SENDBUFFER_SIZE + ", Empfangspuffer: " + DEFAULT_RECEIVEBUFFER_SIZE);
		
		try {
			UdpServerSocket udpServerSocket = new UdpServerSocket(DEFAULT_AUDITLOGSERVER_PORT, DEFAULT_SENDBUFFER_SIZE,
					DEFAULT_RECEIVEBUFFER_SIZE);
			return new AuditLogServerUdpImpl(udpServerSocket);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
}

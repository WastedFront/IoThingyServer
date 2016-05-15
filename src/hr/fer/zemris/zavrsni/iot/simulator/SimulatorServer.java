package hr.fer.zemris.zavrsni.iot.simulator;

import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;

import hr.fer.zemris.zavrsni.iot.utils.Message;

/**
 * SOmething
 * 
 * @author Nikola Presečki
 * @version 1.0
 */
public class SimulatorServer {

	/** Datagram socket for connection. */
	private DatagramSocket serverSocket;
	/** Output stream on which server will write its 'log' */
	private PrintStream logStream = System.out;

	/**
	 * Constructor with no arguments. It defines that default port for listening
	 * is 25000.
	 * 
	 * @throws Exception
	 *             if there is problem with server
	 */
	public SimulatorServer() throws Exception {
		this(20000);
	}

	/**
	 * Constructor with one argument.
	 * 
	 * @param port
	 *            port on which server will listen.
	 * @throws Exception
	 *             if there is problem with server
	 */
	public SimulatorServer(int port) throws Exception {
		serverSocket = new DatagramSocket(port);
		startServer();
	}

	/**
	 * Method for starting server. It is infinite loop
	 * 
	 * @throws IOException
	 *             if there is problem with {@link ServerSocket}.
	 */
	private void startServer() throws IOException {
		logStream.println("Simulator server started listening on port: " + serverSocket.getLocalPort() + "\n");
		byte[] receiveData = new byte[1024];
		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			String msg = new String(receivePacket.getData());
			Message message = Message.parseSimulatorMessage(msg);
			SimulatorMsgList.getInstance().addMessage(message);
		}
	}

	/**
	 * Method for sending datagram packet to given adress and port.
	 * 
	 * @param ipAddress
	 *            ip adress of server
	 * @param port
	 *            port on which server is listening
	 * @param data
	 *            data which will be send
	 * @throws IOException
	 *             if there is problem with sending packet
	 */
	public static void sendMessage(String ipAddress, int port, byte[] data) throws IOException {
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName(ipAddress);
		DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, port);
		clientSocket.send(sendPacket);
		clientSocket.close();
	}
}

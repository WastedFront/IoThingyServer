package hr.fer.zemris.zavrsni.iot.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import hr.fer.zemris.zavrsni.iot.utils.Message;
import hr.fer.zemris.zavrsni.iot.utils.MyUtils;

/**
 * Server for client TCP connection. It listen for new connection on given port.
 * When there is new client, it makes new thread for handling client. If there
 * is some data which needs to be send to client, it uses same connection for
 * response. Default port for listening is 25000. All received messages are
 * stored in {@code ClientMsgList}. If there is no data for client, it sends
 * "IDLE".
 * 
 * @author Nikola Presečki
 * @version 1.0
 */
public class ClientServer {

	/** Server socket for connection. */
	private ServerSocket serverSocket;
	/** Counter of active workers. */
	private int numOfWorkers = 0;
	/** Output stream on which server will write its 'log' */
	private PrintStream logStream = System.out;
	/** Output stream on which server will write errors */
	private PrintStream errorStream = System.err;

	/**
	 * Constructor with no arguments. It defines that default port for listening
	 * is 25000.
	 * 
	 * @throws Exception
	 *             if there is problem with server
	 */
	public ClientServer() throws Exception {
		this(25000);
	}

	/**
	 * Constructor with one argument.
	 * 
	 * @param port
	 *            port on which server will listen.
	 * @throws Exception
	 *             if there is problem with server
	 */
	public ClientServer(int port) throws Exception {
		serverSocket = new ServerSocket(port);
		startServer();
	}

	/**
	 * Method for starting server. It is infinite loop
	 * 
	 * @throws IOException
	 *             if there is problem with {@link ServerSocket}.
	 * @throws InterruptedException
	 *             if there is problem while sleeping when there is too much
	 *             workers
	 */
	private void startServer() throws IOException, InterruptedException {
		logStream.println("Server started listening on port: " + serverSocket.getLocalPort() + "\n");
		while (true) {
			Socket socket = serverSocket.accept();
			while (numOfWorkers > 1000)
				Thread.sleep(1000);
			new Thread(new ClientHandler(socket)).start();
		}
	}

	/**
	 * Class which will handler communication with client (thing).
	 * 
	 * @author Nikola Presečki
	 * @version 1.0
	 *
	 */
	private class ClientHandler implements Runnable {

		/**
		 * Socket for handling connection.
		 */
		private Socket socket;

		/**
		 * Constructor with one argument.
		 * 
		 * @param socket
		 *            socket on which connection is established
		 */
		public ClientHandler(Socket socket) {
			this.socket = socket;
			++numOfWorkers;
		}

		@Override
		public void run() {
			printDashes();
			System.out.println("New connection accepted " + socket.getInetAddress() + ":" + socket.getPort()
					+ "\nWorker number:" + numOfWorkers);
			try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					PrintWriter output = new PrintWriter(socket.getOutputStream(), true);) {
				// read message
				String message = input.readLine();
				if (message == null)
					return;
				if (message == "")
					return;
				// parse received message
				Message tcpMsg = Message.parseClientMessage(message);
				// print message
				logStream.println("**\n" + tcpMsg.toString() + "**");
				// store message
				ClientMsgList.getInstance().addMessage(tcpMsg);
				// check if there is some return message for this client
				String returnMsg = MyUtils.getReturnClientMessage(tcpMsg.getSrcID());
				// print return message
				logStream.println("++\nReturn message:\n" + returnMsg + "++");
				output.println(returnMsg);
				output.flush();
			} catch (IOException e) {
				errorStream.println(e.toString());
			} finally {
				try {
					socket.close();
					logStream.println("Connection closed by client");
				} catch (IOException e) {
					errorStream.println(e.toString());
				} finally {
					printDashes();
					--numOfWorkers;
				}
			}
		}

		/**
		 * Method for printing 80 dashes in a single line.
		 */
		private void printDashes() {
			for (int i = 0; i < 80; ++i)
				logStream.print('-');
			logStream.println();
		}
	}
}

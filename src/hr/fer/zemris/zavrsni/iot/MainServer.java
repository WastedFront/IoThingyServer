package hr.fer.zemris.zavrsni.iot;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import hr.fer.zemris.zavrsni.iot.client.ClientMsgList;
import hr.fer.zemris.zavrsni.iot.client.ClientServer;
import hr.fer.zemris.zavrsni.iot.simulator.SimulatorMsgList;
import hr.fer.zemris.zavrsni.iot.simulator.SimulatorServer;
import hr.fer.zemris.zavrsni.iot.storage.MessagesStoreClass;

/**
 * Class containg main method which runs proper servers. There are 4 command
 * line arguments needed:
 * 
 * <pre>
 * 1. port for the client server on which it will listen for incoming TCP messages
 * 2. port for simulator server on which it will listen for incoming UDP messages
 * 3. IP address of simulator too which client messages will be relayed
 * 4. port of simulator too which client messages will be relayed
 * </pre>
 * 
 * Main class uses {@code ClientServer} amd {@code SimulatorServer}. Both
 * servers have their own threads for running (implementation of those servers
 * blocks thread which uses them). There is also thread which sends data to
 * simulator.
 * 
 * @author Nikola Presečki
 * @version 1.0
 */
public class MainServer {

	/** Name of the xml file for client messages. */
	private static final String FILENAME_CLIENT = "client_msgs.xml";
	/** Name of the xml file for simulator messages. */
	private static final String FILENAME_SIMULATOR = "simulator_msgs.xml";
	/** */

	/**
	 * Store messages before this program is terminated.
	 */
	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					MessagesStoreClass.storeClientMessages(FILENAME_CLIENT, ClientMsgList.getInstance().getMessages());
					MessagesStoreClass.storeClientMessages(FILENAME_SIMULATOR,
							SimulatorMsgList.getInstance().getMessages());
				} catch (ParserConfigurationException | TransformerException e) {
					System.err.println(e.toString());
				}
			}
		});
	}

	/**
	 * Main method which is called first.
	 * 
	 * @param args
	 *            command line arguments, first argument is port number for
	 *            server.
	 */
	public static void main(String args[]) {
		int clientPort = 25000, simulatorRecievePort = 20000, simulatorSendPort = 9876;
		String simulatorIPAddress = "127.0.0.1";
		if (args.length == 4) {
			try {
				clientPort = Integer.parseInt(args[0]);
				simulatorRecievePort = Integer.parseInt(args[1]);
				simulatorIPAddress = args[2];
				simulatorSendPort = Integer.parseInt(args[3]);
			} catch (Exception e) {
				System.err.println("Error while parsing command line argument\n" + e.toString());
				return;
			}
		} else {
			System.err.println("Invalid number of command line arguments!");
			return;
		}
		try {
			// get stored client and simulator messages
			ClientMsgList.getInstance().addAllMessages(MessagesStoreClass.readStoredClientMessages(FILENAME_CLIENT));
			SimulatorMsgList.getInstance()
					.addAllMessages(MessagesStoreClass.readStoredClientMessages(FILENAME_SIMULATOR));
			// start all threads that you need
			ClientServerThread clientServerThread = new ClientServerThread(clientPort);
			SimulatorServerThread simulatorServerThread = new SimulatorServerThread(simulatorRecievePort);
			SendToSimulatorThread sendToSimulatorThread = new SendToSimulatorThread(simulatorIPAddress,
					simulatorSendPort);
			clientServerThread.start();
			simulatorServerThread.start();
			sendToSimulatorThread.start();
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * Thread which implements client server.
	 * 
	 * @author Nikola Presečki
	 * @version 1.0
	 *
	 */
	private static class ClientServerThread extends Thread {
		/** Server port */
		private int port;

		/**
		 * Constructor.
		 * 
		 * @param port
		 *            server port
		 */
		public ClientServerThread(int port) {
			this.port = port;
		}

		@Override
		public void run() {
			super.run();
			try {
				new ClientServer(port);
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
	}

	/**
	 * Thread which implements simulator server.
	 * 
	 * @author Nikola Presečki
	 * @version 1.0
	 *
	 */
	private static class SimulatorServerThread extends Thread {
		/** Server port */
		private int port;

		/**
		 * Constructor.
		 * 
		 * @param port
		 *            server port
		 */
		public SimulatorServerThread(int port) {
			this.port = port;
		}

		@Override
		public void run() {
			super.run();
			try {
				new SimulatorServer(port);
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
	}

	/**
	 * Thread which implements communication with simulator.
	 * 
	 * @author Nikola Presečki
	 * @version 1.0
	 *
	 */
	private static class SendToSimulatorThread extends Thread {
		/** Simulator port */
		private int port;
		/** Simulator IP address. */
		private String ipAddress;

		/**
		 * Constructor.
		 * 
		 * @param ipAddress
		 *            simulator IP address
		 * @param port
		 *            simulator port
		 */
		public SendToSimulatorThread(String ipAddress, int port) {
			this.ipAddress = ipAddress;
			this.port = port;
		}

		@Override
		public void run() {
			super.run();
			try {
				while (true) {
					if (ClientMsgList.getInstance().isEmpty()) {
						Thread.sleep(2000);
						continue;
					}
					byte[] message = ClientMsgList.getInstance().popFirstMessage().makeReturnMessageForSimulator();
					SimulatorServer.sendMessage(ipAddress, port, message);
				}
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
	}
}

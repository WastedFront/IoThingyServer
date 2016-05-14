package hr.fer.zemris.zavrsni.iot;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import hr.fer.zemris.zavrsni.iot.client.ClientMsgList;
import hr.fer.zemris.zavrsni.iot.client.ClientServer;
import hr.fer.zemris.zavrsni.iot.storage.MessagesStoreClass;

/**
 * Some fancy documentation.
 * 
 * @author Nikola Presečki
 * @version 1.0
 */
public class MainServer {

	/** Name of the xml file for client messages. */
	private static final String FILENAME_CLIENT = "client_msgs.xml";
	/** Name of the xml file for simulator messages. */
	private static final String FILENAME_SIMULATOR = "client_msgs.xml";

	/**
	 * Store messages before this program is terminated.
	 */
	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					MessagesStoreClass.storeClientMessages(FILENAME_CLIENT, ClientMsgList.getInstance().getMessages());
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
		int port = 25000;
		if (args.length == 1) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (Exception e) {
				System.err.println("Error while parsing command line argument\n" + e.toString());
			}
		}
		try {
			// učitaj spremljene TCP i UDP poruke u odgovarajuće kolekcije
			ClientMsgList.getInstance().addAllMessages(MessagesStoreClass.readStoredClientMessages(FILENAME_CLIENT));
			new ClientServer(port);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
}

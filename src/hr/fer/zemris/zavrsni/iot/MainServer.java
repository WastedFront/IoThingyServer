package hr.fer.zemris.zavrsni.iot;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import hr.fer.zemris.zavrsni.iot.client.ClientServer;
import hr.fer.zemris.zavrsni.iot.storage.ClientMsgsStore;

/**
 * Some fancy documentation.
 * 
 * @author Nikola Presečki
 * @version 1.0
 */
public class MainServer {

	/**
	 * Store messages before this program is terminated.
	 */
	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					ClientMsgsStore.storeClientMessages();
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
			// učitaj spremljene TCP i UDP poruke u odgovarajuće liste
			ClientMsgsStore.readStoredClientMessages();
			new ClientServer(port);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
}

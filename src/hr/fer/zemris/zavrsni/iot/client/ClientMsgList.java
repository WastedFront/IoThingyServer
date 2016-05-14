package hr.fer.zemris.zavrsni.iot.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Singleton class for storing client messages.
 * 
 * @author Nikola Presečki
 * @version 1.0
 */
public class ClientMsgList {

	/** Class instance. */
	private static ClientMsgList instance = null;
	/** List for storing messages. */
	private static List<ClientMessage> messages;

	/** Private constructor for stoping instantiation. */
	private ClientMsgList() {
		messages = Collections.synchronizedList(new ArrayList<ClientMessage>());
	}

	/**
	 * Method for getting class instance (single instance for every method
	 * call).
	 * 
	 * @return class instance
	 */
	public static ClientMsgList getInstance() {
		if (instance == null) {
			synchronized (ClientMsgList.class) {
				if (instance == null) {
					instance = new ClientMsgList();
				}
			}
		}
		return instance;
	}

	/**
	 * Method for adding message into inner collection.
	 * 
	 * @param message
	 *            client message
	 */
	public void addMessage(ClientMessage message) {
		if (!messages.contains(message))
			messages.add(message);
	}

	/**
	 * Method for removing message from inner collection.
	 * 
	 * @param message
	 *            client message
	 */
	public void removeMessage(ClientMessage message) {
		messages.remove(message);
	}

	/**
	 * Method for getting client messages.
	 * 
	 * @return list of client messages
	 */
	public List<ClientMessage> getMessages() {
		return messages;
	}
}
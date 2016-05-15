package hr.fer.zemris.zavrsni.iot.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.zavrsni.iot.utils.Message;

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
	private static List<Message> messages;

	/** Private constructor for stoping instantiation. */
	private ClientMsgList() {
		messages = Collections.synchronizedList(new ArrayList<Message>());
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
	public void addMessage(Message message) {
		if (!messages.contains(message))
			messages.add(message);
	}

	/**
	 * Method for removing message from inner collection.
	 * 
	 * @param message
	 *            client message
	 */
	public void removeMessage(Message message) {
		messages.remove(message);
	}

	/**
	 * Method for getting client messages.
	 * 
	 * @return list of client messages
	 */
	public List<Message> getMessages() {
		return messages;
	}

	/**
	 * Method for adding all client messages.
	 * 
	 * @param list
	 *            of client messages
	 */
	public void addAllMessages(List<Message> msgs) {
		messages.addAll(msgs);
	}

	/**
	 * Method for checking if list is empty.
	 * 
	 * @return true if list is empty, otherwise false
	 */
	public boolean isEmpty() {
		return messages.isEmpty();
	}

	/**
	 * Method for 'poping' (get and remove) first message from collection.
	 * 
	 * @return message or null if collection is empty
	 */
	public Message popFirstMessage() {
		if (!messages.isEmpty()) {
			Message msg = messages.get(0);
			messages.remove(0);
			return msg;
		}
		return null;
	}
}

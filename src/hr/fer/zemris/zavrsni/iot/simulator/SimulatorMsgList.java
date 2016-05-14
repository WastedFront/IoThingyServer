package hr.fer.zemris.zavrsni.iot.simulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.zavrsni.iot.utils.Message;

/**
 * Singleton class for storing simulator messages.
 * 
 * @author Nikola Presečki
 * @version 1.0
 */
public class SimulatorMsgList {

	/** Class instance. */
	private static SimulatorMsgList instance = null;
	/** List for storing messages. */
	private static List<Message> messages;

	/** Private constructor for stoping instantiation. */
	private SimulatorMsgList() {
		messages = Collections.synchronizedList(new ArrayList<Message>());
	}

	/**
	 * Method for getting class instance (single instance for every method
	 * call).
	 * 
	 * @return class instance
	 */
	public static SimulatorMsgList getInstance() {
		if (instance == null) {
			synchronized (SimulatorMsgList.class) {
				if (instance == null) {
					instance = new SimulatorMsgList();
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
}

package hr.fer.zemris.zavrsni.iot.simulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	private static List<SimulatorMessage> messages;

	/** Private constructor for stoping instantiation. */
	private SimulatorMsgList() {
		messages = Collections.synchronizedList(new ArrayList<SimulatorMessage>());
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
	 *            simulator message
	 */
	public void addMessage(SimulatorMessage message) {
		if (!messages.contains(message))
			messages.add(message);
	}

	/**
	 * Method for removing message from inner collection.
	 * 
	 * @param message
	 *            simulator message
	 */
	public void removeMessage(SimulatorMessage message) {
		messages.remove(message);
	}

	/**
	 * Method for getting messages.
	 * 
	 * @return list of simulator messages
	 */
	public List<SimulatorMessage> getMessages() {
		return messages;
	}
}

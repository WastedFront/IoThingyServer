package hr.fer.zemris.zavrsni.iot.client;

/**
 * Representation of received message from client.
 * 
 * @author Nikola Presečki
 * @version 1.0
 *
 */
public class ClientMessage {

	/** Message ID as 64 bit number. */
	private String messageID;
	/** Message ID as 64 bit number. */
	private String previousMessageID;
	/** Source thing ID. */
	private String srcID;
	/** Destination thing ID (this server ID). */
	private String destID;
	/** Unparsed JSON data. */
	private String jsonData;

	/**
	 * Constructor.
	 * 
	 * @param messageID
	 *            Message ID as 64 bit number
	 * @param srcID
	 *            Source thing ID
	 * @param destID
	 *            Destination thing ID (this server ID)
	 * @param jsonDATA
	 *            Unparsed JSON data.
	 * @param previousMessageID
	 *            id of previous message (if exists, otherwise 0)
	 */
	public ClientMessage(String messageID, String srcID, String destID, String jsonDATA, String previousMessageID) {
		super();
		this.messageID = messageID;
		this.srcID = srcID;
		this.destID = destID;
		this.jsonData = jsonDATA;
		this.previousMessageID = previousMessageID;
	}

	/**
	 * Method for parsing received TCP message. Message is in following format:
	 * - MSG_ID SRC_ID DEST_ID JSON_DATA - MSG_ID is 64 bits long SRC_ID is 64
	 * bits long DEST_ID is 64 bits long
	 * 
	 * @param message
	 *            properly formaŁtted message.
	 * @return newly created TCP message.
	 */
	public static ClientMessage parseClientMessage(String message) {
		String msgID = message.substring(0, 8);
		String srcID = message.substring(8, 16);
		String destID = message.substring(16, 24);
		String pMsgID = message.substring(24, 32);
		String jsonData = message.substring(32);
		return new ClientMessage(msgID, srcID, destID, jsonData, pMsgID);
	}

	/**
	 * Getter for previous message ID.
	 * 
	 * @return previous message ID
	 */
	public String getPreviousMessageID() {
		return previousMessageID;
	}

	/**
	 * Getter for message ID.
	 * 
	 * @return message ID
	 */
	public String getMessageID() {
		return messageID;
	}

	/**
	 * Getter for source ID.
	 * 
	 * @return source ID
	 */
	public String getSrcID() {
		return srcID;
	}

	/**
	 * Getter for destination ID.
	 * 
	 * @return destination ID
	 */
	public String getDestID() {
		return destID;
	}

	/**
	 * Getter for JSON data.
	 * 
	 * @return JSON data
	 */
	public String getJsonData() {
		return jsonData;
	}

	@Override
	public String toString() {
		return "SimulatorMessage:\nmessageID=" + messageID + "\nsrcID=" + srcID + "\ndestID=" + destID
				+ "\nprevMessageID=" + previousMessageID + "\ndata:" + jsonData + "\n";
	}

}

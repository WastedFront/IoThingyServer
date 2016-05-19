package hr.fer.zemris.zavrsni.iot.utils;

import java.nio.charset.StandardCharsets;

/**
 * Class which represents message. There are messages from client and from
 * simulator and they both have the same format. Every message has its own id,
 * source thing id, destination thing id and previous message id (if this id is
 * 0, it means there is no previous message).
 * 
 * @author Nikola Presečki
 * @version 1.0
 *
 */
public class Message {
	/** Message ID. */
	private String messageID;
	/** Previous message ID . */
	private String previousMessageID;
	/** Source thing ID. */
	private String srcID;
	/** Destination thing ID. */
	private String destID;
	/** Unparsed JSON data. */
	private String jsonData;
	/** Encryption 0 - NONE, 1 - FULL ... */
	private int encryption;

	/**
	 * Constructor.
	 * 
	 * @param messageID
	 *            Message ID as 64 bit number
	 * @param srcID
	 *            Source thing ID
	 * @param destID
	 *            Destination thing ID
	 * @param jsonDATA
	 *            Unparsed JSON data.
	 * @param previousMessageID
	 *            id of previous message (if exists, otherwise 0)
	 * @param encryption
	 *            Encryption 0 - NONE, 1 - FULL ...
	 */
	public Message(String messageID, String srcID, String destID, String jsonDATA, String previousMessageID,
			int encryption) {
		super();
		this.messageID = messageID;
		this.srcID = srcID;
		this.destID = destID;
		this.jsonData = jsonDATA;
		if(previousMessageID != null)
			this.previousMessageID = previousMessageID;
		else
			this.previousMessageID = "00000000";
		this.encryption = encryption;
	}

	/**
	 * Method for parsing received message. Message is in following format: -
	 * MSG_ID SRC_ID DEST_ID PREV_MSG_ID JSON_DATA - Every ID is 64 bit long.
	 * 
	 * @param message
	 *            properly formatted message.
	 * @return newly created message.
	 */
	public static Message parseClientMessage(String message) {
		int encryption = message.charAt(0) - '0';
		String msgID = message.substring(1, 9);
		String srcID = message.substring(9, 17);
		String destID = message.substring(17, 25);
		String pMsgID = message.substring(25, 33);
		String jsonData = message.substring(33);
		return new Message(msgID, srcID, destID, jsonData, pMsgID, encryption);
	}

	/**
	 * Method for parsing received simulator message. Message is in following
	 * format: - OUTER_FLAGS LENGTH INNER_FLAGS LENGTH MSG_ID SRC_ID DEST_ID
	 * PREV_MSG_ID JSON_DATA -.
	 * 
	 * @param message
	 *            properly formatted message.
	 * @return newly created message.
	 */
	public static Message parseSimulatorMessage(String message) {
		// first 32 bits are flags + length --- we don't need this
		int encryption = 0;
		String msgID = message.substring(8, 16);
		String srcID = message.substring(16, 24);
		String destID = message.substring(24, 32);
		String pMsgID = message.substring(32, 40);
		String jsonData = message.substring(40);
		return new Message(msgID, srcID, destID, jsonData, pMsgID, encryption);
	}

	/**
	 * Method for parsing simulator message to client in string format. Return
	 * format of message is: -messageID srcID destID prevMsgID JSON_DATA-.
	 * 
	 * @return message in described format
	 */
	public String makeReturnMessageForClient() {
		return "" + encryption + messageID + srcID + destID + previousMessageID + jsonData;
	}

	/**
	 * Method for parsing simulator message to client in string format. Return
	 * format of message is: -messageID srcID destID prevMsgID JSON_DATA-.
	 * 
	 * @return message in described format as byte array
	 */
	public byte[] makeReturnMessageForSimulator() {
		String message = messageID + srcID + destID + jsonData;
		String innerFlags = new String(new char[] { 4, 128 }); // 0000010010000000
		if (Long.valueOf(previousMessageID) != 0) {
			innerFlags = new String(new char[] { 12, 128 }); // 0000110010000000
			message = messageID + srcID + destID + previousMessageID + jsonData;
		}
		String messageLen = String.format("%02d", message.length());
		String innerMsg = innerFlags + messageLen + message;
		String outerLen = String.format("%02d", innerMsg.length());
		String outerFlags = new String(new char[] { 0, 0 }); // 0000000000000000
		String outMsg = outerFlags + outerLen + innerMsg;
		return outMsg.getBytes(StandardCharsets.UTF_8);
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

	/**
	 * Getter for encryption.
	 * 
	 * @return encryption
	 */
	public int getEncryption() {
		return encryption;
	}

	@Override
	public String toString() {
		return "Message:\nmessageID=" + messageID + "\nsrcID=" + srcID + "\ndestID=" + destID + "\nprevMessageID="
				+ previousMessageID + "\ndata:" + jsonData + "\n";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destID == null) ? 0 : destID.hashCode());
		result = prime * result + ((jsonData == null) ? 0 : jsonData.hashCode());
		result = prime * result + ((messageID == null) ? 0 : messageID.hashCode());
		result = prime * result + ((previousMessageID == null) ? 0 : previousMessageID.hashCode());
		result = prime * result + ((srcID == null) ? 0 : srcID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (destID == null) {
			if (other.destID != null)
				return false;
		} else if (!destID.equals(other.destID))
			return false;
		if (jsonData == null) {
			if (other.jsonData != null)
				return false;
		} else if (!jsonData.equals(other.jsonData))
			return false;
		if (messageID == null) {
			if (other.messageID != null)
				return false;
		} else if (!messageID.equals(other.messageID))
			return false;
		if (previousMessageID == null) {
			if (other.previousMessageID != null)
				return false;
		} else if (!previousMessageID.equals(other.previousMessageID))
			return false;
		if (srcID == null) {
			if (other.srcID != null)
				return false;
		} else if (!srcID.equals(other.srcID))
			return false;
		return true;
	}

}
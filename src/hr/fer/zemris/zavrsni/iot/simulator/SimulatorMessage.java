package hr.fer.zemris.zavrsni.iot.simulator;

/**
 * Representation of received message from client.
 * 
 * @author Nikola Presečki
 * @version 1.0
 *
 */
public class SimulatorMessage {

	/** Message ID as 64 bit number. */
	private long messageID;
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
	 */
	public SimulatorMessage(long messageID, String srcID, String destID, String jsonDATA) {
		super();
		this.messageID = messageID;
		this.srcID = srcID;
		this.destID = destID;
		this.jsonData = jsonDATA;
	}

	/**
	 * Method for parsing received TCP message. Message is in following format:
	 * - MSG_ID SRC_ID DEST_ID JSON_DATA - MSG_ID is 64 bits long SRC_ID is 64
	 * bits long DEST_ID is 64 bits long
	 * 
	 * @param message
	 *            properly formatted message.
	 * @return newly created TCP message.
	 */
	public static SimulatorMessage parseClientMessage(String message) {
		long msgID = Long.valueOf(message.substring(0, 8));
		String srcID = message.substring(8, 16);
		String destID = message.substring(16, 24);
		String jsonData = message.substring(24);
		return new SimulatorMessage(msgID, srcID, destID, jsonData);
	}

	/**
	 * Getter for message ID.
	 * 
	 * @return message ID
	 */
	public long getMessageID() {
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
		return "SimulatorMessage:\nmessageID=" + messageID + "\nsrcID=" + srcID + "\ndestID=" + destID + "\njsonDATA="
				+ jsonData + "\n";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destID == null) ? 0 : destID.hashCode());
		result = prime * result + ((jsonData == null) ? 0 : jsonData.hashCode());
		result = prime * result + (int) (messageID ^ (messageID >>> 32));
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
		SimulatorMessage other = (SimulatorMessage) obj;
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
		if (messageID != other.messageID)
			return false;
		if (srcID == null) {
			if (other.srcID != null)
				return false;
		} else if (!srcID.equals(other.srcID))
			return false;
		return true;
	}

}

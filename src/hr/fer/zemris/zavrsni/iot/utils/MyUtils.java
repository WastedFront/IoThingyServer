package hr.fer.zemris.zavrsni.iot.utils;

import hr.fer.zemris.zavrsni.iot.simulator.SimulatorMsgList;

/**
 * Class contains methods which are used by multiple other classes.
 * 
 * @author Nikola Presečki
 * @version 1.0
 *
 */
public class MyUtils {

	/** Representation of idle return. */
	public static final String RETURN_IDLE = "IDLE\r\n";

	public static String getReturnClientMessage(String srcID) {
		Message rmMsg = null;
		String returnMsg = RETURN_IDLE;
		for (Message message : SimulatorMsgList.getInstance().getMessages()) {
			if (srcID.equals(message.getDestID())) {
				rmMsg = message;
				returnMsg = message.makeReturnMessageForClient();
				break;
			}
		}
		SimulatorMsgList.getInstance().removeMessage(rmMsg);
		return returnMsg;
	}
}

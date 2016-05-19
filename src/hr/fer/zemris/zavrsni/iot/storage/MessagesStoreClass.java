package hr.fer.zemris.zavrsni.iot.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import hr.fer.zemris.zavrsni.iot.utils.Message;

/**
 * Class contains methods for storing and receiving client messages from some
 * persistent file. Messages are from {@code ClientMsgList}.
 * 
 * @author Nikola Presečki
 * @version 1.0
 *
 */
public class MessagesStoreClass {
	/** Name of the root element. */
	private static final String NAME_ROOT = "messages";
	/** Name of the message element. */
	private static final String NAME_MSG = "message";
	/** Name of the message id attribute. */
	private static final String NAME_MSG_ID = "id";
	/** Name of the source id element. */
	private static final String NAME_SRC_ID = "source_id";
	/** Name of the destination id element. */
	private static final String NAME_DEST_ID = "destination_id";
	/** Name of the json data element. */
	private static final String NAME_JSON_DATA = "json_data";
	/** Name of the previous message element. */
	private static final String NAME_PREV_MSG_ID = "previous_message_id";
	/** Name of the encryption element. */
	private static final String NAME_ENCRYPT = "encryption";

	/**
	 * Method for storing client messages to persistent file. Messages are
	 * readed from {@code ClientMsgList}.
	 * 
	 * @param fileName
	 *            name of the XML file in which messages will be stored
	 * @param messages
	 *            list of messages for storing
	 * @throws ParserConfigurationException
	 *             if there is something wrong with parsing
	 * @throws TransformerException
	 *             if there is something wrong with making xml file from DOM
	 */
	public static void storeClientMessages(String fileName, List<Message> messages)
			throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		// root element
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement(NAME_ROOT);
		doc.appendChild(rootElement);
		// get messages for storing
		for (Message clientMessage : messages) {
			// message element
			Element message = doc.createElement(NAME_MSG);
			rootElement.appendChild(message);

			// set message id as attribute
			message.setAttribute(NAME_MSG_ID, String.valueOf(clientMessage.getMessageID()));

			// source id element
			Element srcID = doc.createElement(NAME_SRC_ID);
			srcID.appendChild(doc.createTextNode(clientMessage.getSrcID()));
			message.appendChild(srcID);

			// destination id element
			Element destID = doc.createElement(NAME_DEST_ID);
			destID.appendChild(doc.createTextNode(clientMessage.getDestID()));
			message.appendChild(destID);

			// json data element
			Element jsonData = doc.createElement(NAME_JSON_DATA);
			jsonData.appendChild(doc.createTextNode(clientMessage.getJsonData()));
			message.appendChild(jsonData);

			// encryption element
			Element encryption = doc.createElement(NAME_ENCRYPT);
			encryption.appendChild(doc.createTextNode("" + clientMessage.getEncryption()));
			message.appendChild(encryption);

			// set previous message id as attribute
			message.setAttribute(NAME_PREV_MSG_ID, String.valueOf(clientMessage.getPreviousMessageID()));
		}
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(fileName));
		transformer.transform(source, result);
	}

	/**
	 * Method for reading stored client messages from persistent file. Messages
	 * are stored in {@code ClientMsgList}. If file is not already created or it
	 * doesn't exists, method will do nothing.
	 * 
	 * @param fileName
	 *            name of the XML file
	 * @return list of stored messages
	 * @throws ParserConfigurationException
	 *             if there is problem with parsing
	 * @throws IOException
	 *             if there is problem with file
	 * @throws SAXException
	 *             if there is problem with reading file
	 */
	public static List<Message> readStoredClientMessages(String fileName)
			throws ParserConfigurationException, SAXException, IOException {
		List<Message> messages = new ArrayList<>();
		File fXmlFile = Paths.get(fileName).toFile();
		if (!fXmlFile.exists())
			return messages;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		// find root element
		NodeList nList = doc.getElementsByTagName(NAME_MSG);
		for (int i = 0, len = nList.getLength(); i < len; ++i) {
			// find message node
			Node nNode = nList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				Message clientMessage = new Message(eElement.getAttribute(NAME_MSG_ID),
						eElement.getElementsByTagName(NAME_SRC_ID).item(0).getTextContent(),
						eElement.getElementsByTagName(NAME_DEST_ID).item(0).getTextContent(),
						eElement.getElementsByTagName(NAME_JSON_DATA).item(0).getTextContent(),
						eElement.getAttribute(NAME_PREV_MSG_ID),
						eElement.getElementsByTagName(NAME_ENCRYPT).item(0).getTextContent().charAt(0)-'0');
				messages.add(clientMessage);
			}
		}
		return messages;
	}
}

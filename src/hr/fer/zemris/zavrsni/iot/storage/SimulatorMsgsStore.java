package hr.fer.zemris.zavrsni.iot.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

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

import hr.fer.zemris.zavrsni.iot.client.ClientMessage;
import hr.fer.zemris.zavrsni.iot.client.ClientMsgList;

/**
 * Class contains methods for storing and receiving simulator messages from some
 * persistent file. Messages are from {@code ClientMsgList}.
 * 
 * @author Nikola Presečki
 * @version 1.0
 *
 */
public class SimulatorMsgsStore {

	

}

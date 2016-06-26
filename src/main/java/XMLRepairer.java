import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Problem Statement:
 * JSoup is a cheeky monkey that sets all of our XML tags/attributes to lowercase, breaking the CryEngine XML format.<br>
 * When starting the game after a DOM write, it destroys all entities and prefabs in the game.<br>
 * Essentially, since it is still well-formed XML, the game still runs, but all items are missing.<br>
 * Objective:<br>
 * <br>
 * and since JSoup is the most versatile and efficient for modelling and modifying our XML, 
 * we'll need a class to fix the tags after the fact.<br>
 * <br>
 * Analysis:<br>
 * We'll just go for the simplest method, which is to iterate the expected tags and attributes from the original, 
 * regex match, repair any faulty ones so we can complete the overwrite.<br>
 * <br>
 * @author Phacops
 */
public class XMLRepairer {

	public static String repair (String fileDataTorepair) {
		
		// Build a map of the elements and attributes that we need to fix
		// Create a method to parse the existing DOM as a string and return a repaired string of XML
		// Can we delete the layer/id/guid data since it's for the Dev Engine and not the Game Engine? - would be easier to manage the file
		
		String replaceDoubleStart = fileDataTorepair.replaceAll("<<", "<");
		String replaceDoubleSelfEnd = replaceDoubleStart.replaceAll("/>/>", "/>");
		String replaceDoubleEnd = replaceDoubleSelfEnd.replaceAll("(\\s?\\w?)>/>", "$1>");
		String replaceDomFourJJams = replaceDoubleEnd.replaceAll("><", ">\n<");
		
		return toPrettyString(replaceDomFourJJams, 2);
	}
	
	// http://stackoverflow.com/a/33541820
	public static String toPrettyString(String xml, int indent) {
	    try {
	        // Turn xml string into a document
	        Document document = DocumentBuilderFactory.newInstance()
	                .newDocumentBuilder()
	                .parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

	        // Remove whitespaces outside tags
	        XPath xPath = XPathFactory.newInstance().newXPath();
	        NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']",
	                                                      document,
	                                                      XPathConstants.NODESET);

	        for (int i = 0; i < nodeList.getLength(); ++i) {
	            Node node = nodeList.item(i);
	            node.getParentNode().removeChild(node);
	        }

	        // Setup pretty print options
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        //transformerFactory.setAttribute("indent-number", indent);
	        Transformer transformer = transformerFactory.newTransformer();
	        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

	        // Return pretty print xml string
	        StringWriter stringWriter = new StringWriter();
	        transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
	        return stringWriter.toString();
	    } catch (Exception e) {
	    	e.printStackTrace();
	        throw new RuntimeException(e);
	    }
	}
	
}

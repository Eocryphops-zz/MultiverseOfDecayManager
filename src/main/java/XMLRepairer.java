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
 * Although JSoup is the most wieldy parser, I think, it's still a cheeky monkey 
 * that sets all of our XML tags/attributes to lowercase, breaking the CryEngine XML format.<br>
 * When starting the game after a DOM write, it destroys all entities and prefabs in the game.<br>
 * Essentially, since it is still well-formed XML, the game still runs, but all items are missing.<br>
 * <br>
 * An alternative, semi-wieldy lib, called Dom4j, wraps all new elements in self-closing tags segments when pushed as a new object.<br>
 * Seems we could stop this by applying the attributes after the fact but we want a single generation string for ease of handling.<br>
 * Therefore, we have to create a fix class to remove the additional characters, 
 * which is still more simple than attribute iteration & addition, I think.<br>
 * <br>
 * Objective:<br>
 * <br>
 * Create a class with a simple repair function, that can have the final Dom4j results passed to it, and return the clean version<br>
 * <br>
 * Analysis:<br>
 * We'll just go for the simplest method, which is to iterate the expected tags and attributes from the original, 
 * regex match, repair any faulty ones so we can complete the overwrite.<br>
 * <br>
 * @author Phacops
 */
public class XMLRepairer {

	/**
	 * Specifically repairs the issue presented with the Dom4j mechanics of new elements 
	 * not having an extremely simple method of adding new elements. When you add an element, 
	 * it seems to generate it as though it's a new self-closing element, which wraps the passed HTML, 
	 * thus it needs pruning - enter this method.
	 * 
	 * @param fileDataTorepair
	 * @return
	 */
	public static String repair (String fileDataTorepair) {
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

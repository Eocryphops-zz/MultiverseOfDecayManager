import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.testng.Assert;
import org.testng.annotations.Test;

public class XMLTest {
	ModXMLHelper modXMLHelper = new ModXMLHelper();
	String modTestXMLFileTarget = "src/test/resources/FortitudeModTestXML.xml";
	String insertionTestXMLFileTarget = "src/test/resources/TestFacilitiesXML.xml";
	
	@Test (description = "Test that we can access the XML resource, and are able to open it")
	public void testOpen () {
		try {
			String fileContents = FileUtil.readFileContents(modTestXMLFileTarget);
			System.out.println("FileUtil successfully read XML contents, contents were: \n" + fileContents);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to open XML file with FileUtil, error was: \n" + e.getMessage());
		}
	}
	
	@Test (description = "Test that we can properly parse the XML data")
	public void testOpenAndParse () {
		try {
			String fileContents = FileUtil.readFileContents(modTestXMLFileTarget);
			System.out.println("FileUtil successfully read XML contents, now testing parsing of raw String");
			
			Document DOM = modXMLHelper.buildXMLFromString(fileContents);
			System.out.println("XMLHelper successfully read XML contents, DOM is: \n" + DOM.toString());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to build and parse XML file with XMLHelper, error was: \n" + e.getMessage());
		}
	}
	
	@Test (description = "Test that we can properly parse the XML data")
	public void testOpenAndParseWithOnlyXMLHelper () {
		try {
			Document DOM = modXMLHelper.getXmlFileAndBuild(modTestXMLFileTarget);
			System.out.println("XMLHelper successfully read XML contents, DOM is: \n" + DOM.toString());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to build and parse XML file with XMLHelper, error was: \n" + e.getMessage());
		}
	}
	
	@Test (description = "Test XML element insertion - "
			+ "explicitly grabbing only one unit of start/end tags and the core content and inserting")
	public void testInsertFewElementsIntoXMLDOM () {
		try {
			Document DOM = modXMLHelper.getXmlFileAndBuild(modTestXMLFileTarget);
			Document insertionDOM = modXMLHelper.getXmlFileAndBuild(insertionTestXMLFileTarget);
			
			List<Element> modElements = new ArrayList<Element>();
			
			Element firstFacilitiesData = DOM.select("FacilitiesData").first();
			String modName = firstFacilitiesData.attr("name");
			
			Element fortitudeSnyderFacilitiesData = firstFacilitiesData.select("Objects").first();
			
			modElements.add(fortitudeSnyderFacilitiesData.select("ModWrapper[placement=start_tag]").first());
			modElements.addAll(fortitudeSnyderFacilitiesData.select("Object[mod_name=" + modName + "]"));
			modElements.add(fortitudeSnyderFacilitiesData.select("ModWrapper[placement=end_tag]").first());
			
			Elements originalSnyderElement = insertionDOM.select(
					fortitudeSnyderFacilitiesData.attr("parent_tag") 
					+ "[name=" + fortitudeSnyderFacilitiesData.attr("parent_name") + "]");
			
			originalSnyderElement.select("[mod_name=" + modName + "]").remove();
			
			Elements originalSnyderElementObjects = originalSnyderElement.select("Objects");
			
			for (Element element : modElements) {
				originalSnyderElementObjects.prepend(element.toString());
			}
			
			for (Element element : modElements) {
				Elements newElement;
				
				// The system automatically converts to lowercase
				if (!element.tagName().equals("modwrapper")) {
					newElement = originalSnyderElementObjects.select("[Name=" + element.attr("Name") + "]");
				} else {
					newElement = originalSnyderElementObjects.select("[mod_name=" + modName + "]");
				}
				
				if (newElement.isEmpty()) {
					Assert.fail("XMLHelper seemed to successfully insert XML nodes, but was unable to confirm, "
							+ "DOM is now: \n" + insertionDOM.toString());
				}
			}
			
			System.out.println("XMLHelper successfully inserted XML nodes, "
					+ "SnyderObjects are now: \n" + originalSnyderElementObjects.toString());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to insert elements into XML, error was: \n" + e.getMessage());
		}
	}
	
	@Test (description = "Test XML element insertion")
	public void testInsertElementsIntoXMLDOM () {
		try {
			List<ModXML> modList = new ArrayList<ModXML>();
			modList.add(new ModXML(modXMLHelper.getXmlFileAndBuild(modTestXMLFileTarget)));
			
			modXMLHelper.setModXMLs(modList);
			modXMLHelper.setFacilitiesXMLTarget(insertionTestXMLFileTarget);
			modXMLHelper.setMissionXMLTarget(insertionTestXMLFileTarget);
			
			modXMLHelper.handleAllMods();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to insert elements into XML, error was: \n" + e.getMessage());
		}
	}
	
	@Test (description = "Test XML element removal")
	public void testRemoveElementsFromXMLDOM () {
		try {
			Document DOM = modXMLHelper.getXmlFileAndBuild(insertionTestXMLFileTarget);
			DOM.select("Prefab[name=warehouse.workshop] Objects Object[Name=folding_chair26]").remove();
			DOM.select("Prefab[name=riverside.command_center]").remove();
			DOM.select("Prefab[name=riverside.library_trashed]").remove();
			
			System.out.println("XMLHelper successfully deleted XML nodes, DOM is now: \n" + DOM.toString());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to remove elements from XML, error was: \n" + e.getMessage());
		}
	}
	
//////////////////////////////////////////////////////
// TODO - implement sorters
//////////////////////////////////////////////////////

	@Test (description = "Test that we are correctly able to parse an XML file for a vector-space point, "
			+ "in order to allow referential ordering ", enabled=false)
	public void testGetVectorElement () {
		try {
			Document DOM = modXMLHelper.getXmlFileAndBuild(modTestXMLFileTarget);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to get VectorElement from DOM, error was: \n" + e.getMessage());
		}
	}
	
	@Test (description = "Test sorting vector positions by the X axis", enabled=false)
	public void testSortThreeElementsByVectorXPosition () {
		try {
			Document DOM = modXMLHelper.getXmlFileAndBuild(modTestXMLFileTarget);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to sort VectorElements from DOM, error was: \n" + e.getMessage());
		}
	}
	
	@Test (description = "Test sorting vector positions by the Y axis", enabled=false)
	public void testSortThreeElementsByVectorYPosition () {
		try {
			Document DOM = modXMLHelper.getXmlFileAndBuild(modTestXMLFileTarget);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to sort VectorElements from DOM, error was: \n" + e.getMessage());
		}
	}
	
	@Test (description = "Test sorting vector positions by the Z axis", enabled=false)
	public void testSortThreeElementsByVectorZPosition () {
		try {
			Document DOM = modXMLHelper.getXmlFileAndBuild(modTestXMLFileTarget);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to sort VectorElements from DOM, error was: \n" + e.getMessage());
		}
	}
}

import org.dom4j.Document;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Objective: <br>
 * Unit Tester for element handling in XML files<br>
 * @author Phacops
 */
public class TestXMLHandling extends LoggingHelper {
	ModXMLHelper modXMLHelper = new ModXMLHelper();
	ModFileUtil modFileUtil = new ModFileUtil();
	
	String facilitiesTestXMLFileTarget = "src/test/resources/TestFacilitiesXML.xml";
	
	private void getNodeRemoveAndConfirm (Document DOM, String nodeTarget) {
		DOM.selectSingleNode(nodeTarget).detach();
		if (DOM.selectSingleNode(nodeTarget) != null) {
			Assert.fail("Failed to remove element from XML, element was: "
					+ nodeTarget);
		}
	}
	
	@Test (description = "Test XML element removal")
	public void testRemoveElementsFromXMLDOM () {
		try {
			SCENARIO("Test the ability to delete elements from an XML DOM");
			FIRST("Build the XML file into an XML DOM for parsing");
			Document DOM = modFileUtil.getXmlFileAndBuild(facilitiesTestXMLFileTarget);
			
			GIVEN("DOM was able to be constructed");
			THEN("Printing the Original DOM, which was: \n" + DOM.asXML() + "\n\n");
			
			THEN("Attempting to remove element named search_ground27");
			getNodeRemoveAndConfirm(DOM, "//*[@Name='search_ground27']");
			
			THEN("Attempting to remove element named folding_chair26");
			getNodeRemoveAndConfirm(DOM, "//Prefab[@Name='warehouse.workshop']/Objects/Object[@Name='folding_chair26']");
			
			THEN("Attempting to remove element named folding_chair26");
			getNodeRemoveAndConfirm(DOM, "//Prefab[@Name='riverside.command_center']");
			
			THEN("Attempting to remove element named folding_chair26");
			getNodeRemoveAndConfirm(DOM, "//Prefab[@Name='riverside.library_trashed']");
			
			THEN("Attempting to remove element named folding_chair26");
			getNodeRemoveAndConfirm(DOM, "//*[@mod_name='Fortitude_Mod']");
			
			GIVEN("This was able to print");
			THEN("ModXMLHelper successfully deleted XML nodes");
			LASTLY("Therefore printing DOM, which is now: \n" + DOM.asXML());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to remove elements from XML, error was: \n" + e.getMessage());
		}
	}
	
	@Test (description = "Test finding XML files in XmlMods Directory")
	public void testGatherXmlFilesFromXmlModsDir () {
		SCENARIO("Test the ability to delete elements from an XML DOM");
		try {
			THEN("Using ModXMLHelper to find all mod requests in the XmlMods directory and build them through ModFileUtil");
			modXMLHelper.findAllXmlModFilesAndBuildThem();
			GIVEN("This was able to print");
			THEN("ModXMLHelper successfully orchestrated acquisition of mod requests without an exception");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to find XML files in XmlMods Directory, error was: \n" + e.getMessage());
		}
	}
}

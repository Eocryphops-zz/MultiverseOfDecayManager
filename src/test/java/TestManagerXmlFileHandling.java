import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Objective: <br>
 * Simple tester for some of the general mechanics.<br>
 * <br>
 * Note:<br>
 * Is not a comprehensive unit tester, but does just enough to Red-Green through.<br>
 * Further utility will arise when a UI is implemented.<br>
 * @author Phacops
 */
public class TestManagerXmlFileHandling {
	ModXMLHelper modXMLHelper = new ModXMLHelper();
	String modTestXMLFileTarget = "src/test/resources/FortitudeModTestXML.xml";
	String facilitiesTestXMLFileTarget = "src/test/resources/TestFacilitiesXML.xml";
	String missionTestXMLFileTarget = "src/test/resources/TestMissionXML.xml";
	
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
	public void testOpenAndParseWithOnlyXMLHelper () {
		try {
			Document DOM = modXMLHelper.getXmlFileAndBuild(modTestXMLFileTarget);
			System.out.println("XMLHelper successfully read XML contents, DOM is: \n" + DOM.toString());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to build and parse XML file with XMLHelper, error was: \n" + e.getMessage());
		}
	}
	
	@Test (description = "Test XML element insertion")
	public void testFullHandling () {
		try {
			List<ModXML> modList = new ArrayList<ModXML>();
			modList.add(new ModXML(modXMLHelper.getXmlFileAndBuild(modTestXMLFileTarget)));
			
			modXMLHelper.setModXMLs(modList);
			modXMLHelper.setFacilitiesXMLTarget(facilitiesTestXMLFileTarget);
			modXMLHelper.setMissionXMLTarget(missionTestXMLFileTarget);
			
			modXMLHelper.handleAllMods(true);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to insert elements into XML, error was: \n" + e.getMessage());
		}
	}
	
	
	
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
			Document DOM = modXMLHelper.getXmlFileAndBuild(facilitiesTestXMLFileTarget);
			
			getNodeRemoveAndConfirm(DOM, "//*[@Name='search_ground27']");
			getNodeRemoveAndConfirm(DOM, "//Prefab[@Name='warehouse.workshop']/Objects/Object[@Name='folding_chair26']");
			getNodeRemoveAndConfirm(DOM, "//Prefab[@Name='riverside.command_center']");
			getNodeRemoveAndConfirm(DOM, "//Prefab[@Name='riverside.library_trashed']");
			getNodeRemoveAndConfirm(DOM, "//*[@mod_name='Fortitude_Mod']");
			
			System.out.println("XMLHelper successfully deleted XML nodes, DOM is now: \n" + DOM.asXML());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to remove elements from XML, error was: \n" + e.getMessage());
		}
	}
	
	@Test (description = "Test finding XML files in XmlMods Directory")
	public void testGatherXmlFilesFromXmlModsDir () {
		try {
			modXMLHelper.findAllXmlModFilesAndBuildThem();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to find XML files in XmlMods Directory, error was: \n" + e.getMessage());
		}
	}
}

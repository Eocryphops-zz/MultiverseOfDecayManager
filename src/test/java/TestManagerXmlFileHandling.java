import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
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
	
	@Test (description = "Test XML element insertion")
	public void testInsertElementsIntoXMLDOM () {
		try {
			List<ModXML> modList = new ArrayList<ModXML>();
			modList.add(new ModXML(modXMLHelper.getXmlFileAndBuild(modTestXMLFileTarget)));
			
			modXMLHelper.setModXMLs(modList);
			modXMLHelper.setFacilitiesXMLTarget(facilitiesTestXMLFileTarget);
			modXMLHelper.setMissionXMLTarget(missionTestXMLFileTarget);
			
			modXMLHelper.handleAllMods();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to insert elements into XML, error was: \n" + e.getMessage());
		}
	}
	
	@Test (description = "Test XML element removal")
	public void testRemoveElementsFromXMLDOM () {
		try {
			Document DOM = modXMLHelper.getXmlFileAndBuild(facilitiesTestXMLFileTarget);
			DOM.select("Prefab[name=warehouse.workshop] Objects Object[Name=folding_chair26]").remove();
			DOM.select("Prefab[name=riverside.command_center]").remove();
			DOM.select("Prefab[name=riverside.library_trashed]").remove();
			
			System.out.println("XMLHelper successfully deleted XML nodes, DOM is now: \n" + DOM.toString());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to remove elements from XML, error was: \n" + e.getMessage());
		}
	}
}

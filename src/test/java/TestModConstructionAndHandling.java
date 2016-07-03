import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Objective: <br>
 * Unit Test class for construction and handling of mods.<br>
 * @author Phacops
 */
public class TestModConstructionAndHandling extends LoggingHelper {
	ModXMLHelper modXMLHelper = new ModXMLHelper();
	ModFileUtil modFileUtil = new ModFileUtil();
	
	String modTestXMLFileTarget = "src/test/resources/FortitudeModTestXML.xml";
	String facilitiesTestXMLFileTarget = "src/test/resources/TestFacilitiesXML.xml";
	String missionTestXMLFileTarget = "src/test/resources/TestMissionXML.xml";
	
	@Test (description = "Test XML element insertion")
	public void testFullHandling () {
		SCENARIO("Test the full spectrum of the handling of mods, E2E");
		try {
			FIRST("Building a basic test XML into the ModXML we want to parse.");
			List<ModXML> modList = new ArrayList<ModXML>();
			modList.add(new ModXML(modFileUtil.getXmlFileAndBuild(modTestXMLFileTarget)));
			modXMLHelper.setModXMLs(modList);
			
			GIVEN("ModXML has been set...");
			THEN("Setting the test Facilities XML");
			modFileUtil.setFacilitiesXMLTarget(facilitiesTestXMLFileTarget);
			AND("Setting the test Mission XML");
			modFileUtil.setMissionXMLTarget(missionTestXMLFileTarget);
			THEN("[DEBUG ENABLED] Attempting to insert the test mods into each of the test files");
			modXMLHelper.handleAllMods(true);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to insert elements into XML, error was: \n" + e.getMessage());
		}
	}
	
	@Test (description = "Test XML element insertion")
	public void testModXMLHandling () {
		SCENARIO("Test inserting requested mod changes into the 2 test XMLs");
		try {
			FIRST("Building a basic test XML into the ModXML we want to parse.");
			List<ModXML> modList = new ArrayList<ModXML>();
			modList.add(new ModXML(modFileUtil.getXmlFileAndBuild(modTestXMLFileTarget)));
			modXMLHelper.setModXMLs(modList);
			
			GIVEN("ModXML has been set...");
			THEN("Setting the test Facilities XML");
			modFileUtil.setFacilitiesXMLTarget(facilitiesTestXMLFileTarget);
			AND("Setting the test Mission XML");
			modFileUtil.setMissionXMLTarget(missionTestXMLFileTarget);
			THEN("Attempting to insert the test mods into each of the test files");
			modXMLHelper.handleModXMLFiles();
			GIVEN("This was able to print");
			THEN("We were able to successfully handle all mods without an exception.");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to insert elements into XML, error was: \n" + e.getMessage());
		}
	}
	
	@Test (description = "Test the building of sub-containers from each mod")
	public void testBuildContainer () {
		SCENARIO("Test the building of sub-containers from each mod, "
				+ "which dictate mod subsegments to be applied to their relative XML");
		try {
			FIRST("Building a basic test XML into the ModXML we want to parse.");
			ModXML mod = new ModXML(modFileUtil.getXmlFileAndBuild(modTestXMLFileTarget));
			List<ModChangeObjectContainer> modObjects = mod.getModObjects();
			
			GIVEN("ModXML has been set...");
			THEN("Confirming ModXML parsing shows the same object count as in the actual file");
			Assert.assertEquals(modObjects.size(), 3);
			
			GIVEN("The size [price? ;)] is right...");
			THEN("Printing the first ModChangeObjectContainer for debugging, "
					+ "before checking the actual attributes...");
			ModChangeObjectContainer item1 = modObjects.get(0);
			System.out.println(item1.toString());
			
			THEN("Confirming the parsed mod_name matches the actual XML...");
			Assert.assertEquals(item1.getName(), "Snyder_Objects");
			
			AND("Confirming the parsed mod_segment matches the actual XML...");
			Assert.assertEquals(item1.getModSegment(), "Snyder Trucking Warehouse");
			
			AND("Confirming the parsed File to Mod matches the actual XML's <FacilitiesData> wrapper...");
			Assert.assertEquals(item1.getFileToMod(), "FacilitiesData");
			
			AND("Confirming the parsed parent_tag matches the actual XML...");
			Assert.assertEquals(item1.getParentTag(), "Prefab");
			
			AND("Confirming the parsed parent_name matches the actual XML...");
			Assert.assertEquals(item1.getParentName(), "warehouse.workshop");
			
			LASTLY("Confirming the parsed child element count matches the actual XML...");
			Assert.assertEquals(item1.getChildElements().size(), 1);
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to insert elements into XML, error was: \n" + e.getMessage());
		}
	}
}

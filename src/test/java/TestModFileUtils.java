import java.io.File;

import org.dom4j.Document;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestModFileUtils extends LoggingHelper {
	ModXMLHelper modXMLHelper = new ModXMLHelper();
	ModFileUtil modFileUtil = new ModFileUtil();
	
	String modTestXMLFileTarget = "src/test/resources/FortitudeModTestXML.xml";
	String facilitiesTestXMLFileTarget = "src/test/resources/TestFacilitiesXML.xml";
	String missionTestXMLFileTarget = "src/test/resources/TestMissionXML.xml";
	
	@Test (description = "Test that we can access the XML resource, and are able to open it")
	public void testWriteReadDelete () {
		try {
			String tempFileTarget = "src/test/resources/testWriteReadDeleteTemp.xml";
			String expectedFileContents = 
					"testWriteReadDelete - We put people on the Moon and..."
					+ "well, it looks like we were able to write to a new file; what an achievement ;)";
			
			SCENARIO("Test - Write, Read, Delete");
			
			FIRST("Confirming we can access, "
					+ "and write the expected contents to, the Temp file...");
			modFileUtil.writeContentsToFile(tempFileTarget, expectedFileContents);
			Assert.assertTrue(new File(tempFileTarget).exists());
			
			GIVEN("FileUtil successfully wrote the XML contents.");
			
			THEN("Confirming we can target, and have access to open, the file, "
					+ "then confirming we wrote the right contents...");
			String actualFileContents = modFileUtil.readFileContents(tempFileTarget);
			Assert.assertEquals(expectedFileContents, actualFileContents);
			
			GIVEN("FileUtil successfully read XML contents, "
					+ "contents were: \n\t\t" + actualFileContents);
			
			THEN("Now confirming ability to delete...");
			modFileUtil.deleteTempFile(tempFileTarget);
			Assert.assertFalse(new File(tempFileTarget).exists());
			
			LASTLY("FileUtil successfully deleted the Temp XML");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to open XML file with FileUtil, error was: \n" + e.getMessage());
		}
	}
	
	@Test (description = "Test that we can access the XML resource, and are able to open it")
	public void testOpen () {
		try {
			SCENARIO("Test opening a file");
			
			String fileContents = modFileUtil.readFileContents(modTestXMLFileTarget);
			
			GIVEN("Printing this means no exception ocurred");
			THEN("FileUtil successfully read XML contents, contents were: \n" + fileContents);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to open XML file with FileUtil, error was: \n" + e.getMessage());
		}
	}
	
	@Test (description = "Test that we can properly parse the XML data")
	public void testOpenAndParseWithOnlyXMLHelper () {
		try {
			SCENARIO("Test opening a file, and parsing the XML");
			
			Document DOM = modFileUtil.getXmlFileAndBuild(modTestXMLFileTarget);
			
			GIVEN("Printing this means no exception ocurred");
			THEN("XMLHelper successfully read XML contents, DOM is: \n" + DOM.toString());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to build and parse XML file with ModXMLHelper, error was: \n" + e.getMessage());
		}
	}
	
	@Test (description = "Test finding XML files in XmlMods Directory")
	public void testGatherXmlFilesFromXmlModsDir () {
		try {
			SCENARIO("Test gathering all XML files in the XmlMods Directory and building them");
			modXMLHelper.findAllXmlModFilesAndBuildThem();
			
			GIVEN("Printing this means no exception ocurred");
			THEN("ModXMLHelper successfully found and read XML contents through ModFileUtil "
					+ "then built them");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to find XML files in XmlMods Directory, error was: \n" + e.getMessage());
		}
	}
	
	
}

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.testng.Assert;

/**
 * Helper class for file handling
 * 
 * @author Phacops
 */
public class ModFileUtil {

	private SAXReader reader = new SAXReader();

	// Until I have a fuller UI, this will service skipping mods - woe be to those with a fear of manual labor :)
	private String xmlModsExclusionsFileTarget = "XmlMods/ModManagement/XmlFilesToExcludeFromBuilding.txt";
	private String userPropsFileTarget = "XmlMods/ModManagement/preferences.properties";
	private String missionXMLTarget = "Levels/Class3/mission_mission0.xml";
	private String facilitiesXMLTarget = "Libs/Prefabs/facilities.xml";
	private String originalMissionXMLTarget = "XmlMods/Backup/mission_mission0.original.xml";
	private String originalFacilitiesXMLTarget = "XmlMods/Backup/facilities.original.xml";
	private String xmlModsFolder = "XmlMods";

	private File missionFile;
	private File facilitiesFile;

	private Document facilitiesXmlFileDOM;
	private Document missionXmlFileDOM;
	
	private File xmlModsExclusionsFile;
	private File propsFile;
	
	/* 
	 * Objective:
	 * Assure props file exists to help less technical users 
	 * that may not think first to redeploy zip on accidental deletion,
	 * will also be used to keep a min state for program operation - 
	 * this means every mod gets build in XmlMods folder, with no sense of order
	 */
	String userPropertiesDefaultContent = 
			"#####\n"
			+ "# Mod Order Handling\n"
			+ "#####\n"
			+ "# For UI Implementation\n"
			+ "# Will be used by the UI to take your sequence choices and convert that \n"
			+ "# into a load order, thus dictating that a mod loaded first will be overwritten by mods loaded later.\n"
			+ "# E.g. if you really like the mod that turns all your flares into nukes, \n"
			+ "# but you also have a mod that makes flares brighter, then you could load the Nuke Flares later,\n"
			+ "# which would let it be the last one to leave it's mark, and keep it as the one you get working.\n"
			+ "# \n"
			+ "# Note: Only matters in a case where a mod may be acting on the same elements as another mod.\n"
			+ "# Manual usage: By Mod Name, simply separates with pipes and follows LTR array sequence for order\n"
			+ "#####\n"
			+ "mod.order=\n"
			+ "\n"
			+ "#####\n"
			+ "# Mod Ignorance\n"
			+ "#####\n"
			+ "# For UI Implementation\n"
			+ "# Will be used by the UI to save choices for which mods are not loaded\n"
			+ "# Will read/write to the existing /sodmodmanager/XmlMods/ModManagement/XmlFilesToExcludeFromBuilding.txt\n"
			+ "# This will allow users to add them manually, rather than have to understand prop file mechanics and\n"
			+ "# how the data is being parsed by the program\n"
			+ "# \n"
			+ "# Manual usage: Add the path to the exclusion file mentioned above rather than here, as this is volatile,\n"
			+ "# being that it is only written here, and read/rewritten there.\n"
			+ "#####\n"
			+ "mod.ignore=\n";
	
	/*
	 * Just in case it gets deleted on accident
	 * Remember to escape file paths, e.g.: XmlMods\\FortitudeMod.xml
	 */
	String defaultExclusionsContent = 
			"# Use the following as a guide: It'll be updated to match the actual base mod for Fortitude, \n"
					+ "# yet applying it to your game, by removing the following line, "
					+ "will leave broken mesh indicators all over your Savini/Snyder area unless you have the mod \n"
					+ "#XmlMods\\FortitudeMod.xml";

	/**
	 * The singular for building a ModXML, also establishes each mod segment request as a separate child object
	 * 
	 * @param modFileTarget
	 * @return
	 * @throws Exception
	 */
	public ModXML buildModXMLDOM (String modFileTarget) throws Exception {

		return new ModXML(getXmlFileAndBuild(modFileTarget));
	}

	public void checkAndBuildExclusions () throws IOException {
		xmlModsExclusionsFile = new File(xmlModsExclusionsFileTarget);
		
		if (!xmlModsExclusionsFile.exists()) {
			FileUtils.write(xmlModsExclusionsFile, defaultExclusionsContent, "UTF-8");
		}
	}

	/**
	 * Basic builder for the Facilities & Mission XML files<br>
	 * If they don't exist, then we write them to the proper directories from our backup location,
	 * which houses the originals for the game. <br>
	 * <br>
	 * Either way, we build up an XML document for parsing, insertion, and ultimately writing of our mods.
	 * @throws Exception
	 */
	public void checkAndBuildMissionAndFacilities () throws Exception {
		missionFile = new File(missionXMLTarget);
		facilitiesFile = new File(facilitiesXMLTarget);

		if (!missionFile.exists()) {
			setMissionXmlFileDOM(
					getXmlFileAndBuild(originalMissionXMLTarget));
		} else {
			setMissionXmlFileDOM(
					getXmlFileAndBuild(missionXMLTarget));
		}

		if (!facilitiesFile.exists()) {
			setFacilitiesXmlFileDOM(
					getXmlFileAndBuild(originalFacilitiesXMLTarget));
		} else {
			setFacilitiesXmlFileDOM(
					getXmlFileAndBuild(facilitiesXMLTarget));
		}
	}

	/**
	 * Go to the XmlMods directory, find all XML files, 
	 * and build them into ModXML objects, then return the list
	 * @return
	 * @throws Exception
	 */
	public List<ModXML> checkAndBuildModXMLs () throws Exception {
		List<ModXML> modXMLs = new ArrayList<>();

		List<String> xmlModsExclusions = new ArrayList<>(
				FileUtils.readLines(xmlModsExclusionsFile, "UTF-8"));
		File directory = new File(xmlModsFolder);

		if (directory.exists()) {
			Iterator<File> iterator = FileUtils.iterateFiles(directory, new String[]{"xml"}, false);

			/* 
			 * Adds more complexity but should handle if multiple items with similar names, 
			 * Then we dump extras autonomously and not wreck the .xml
			 */
			Map<String, String> modFileTargetList = new HashMap<>();

			while (iterator.hasNext()) {
				String path = iterator.next().getPath();
				System.out.println("[INFO] - XML Iterator found XML file: " + path);
				modFileTargetList.put(path.replaceAll("XmlMods\\\\(.*).xml", "$1"), path);
			}

			for (String key : modFileTargetList.keySet()) {
				String path = modFileTargetList.get(key);

				System.out.println("Building XML: " + key + ", " + path);
				if (!xmlModsExclusions.contains(path)) {
					modXMLs.add(buildModXMLDOM(path));
				} else {
					System.out.println("[SKIPPED] - XML file was excluded: " + path);
				}
			}

		} else {
			String msg = "[ERROR] - XmlMods directory does not exist?";
			System.out.println(msg);
			Assert.fail(msg);
		}

		if (modXMLs.isEmpty()) {
			String msg = "[ERROR] - Found NO XML Files in XmlMods directory...";
			System.out.println(msg);
			Assert.fail(msg);
		}

		return modXMLs;
	}

	public void checkAndBuildProps () throws IOException {
		propsFile = new File(userPropsFileTarget);
		
		if (!propsFile.exists()) {
			FileUtils.write(propsFile, userPropertiesDefaultContent, "UTF-8");
		}
	}

	/**
	 * Objective: <br>
	 * Provide a method with protections for deleting ONLY temp files 
	 * generated by Unit Tests, after confirming contents
	 * @param fileTarget
	 */
	public void deleteTempFile (String fileTarget) {
		File fileToDelete = new File(fileTarget);

		if (!fileTarget.contains("Temp") || fileToDelete.isDirectory()) {
			System.out.println(
					"Attempted to delete a File, but this was not a temp file - aborting! "
							+ "File was: " + fileTarget);
			return;
		}

		// isDir Should have been caught above, but we'll just make REALLY sure :)
		if (fileToDelete.exists() && !fileToDelete.isDirectory()) {
			fileToDelete.delete();
		}
	}

	public Document getFacilitiesXmlFileDOM() {
		return facilitiesXmlFileDOM;
	}

	public Document getMissionXmlFileDOM() {
		return missionXmlFileDOM;
	}

	/**
	 * Grab an XML file, parse, and create a Document for handy parsing
	 * 
	 * @param fileTarget
	 * @return
	 * @throws Exception
	 */
	public Document getXmlFileAndBuild (String fileTarget) throws Exception {
		if (fileTarget == null) {
			System.out.println("[ERROR] - XML file target was empty - nothing to build!");
			throw new Exception("XML String was empty - nothing to build!");
		}

		return reader.read(fileTarget);
	}

	public String readFileContents (String fileTarget) throws IOException {
		return FileUtils.readFileToString(FileUtils.getFile(fileTarget), "UTF-8");
	}

	public void setFacilitiesXmlFileDOM(Document facilitiesXmlFileDOM) {
		this.facilitiesXmlFileDOM = facilitiesXmlFileDOM;
	}
	
	public void setFacilitiesXMLTarget (String facilitiesTarget) {
		this.facilitiesXMLTarget = facilitiesTarget;
	}
	
	public void setMissionXmlFileDOM(Document missionXmlFileDOM) {
		this.missionXmlFileDOM = missionXmlFileDOM;
	}

	public void setMissionXMLTarget (String missionTarget) {
		this.missionXMLTarget = missionTarget;
	}
	
	/**
	 * Master setup, with full spectrum building of our files and composing them into ModXMLs for handling
	 * @return
	 * @throws Exception
	 */
	public List<ModXML> setupExpectedFiles() throws Exception {

		checkAndBuildProps();
		checkAndBuildExclusions();
		checkAndBuildMissionAndFacilities();

		return checkAndBuildModXMLs();
	}

	public void writeContentsToFile (String fileTarget, String dataToWrite) throws IOException {
		FileUtils.write(new File(fileTarget), dataToWrite, "UTF-8");
	}

	/**
	 * Write the files with changes to their appropriate locations 
	 * in the Levels/Class3 & Libs/Prefabs folders respectively
	 */
	public void writeDomsToFiles() {
		try {
			FileUtils.write(missionFile, XMLRepairer.repair(missionXmlFileDOM.asXML()), "UTF-8");
			System.out.println("[INFO] - Wrote modded Mission_Mission0.xml to: " + missionFile.getPath());

			FileUtils.write(facilitiesFile, XMLRepairer.repair(facilitiesXmlFileDOM.asXML()), "UTF-8");
			System.out.println("[INFO] - Wrote modded Facilities.xml to: " + facilitiesFile.getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.testng.Assert;

/**
 * Objective: <br>
 * Provide a class to orchestrate the acquisition, parsing, and application of ModXMLs <br>
 * <br>
 * Analysis: <br>
 * This class orchestrates the hard work to produce the results, 
 * from building up the XML, to requesting parsing, to triggering the mod changes. <br>
 * <br>
 * @author Phacops
 */
public class ModXMLHelper {

	SAXReader reader = new SAXReader();
	boolean debug = true;
	String xmlModsFolder = "XmlMods";

	// Until I have a fuller UI, this will service skipping mods - woe be to those with a fear of manual labor :)
	String xmlModsExclusionsFileTarget = "XmlMods/XmlFilesToExcludeFromBuilding.txt";
	String missionXMLTarget = "Levels/Class3/mission_mission0.xml";
	String facilitiesXMLTarget = "Libs/Prefabs/facilities.xml";
	String originalMissionXMLTarget = "Levels/Class3/mission_mission0.original.xml";
	String originalFacilitiesXMLTarget = "Libs/Prefabs/facilities.original.xml";
	File missionFile;
	File facilitiesFile;

	Document facilitiesXmlFileDOM;
	Document missionXmlFileDOM;

	List<ModXML> modXMLs = new ArrayList<>();

	public List<ModXML> getModXMLs() {
		return modXMLs;
	}

	public void setModXMLs(List<ModXML> modXMLs) {
		this.modXMLs = modXMLs;
	}

	public void setMissionXMLTarget (String missionTarget) {
		this.missionXMLTarget = missionTarget;
	}

	public void setFacilitiesXMLTarget (String facilitiesTarget) {
		this.facilitiesXMLTarget = facilitiesTarget;
	}

	public Document getFacilitiesXmlFileDOM() {
		return facilitiesXmlFileDOM;
	}

	public void setFacilitiesXmlFileDOM(Document facilitiesXmlFileDOM) {
		this.facilitiesXmlFileDOM = facilitiesXmlFileDOM;
	}

	public Document getMissionXmlFileDOM() {
		return missionXmlFileDOM;
	}

	public void setMissionXmlFileDOM(Document missionXmlFileDOM) {
		this.missionXmlFileDOM = missionXmlFileDOM;
	}

	/**
	 * Grab an XML file, parse with JSoup, and create a Document for handy parsing
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


	/**
	 * Iterate the established XML Mod Directory and aggregate the files into plain Java Objects for handling
	 * 
	 * @throws Exception
	 */
	public void findAllXmlModFilesAndBuildThem () throws Exception {

		File xmlModsExclusionsFile = new File(xmlModsExclusionsFileTarget);
		
		if (!xmlModsExclusionsFile.exists()) {
			FileUtils.write(xmlModsExclusionsFile, "XmlMods\\CopyMe-ModXMLTemplate.xml", "UTF-8");
		}
		
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
	}
	
	private void writeDomsToFiles() {
		try {
			FileUtils.write(missionFile, XMLRepairer.repair(missionXmlFileDOM.asXML()), "UTF-8");
			System.out.println("[INFO] - Wrote modded Mission_Mission0.xml to: " + missionFile.getPath());
			
			FileUtils.write(facilitiesFile, XMLRepairer.repair(facilitiesXmlFileDOM.asXML()), "UTF-8");
			System.out.println("[INFO] - Wrote modded Facilities.xml to: " + facilitiesFile.getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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

	/**
	 * The master method for orchestrating the entire operation
	 * 
	 * @throws Exception
	 */
	public void handleAllMods (boolean debug) throws Exception {
		this.debug = debug;
		
		System.out.println("[INFO] - ModXMLHelper found [" + modXMLs.size() + "] Mods!");
		
		buildXMLFiles();
		handleModXMLFiles();
		writeDomsToFiles();
		
		System.out.println("\n\n[INFO] - Finished handling all Mods - Huzzah!");
	}
	
	public void buildXMLFiles () throws Exception {
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
	
	public void handleModXMLFiles () {
		for (ModXML modXml : modXMLs) {
			List<ModChangeObjectContainer> modObjects = modXml.getModObjects();
			
			System.out.println("\n\n==========================="
					+ "\n[INFO] - Now loading: " + modXml.modName + ", "
							+ "which contained [" + modObjects.size() + "] overall change requests..."
							+ "\n===========================\n");
			
			for (ModChangeObjectContainer container : modObjects ) {
				handleChangesForMod(container);
				System.out.println("\n\n[INFO] - Completed handling for [file][mod_name][mod_section]: ["
						+ container.getFileToMod() + "][" + container.getModName() + "][" + container.getModSegment() + "]\n\n");
			}
		}
	}

	/**
	 * Specifically modify the original element that each mod requests
	 * 
	 * @param modChangeRequestContainer
	 */
	public void handleChangesForMod (ModChangeObjectContainer modChangeRequestContainer) {
		Element parentToMod = getExpectedParentElement(modChangeRequestContainer);
		parentToMod = removeModElementsIfPresent(modChangeRequestContainer.getModName(), parentToMod);
		parentToMod = addModElements(modChangeRequestContainer, parentToMod);
	}


	/**
	 * Accepts the mod element that holds the parent targeting, 
	 * which it will extract from the DOM passed<br>
	 * <br>
	 * Incoming mod element has attributes, e.g. for a Facilities mod:<br>
	 * parent_tag="Prefab"<br>
	 * parent_name="warehouse.workshop"<br>
	 * 
	 * @param domToAddModElementsTo
	 * @param modContainerElement
	 */
	public Element getExpectedParentElement(ModChangeObjectContainer modContainerElement) {

		Element domToAddTo = (modContainerElement.getFileToMod()
				.equals("FacilitiesData")) ? facilitiesXmlFileDOM.getRootElement() : missionXmlFileDOM.getRootElement();

		String parentTag = modContainerElement.getParentTag();
		String parentName = modContainerElement.getParentName();


		@SuppressWarnings("unchecked")
		List<Element> domParentElement = (!StringUtils.isBlank(parentName)) 
				? domToAddTo.selectNodes("//" + parentTag + "[@Name='" + parentName + "']/Objects") : domToAddTo.selectNodes("//" + parentTag);

				if (domParentElement.size() > 1) {
					// In theory this shouldn't happen...but the element quantity is reasonably enormous so I'll assume my sample may be faulty
					System.out.println("[WARNING] - Quantity of potential parent elements was > 1 - "
							+ "Since only a person running from source would see this: "
							+ "please check your parent element targeting in the original mission or facilities file "
							+ "and assure that the name is a unique key");
				}

				if (debug) { System.out.println(domParentElement); };
				
				if (domParentElement == null || domParentElement.isEmpty()) {
					throw new NullPointerException("Could not find domParentElement for: \n" 
							+ modContainerElement + "\nIn DOM: \n" + domToAddTo);
				}
				// Relies on the assumption we only get one - merely allowing us to circumvent wielding an array
				return domParentElement.get(0);
	}

	/**
	 * Dump old versions of Elements, so long as you named them in the expected fashion with your mod_name
	 * One presumes that any version prior to this manager will require manual removal, of course
	 * 
	 * @param modName
	 * @param parentToRemoveFrom
	 * @return
	 */
	public Element removeModElementsIfPresent (String modName, Element parentToRemoveFrom) {

		@SuppressWarnings("unchecked")
		List<Element> potentialOldElements = parentToRemoveFrom.selectNodes("//*[@mod_name='" + modName + "']");

		if (potentialOldElements != null & !potentialOldElements.isEmpty()) {
			for (Element elementToRemove : potentialOldElements) {
				elementToRemove.detach();
			}
		}

		return parentToRemoveFrom;
	}

	/**
	 * The direct addition of the requested changes into the parent. <br>
	 * <br>
	 * Simply prepends all objects from the ModXML into the targetted parent element.<br>
	 * One additional operation is to prepend/append each side of the new elements
	 * with indicators of what mod they came from, and the author, 
	 * so it's much easier to see what's already in there.
	 * 
	 * @param modContainerElement
	 * @param parentToAddTo
	 * @return
	 */
	public Element addModElements (ModChangeObjectContainer modContainerElement, Element parentToAddTo) {

		System.out.println("[INFO] - Adding Elements to " + parentToAddTo.getPath() + " for: \n" + modContainerElement.toString());
		
		String modName = modContainerElement.getModName();
		List<Element> modChildObjectsToAdd = modContainerElement.getChildElements();
		
		if (debug) {
			System.out.println("[INFO] - modChildObjectsToAdd contained "
					+ "[" + modChildObjectsToAdd.size() + "]\n\n");
		}
		
		/* 
		 * Might look a little weird, but these do some wrapping to the mod data 
		 * to identify it visually when browsing the physical files; 
		 * that goes for the builder, wrapper doc, and the prepends surrounding the actual mod data.
		 */
		String modWrapperBuilder = "ModWrapper mod_name=\"" + modName 
		+ "\" mod_author=\"" + modContainerElement.getModAuthor() 
		+ "\" mod_segment=\"" + modContainerElement.getName() 
		+ "\" ";

		parentToAddTo.addElement(modWrapperBuilder + "placement=\"start_tag\" />");
		
		for (Element element : modChildObjectsToAdd) {
			element.addAttribute("mod_name", modName);
			
			parentToAddTo.addElement(element.asXML().replaceAll("^<(.*)/>$", "$1"));
		}

		parentToAddTo.addElement(modWrapperBuilder + "placement=\"end_tag\" ");

		return parentToAddTo;
	}
}

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

public class ModXMLHelper {

	String xmlModsFolder = "/XmlMods";

	// Until I have a fuller UI, this will service skipping mods - woe be to those with a fear of manual labor :)
	String xmlModsExclusionsFile = "/XmlMods/exclusions.txt";
	String missionXMLTarget = "/Levels/Class3/mission_mission0.xml";
	String facilitiesXMLTarget = "/Libs/Prefabs/facilities.xml";
	String originalMissionXMLTarget = "/Levels/Class3/mission_mission0.original.xml";
	String originalFacilitiesXMLTarget = "/Libs/Prefabs/facilities.original.xml";

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
	
	/**
	 * @param pageSource
	 * @return
	 * @throws Exception 
	 */
	public Document buildXMLFromString (String xmlData) throws Exception {
		if (xmlData == null) {
			System.out.println("[ERROR] - XML String was empty - nothing to build!");
			throw new Exception("XML String was empty - nothing to build!");
		}

		return Jsoup.parse(xmlData, "", Parser.xmlParser());
	}

	public Document getXmlFileAndBuild (String fileTarget) throws Exception {
		if (fileTarget == null) {
			System.out.println("[ERROR] - XML file target was empty - nothing to build!");
			throw new Exception("XML String was empty - nothing to build!");
		}

		return Jsoup.parse(FileUtil.readFileContents(fileTarget), "", Parser.xmlParser());
	}

	//TODO Need to first build up the XML file then we can handle the nodes and sort
	public String orderByPosition (String axisToOrderBy) {
		String xml = "";

		switch (axisToOrderBy) {
		case "x":
			//Something
			break;
		case "y":
			//Something
			break;
		case "z":
			//Something
			break;
		default:
			//Something
			break;
		}

		return xml;
	}

	public void findAllXmlModFilesAndBuildThem () throws Exception {

		Iterator<File> iterator = FileUtils.iterateFiles(FileUtils.getFile(xmlModsFolder), new String[]{".xml"}, false);
		while (iterator.hasNext()) {
			modXMLs.add(buildModXMLDOM(iterator.next().getPath()));
		}
	}

	public ModXML buildModXMLDOM (String modFileTarget) throws Exception {

		return new ModXML(getXmlFileAndBuild(modFileTarget));
	}

	public void handleAllMods () {
		for (ModXML modXml : modXMLs) {
			for (ModChangeObjectContainer container : modXml.getModObjects() ) {
				handleChangesForMod(container);
			}
		}
	}
	
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

		Document domToAddTo = (modContainerElement.getTagName()
				.equals("FacilitiesData")) ? facilitiesXmlFileDOM : missionXmlFileDOM;
		Elements domParentElement = domToAddTo.select(modContainerElement.getParentTag()
				+ "[name=" + modContainerElement.getParentName() + "]");

		if (domParentElement.size() > 1) {
			// In theory this shouldn't happen...but the element quantity is reasonably enormous so I'll assume my sample may be faulty
			System.out.println("[WARNING] - Quantity of potential parent elements was > 1 - "
					+ "Since only a person running from source would see this: "
					+ "please check your parent element targeting in the original mission or facilities file "
					+ "and assure that the name is a unique key");
		}

		// Relies on the assumption we only get one - merely allowing us to circumvent wielding an array
		return domParentElement.first();
	}
	
	public Element removeModElementsIfPresent (String modName, Element parentToRemoveFrom) {
		
		parentToRemoveFrom.select("[mod_name=" + modName + "]").remove();
		return parentToRemoveFrom;
	}
	
	public Element addModElements (ModChangeObjectContainer modContainerElement, Element parentToAddTo) {
		
		/* 
		 * Might look a little weird, but these do some wrapping to the mod data 
		 * to identify it visually when browsing the physical files; 
		 * that goes for the builder, wrapper doc, and the prepends surrounding the actual mod data.
		 */
		String modWrapperBuilder = "<ModWrapper mod_name=\"" + modContainerElement.getModName() 
		+ "\" mod_author=\"" + modContainerElement.getModAuthor() 
		+ "\" mod_segment=\"" + modContainerElement.getName() 
		+ "\" ";
		
		Document modWrapper = new Document(
			modWrapperBuilder + "placement=\"start_tag\" />" 
			+ modWrapperBuilder + "placement=\"end_tag\" />");
		
		parentToAddTo.prepend(modWrapper.select("ModWrapper").first().toString());
		
		for (Element element : modContainerElement.getChildElements()) {
			parentToAddTo.prepend(element.toString());
		}
		
		parentToAddTo.prepend(modWrapper.select("ModWrapper").last().toString());
		
		return parentToAddTo;
	}
}

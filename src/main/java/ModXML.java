import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Objective: <br>
 * Provide Class representing the established template for a ModXML, 
 * which contains a specified format to request changes to the main Mission_Mission0.xml, 
 * or the Facilities.xml. <br>
 * <br>
 * These XML files should allow us to have multiple mods of these types, 
 * rather than being stuck with a single mod, given that they overwrite these files. <br>
 * <br>
 * @See /SoDModManager/XmlMods/CopyMe-ModXMLTemplate.xml for the expected structure.
 * @author Phacops
 */
public class ModXML {
	
	Document modXmlFileDOM;
	String modName;
	String modAuthor;
	List<ModChangeObjectContainer> modObjects = new ArrayList<>();
	
	/**
	 * Quick builder
	 * 
	 * @param modDom
	 */
	public ModXML (Document modDom) {
		this.modXmlFileDOM = modDom;
		this.modName = modDom.select("ModData").attr("mod_name");
		this.modAuthor = modDom.select("ModData").attr("mod_author");
		setModObjects();
	}
	
	@Override
	public String toString() {
		return this.modName + " by " + this.modAuthor + ", requests the following changes: \n" 
				+ modObjects.toString() + "";
	}
	
	/**
	 * Build the objects separately for facilities and mission
	 */
	private void setModObjects () {
		setModFacilityObjects();
		setModMissionObjects();
	}
	
	/**
	 * Create mission-related objects and add them to our list of all objects
	 */
	private void setModMissionObjects () {
		for (Element objectsChild : this.modXmlFileDOM.getElementsByTag("MissionData").select("Objects")) {
			modObjects.add(new ModChangeObjectContainer(this.modName, this.modAuthor, "MissionData", objectsChild));
		}
	}
	
	/**
	 * Create mission-related objects and add them to our list of all objects
	 */
	private void setModFacilityObjects () {
		for (Element objectsChild : this.modXmlFileDOM.getElementsByTag("FacilitiesData").select("Objects")) {
			modObjects.add(new ModChangeObjectContainer(this.modName, this.modAuthor, "FacilitiesData", objectsChild));
		}
	}
	
	/**
	 * Get the list of all objects this mod has requested be applied to their designated files,
	 * including both facilities-related and mission related items.
	 * 
	 * @return
	 */
	public List<ModChangeObjectContainer> getModObjects () {
		return this.modObjects;
	}
}

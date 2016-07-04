import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

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
		this.modName = modDom.selectSingleNode("ModData").valueOf("@mod_name");
		this.modAuthor = modDom.selectSingleNode("ModData").valueOf("@mod_author");
		setModObjects();
	}
	
	@Override
	public String toString() {
		return this.modName + " by " + this.modAuthor + ", requests the following changes: \n" 
				+ modObjects.toString() + "";
	}
	
	/**
	 * Build the objects separately for cleanup, facilities, and mission
	 * Note: Any new elements must be set here explicitly - 
	 * just an attempt at greater integrity by not parsing unwanted code
	 */
	private void setModObjects () {
		setModCleanupObjects();
		setModMissionObjects();
		setModFacilityObjects();
	}
	
	/**
	 * Create mission-related objects and add them to our list of all objects
	 */
	private void setModMissionObjects () {
		@SuppressWarnings("unchecked")
		List<Element> missionElements = this.modXmlFileDOM.selectNodes("//ModData/MissionData/Objects");
		System.out.println("\nNumber of Mission Elements in " + this.modName + " - [" + missionElements.size() + "]");
		
		for (Node objectsChild : missionElements) {
			System.out.println("[" + this.modName + "] - [" + objectsChild.getPath() + "]");
			modObjects.add(new ModChangeObjectContainer(this.modName, this.modAuthor, "MissionData", objectsChild));
		}
		
	}
	
	/**
	 * Create mission-related objects and add them to our list of all objects
	 */
	private void setModFacilityObjects () {
		@SuppressWarnings("unchecked")
		List<Element> facilitiesElements = this.modXmlFileDOM.selectNodes("//ModData/FacilitiesData/Objects");
		System.out.println("\nNumber of Facilities Elements in " + this.modName + " - [" + facilitiesElements.size() + "]");
		
		for (Node objectsChild : facilitiesElements) {
			System.out.println("[" + this.modName + "] - [" + objectsChild.getPath() + "]");
			modObjects.add(new ModChangeObjectContainer(this.modName, this.modAuthor, "FacilitiesData", objectsChild));
		}
	}
	
	/**
	 * Create cleanup-related objects and add them to our list of all objects
	 * These will be iterated for destroying deprecated elements in the game files before applying the new
	 * This is only if a mod changes names during the course of development, 
	 * otherwise all elements are naturally destroyed before applying the mod.
	 */
	private void setModCleanupObjects () {
		@SuppressWarnings("unchecked")
		List<Element> cleanupElements = this.modXmlFileDOM.selectNodes("//ModData/CleanupOldModNames/Objects");
		System.out.println("\nNumber of Cleanup Elements in " + this.modName + " - [" + cleanupElements.size() + "]");
		
		for (Node objectsChild : cleanupElements) {
			System.out.println("[" + this.modName + "] - [" + objectsChild.getPath() + "]");
			modObjects.add(new ModChangeObjectContainer(this.modName, this.modAuthor, "CleanupOldModNames", objectsChild));
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

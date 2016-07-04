import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.Node;

/**
 * Objective: <br>
 * Provide a simple container to assign attributes and methods to that represents each segment of a mod. <br>
 *  <br>
 * Analysis:  <br>
 * Contains metadata for the overall mod, specifically the mod name and the author, 
 * so that it can apply this to wrappers and the physical elements, so that modders don't have to. <br>
 * <br>
 * Simple application is ease of querying the requested details and minimizing workload for larger mods.<br>
 * <br>
 * @author Phacops
 */
public class ModChangeObjectContainer {
	String modName;
	String modAuthor;
	String fileToMod;
	String name;
	String modSubSegment;
	String parentTag;
	String parentName;
	
	List<Element> childElements = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	public ModChangeObjectContainer (String modName, String modAuthor, String fileToMod, Node containerElement) {
		this.modName = modName;
		this.modAuthor = modAuthor;
		this.fileToMod = fileToMod;
		this.name = containerElement.valueOf("@name");
		this.modSubSegment = containerElement.valueOf("@mod_subsegment");
		this.parentTag = containerElement.valueOf("@parent_tag");
		this.parentName = containerElement.valueOf("@parent_name");
		
		if (fileToMod.equals("CleanupOldModNames")) {
			this.childElements = containerElement.selectNodes("//Objects/descendant::*");
		} else {
			this.childElements = containerElement.selectNodes("//Objects[@name='" + this.name + "']/descendant::*");
		}
	}
	
	public String getModName() {
		return modName;
	}

	public String getModAuthor() {
		return modAuthor;
	}
	
	public String getFileToMod() {
		return fileToMod;
	}
	
	public String getName() {
		return name;
	}

	public String getModSubSegment() {
		return modSubSegment;
	}

	public String getParentTag() {
		return parentTag;
	}

	public String getParentName() {
		return parentName;
	}

	public List<Element> getChildElements() {
		return childElements;
	}
	
	@Override
	public String toString() {
		
		return this.modSubSegment + "\n"
				+ "name: " + this.name + "\n"
				+ "modSegment: " + this.modSubSegment + "\n"
				+ "fileToMod: " + this.fileToMod + "\n"
				+ "parentTag: " + this.parentTag + "\n"
				+ "parentName: " + this.parentName + "\n"
				+ "childElements: " + this.childElements.toString() + "\n\n";
	}
}

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;

public class ModChangeObjectContainer {
	String modName;
	String modAuthor;
	String fileToMod;
	String name;
	String modSegment;
	String parentTag;
	String parentName;
	
	List<Element> childElements = new ArrayList<>();
	
	public ModChangeObjectContainer (String modName, String modAuthor, String fileToMod, Element containerElement) {
		this.modName = modName;
		this.modAuthor = modAuthor;
		this.fileToMod = fileToMod;
		this.name = containerElement.attr("name");
		this.modSegment = containerElement.attr("mod_segment");
		this.parentTag = containerElement.attr("parent_tag");
		this.parentName = containerElement.attr("parent_name");
		
		this.childElements = containerElement.children();
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

	public String getModSegment() {
		return modSegment;
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
		
		return this.modSegment + "\n"
				+ "name: " + this.name + "\n"
				+ "modSegment: " + this.modSegment + "\n"
				+ "parentTag: " + this.parentTag + "\n"
				+ "parentName: " + this.parentName + "\n"
				+ "childElements: " + this.childElements.toString() + "\n\n";
	}
}

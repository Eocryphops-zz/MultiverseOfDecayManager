import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ModXML {
	
	Document modXmlFileDOM;
	String modName;
	String modAuthor;
	List<ModChangeObjectContainer> modObjects = new ArrayList<>();
	
	public ModXML (Document modDom) {
		this.modXmlFileDOM = modDom;
		this.modName = modDom.select("ModData").attr("mod_name");
		this.modAuthor = modDom.select("ModData").attr("mod_author");
		setModObjects();
		
		System.out.println(this.toString());
	}
	
	@Override
	public String toString() {
		return this.modName + " by " + this.modAuthor + ", requests the following changes: \n" 
				+ modObjects.toString() + "";
	}
	
	public void setModObjects () {
		setModFacilityObjects();
		setModMissionObjects();
	}
	
	public void setModMissionObjects () {
		for (Element objectsChild : this.modXmlFileDOM.getElementsByTag("MissionData").select("Objects")) {
			modObjects.add(new ModChangeObjectContainer(this.modName, this.modAuthor, objectsChild));
		}
	}
	
	public void setModFacilityObjects () {
		for (Element objectsChild : this.modXmlFileDOM.getElementsByTag("FacilitiesData").select("Objects")) {
			modObjects.add(new ModChangeObjectContainer(this.modName, this.modAuthor, objectsChild));
		}
	}
	
	public List<ModChangeObjectContainer> getModObjects () {
		return this.modObjects;
	}
}

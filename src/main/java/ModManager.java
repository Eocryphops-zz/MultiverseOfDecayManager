
public class ModManager {
	private static ModXMLHelper modXMLHelper = new ModXMLHelper();
	
	public static void main (String[] args) {
		try {
			modXMLHelper.findAllXmlModFilesAndBuildThem();
			modXMLHelper.handleAllMods(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

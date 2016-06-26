import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * Helper class for file handling
 * 
 * @author Phacops
 */
public class FileUtil {

	public static String readFileContents(String fileTarget) throws IOException {
		return FileUtils.readFileToString(FileUtils.getFile(fileTarget), "UTF-8");
	}
}
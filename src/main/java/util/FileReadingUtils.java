package util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

public class FileReadingUtils {
	public static String readFile(String path) throws IOException {
		Charset encoding = StandardCharsets.UTF_8;
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static boolean doesFileExist(String strPathToFile) {
		File f = new File(strPathToFile);

		if (f.exists()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String getCurrentPath()
	{
		Path currentRelativePath = Paths.get("");
		return(currentRelativePath.toAbsolutePath().toString());
	}

	public static void writeFile(String fileNameAndPath, byte[] byteArray)
	{
		try {
			FileUtils.writeByteArrayToFile(new File(fileNameAndPath), byteArray);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeTextFile(String fileNameAndPath, String fileContext)
	{
		try {
			FileUtils.writeStringToFile(new File(fileNameAndPath), fileContext, Charset.defaultCharset());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}

package util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pojos.DocumentType;
import pojos.Documents;

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
	
	public static DocumentType readDocumentType(File file)
	{
	    Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String strFile =  null;

		if(file.exists() == false)
		{
			return null;
		}
		
		try {
			strFile = FileUtils.readFileToString(file, Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		DocumentType docType = gson.fromJson(strFile, DocumentType.class);
		
		return docType;
	}
	
	public static List<DocumentType> readDocumentTypes(String strFolderPath)
	{
		List<DocumentType> listOfDocumentTypes = new Vector<DocumentType>(10);
		File fileFolderPath = new File(strFolderPath);
		if(fileFolderPath == null || fileFolderPath.exists() == false || fileFolderPath.isDirectory() == false)
		{
			return null;
		}
		
		for(File file : fileFolderPath.listFiles())
		{
			DocumentType doc = readDocumentType(file);
			listOfDocumentTypes.add(doc);
		}
		
		return listOfDocumentTypes;
	}
	
	public static Documents readDocument(File file)
	{
	    Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String strFile =  null;

		if(file.exists() == false)
		{
			return null;
		}
		
		try {
			strFile = FileUtils.readFileToString(file, Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		Documents document = gson.fromJson(strFile, Documents.class);
		
		return document;
	}
	
	public static List<Documents> readDocuments(String strFolderPath)
	{
		List<Documents> listOfDocuments = new Vector<Documents>(50);
		File fileFolderPath = new File(strFolderPath);
		if(fileFolderPath == null || fileFolderPath.exists() == false || fileFolderPath.isDirectory() == false)
		{
			return null;
		}
		
		for(File file : fileFolderPath.listFiles())
		{
			Documents doc = readDocument(file);
			listOfDocuments.add(doc);
		}
		
		return listOfDocuments;
	}
}

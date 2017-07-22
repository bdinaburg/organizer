package util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pojos.DocumentType;
import pojos.Documents;
import pojos.ScannedFiles;
import transientPojos.DocTypes;

public class FileWritingUtils {
	
	/**
	 * opens just the text and meta data portion of the Document, not the scanned files 
	 * @param document
	 */
	public static void openDocument(Documents document, String strFileName)
	{
    	String fileType = ".txt";
	    Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	    String jsonStr = gson.toJson(document);
	    
/*    	String strFileName = FileReadingUtils.getCurrentPath();
    	strFileName = strFileName + "/temp/";
*/    	
	    strFileName = strFileName + document.getID().toString();
    	strFileName = strFileName + fileType;
    	if(FileReadingUtils.doesFileExist(strFileName) == false)
    	{
        	FileReadingUtils.writeTextFile(strFileName, jsonStr);
    	}
    	
    	
    	try
    	{
    		String X86Folder = System.getenv("ProgramFiles(X86)");
    		// get executable of wordpad
	    	String wordPadExecutable = X86Folder + "\\Windows NT\\Accessories\\wordpad.exe";
	    	
	    	File checkWordPadExistence = new File(wordPadExecutable);

	    	if(checkWordPadExistence.exists())
	    	{
		    	// create a process builder that executes wordpad and passes filename as parameter
		    	ProcessBuilder pb = new ProcessBuilder(wordPadExecutable, strFileName);
		    	pb.start();
	    	}
	    	else
	    	{
		    	if (Desktop.isDesktopSupported()) 
		    	{
		    	        File myFile = new File(strFileName);
		   	        	Desktop.getDesktop().edit(myFile);
		    	}
	    	}

    	}
    	catch(Exception anyExc)
    	{
    		System.out.println(anyExc);
    	}

	    

	}

	public static void openScannedDocument(Documents document, String strFileName)
	{
    	Iterator<ScannedFiles> iter = document.getScannedFiles().iterator();
    	ScannedFiles scannedFile =  null;
    	while(iter.hasNext())
    	{
    		scannedFile = iter.next();
    	}
    	openScannedFile(scannedFile, strFileName);
	}
	/**
	 * in order to open a file it writes it out to the disk, and then opens it up with a default editor
	 * @param scannedFile
	 */
	public static void openScannedFile(ScannedFiles scannedFile, String strFileName)
	{

    	String fileType = scannedFile.getFileType();
    	if(fileType == null || fileType.length() < 1)
    	{
    		fileType = ".pdf";
    	}
/*    	String strFileName = FileReadingUtils.getCurrentPath();
    	strFileName = strFileName + "/";
*/
    	strFileName = strFileName + scannedFile.getSHA256HashTotal() + ".";
    	strFileName = strFileName + fileType;
    	if(FileReadingUtils.doesFileExist(strFileName) == false)
    	{
        	FileReadingUtils.writeFile(strFileName, scannedFile.getDocument_inbytearray());
    	}
    	
    	if (Desktop.isDesktopSupported()) {
    	    try {
    	        File myFile = new File(strFileName);
    	        if(strFileName.endsWith("pdf") == true || strFileName.endsWith("mp4") == true)
    	        {
    	        	Desktop.getDesktop().open(myFile);	
    	        }
    	        else
    	        {
    	        	Desktop.getDesktop().edit(myFile);
    	        }
    	        
    	    } catch (IOException ex) {
    	        // no application registered for PDFs
    	    }
    	}
	}

	public static void saveDocumentTypesToDisk(String strFolderToPlaceFiles, List<DocumentType> documentTypeList)
	{
		if(strFolderToPlaceFiles.endsWith("/") || strFolderToPlaceFiles.endsWith("\\"))
		{
			strFolderToPlaceFiles = strFolderToPlaceFiles.substring(0,  strFolderToPlaceFiles.length() - 1);
		}

		
		for(DocumentType  documentType : documentTypeList  )
		{
			try
			{
		    	String fileType = ".json";

				String strFileName =  strFolderToPlaceFiles;
		    	strFileName = strFileName + "/";
		    	strFileName = strFileName + documentType.getId().toString();
		    	
			}
			catch(Exception anyExc)
			{
				System.out.println("File couldn't be written " + documentType.toString());
			}
		}
	}
	
	public static void saveScannedFilesToDisk(String strFolderToPlaceFiles, List<ScannedFiles> listScannedFiles)
	{
		if(strFolderToPlaceFiles.endsWith("/") || strFolderToPlaceFiles.endsWith("\\"))
		{
			strFolderToPlaceFiles = strFolderToPlaceFiles.substring(0,  strFolderToPlaceFiles.length() - 1);
		}

		
		for(ScannedFiles  scannedFile : listScannedFiles  )
		{
			try
			{
		    	String fileType = scannedFile.getFileType();
		    	if(fileType == null || fileType.length() < 1)
		    	{
		    		fileType = ".pdf";
		    	}

				String strFileName =  strFolderToPlaceFiles;
		    	strFileName = strFileName + "/";
		    	strFileName = strFileName + scannedFile.getSHA256HashTotal() + ".";
		    	strFileName = strFileName + fileType;

				FileReadingUtils.writeFile(strFileName, scannedFile.getDocument_inbytearray());
			}
			catch(Exception anyExc)
			{
				System.out.println("File couldn't be written " + scannedFile.toString());
			}
		}
	}
	
	public static void saveDocumentsToDisk(String strFolderToPlaceFiles, List<Documents> listScannedDocuments)
	{
		if(strFolderToPlaceFiles.endsWith("/") || strFolderToPlaceFiles.endsWith("\\"))
		{
			strFolderToPlaceFiles = strFolderToPlaceFiles.substring(0,  strFolderToPlaceFiles.length() - 1);
		}

		
		for(Documents  document : listScannedDocuments  )
		{
			try
			{
		    	String fileType = ".json";

				String strFileName =  strFolderToPlaceFiles;
		    	strFileName = strFileName + "/";
		    	strFileName = strFileName + document.getID().toString();
		    	//document.
		    	//FileUtils.write
			}
			catch(Exception anyExc)
			{
				System.out.println("File couldn't be written " + document.toString());
			}
		}
	}
}

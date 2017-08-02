package mongodbtester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Properties;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import util.JSONBuilder;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Test3 {

	public static void main(String[] args) {
		
		showMetaData();
		String test = getUrlContents("http://nominatim.openstreetmap.org/reverse?format=json&lat=39.936230555555554&lon=-74.07645277777777&zoom=18&addressdetails=1");
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(test);
		String prettyJsonString = gson.toJson(je);		
	    System.out.println(prettyJsonString);
		/*		String test = "[GPS] - GPS Latitude = 39° 56' 10.43";
		if(test.toUpperCase().indexOf("GPS L") > 0 && test.indexOf(" = ") > 0)
		{
			System.out.println("yea!!");
		}*/
		
		/*DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		Calendar cal = Calendar.getInstance();
		String strDate = dateFormat.format(cal.getTime());
		System.out.println(strDate);*/
		//Test2 tst2 = new Test2();
		//System.out.println(tst2.getFile());
		//testDates();
		//writePropertiesFile();
		//checkPath();
	}
	
	public static void checkPath()
	{
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		System.out.println("Current absolute path is: " + s);
	}
	
	public static void testDates()
	{
		HashSet<String> testRoles = new HashSet<String>();
		testRoles.add("role1");
		testRoles.add("role2");
		
		String test = JSONBuilder.buildCreateUserJSON("boris", "password", testRoles);
		
		System.out.println(test);

	}

	public static void writePropertiesFile() {

		Properties prop = new Properties();
		OutputStream output = null;

		try {

			
			output = new FileOutputStream("configuration.properties");

			// set the properties value
			prop.setProperty("hostname", "chaglei.com");
			prop.setProperty("port", "27017");
			prop.setProperty("schema", "admin");
			prop.setProperty("username", "admin");
			prop.setProperty("password", "PA$$Word12345");

			// save properties to project root folder
			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
	
	private String getFile()
	{
		ClassLoader classLoader = getClass().getClassLoader();
		return classLoader.getResource(".").toString();
		
		
	}
	
	public static void showMetaData() {
		final String alphabet = "+-.1234567890";
		File file = new File("c:\\development\\pic.jpg");
		Metadata metadata = null;
		try {
			metadata = ImageMetadataReader.readMetadata(file);
		} catch (ImageProcessingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Directory directory : metadata.getDirectories()) {
			for (Tag tag : directory.getTags()) {
				String strDescription = tag.getDescription();
				String strTagName = tag.getTagName();

				strDescription = String.format("[%s] - %s = %s", directory.getName(), strTagName, strDescription);
				//System.out.format("[%s] - %s = %s", directory.getName(), strTagName, strDescription);
				//strDescription += directory.getName() + strTagName + strDescription;
				System.out.println(strDescription);
				if(strDescription.toUpperCase().indexOf("GPS L") > 0 && strDescription.toUpperCase().indexOf("REF") < 0)
				{
					int position = strDescription.indexOf(" = ");
					position += 3; 	//this should put the positioning marker right before 
												// the first number which should be days
					String strDays = "";
					String strHours = "";
					String strMinutes = "";

					boolean boolExit = false;
					while(position < strDescription.length() - 1 && boolExit != true)
					{
						if(alphabet.contains(new Character(strDescription.charAt(position)).toString()))
						{
							strDays += new Character(strDescription.charAt(position)).toString();
							position++;
						}
						else
						{
							boolExit = true;
						}

					}
					/**
					 * continue moving, we just got first number, keep going through garbage
					 * characters until we encounter a number again
					 */
					boolExit = false;
					while(position < strDescription.length() - 1 && boolExit != true)
					{
						if(alphabet.contains(new Character(strDescription.charAt(position)).toString()) == false)
						{
							position++;
						}
						else
						{
							boolExit = true;
						}
					}
					/**
					 * We should be at the start of a second number at this point, get the minutes
					 */
					boolExit = false;
					while(position < strDescription.length() - 1 && boolExit != true)
					{
						if(alphabet.contains(new Character(strDescription.charAt(position)).toString()))
						{
							strHours += new Character(strDescription.charAt(position)).toString();
							position++;
						}
						else
						{
							boolExit = true;
						}
					}
					
					/**
					 * continue moving, we just got first number, keep going through garbage
					 * characters until we encounter a number again //looking for last number now
					 */
					boolExit = false;
					while(position < strDescription.length() - 1 && boolExit != true)
					{
						if(alphabet.contains(new Character(strDescription.charAt(position)).toString()) == false)
						{
							position++;
						}
						else
						{
							boolExit = true;
						}
					}
					
					/**
					 * We should be at the start of a third number at this point, get the hours
					 */
					boolExit = false;
					while(position < strDescription.length() - 1 && boolExit != true)
					{
						if(alphabet.contains(new Character(strDescription.charAt(position)).toString()))
						{
							strMinutes += new Character(strDescription.charAt(position)).toString();
							position++;
						}
						else
						{
							boolExit = true;
						}
					}
					
					System.out.println("DAYS: " + strDays);
					System.out.println("HOURS: " + strHours);
					System.out.println("MINUTES: " + strMinutes);
					Double dd = Math.signum(Double.parseDouble(strDays)) * (Math.abs(Double.parseDouble(strDays)) + (Double.parseDouble(strHours) / 60.0) + (Double.parseDouble(strMinutes) / 3600.0));
					System.out.println("DECIMAL DEGREES:" + dd);
				}
				
			}
			if (directory.hasErrors()) {
				for (String error : directory.getErrors()) {
					System.err.format("ERROR: %s", error);
				}
			}
		}

	}
	
	
	private static String getUrlContents(String theUrl) {
		StringBuilder content = new StringBuilder();

		// many of these calls can throw exceptions, so i've just
		// wrapped them all in one try/catch statement.
		try {
			// create a url object
			URL url = new URL(theUrl);

			// create a urlconnection object
			URLConnection urlConnection = url.openConnection();

			// wrap the urlconnection in a bufferedreader
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

			String line;

			// read from the urlconnection via the bufferedreader
			while ((line = bufferedReader.readLine()) != null) {
				content.append(line + "\n");
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content.toString();
	}

}

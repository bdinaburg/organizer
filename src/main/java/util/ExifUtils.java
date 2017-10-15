package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.json.JSONObject;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import transientPojos.ExifData;

public class ExifUtils {

	public static ExifData getExifData(String strPathToFile) {
		ExifData exifData = new ExifData();
		File file = new File(strPathToFile);
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

				//System.out.println(strDescription);
				if (strDescription.toUpperCase().indexOf("GPS LATITUDE") > 0 && strDescription.toUpperCase().indexOf("REF") < 0) {
					double dblLatitude = extractLatOrLongitude(strDescription);
					exifData.setDblLatitude(dblLatitude);
				}
				
				if (strDescription.toUpperCase().indexOf("GPS LONGITUDE") > 0 && strDescription.toUpperCase().indexOf("REF") < 0) {
					double dblLongitude = extractLatOrLongitude(strDescription);
					exifData.setDblLongitude(dblLongitude);
				}
				
				if (strDescription.toUpperCase().indexOf("DATE/TIME = ") > 0) {
					Date date = extractDate(strDescription);
					exifData.setCreateDate(date);
				}
				//System.out.println(strDescription);
				
			}
			if (directory.hasErrors()) {
				for (String error : directory.getErrors()) {
					System.err.format("ERROR: %s", error);
				}
			}
		}
		
		String strAddress = ExifUtils.extractAddress(exifData.getDblLatitude(), exifData.getDblLongitude());
		exifData.setStrAddress(strAddress);
		
		return exifData;
	}
	
	private static double extractLatOrLongitude(final String strDescription)
	{
		final String alphabet = "+-.1234567890";
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
		
		//System.out.println("DAYS: " + strDays);
		//System.out.println("HOURS: " + strHours);
		//System.out.println("MINUTES: " + strMinutes);
		Double dd = Math.signum(Double.parseDouble(strDays)) * (Math.abs(Double.parseDouble(strDays)) + (Double.parseDouble(strHours) / 60.0) + (Double.parseDouble(strMinutes) / 3600.0));
		//System.out.println("DECIMAL DEGREES:" + dd);
		return dd;
	}
	
	private static Date extractDate(final String strDescription) {
		Date date = null;
		try {
			String[] strDateAndTime = strDescription.split(" ");
			String[] strDate = strDateAndTime[5].split(":");
			String[] strTime = strDateAndTime[6].split(":");

			date = DateUtil.getDateObject(DateUtil.fromStringToEnum(strDate[1]), strDate[2], strDate[0], strTime[0], strTime[1], strTime[2]);
		} catch (Exception anyExc) {
			anyExc.printStackTrace();
		}

		return date;
	}
	/**
	 * requires internet connection to extract, otherwise return "";
	 * @param dlbLatitude
	 * @param dblLongitude
	 * @return
	 */
	public static String extractAddress(Double dlbLatitude, Double dblLongitude) 
	{
		try
		{
			String test = getUrlContents("http://nominatim.openstreetmap.org/reverse?format=json&lat=" + dlbLatitude.toString() + "&lon=" + dblLongitude.toString() + "&zoom=18&addressdetails=1");
			Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(test);
			String prettyJsonString = gson.toJson(je);		
			
			JSONObject jsonObj = new JSONObject(prettyJsonString);
			String strStreetAddress = jsonObj.getString("display_name");	
			
			return strStreetAddress;		
		}
		catch(Exception anyExc)
		{
			System.err.println(anyExc);
			return null;
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

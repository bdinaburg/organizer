package mongodbtester;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import util.DateUtil;

public class Test2 {

	public static void main(String[] args) {
		//Test2 tst2 = new Test2();
		//System.out.println(tst2.getFile());
		writePropertiesFile();
	}
	
	public static void testDates()
	{
		Date mydate = DateUtil.getDateObject("08/04/1981");
		System.out.println(mydate);

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

}

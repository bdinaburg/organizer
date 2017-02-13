package mongodbtester;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Properties;

import util.JSONBuilder;

public class Test3 {

	public static void main(String[] args) {
		//Test2 tst2 = new Test2();
		//System.out.println(tst2.getFile());
		testDates();
		//writePropertiesFile();
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

}

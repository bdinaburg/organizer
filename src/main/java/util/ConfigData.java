package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigData {

	private static Properties getPropertiesFile() {
		Properties prop = new Properties();
		InputStream inputStream = null;
		try {

			inputStream = new FileInputStream("configuration.properties");
			prop.load(inputStream);
			inputStream.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return prop;
	}

	public static String getHostName() {
		return getPropertiesFile().getProperty("hostname");
	}
	public static String getPort() {
		return getPropertiesFile().getProperty("port");
	}
	public static String getSchema() {
		return getPropertiesFile().getProperty("schema");
	}
	public static String getUserName() {
		return getPropertiesFile().getProperty("username");
	}
	public static String getPassword() {
		return getPropertiesFile().getProperty("password");
	}
	public static String getAuthSchema() {
		return getPropertiesFile().getProperty("authschema");
	}

	
}

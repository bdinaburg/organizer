package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class ConfigData {
	private static Properties prop = null;
	private static Properties getPropertiesFile() {
		if(prop != null)
		{
			return prop;
		}
		prop = new Properties();
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
	
	private static void savePropertiesFile() {
		OutputStream outputStream = null;
		try {

			outputStream = new FileOutputStream("configuration.properties");
			getPropertiesFile().store(outputStream, null);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		finally
		{
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}


	public static String getTempFolder() {
		return getPropertiesFile().getProperty("tempfolder");
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
	
	public static String getLoginCredentialsWidth() {
		return getPropertiesFile().getProperty("loginCredentials.width");
	}

	public static String getLoginCredentialsHeight() {
		return getPropertiesFile().getProperty("loginCredentials.height");
	}
	
	/**
	 * returns the column names of the columns that were last visible
	 * @return
	 */
	public static String getColumns() {
		return getPropertiesFile().getProperty("table.columns");
	}
	
	/**
	 * returns sets the column names of the columns that were last visible and saves their widths
	 * @return
	 */
	public static void setColumns(JTable table) {
		Enumeration<TableColumn> enumer =  table.getColumnModel().getColumns();
		String columns = "";
		while (enumer.hasMoreElements()) {
			TableColumn tableColumn = enumer.nextElement();
			columns += tableColumn.getHeaderValue().toString() + ",";
			setColumnWidth(tableColumn);
		}
		columns = columns.substring(0, columns.length() -1);
		
		getPropertiesFile().setProperty("table.columns", columns);
		savePropertiesFile();	
	}

	/**
	 * returns the width of the column if provided the name of it
	 * meant to be called form setColumns so that it saves the width of all visible columns
	 * it is a little inefficient, but shouldn't be a big deal
	 * @return
	 */
	private static void setColumnWidth(TableColumn tableColumn) {
		String columnName = tableColumn.getHeaderValue().toString();
		Integer columnWidth = tableColumn.getWidth();
		getPropertiesFile().setProperty("table." + columnName, columnWidth.toString());
	}
	

	/**
	 * returns the width of the column if provided the name of it
	 * @return
	 */
	public static String getColumnWidth(String columnName) {
		return getPropertiesFile().getProperty("table." + columnName);
	}
	

	
}

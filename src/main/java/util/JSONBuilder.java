package util;

import java.util.Set;

public class JSONBuilder {

	final static String quote = "\"";
	final static String coma = ",";

	public static String buildCreateUserJSON(final String username, final String password, final Set<String> roles)
	{
/*		{ 					
			createUser: "testuser",
			pwd: "password",
			roles: [
				"readWrite", "read"
				]
		}
*/
		String str = "{";
		str += "createUser: " + quote + username + quote + coma;
		str += "pwd: " + quote + password + quote + coma;
		String strRoles = buildRolesString(roles);
		str += strRoles;
		str += "}";
		
				
		return str;
	}
	
	public static String buildRolesString(Set<String> roles)
	{
		
		String strRoles = "roles: [";
		for(String role : roles)
		{
			strRoles += quote + role + quote + coma;
		}
		
		strRoles = strRoles.substring(0, strRoles.length() - 1); //delete the last coma
		strRoles += "]";
		
		return strRoles;
	}
	
	public static String buildStringToQueryUsersInfo(final String USER_NAME, final String DATABASE, final boolean showPriveleges)
	{
		if(showPriveleges == true)
		{
			String strJSON = new String();
			strJSON = "{";
			strJSON += "usersInfo:  { user: \"" + USER_NAME + "\", db: \""+ DATABASE +"\" },";
			strJSON += "showPrivileges: true";
		    strJSON += "}";
		    return strJSON;
		}
		else
		{
			String strJSON = new String();
			strJSON = "{";
			strJSON += "usersInfo:  { user: \"" + USER_NAME + "\", db: \""+ DATABASE +"\" }";
		    strJSON += "}";
		    return strJSON;
		}
	}
	
	public static String buildUpdateUserRolesJSON(String strUserName, Set<String> roles)
	{
		String str = "{";
		str += "updateUser: " + quote + strUserName + quote + coma;
		String strRoles = buildRolesString(roles);
		str += strRoles;
		str += "}";
		
		return str;
	}
	
	public static String buildDropUserJSON(String strUserName)
	{
		String str = "{";
		str += "dropUser: " + quote + strUserName + quote;
		str += "}";
		
		return str;
	}


}

package transientPojos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class UserDBRoles 
{
	String strUser;
	boolean isGlobalAdmin = false;
	HashMap<String, Set<String>> hashMapDBToRoles = new HashMap<String, Set<String>>();
	
	public UserDBRoles(String user)
	{
		strUser = user;
	}
	
	public void addRole(String db, String role)
	{
		HashSet<String> hashsetRoles = getRoles(db);
		if(hashsetRoles == null)
		{
			hashsetRoles = new HashSet<String>();
		}
		
		hashsetRoles.add(role);
		hashMapDBToRoles.put(db, hashsetRoles);
	}
	
	public HashSet<String> getRoles(String db)
	{
		HashSet<String> hashsetRoles = (HashSet<String>)hashMapDBToRoles.get(db);
		return hashsetRoles;
	}
	
	public Set<String> getDBs()
	{
		return hashMapDBToRoles.keySet();
	}
	
	public String getUser()
	{
		return this.strUser;
	}

	@Override
	public String toString()
	{
		String str = "user: " + getUser();
		str = str + "\n";
		Iterator<String> iter = getDBs().iterator();
		while(iter.hasNext())
		{
			String strDB = iter.next();
			str = str + "db name: " + strDB + " roles --> ";
			Iterator<String> rolesIter = this.getRoles(strDB).iterator();
			while(rolesIter.hasNext())
			{
				String strRole = rolesIter.next();
				str = str + strRole;
				if(rolesIter.hasNext())
				{
					str = str + ", ";
				}
			}
			str = str + "\n";
		}
		
		return str;
	}
	
	public boolean doesUserHaveRole(final String dbName, final String strRoleToCheck)
	{
		HashSet<String> setRoles = getRoles(dbName);
		if (setRoles == null)
		{
			return false;
		}
		for(String strRole : setRoles)
		{
			if(strRole.equalsIgnoreCase(strRoleToCheck))
			{
				return true;
			}
		}
		return false;
		
	}
	
	public void setIsGlobalAdmin(boolean boolGlobalAdmin)
	{
		this.isGlobalAdmin = true;
	}
	/**
	 * setting that he is a global admin is not enough, should also make sure he actually has the needed roles
	 * @return
	 */
	public boolean getIsGlobalAdmin()
	{
		if(doesUserHaveRole("admin", "root") == true)
		{
			return isGlobalAdmin;
		}
		else 
		{
			return false;
		}
	}
}

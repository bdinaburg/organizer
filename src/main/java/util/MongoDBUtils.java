package util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import transientPojos.UserDBRoles;


public class MongoDBUtils {
	public static final String DBNAM_AUTH = "admin";
	public static final String DOCUMENT_DESCRIPTION_STORE_NAME = "documents";
	public static final String BINARY_DOCUMENTS_STORE_NAME = "binary_documents";

	
	public static MongoClient connectToMongoDB(String user, char[] password, String dbschema, String hostname, String port)
	{
		MongoClient mongoClient = null;
		try
		{
			int iPort = Integer.parseInt(port);
			List<ServerAddress> listServerAddresesSeeds = new ArrayList<ServerAddress>();
			listServerAddresesSeeds.add(new ServerAddress(hostname, iPort));
			List<MongoCredential> listMongoCredentials = new ArrayList<MongoCredential>();
			MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(user, dbschema, password);
			listMongoCredentials.add(mongoCredential);
			mongoClient = new MongoClient(listServerAddresesSeeds, listMongoCredentials);
		}
		catch(Exception anyExc)
		{
			System.out.println(anyExc);
			if(mongoClient != null)
			{
				mongoClient.close();
				mongoClient = null;
			}
		}
		return mongoClient;
	}
	
	public static boolean checkConnection(MongoClient mongoClient, final String DOCUMENTS_DATABASE_NAME, final String strCollectionToCount)
	{
		try
		{
			MongoDatabase database = mongoClient.getDatabase(DOCUMENTS_DATABASE_NAME);
			MongoCollection<Document> collection = database.getCollection(strCollectionToCount);
			long count = collection.count();
			if(count > 0)
			{
				return true;
			}
		}
		catch(Exception e)
		{
			return false;
		}
		return false;
	}
	
	public static Vector<UserDBRoles> getUserDBRolesOutOfDocument(Iterable<?> iterationOfDocuments)
	{
	    Iterator<?> iter = iterationOfDocuments.iterator();
	    /**
	     * at this point we have all the users inside the iteration
	     */
	    Vector<UserDBRoles> vectorUserDBRoles =  new Vector<UserDBRoles>();
	    while(iter.hasNext())
	    {
	    	Document document = (Document) iter.next();
	    	Set<Entry<String, Object>> set = document.entrySet();
	    	Iterator<Entry<String, Object>> setIter = set.iterator();

	    	String strUser = null;
    		ArrayList<?> arrayListRoles = null;
    		/**
    		 * looping through user properties
    		 * At this point user Documents have other documents and also key values such as _id=admin.admin, user=admin, db=admin,
    		 */
    		boolean breakout = false;
    		while(setIter.hasNext() && breakout == false)
	    	{
	    		Entry<String,Object> entry = setIter.next();
	    		Object objKey = entry.getKey();
	    		if(objKey instanceof String)
	    		{
	    			if(((String) objKey).equalsIgnoreCase("user")) //username found
	    			{
	    				strUser = entry.getValue().toString();
	    			}
	    			else if (((String) objKey).equalsIgnoreCase("roles"))//userroles found
	    			{
	    				arrayListRoles = (ArrayList<?>)entry.getValue();
	    			}
	    			
	    			/**
	    			 * if we have the user and the roles there no need to look at anything else, break out next iteration
	    			 */
	    			if(strUser != null && arrayListRoles !=null)
	    			{
	    				breakout = true;
	    			}
	    		}
	    	}
			UserDBRoles userDBRoles = unwrapRoles(arrayListRoles, strUser);
			vectorUserDBRoles.add(userDBRoles);
	    }

	    return vectorUserDBRoles;

	}
	
	private static UserDBRoles unwrapRoles(ArrayList<?> arrayListRoles, String username)
	{
		UserDBRoles userDBRole = new UserDBRoles(username);
		Iterator<?> iter = arrayListRoles.iterator();
		while(iter.hasNext())
		{
			Object mysteryObject = iter.next();
			if(mysteryObject instanceof Document)
			{
				Document documentRole = (Document)mysteryObject;
				String strRole = documentRole.get("role").toString();
				String strDB = documentRole.get("db").toString();
				userDBRole.addRole(strDB, strRole);
			}
		}
		
		return userDBRole;
	}
	
	public static UserDBRoles getRolesForUser(final MongoClient MONGO_CLIENT, final String LOOKUP_DATABASE_NAME, final String DATA_DATABASE_NAME, final String USERNAME)
	{
	    MongoDatabase mongoDatabase = MONGO_CLIENT.getDatabase(LOOKUP_DATABASE_NAME);
	    String str_json = JSONBuilder.buildStringToQueryUsersInfo(USERNAME, DATA_DATABASE_NAME, false); 
	    final Document doc = Document.parse(str_json);
	    Document documentUsersInfo = mongoDatabase.runCommand(doc);

		Object object = documentUsersInfo.get("users");
		Iterable<?> iterationOfDocuments = null;
		if(object instanceof Iterable<?>)
		{
			iterationOfDocuments = (Iterable<?>) object;
		}
		
		Vector<UserDBRoles> vectorUserDBRoles = MongoDBUtils.getUserDBRolesOutOfDocument(iterationOfDocuments);
		if(vectorUserDBRoles == null || vectorUserDBRoles.size() != 1)
		{
			return null;
		}
		return vectorUserDBRoles.get(0);
		
	}
	
	public static Vector<UserDBRoles> getRolesForAllUsers(final MongoClient MONGO_CLIENT, final String DATABASE_NAME)
	{
	    MongoDatabase mongoDatabase = MONGO_CLIENT.getDatabase(DATABASE_NAME);
	    MongoCollection<Document> collectionOfUsers = mongoDatabase.getCollection("system.users");
	    FindIterable<Document> findInterable = collectionOfUsers.find();
	    Iterable<?> iterableObject = findInterable;
	    /**
	     * at this point we have all the users inside the iteration
	     */
	    Vector<UserDBRoles> vectorUserDBRoles = MongoDBUtils.getUserDBRolesOutOfDocument(iterableObject);

	    Iterator<UserDBRoles> iter = vectorUserDBRoles.iterator();
	    while(iter.hasNext())
	    {
	    	UserDBRoles userDBRole = (UserDBRoles)iter.next();
	    	System.out.println(userDBRole.toString());
	    }

		return vectorUserDBRoles;
		
	}

	
	public static void createUser(final MongoClient mongoClient, final String database, final String userName, final String password, final Set<String> setRoles)
	{
		MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
		String strJSON = JSONBuilder.buildCreateUserJSON(userName, password, setRoles);
		final Document doc = Document.parse(strJSON);
		Document documentOutput = mongoDatabase.runCommand(doc);
		System.out.println(documentOutput);
	}
	

	
}

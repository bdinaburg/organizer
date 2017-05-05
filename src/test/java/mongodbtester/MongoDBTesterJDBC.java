package mongodbtester;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.MapperOptions;
import org.mongodb.morphia.query.Query;

import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

import pojos.DocumentType;
import pojos.Documents;
import pojos.ScannedFiles;
import transientPojos.DocTypes;
import transientPojos.DocTypes.DocType;
import transientPojos.UserDBRoles;
import util.DateUtil;
import util.FileReadingUtils;
import util.MongoDBUtils;

public class MongoDBTesterJDBC {
	public static final String DATABASE_NAME = "borisStorage";
	public static final String DOCUMENT_DESCRIPTION_STORE_NAME = "documents";
	public static final String BINARY_DOCUMENTS_STORE_NAME = "binary_documents";
	public static final String USER = "admin";
	public static final String DB_NAME = "admin";
	public static String password = "PA$$Word12345";
	
/**
 * adds a role of userAdmin to 
 * 
 * db.runCommand( { grantRolesToUser: "boris",
                 roles: [
                    { role: "userAdmin", db: "borisStorage"}

                        ]
                } )
 * 	
 * @param args
 */
	
	/**
	 *
	 *Query information about a user
	 * 
	 * 
	 * db.runCommand(
               {
                 usersInfo:  { user: "boris", db: "borisStorage" },
                 showPrivileges: true
               }
)

	 * 
	 * @param args
	 */
	
	public static String contructJSONString(final String USER_NAME, final String DATABASE, boolean showPriveleges)
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
	
	public static void main(String args[])
	{
		MongoClient mongoClient = MongoDBUtils.connectToMongoDB("admin", password.toCharArray(), "admin", "chaglei.com", "27017");
		//MongoDBUtils.getRolesForAllUsers(mongoClient, "admin");
		queryDocumentById(null);
		//UserDBRoles userDBRoles = MongoDBUtils.getRolesForUser(mongoClient, "borisStorage", "borisStorage", "boris");
		//System.out.println(userDBRoles);
		mongoClient.close();
	}
	
	public static void querySystemUsers()
	{
		MongoClient mongoClient = MongoDBUtils.connectToMongoDB("admin", password.toCharArray(), DB_NAME, "chaglei.com", "27017");
	    MongoDatabase mongoDatabase = mongoClient.getDatabase("admin");
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
	    		
	    mongoClient.close();

	}
	
	public static void queryDocumentById(ObjectId objectId)
	{
		MongoClient mongoClient = MongoDBUtils.connectToMongoDB("admin", password.toCharArray(), DB_NAME, "chaglei.com", "27017");
	    MongoDatabase mongoDatabase = mongoClient.getDatabase("borisStorage");
	    MongoCollection<Document> collectionOfDocuments = mongoDatabase.getCollection("documents");
	    ObjectId objid = new ObjectId("588ecbc47ce9482758a3e99c");
	    
	    Document doc = collectionOfDocuments.find(Filters.eq("_id", objid)).first();

	    Gson gson = new Gson();
	    
	    String jsonStr = gson.toJson(doc);
	    System.out.println(jsonStr);
	    
	    //jsonStr = gson.toJson(objid);
	    //System.out.println(jsonStr);
	    
	    
	    		
	    mongoClient.close();
	}

	

	
	
	
}

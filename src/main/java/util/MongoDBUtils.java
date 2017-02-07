package util;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class MongoDBUtils {
	public static final String DBNAM_AUTH = "admin";
	public static final String DOCUMENTS_DATABASE_NAME = "borisStorage";
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
	
	public static boolean checkConnection(MongoClient mongoClient)
	{
		try
		{
			MongoDatabase database = mongoClient.getDatabase(DOCUMENTS_DATABASE_NAME);
			MongoCollection<Document> collection = database.getCollection("document_type");
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
}

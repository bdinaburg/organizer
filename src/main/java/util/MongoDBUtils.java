package util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;

import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.MapperOptions;
import org.mongodb.morphia.query.Query;

import com.mongodb.DBRefCodecProvider;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import pojos.DocumentType;
import pojos.Documents;
import pojos.ScannedFiles;
import transientPojos.DocTypes;
import transientPojos.UserDBRoles;


public class MongoDBUtils {
	
	private static Morphia morphia;
	private static MapperOptions mapperOptions;
	
	public static MongoClient connectToMongoDB(String user, char[] password, String dbschema, String hostname, String port)
	{
		MongoClient mongoClient = null;
		try
		{
		    CodecRegistry registry =  MongoClient.getDefaultCodecRegistry();
	                	
			MongoClientOptions settings = MongoClientOptions.builder().readPreference(ReadPreference.primary())
				    .codecRegistry(registry).build();

			int iPort = Integer.parseInt(port);
			List<ServerAddress> listServerAddresesSeeds = new ArrayList<ServerAddress>();
			listServerAddresesSeeds.add(new ServerAddress(hostname, iPort));
			List<MongoCredential> listMongoCredentials = new ArrayList<MongoCredential>();
			MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(user, dbschema, password);
			listMongoCredentials.add(mongoCredential);
			mongoClient = new MongoClient(listServerAddresesSeeds, listMongoCredentials, settings);
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
	
	public static boolean setRolesForUser(final MongoClient mongoClient, final String strDatabaseWhereToUpdateUser, final String strUserName , final Set<String> setRoles)
	{
	    MongoDatabase mongoDatabase = mongoClient.getDatabase(strDatabaseWhereToUpdateUser);
	    String str_json = JSONBuilder.buildUpdateUserRolesJSON(strUserName, setRoles); 
	    final Document doc = Document.parse(str_json);
	    Document documentOutput;
	    try
	    {
	    	documentOutput = mongoDatabase.runCommand(doc);
	    }
	    catch(Exception anyExc)
	    {
	    	return false;
	    }
	    
	    
	    if(documentOutput.toString().indexOf("ok=1.0") > 0 && documentOutput.toString().length() < 30)
	    {
	    	return true;
	    }
	    else
	    {
	    	return false;
	    }
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

	
	public static boolean createUser(final MongoClient mongoClient, final String database, final String userName, final String password, final Set<String> setRoles)
	{
		MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
		String strJSON = JSONBuilder.buildCreateUserJSON(userName, password, setRoles);
		final Document doc = Document.parse(strJSON);
		Document documentOutput = mongoDatabase.runCommand(doc);
	    if(documentOutput.toString().indexOf("ok=1.0") > 0 && documentOutput.toString().length() < 30)
	    {
	    	return true;
	    }
	    else
	    {
	    	return false;
	    }
	}
	
	public static boolean deleteUser(final MongoClient mongoClient, final String database, final String userName)
	{
		MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
		String strJSON = JSONBuilder.buildDropUserJSON(userName);
		final Document doc = Document.parse(strJSON);
		Document documentOutput = mongoDatabase.runCommand(doc);
	    if(documentOutput.toString().indexOf("ok=1.0") > 0 && documentOutput.toString().length() < 30)
	    {
	    	return true;
	    }
	    else
	    {
	    	return false;
	    }
	}
	
	public static void deleteScannedFiles(final MongoClient mongoClient, final String database, List<ScannedFiles> listScannedFiles)
	{
		Morphia localMorphia = getMorphia();
		final Datastore datastore = localMorphia.createDatastore(mongoClient, database);
		datastore.ensureIndexes();
		if(listScannedFiles == null)
		{
			return;
		}
		for(ScannedFiles scannedFile : listScannedFiles) 
		{
			try
			{
				datastore.delete(scannedFile);				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	public static void deleteDocument(final MongoClient mongoClient, final String database, Documents document)
	{
		Morphia localMorphia = getMorphia();
		final Datastore datastore = localMorphia.createDatastore(mongoClient, database);
		datastore.ensureIndexes();
		if(document == null)
		{
			return;
		}
			try
			{
				datastore.delete(document);				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
	
	}
	
	public static List<Documents> getDocuments(final MongoClient mongoClient, final String database)
	{
		Morphia localMorphia = getMorphia();
		final Datastore datastore = localMorphia.createDatastore(mongoClient, database);
		datastore.ensureIndexes();

		final Query<Documents> query = datastore.createQuery(Documents.class);
		List<Documents> listOfDocuments = query.asList();
		
		return listOfDocuments;
	}
	
	public static List<DocumentType> getDocTypes(final MongoClient mongoClient, final String database)
	{
		Morphia localMorphia = getMorphia();
		final Datastore datastore = localMorphia.createDatastore(mongoClient, database);
		datastore.ensureIndexes();

		final Query<DocumentType> query = datastore.createQuery(DocumentType.class);
		List<DocumentType> listOfDocTypes = query.asList();
		
		return listOfDocTypes;
	}

	/**
	 * generates document, without the scanned file
	 * 
	 * @param txtName
	 * @param strInsertDate
	 * @param strDueDate
	 * @param strAmount
	 * @param strPaidDate
	 * @param strCurrency
	 * @param strDescription
	 * @param documentType
	 * @return
	 */
	public static Documents saveDocument(	String txtName, String strDocumentCreateDate, String strDueDate, 
										String strAmount, String strPaidDate, String strCurrency,
										String strDescription, DocumentType documentType,  
										MongoClient mongoClient, String database)
	{
		Morphia localMorphia = getMorphia();
		final Datastore datastore = localMorphia.createDatastore(mongoClient, database);
		datastore.ensureIndexes();
		
		ScannedFiles scannedFile = saveScannedFile(txtName, datastore);
		
		if(scannedFile == null)
		{
			return null;
		}
		
		Documents document = new Documents();
		document.setStrName(txtName);
		document.setDate_document_creation(DateUtil.getDateObject(strDocumentCreateDate));
		document.setDate_due_date(DateUtil.getDateObject(strDueDate));
		document.setBigDecimalAmount(new BigDecimal(strAmount));
		document.setDate_paid_date(DateUtil.getDateObject(strPaidDate));
		document.setCurrency(Currency.getInstance(strCurrency));
		document.setStrDocumentDescription(strDescription);
		document.setDocumentType(documentType);
		document.addScannedFile(scannedFile);
		
		datastore.save(document);
		
		List<ScannedFiles> scannedFilesList = document.getScannedFiles();
		for(ScannedFiles scannedFile1 : scannedFilesList)
		{
			scannedFile1.setDocumentsID(document.getID());
			datastore.save(scannedFile1);
		}
		return document;
	}

	/**
	 * returns all documents that don't have scanned files. 
	 * @param mongoClient
	 * @param database
	 * @return
	 */
	public static List<ScannedFiles> findScannedFilesWithoutADocument(MongoClient mongoClient, String database)
	{
		Morphia localMorphia = getMorphia();
		final Datastore datastore = localMorphia.createDatastore(mongoClient, database);
		datastore.ensureIndexes();
		
		final Query<ScannedFiles> query = datastore.createQuery(ScannedFiles.class);
		final List<ScannedFiles> listScannedFiles = query.asList();
		List<ScannedFiles> vectorScannedFilesWithoutDocuments = new Vector<ScannedFiles>(20);
		Iterator<ScannedFiles> iter = listScannedFiles.iterator();
		while(iter.hasNext())
		{
			ScannedFiles scannedFile = iter.next();
			ObjectId objectIdDocumentsId = scannedFile.getDocumentsID();
			try
			{
				Object temp = datastore.get(Documents.class, objectIdDocumentsId);
				if(temp == null)
				{
					vectorScannedFilesWithoutDocuments.add(scannedFile);
				}
			}
			catch(Exception anyExc)
			{
				vectorScannedFilesWithoutDocuments.add(scannedFile);
			}
		}
		
		return vectorScannedFilesWithoutDocuments;
	}	
	
	/**
	 * returns all documents that don't have scanned files. 
	 * @param mongoClient
	 * @param database
	 * @return
	 */
	public static List<Documents> findDocumentsWithoutScannedFiles(MongoClient mongoClient, String database)
	{
		Morphia localMorphia = getMorphia();
		final Datastore datastore = localMorphia.createDatastore(mongoClient, database);
		datastore.ensureIndexes();
		
		final Query<Documents> query = datastore.createQuery(Documents.class);
		final List<Documents> listDocuments = query.asList();
		List<Documents> vectorDocumentsWithoutScannedFiles = new Vector<Documents>(20);
		Iterator<Documents> iter = listDocuments.iterator();
		while(iter.hasNext())
		{
			Documents document = iter.next();
			//System.out.println("Document: " + document.getID().toString());
			try
			{
				List<ScannedFiles> scannedFiles = document.getScannedFiles();
				if(scannedFiles == null || scannedFiles.size() == 0)
				{
					vectorDocumentsWithoutScannedFiles.add(document);
				}
			}
			catch(Exception anyExc)
			{
				vectorDocumentsWithoutScannedFiles.add(document);
			}
		}
		
		return vectorDocumentsWithoutScannedFiles;
	}	
	
	/**
	 * returns all ScannedFile that do have a Document, but don't have a reference to that document
	 * @param mongoClient
	 * @param database
	 * @return
	 */
	public static List<Documents> findDocumentsWithScannedFilesWithoutDocumentReference(MongoClient mongoClient, String database)
	{
		Morphia localMorphia = getMorphia();
		final Datastore datastore = localMorphia.createDatastore(mongoClient, database);
		datastore.ensureIndexes();
		
		final Query<Documents> query = datastore.createQuery(Documents.class);
		final List<Documents> listDocuments = query.asList();
		List<Documents> vectorDocumentsWithScannedFilesWithBrokenReferenceBack = new Vector<Documents>(20);
		Iterator<Documents> iter = listDocuments.iterator();
		while(iter.hasNext())
		{
			Documents document = iter.next();
			//System.out.println("Document: " + document.getID().toString());

			final List<ScannedFiles> listScannedFiles = document.getScannedFiles();
			Iterator<ScannedFiles> iterScannedFile = listScannedFiles.iterator();
			while(iterScannedFile.hasNext())
			{
				ScannedFiles scannedFile = iterScannedFile.next();
				//System.out.print("Scanned File: " + scannedFile.getId().toString());
				Object obj = scannedFile.getDocumentsID();
				if(obj == null)
				{
					vectorDocumentsWithScannedFilesWithBrokenReferenceBack.add(document);
				}
				//System.out.println(" Scanned --> Document: " + strDocumentsId);
			}
		}
		
		return vectorDocumentsWithScannedFilesWithBrokenReferenceBack;
	}
	
	/**
	 * returns all ScannedFile that do have a Document, but don't have a reference to that document
	 * @param mongoClient
	 * @param database
	 * @return
	 */
	public static void fixDocumentsWithScannedFilesWithoutDocumentReference(MongoClient mongoClient, String database)
	{
		Morphia localMorphia = getMorphia();
		final Datastore datastore = localMorphia.createDatastore(mongoClient, database);
		datastore.ensureIndexes();
		
		final Query<Documents> query = datastore.createQuery(Documents.class);
		final List<Documents> listDocuments = query.asList();
		Iterator<Documents> iter = listDocuments.iterator();
		while(iter.hasNext())
		{
			Documents document = iter.next();

			final List<ScannedFiles> listScannedFiles = document.getScannedFiles();
			Iterator<ScannedFiles> iterScannedFile = listScannedFiles.iterator();
			while(iterScannedFile.hasNext())
			{
				ScannedFiles scannedFile = iterScannedFile.next();
				Object obj = scannedFile.getDocumentsID();
				if(obj == null)
				{
					scannedFile.setDocumentsID(document.getID());
					datastore.save(scannedFile);
				}
			}
		}
	}	

	
	public static List<ScannedFiles> doesScannedFileExist(MongoClient mongoClient, String database, String txtFileName)
	{
		Morphia localMorphia = getMorphia();
		final Datastore datastore = localMorphia.createDatastore(mongoClient, database);
		datastore.ensureIndexes();
		
		File file = new File(txtFileName); //File pdfFile = new File("C:\\development\\receipt scans\\img012.pdf");
		InputStream is;
		byte[] byteArrayPdfFile = null;
		try {
			is = FileUtils.openInputStream(file);
			byteArrayPdfFile = IOUtils.toByteArray(is);
		} catch (IOException e) 
		{
			return null;
		}

		String strHashCode = SHA256.getSHA256Hash(byteArrayPdfFile);
		
		List<ScannedFiles> listScannedFiles = datastore.createQuery(ScannedFiles.class)
                .field("total_hashcode").equal(strHashCode)
                .asList();
		
		//It is a bad thing if we already fine a file, that means the file has already been scanned in
		if(listScannedFiles.isEmpty() == false)
		{
			return listScannedFiles;
		}
		return null;

	}	
	/**
	 * generates a ScannedFiles object, then checks to see if it is already in the database, and stores it in the database
	 * if it is not already there. Returns null if it is already there or a problem has happened.
	 * 
	 * @param txtName
	 * @param strInsertDate
	 * @param strDueDate
	 * @param strAmount
	 * @param strPaidDate
	 * @param strCurrency
	 * @param strDescription
	 * @param documentType
	 * @return
	 */
	private static ScannedFiles saveScannedFile(String txtFileName, final Datastore datastore)
	{
		/**
		 * repeating the logic for making sure that the file doesn't already exist.
		 */
		File file = new File(txtFileName); //File pdfFile = new File("C:\\development\\receipt scans\\img012.pdf");
		InputStream is;
		byte[] byteArrayPdfFile = null;
		try {
			is = FileUtils.openInputStream(file);
			byteArrayPdfFile = IOUtils.toByteArray(is);
		} catch (IOException e) 
		{
			return null;
		}

		String strHashCode = SHA256.getSHA256Hash(byteArrayPdfFile);
		
		List<ScannedFiles> listScannedFiles = datastore.createQuery(ScannedFiles.class)
                .field("total_hashcode").equal(strHashCode)
                .asList();
		
		//It is a bad thing if we already fine a file, that means the file has already been scanned in
		if(listScannedFiles.isEmpty() == false)
		{
			return null;
		}

		try
		{
			String strFileExtension = txtFileName.substring(txtFileName.length() - 3);
			final ScannedFiles scannedFile = new ScannedFiles(byteArrayPdfFile, 0, strHashCode, strFileExtension);
			datastore.save(scannedFile);
			return scannedFile;
		}
		catch(Exception anyExc)
		{
			return null;
		}
	}
	

	private static Morphia getMorphia()
	{
		if(morphia == null)
		{
			morphia = new Morphia();
			mapperOptions = new MapperOptions();
			mapperOptions.setStoreEmpties(true);
			mapperOptions.setStoreNulls(true);
			morphia.getMapper().setOptions(mapperOptions);
			morphia.mapPackage("pojos");

		}
		return morphia;
	}
	

	
}

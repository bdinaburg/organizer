package mongodbtester;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.MapperOptions;
import org.mongodb.morphia.query.Query;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

import pojos.DocumentType;
import pojos.Documents;
import pojos.ScannedFiles;
import transientPojos.DocTypes;
import transientPojos.DocTypes.DocType;
import util.DateUtil;
import util.FileReadingUtils;

public class MongoDBMorphia {
	public static final String DATABASE_NAME = "borisStorage";
	public static final String DOCUMENT_DESCRIPTION_STORE_NAME = "documents";
	public static final String BINARY_DOCUMENTS_STORE_NAME = "binary_documents";
	public static final String USER = "admin";
	public static final String DB_NAME = "admin";
	public static String password = "PA$$Word12345";

	
	public static void main (String args[])
	{
		populateDatabaseWithDocTypes("borisdb");
	}
	
/*	public static void main(String args[]) {

		MongoClient mongoClient =  null;

		
		try {
			mongoClient = connectToMongoDB();
			//createDocumentsStore(mongoClient);
			//createDocumentsBinaryStore(mongoClient);
			//addSampleDocumentsToDescriptionStore(mongoClient);
			
			
			*//** MORPHIA STUFF
			final Morphia morphia = new Morphia();
			MapperOptions options = new MapperOptions();
			options.setStoreEmpties(true);
			options.setStoreNulls(true);
			morphia.getMapper().setOptions(options);
			morphia.mapPackage("pojos");
			
			final Datastore datastore = morphia.createDatastore(mongoClient, DATABASE_NAME);
			datastore.ensureIndexes();


			final Query<DocumentType> docTypeQuery = datastore.createQuery(DocumentType.class).field("document_type").equal(DocumentType.docTypeToString(DocType.BILL_CAR));
			final DocumentType documentType = docTypeQuery.get();
			System.out.println(documentType.getStrDocument_type().toString());
			//final List<DocumentType> listScannedFiles = query.fi

			ObjectId objectId = new ObjectId("587c03a41422302378b99b09");
			ScannedFiles scannedFile = datastore.get(ScannedFiles.class, objectId);
			
			Documents document = new Documents();
			document.setStrName("2009 dodge charger repair bill 1823.49");
			document.setDate_document_creation(DateUtil.getDateObject("09/25/2015"));
			document.setDate_due_date(null);
			document.setBigDecimalAmount(new BigDecimal("1823.49"));
			document.setDate_paid_date(DateUtil.getDateObject("09/30/2015"));
			document.setCurrency(Currency.getInstance("USD"));
			document.setStrDocumentDescription(FileReadingUtils.readFile("C:\\development\\receipt_scans\\image12text.txt"));
			document.setDocumentType(documentType);
			document.addScannedFile(scannedFile);
			datastore.save(document);
			
			
			**//*
			
			*//**
			 * the following finds all scanned in files in the database
			
			final Query<ScannedFiles> query = datastore.createQuery(ScannedFiles.class);
			final List<ScannedFiles> listScannedFiles = query.asList();
			Iterator<ScannedFiles> iter = listScannedFiles.iterator();
			while(iter.hasNext())
			{
				ScannedFiles scannedFile = iter.next();
				System.out.println(scannedFile.getId().toString());
				//FileUtils.writeByteArrayToFile(new File("C:\\development\\receipt scans\\" + scannedFile.getSHA256HashOfChunk() + ".pdf"), scannedFile.getDocument_inbytearray());
			}
			*//* 
			
			*//**
			 * delete an object by ID example
			 *//*
			ObjectId objectId = new ObjectId("587c007414223030c0081855");
			ScannedFiles scannedFile = datastore.get(ScannedFiles.class, objectId);
			System.out.println(scannedFile.getModified_date());
			datastore.delete(scannedFile);
			
			
			File pdfFile = new File("C:\\development\\receipt scans\\img012.pdf");
			InputStream is = FileUtils.openInputStream(pdfFile);
			byte[] byteArrayPdfFile = IOUtils.toByteArray(is);
			
			try
			{
				final ScannedFiles  scannedFile = new ScannedFiles(byteArrayPdfFile, false, 0, null);
				datastore.save(scannedFile);
			}
			catch(Exception anyExc)
			{
				System.err.println(anyExc);
			}
			

			*//**
			 * populate the database with various document types
			 *//*
						
			for(DocType dt : DocType.values())
			{
				DocumentType documentType = new DocumentType(dt);
				datastore.save(documentType);
			}			
			
			
			
		} catch (Exception anyExc) {
			System.out.println(anyExc);
		}
		finally
		{
			if(mongoClient != null)
			{
				mongoClient.close();
			}
		}

	}
	*/
	public static void queryDocuments(MongoClient mongoClient)
	{
		mongoClient.getDatabase(DATABASE_NAME);
	}
	
	public static MongoClient connectToMongoDB()
	{

		List<ServerAddress> listServerAddresesSeeds = new ArrayList<ServerAddress>();
		listServerAddresesSeeds.add(new ServerAddress("chaglei.com", 27017));
		List<MongoCredential> listMongoCredentials = new ArrayList<MongoCredential>();
		MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(USER, DB_NAME, password.toCharArray());
		listMongoCredentials.add(mongoCredential);

		MongoClient mongoClient = new MongoClient(listServerAddresesSeeds, listMongoCredentials);
		
		return mongoClient;
	}
	
	public static void createDocumentsDescriptionStore(MongoClient mongoClient)
	{
		MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);

		database.createCollection(DOCUMENT_DESCRIPTION_STORE_NAME);
		MongoCollection<Document> collectionDocuments = database.getCollection(DOCUMENT_DESCRIPTION_STORE_NAME);
		Document document = new Document("name", "document name");
		document.append("document_creation_date", "1/1/2016");
		document.append("document_database_insertion_date", "11/29/2016");
		document.append("document_description", "description of the document");
		document.append("document type", "electric bill, medical bill, governement document");

		collectionDocuments.insertOne(document);		
	}
	
	/**
	 * assumes we already created the collection
	 * @param mongoClient
	 */
	public static void addSampleDocumentsToDescriptionStore(MongoClient mongoClient)
	{
		MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);

		MongoCollection<Document> collectionDocuments = database.getCollection(DOCUMENT_DESCRIPTION_STORE_NAME);
		Document document = new Document("name", "document1");
		document.append("document_creation_date", "1/1/2016");
		document.append("document_database_insertion_date", "11/29/2016");
		document.append("document_description", "description of the document");
		document.append("document type", "electric bill, medical bill, governement document");

		collectionDocuments.insertOne(document);		
	}
	
	public static void createDocumentsBinaryStore(MongoClient mongoClient) throws IOException
	{
		//MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
		//database.createCollection(MongoDBJDBC.BINARY_DOCUMENTS_STORE_NAME);
		//MongoCollection<Document> collectionDocuments = database.getCollection(BINARY_DOCUMENTS_STORE_NAME);
		File pdfFile = new File("C:\\development\\receipt scans\\img012.pdf");
		@SuppressWarnings("deprecation") //apparently mongodb didn't switch their own code to use .getDatabase instead of .getDB
		GridFS gridfs = new GridFS(mongoClient.getDB(DATABASE_NAME), BINARY_DOCUMENTS_STORE_NAME + "12");
		GridFSInputFile gridFSInputFile = gridfs.createFile(pdfFile);
		gridFSInputFile.save();
	}
	
	public static void populateDatabaseWithDocTypes(String dbName)
	{
		/**
		 * populate the database with various document types
		 */
		MongoClient mongoClient = connectToMongoDB();	
		final Morphia morphia = new Morphia();
		MapperOptions options = new MapperOptions();
		options.setStoreEmpties(true);
		options.setStoreNulls(true);
		morphia.getMapper().setOptions(options);
		morphia.mapPackage("pojos");

		final Datastore datastore = morphia.createDatastore(mongoClient, dbName);
		datastore.ensureIndexes();

		for(DocType dt : DocType.values())
		{
			DocumentType documentType = new DocumentType(dt);
			datastore.save(documentType);
		}	
		
		mongoClient.close();
	}
	
}
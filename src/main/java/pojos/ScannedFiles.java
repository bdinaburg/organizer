/**
 * There is supposed to be a 16MB limit on the size of the file.  I am thinking of breaking up a file into pieces in case it is too big and storing it that way
 * Morphia doesn't support gridfs so I have to do this myself.
 */

package pojos;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Transient;

import util.SHA256;

@Entity("scanned_files")
@Indexes(
	    @Index(fields = @Field("id"))
	)
public class ScannedFiles 
{
	/**
	 * this is the value of 15MB, I don't want the file any bigger than this due to limitation of mongodb
	 */
	@Transient
	private final int MAX_FILE_SIZE = 15728640; 
	
	@Id
	private ObjectId id;
	
	@Property("filename")
	private String strFileName = "";
	
	@Property("ischunked")
	private boolean boolIsChunked = false;
	
	@Property("fileType")
	private String strFileType = "pdf";
	
	@Property("chunk_number")
	private int int_chunk_number = 0;
	
	@Property("chunk_hashcode")
	private String strSHA256HashOfChunk =  null;

	@Property("total_hashcode")
	private String strSHA256HashTotal = null;
	
	@Property("modified_date")
	private Date modified_date;

	@Property("document_in_byte_array")
	private byte[] document_in_byte_array =  null;

	@Property("documentsID")
	private ObjectId documentsID;
	
	public ScannedFiles()
	{
		
	}
	
	public ScannedFiles(byte[] document_byte_array, int chunk_number, String SHA256_of_all_chunks) throws Exception
	{
		this(document_byte_array, chunk_number, SHA256_of_all_chunks, "pdf");
	}
	
	/**
	 * note: make sure the file has a valid file extension, the extension is used to determine what type of file it is. 
	 * 
	 * @param document_byte_array
	 * @param chunk_number
	 * @param SHA256_of_all_chunks
	 * @param strFileType
	 * @throws Exception
	 */
	public ScannedFiles(byte[] document_byte_array, int chunk_number, String SHA256_of_all_chunks, String strFileType) throws Exception
	{
		this.strFileType = strFileType;
		boolean ischunked = false;
		setDocument_inbytearray(document_byte_array);
		String strInHash = SHA256.getSHA256Hash(document_byte_array);
		if(strInHash.contentEquals((SHA256_of_all_chunks)) == false)
		{
			ischunked = true;
		}
		this.setBoolIsChunked(ischunked);
		this.setInt_chunk_number(chunk_number);
		this.setStrSHA256HashTotal(SHA256_of_all_chunks);
	}

	public byte[] getDocument_inbytearray() {
		return document_in_byte_array;
	}
	
	public void setDocument_inbytearray(byte[] document_in_byte_array) throws Exception {
		if(document_in_byte_array.length > MAX_FILE_SIZE)
		{
			throw new Exception("Attempted to set document that is bigger than 15MB");
		}
		this.document_in_byte_array = document_in_byte_array;
		this.setStrSHA256HashOfChunk(SHA256.getSHA256Hash(document_in_byte_array));
	}
	
	public boolean isChunked() {
		return boolIsChunked;
	}

	private void setBoolIsChunked(boolean boolIsChunked) {
		this.boolIsChunked = boolIsChunked;
	}

	public int getInt_chunk_number() {
		return int_chunk_number;
	}

	public void setInt_chunk_number(int int_chunk_number) {
		this.int_chunk_number = int_chunk_number;
	}
	
	public String getFileName() {
		return this.strFileName;
	}

	public String getSHA256HashOfChunk() {
		return strSHA256HashOfChunk;
	}
	/**
	 * should be called when we assigne the byte array
	 * @param strSHA256HashOfChunk
	 */
	private void setStrSHA256HashOfChunk(String strSHA256HashOfChunk) {
		this.strSHA256HashOfChunk = strSHA256HashOfChunk;
	}

	public String getSHA256HashTotal() {
		return strSHA256HashTotal;
	}

	public void setStrSHA256HashTotal(String strSHA256HashTotal) {
		this.strSHA256HashTotal = strSHA256HashTotal;
	}

	public ObjectId getId() {
		return id;
	}

	public Date getModified_date() {
		return modified_date;
	}

	public void setModified_date(Date modified_date) {
		this.modified_date = modified_date;
	}
	
	public String getFileType()
	{
		return strFileType;
	}
	
	public void setDocumentsID(ObjectId documentsid) {
		this.documentsID = documentsid;
	}
	
	public ObjectId getDocumentsID()
	{
		return this.documentsID;
	}
	
	@PrePersist
	private void Validation() throws Exception
	{
		if(this.document_in_byte_array == null)
		{
			throw new Exception("no document to save");
		}
		
		if(this.getSHA256HashOfChunk() == null)
		{
			throw new Exception("No hash of the document chunk is present");
		}
		
		if(this.isChunked() == false && this.getSHA256HashTotal() == null)
		{
			this.setStrSHA256HashTotal(this.getSHA256HashOfChunk());
		}
		
		if(this.isChunked() == true && this.getSHA256HashTotal() == null)
		{
			throw new Exception("can't have a partial document and not have a hash of the complete document");
		}
		
		this.setModified_date(new Date());
		
	}
	
	@Override
	public String toString()
	{
		return "id: " + id.toString();
	}

	
}

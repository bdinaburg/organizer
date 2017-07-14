package pojos;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.PostLoad;
import org.mongodb.morphia.annotations.PreLoad;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;

import com.mongodb.DBObject;

@Entity("documents")
@Indexes(
	    @Index(fields = @Field("name"))
	)
public class Documents 
{
	@Id
	private ObjectId id;
	
	@Property("name")
	private String strName = null;
	
	@Property("document_creation_date")
	private Date date_document_creation = null;
	
	@Property("document_database_insertion_date")
	private Date date_document_database_insertion = null;
	
	@Property("document_due_date")
	private Date date_due_date = null;

	@Property("document_amount")
	private double bigDecimalAmount = 0;

	@Property("document_paid_date")
	private Date date_paid_date = null;
	
	@Property("document_currency")
	private String currency = null;

	@Property("document_description")
	private String strDocumentDescription = null;
	
	@Property("text_content")
	private String strTextContent = null;
	
	@Reference
	private DocumentType documentType;
	
	@Reference (lazy = true)
	private List<ScannedFiles> scannedFiles = new LinkedList<ScannedFiles>();
	
	
	public Documents(ObjectId objectId)
	{
		this.id = objectId;
	}
	
	/**
	 * if we don't specify this object Id it should get auto generated
	 */
	public Documents()
	{
	}
	
	public ObjectId getID()
	{
		return id;
	}

	public String getStrTextContent() {
		return strName;
	}

	public void setStrTextContent(String strContent) {
		this.strTextContent = strContent;
	}
	public String getStrName() {
		return strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}

	public Date getDate_document_creation() {
		return date_document_creation;
	}

	public void setDate_document_creation(Date date_document_creation) {
		this.date_document_creation = date_document_creation;
	}

	public Date getDate_document_database_insertion() {
		return date_document_database_insertion;
	}

	protected void setDate_document_database_insertion(Date date_document_database_insertion) {
		this.date_document_database_insertion = date_document_database_insertion;
	}

	public Date getDate_due_date() {
		return date_due_date;
	}

	public void setDate_due_date(Date date_due_date) {
		this.date_due_date = date_due_date;
	}

	public Date getDate_paid_date() {
		return date_paid_date;
	}

	public void setDate_paid_date(Date date_paid_date) {
		this.date_paid_date = date_paid_date;
	}

	public String getStrDocumentDescription() {
		return strDocumentDescription;
	}

	public void setStrDocumentDescription(String strDocumentDescription) {
		this.strDocumentDescription = strDocumentDescription;
	}

	public DocumentType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}

	public List<ScannedFiles> getScannedFiles() {
		return scannedFiles;
	}

	public void setScannedFiles(List<ScannedFiles> scannedFiles) {

		if(scannedFiles == null)
		{
			this.scannedFiles = new LinkedList<ScannedFiles>();
		}
		else
		{
			this.scannedFiles = scannedFiles;			
		}
	}
	
	public void addScannedFile(ScannedFiles scannedFile)
	{
		scannedFiles.add(scannedFile);
	}
	
	
	public BigDecimal getBigDecimalAmount() {
		return new BigDecimal(bigDecimalAmount);
	}

	public void setBigDecimalAmount(BigDecimal bigDecimalAmountIn) {
		this.bigDecimalAmount = bigDecimalAmountIn.doubleValue();
	}	

	public Currency getCurrency() {
		return Currency.getInstance(currency);
	}

	public void setCurrency(Currency currency_in) {
		this.currency = currency_in.getCurrencyCode();
	}
	
	/*@PreLoad void fixup(DBObject dbObject) {
		//System.out.println(dbObject);
		DBObject obj = (DBObject)dbObject.get("scannedFiles");
		System.out.println(obj);
	}
	
	@PostLoad void fixup2(DBObject dbObject) {
		//System.out.println(dbObject);
		DBObject obj = (DBObject)dbObject.get("scannedFiles");
		System.out.println(obj);
	}*/

	
	@PrePersist
	private void Validation() throws Exception
	{
		if(this.getStrName() == null){
			throw new Exception("no file name available");
		}
		
		if (this.date_document_creation == null) {
			throw new Exception("Did not specify when this document was created");
		}

		this.setDate_document_database_insertion(new Date());		

		if(this.getDocumentType().getStrDocument_type() == transientPojos.DocTypes.DocType.BILL_DENTAL ||
		   this.getDocumentType().getStrDocument_type() == transientPojos.DocTypes.DocType.BILL_MEDICAL) {
			
			if(this.getDate_due_date() == null) {
				throw new Exception("Did not specify when this is due");
			}
		}
		
		if(this.getStrDocumentDescription().indexOf(this.getStrName()) < 0)
		{
			this.setStrDocumentDescription(strDocumentDescription + "\n\n : " + this.getStrName()); 
		}
	}
	
	@Override
	public String toString()
	{
		return "id: " + id.toString() + " name: " + this.getStrName();
	}
}

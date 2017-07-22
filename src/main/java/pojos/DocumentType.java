/**
 * I didn't want to just arbitrarily make up a type of a document that can exist. I wanted types of documents to be known so that may be each document will have certain properties
 * associated with it. For example may be a bill document needs an amount and a due date while a government document may be has an expiration date.
 */

package pojos;

import org.bson.types.ObjectId;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Property;

import transientPojos.DocTypes;
import transientPojos.DocTypes.DocType;

// BILL_MEDICAL, BILL_DENTAL, BILL_CAR, DOCUMENT_GOVERNMENT, DOCUMENT_PRIVATE, ESTIMATE 
@Entity("document_type")
@Indexes(
	    @Index(fields = @Field("strDocument_type"))
	)
public class DocumentType {
	
	@Id
	private ObjectId id;
	
	@Property("document_type")
	private String strDocument_type;

	
	public DocumentType()
	{
		
	}
	
	public DocumentType(DocType docType) {
		this.strDocument_type = docTypeToString(docType);
	}

	public static String docTypeToString(DocType docType)
	{
		switch (docType) {
		case DOCUMENT_STATEMENT:
			return "STATEMENT";
		case BILL_MEDICAL:
			return "MEDICAL BILL";
		case BILL_DENTAL:
			return "DENTAL BILL";
		case BILL_CAR:
			return "CAR BILL";
		case DOCUMENT_GOVERNMENT:
			return "GOVERNMENT DOCUMENT";
		case DOCUMENT_PRIVATE:
			return "PRIVATE COMPANY DOCUMENT";
		case ESTIMATE:
			return "ESTIMATE";
		case PHOTO_PERSONAL:
			return "PERSONAL PHOTO";
		default:
			return "UNKNOWN";
		}
	}
	
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public DocType getStrDocument_type() {
		switch (strDocument_type) {
		case "STATEMENT":
			return DocType.DOCUMENT_STATEMENT;
		case "MEDICAL BILL":
			return DocType.BILL_MEDICAL;
		case "DENTAL BILL":
			return DocType.BILL_DENTAL;
		case "CAR BILL":
			return DocType.BILL_CAR;
		case "GOVERNMENT DOCUMENT":
			return DocType.DOCUMENT_GOVERNMENT;
		case "PRIVATE COMPANY DOCUMENT":
			return DocType.DOCUMENT_PRIVATE;
		case "PERSONAL PHOTO":
			return DocType.PHOTO_PERSONAL;
		case "ESTIMATE":
			return DocType.ESTIMATE;
		}
		return DocType.UNKNOWN;
	}

	public void setDocument_type(DocType docType) {
		this.strDocument_type = docTypeToString(docType);
	}
	
	@Override
	public String toString()
	{
		return strDocument_type;
	}
}

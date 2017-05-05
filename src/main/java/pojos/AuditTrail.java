/**
 * Thinking about making this to keep track of when a column is updated in a database. May be this will be a nice to have feature in the future. I am envisioning that a user is known
 * and when the user makes any changes to schemas other this one it records the user name, time, previous value and new value for any column or field as they call it in mongodb.
 */
package pojos;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Property;

@Entity("AuditTrail")
@Indexes(
	    @Index(fields = @Field("id"))
	)
public class AuditTrail 
{
	@Id
	private ObjectId id;
	/**
	 * user who made a change
	 */
	@Property("userName")
	String strUserName;
	/**
	 * the schema to which they authenticated to
	 */
	@Property("authSchema")
	String strAuthSchema;

	@Property("documentsID")
	private ObjectId strDocumentsID = null;
	
	@Property("scannedFilesID")
	private ObjectId scannedFilesID = null;

}

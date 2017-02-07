/**
 * Envisioning that for future use I would want different users to use this.
 */


package pojos;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Property;

@Entity("users")
@Indexes(
	    @Index(fields = @Field("id"))
	)
public class User 
{
	@Id
	private ObjectId id;
	
	@Property("username")
	private String strUserName;
	
	@Property("password")
	private String strPassword;
	
}

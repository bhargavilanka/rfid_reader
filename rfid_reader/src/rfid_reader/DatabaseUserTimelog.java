package rfid_reader;

import static com.sleepycat.persist.model.DeleteAction.CASCADE;
import static com.sleepycat.persist.model.Relationship.MANY_TO_ONE;
import static com.sleepycat.persist.model.Relationship.ONE_TO_ONE;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.Transaction;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
import com.sleepycat.persist.StoreConfig;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.Persistent;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.SecondaryKey;


@Entity
public class DatabaseUserTimelog {

	// The user's name primary key is the user name rather than 
	// being an ID that is assigned automatically
	@PrimaryKey private String username;

	
	public DatabaseUserTimelog(String name) {
	    this.username = name;
	}
	
	/** A default constructor is needed by the DPL for deserialization. */
	private DatabaseUserTimelog() {
	}
	
	public String getName() {
	    return username;
	}
	
	
	public String toString() {
	    return "[User name: " + username + ']';
	    }
} // end class DatabaseUserTimelog    
	

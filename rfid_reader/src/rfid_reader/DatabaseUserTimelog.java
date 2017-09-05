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
import java.time.*;

@Entity
public class DatabaseUserTimelog {

	// The user's name primary key is the user name rather than 
	// being an ID that is assigned automatically
	@PrimaryKey private String username;

	
	private ZonedDateTime timeIn; 			// Scanned in timestamp
	private ZonedDateTime timeOut; 			// Scanned out timestamp
	private Period		  totalTimeToday;	// Total time spent in lab today (HH:MM:SS)
	private int			  checkins; 		// Number of checkings today
	
	public DatabaseUserTimelog(String name) {
	    this.username = name;
	}
	
	/** A default constructor is needed by the DPL for deserialization. */
	private DatabaseUserTimelog() {
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}


	public ZonedDateTime getTimeIn() {
		return timeIn;
	}
	public void setTimeIn(ZonedDateTime timeIn) {
		this.timeIn = timeIn;
	}

	public ZonedDateTime getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(ZonedDateTime timeOut) {
		this.timeOut = timeOut;
	}

	public Period getTotalTimeToday() {
		return totalTimeToday;
	}

	public int getCheckins() {
		return checkins;
	}
	
	
	public String toString() {
	    return "[User name: " + username + ']';
	    }
} // end class DatabaseUserTimelog    
	

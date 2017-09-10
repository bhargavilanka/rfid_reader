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


// A Day is a list of UserTimelog records 
// The relationship between a Day and a UserTimelog is many to one: Each day
// can have multiple UserTimelog records. 
// There is onely ONE UserTimlog record per day

@Entity
public class DatabaseDay {

	
	//@PrimaryKey(sequence="ID") private long id; 	// DB sequence ID
	// The primary key is the monthday itself vs an ID that is automatically assigned
	@PrimaryKey
	private MonthDay day; // Each day/date has a list of UserTimelog entries


	@SecondaryKey(relate=MANY_TO_ONE)
	private DatabaseUserTimelog user_timelog; 		// Each day/date has a list of UserTimelog entries

	public MonthDay getDay() {
		return day;
	}


	public void setDay(MonthDay md) {
		day = md;
	}


	public DatabaseUserTimelog getUser_timelog() {
		return user_timelog;
	}


	public void setUser_timelog(DatabaseUserTimelog user_timelog) {
		this.user_timelog = user_timelog;
	}

	public String toString() {
		return day.toString() + " " + user_timelog.toString(); 
	}
		
}


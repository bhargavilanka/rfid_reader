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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// A Day is a list of UserTimelog records 
// The relationship between a Day and a UserTimelog is many to one: Each day
// can have multiple UserTimelog records. 
// There is onely ONE UserTimlog record per day

@Entity
public class DatabaseDay {

	
	//@PrimaryKey(sequence="ID") private long id; 	// DB sequence ID
	
	// Each Day has a record for the users that scanned in or out that day
	//private MonthDay day; 
	// The primary key is the date itself vs an ID that is automatically assigned
	// Formay is always "yyyy/mm/dd"
	@PrimaryKey
	private String day; // Each day/date has a list of UserTimelog entries


	//@SecondaryKey(relate=MANY_TO_ONE)
	//private List<DatabaseUserTimelog> user_timelog_list =
	//			new ArrayList<DatabaseUserTimelog>(); 		// Each day/date has a list of UserTimelog entries
	private Map<String, DatabaseUserTimelog> user_timelog_map = new HashMap<String, DatabaseUserTimelog>();
	
	/** A default constructor is needed by the DPL for deserialization. */
	private DatabaseDay() {
		
	}
	
	public DatabaseDay(String md) {
		day = md;
	}

	
	public String getDay() {
		return day;
	}


	//public void setDay(MonthDay md) {
	public void setDay(String md) {
		day = md;
	}

/*
	public DatabaseUserTimelog getUser_timelog() {
		return user_timelog;
	}
*/

	public Constants.LoginType setUser_timelog(String user, Date date) {
		DatabaseUserTimelog user_timelog = null; 
		Constants.LoginType type; 
		// See if there is a record for this user today
		if (user_timelog_map.containsKey(user)) {
			Debug.log("User already has a timelog entry for today (so has scanned in): " + user);
			user_timelog = user_timelog_map.get(user);
			type = user_timelog.update(date); // If user already existed, check for scan ir or out and update accordingly

			if (type == Constants.LoginType.INVALID_TIME_SPAN) {
				return type; //Return without updating the DB
			}
		
		} else {
			user_timelog = new DatabaseUserTimelog(user, date);
			type = Constants.LoginType.LOGIN;
		}
		

		this.user_timelog_map.put(user, user_timelog);
		return type;
		
				
		
	} // end setUser_timelog

	public String toString() {
		return day + " " + user_timelog_map.toString(); 
	}
		
}


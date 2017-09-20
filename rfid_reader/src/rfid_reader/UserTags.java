/**
 * Name:
 * 		UserTags - class for managing the user/tag database (aka the username to RFID tag mapping)
 * 		The data stored in a spreadsheet and exported to a CSV file for use here
 */



package rfid_reader;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class UserTags {
	public static Map<String, UserTag> tag_map = new HashMap<String, UserTag>();
	
	public static void main(String[] args) {

		// Really need to use jopt-simple if we add more arguments
    	if (args.length != 0) {
    		for (String argument: args) {
    			if (argument.equals("-d") || argument.equals("--debug") )
    			    Debug.enable(true);
    		}
    	}
    	
    	
		try {
			read_user_tags();
		} catch (Exception e) {
			System.err.println("Unknown error reading tags file " + e.toString());
			e.printStackTrace(System.err);
		}
		
	}
    
    public static void read_user_tags()  {

    	
    	Path currentRelativePath = Paths.get("");
    	String s = currentRelativePath.toAbsolutePath().toString();
    	Debug.log("Current relative path is: " + s);
    	
    	try {
	    	//CSVReader reader = new CSVReader(new FileReader(Constants.USER_RFIDTAG_MAPPING));
	    	CSVReaderBuilder readerBuilder = new CSVReaderBuilder(new FileReader(Constants.USER_RFIDTAG_MAPPING))
	       											.withSkipLines(1);			// Skip header row!!
	    	CSVReader reader = readerBuilder.build();
	
	    	String[] line;
	
	        // File format is:
	        //	0		  1						  2						  3
	        // 	RFID tag, Username (last, first), optional Login Message, optional Logout Message
	        // The first line DOES have the header row text so we need to skip by it. The constructor
	        // for CSVReaderBuild conveniently does that. 
	        // Read in the data and place in a Map for fast tag lookups. 
	    	// pjw: TODO: We dont' expect dup's in the source CVS file - add hardening as we add to the map
	        
	        while ((line = reader.readNext()) != null) {
	            Debug.log("Tag: [" + line[0] + "]\tName: [" + line[1] + "]\tLogin: [" + line[2] + "]\tLogout: [" + line[3] + "]");
	            UserTag user = new UserTag(line[0], line[1], line[2], line[3]);
	            tag_map.put(user.getUserTag(), user);
	        }
		
	        // Sanity check our map
	        if (Debug.isEnabled()) {
		        for(String key: tag_map.keySet()) {
					System.out.println("key: " + key + " User data: " + tag_map.get(key));
		        }
	        }
    	} catch (Exception e) {
    		System.err.println("ERROR: Cannot read RFID tag datebase: " + Constants.USER_RFIDTAG_MAPPING);
			e.printStackTrace(System.err);
    	}
    }
    
    public static UserTag getUser(String tag_uid) {
    	UserTag user = tag_map.get(tag_uid); 
    	return user;
    	 
    }
} // end public class UserTags

class UserTag {
	
	// We using MiFare RFID cards. UIDs can be 4, 7, or 10 bytes. Just treat it as a string
	private String tag_uid;
	private String username;
	private String loginMsg;
	private String logoutMsg;
	
	public UserTag(String tag, String username, String loginMsg, String logoutMsg) {
		this.tag_uid 	= tag;
		this.username 	= username; 
		this.loginMsg 	= loginMsg;
		this.logoutMsg 	= logoutMsg;
	}

	public String getUserTag() { 
		return tag_uid;
	}
	public String getUsername() {
		return username;
	}
	
	public String getUserFirstName() {
		return username; // PJW: TODO - parse to get the first name
	}
	public String getUserLoginMsg() {
		String msg; 
		if (loginMsg.isEmpty()) {
			msg = "Welcome!";
		} else {
			msg = loginMsg; 
		}
		return msg;
	}
	public String getUserLogoutMsg() { 
		String msg; 
		if (logoutMsg.isEmpty()) {
			msg = "Goodbye!";
		} else {
			msg = logoutMsg; 
		}
		return msg;
	}

	@Override
	public String toString() {
		return tag_uid + " " + username + " " + loginMsg + " " + logoutMsg;  
	}
	
} // class UserTag
	


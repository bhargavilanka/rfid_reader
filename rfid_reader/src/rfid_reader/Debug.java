package rfid_reader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Trivial debug logging
 */
public class Debug {
	static Boolean debug_enabled = false;
	static String date = ""; 
	/**
	 * Check to see if debug logging is enabled
	 * @return
	 */
	public static boolean isEnabled(){
        return debug_enabled;
    }
	
	public static void enable(Boolean state){
        debug_enabled = state;
    }
    public static void setDate(String d) {
    	date = d; 
    }

    public static void getDate() {
    	SimpleDateFormat formatter;
    	
    	if (date.isEmpty()) {			// If not debugging
        	Date today = new Date();	// return real date
    	} else {						// else use command line date string
    		date = date.toLowerCase();
    		if (date.contains("AM") || date.contains("PM")) {
    			formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss a");
    		} else {
    			formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		}
    		
    		try {
    			Date d = formatter.parse(date);
    		} catch (ParseException e) {
    			System.err.println("ERROR: Date format must be 'yyyy/mm/dd hh:mm:ss [AM|PM]'");
    			System.exit(1);
    		}
    	}
    	
    } // end getDate

    
    
	public static void log(Object o){
	    if(isEnabled()) {
	        System.out.println(o.toString());
	    }           
	}

	public static void log(String s){
	    if(isEnabled()) {
	        System.out.println(s);
	    }           
	}

}

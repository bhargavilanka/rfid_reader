package rfid_reader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Trivial debug logging
 */
public class Debug {
	static Boolean debug_enabled = false;
	static String date = ""; 				// Simulated date, passed in from command line
	static Date   dateObj = null;
	
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

	public static void setDateObj(Date d) {
    	dateObj = d; 
    }

	
	public static void setDate(String d) {
    	date = d; 
    }
    
	/**
	 * Simulate a specific date for testing. Callers can set either 
	 * a date string or a dateObj but currently we return
	 * only one with preference for the dateObj, if it's set. 
	 * Caller need to clear one or the other to force a specific one
	 * to be user (e.g. set one to null)
	 * 
	 * @return
	 */
	
	public static Date getDate() {
    	SimpleDateFormat formatter;
    	Date d = null;
    	
    	if (dateObj !=null ) {
    		return dateObj;	
    	}
    	    	
    	if (date.isEmpty()) {			// If not debugging
        	d = new Date();				// return real date
    	} else {						// else use command line date string
    		date = date.toLowerCase();
    		if (date.contains("AM") || date.contains("PM")) {
    			formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss a");
    		} else {
    			formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		}
    		
    		try {
    			d = formatter.parse(date);
    		} catch (ParseException e) {
    			System.err.println("ERROR: Date format must be 'yyyy/mm/dd hh:mm:ss [AM|PM]'");
    			System.exit(1);
    		}
    	}
    	
    	return d; 
    	
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

package rfid_reader;

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

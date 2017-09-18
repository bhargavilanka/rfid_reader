/**
 * Name: 
 * 		RFID reader
 * 
 * By: 
 * 		Peter Wolfe
 * 
 * Description:
 * 		RFID tag reader based on Java's smartcard.io framework. We are current using the ACS ACR122U reader. 
 * 		This reader can process MiFare Classic RFID tags (and other tag types). See https://en.wikipedia.org/wiki/MIFARE
 * 		There are 1K tags which can store 1024 byes and 4K tags. I think we've been buying only 1K tags.
 * 		We aren't actually storing any data on tag today - only reading it's ID. However, I'm toying with 
 * 		the idea of storing the timestamp and cumulative data
 *   
 * 		The original project started by Mr. Meredith was based on Processing (the java-based language). 
 * 		Processing makes it easy to create GUIs but is pretty horrible to code and debug in. That
 * 		was the impetus to create to pure Java based version. That and the students are more
 * 		java-savvy than processing-savvy as well.  
 *  
 * 		I started with code from: 
 * 			https://oneguyoneblog.com/2017/02/28/acr122u-nfc-java-eclipse-raspberry-pi/
 * 		which had the basic scan-one-card features using smartcard.io and referred to various
 * 		other open source projects for more detailed usage of the API. 
 * 
 * Additional Info:
 * 		For the code to make sense, refer to the smartcard.io JavaDoc and 
 * 		the ISO 7816-4 spec. Here's a summary of what you need to know. 
 * 
 *		Terms from java's smartcard.io:
 * 		Card 			A Smart Card with which a connection has been established.
 * 		CardChannel 	A logical channel connection to a Smart Card.
 * 		CardPermission 	A permission for Smart Card operations.
 * 		CardTerminal 	A Smart Card terminal, sometimes referred to as a Smart Card Reader.
 * 		CardTerminals 	The set of terminals supported by a TerminalFactory.
 * 		CommandAPDU 	A command APDU following the structure defined in ISO/IEC 7816-4.
 * 		ResponseAPDU 	A response APDU as defined in ISO/IEC 7816-4.
 * 		TerminalFactory A factory for CardTerminal objects.
 * 		APDU			Application Protocol Data Unit - the communication "unit" between a reader and a card
 * 
 *  	ISO/IEC 7816-4 is the standard the defines communications protocols with RFID tags, smartcards etc. 
 *  	SW1 and SW2 are the names for the status bytes from the spec (so two bytes when concatenated)
 *  	Cheat sheet for the SW1-SW2 codes is as follows:
 * 			9000 and 61XX - Process completed normally
 * 			62XX and 63XX - Process completed with warnings
 *   		64XX to  66XX - Process aborted - execution error
 *   		67XX to  6FXX - Process aborted - checking error
 *  	What I've seen so far with our RFID read is 9000 for a successful read
 *  	and 6881 when it tries to read with no card present. We shouldn't
 *  	be trying to read with no card present! Need to debug why the wait for card
 *  	calls aren't waiting...
 *  	The spec says SW1 = 68 is "Functions in CLA not supported. See SW2"
 *  	CLA == "class byte" or the "class" of the command. Things like
 *  	command chaining control, secure message indication, logical channel numbers, etc. 
 *  	6881 == Logical channel not supported
 *  
 *  Usage:
 *  	To run this outside of eclipse (from a command line), you need to set CLASSPATH. One way:
 *  		java -classpath "bin;lib\*" rfid_reader.RFIDreader
 *  	which assumes your PWD is the parent dir for bin and lib. 
 */

package rfid_reader;
 
import java.lang.System;
import java.util.HashMap;
import java.util.List;
import java.math.BigInteger;
import javax.smartcardio.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
 


public class RFIDreader {


	/**
	 * Initialize the card readers and process cards
	 * 
	 * @param args	array of command line arguments
	 */
	
    public static void main(String[] args) {
    	
    	Card card = null;
		CardChannel channel = null;		
		// Command to get data from the card on the reader
		CommandAPDU command = new CommandAPDU(new byte[] { (byte) 0xFF, (byte) ISO7816.INS_GET_DATA, (byte) 0x00, (byte) 0x00, (byte) 0x00 });

    	Boolean inventory = false; 
    	//Map<String, TimeLog> timelogMap = new HashMap<String, TimeLog>();

 
    	// Really need to use jopt-simple if we add more arguments
    	// KISS argument processing for now
    	if (args.length != 0) {
    		for (String argument: args) {
    			if (argument.equals("-d") || argument.equals("--debug") )
    			    Debug.enable(true);
    			if (argument.equals("-i") || argument.equals("--inventory") ) {
    				tagInventory(); 											// Inventory a set of tags to stdout
    				System.exit(0);
    			}
    		}
    	}
    	
    	// Initialize tag-to-user database
    	UserTags.read_user_tags();

    	// ZonedDateTime.now( ZoneId.of( "America/New_York" )) for a fixed timezone
    	// System we're running on MUST have proper time/timezone set!!!
    	ZonedDateTime current_time = ZonedDateTime.now();						// Format is: 2017-09-04T02:51:39.905-04:00[America/New_York]		
    	DateTimeFormatter formatter =
    			DateTimeFormatter.ofPattern(Constants.dateTimeFormatPattern); 	// Format is nicer YYYY-MM-DD time tz
    	System.out.println("Validate that the current time is: " + 
    			formatter.format(current_time)); 
    	
    	// Initialise our DB
    	Database db = new Database();
    	db.DBinit();
    	
	    try {
	        
 
	    	TerminalFactory factory = TerminalFactory.getDefault();
		    CardTerminals terminals = factory.terminals();
		    if (terminals.list().isEmpty()) { 
	            System.err.println("No readers found. Connect a reader and try again."); 
	            System.exit(1); 
	        } else {
	        	// Pjw: Todo: filter list to just all ACR122s!!!
	        	// Right now it's finding TPM drivers and other readers in addition to ours
	            Debug.log("RFID readers detected: " + terminals.list());
	        }
	
		    System.out.println("Place you card/tag/etc. on the reader to start"); 
			
	        while (  (card = waitForCard(terminals)) != null ) {
	        	
	        	card.beginExclusive();							// Only our thread should process this card
	        	try {
					Constants.LoginType login_type; 
					channel = card.getBasicChannel();
									
					ResponseAPDU response = channel.transmit(command);	// Get Data command returns the card UID
					Debug.log("Response: " + response.toString());
					   
					//if (response.getSW1() == 0x63 && response.getSW2() == 0x00)  System.out.println("Failed");
					if (response.getSW() != ISO7816.SW_NO_ERROR) {
						System.err.println("ERROR: Failed to read card. Error codes SW1+SW2: " + response.toString());
						System.err.println("Please tell a mentor!");
					
					} else {
						String UID = bin2hex(response.getData());
						Debug.log("UID: " + UID);
						UserTag user = UserTags.getUser(UID); 
						
						if (user != null) {
							Debug.log("User is: " + user);		

							// PJW: TODO: Is the user logging in or out? Won't know until the DB back end is done
							// For now, assume a login and print that message
														login_type = db.write(user.getUsername());
							
							switch (login_type) {
							case LOGIN:
								System.out.println(user.getUsername() + ". " + user.getUserLoginMsg());	
								break;

							case LOGOUT:
								System.out.println(user.getUsername() + ". " + user.getUserLogoutMsg());	
								break;
							
							case INVALID_TIME_SPAN:
								System.err.println("ERROR: login/outs cannot span multiple days. Login again. ");
								break;
							
							default:
								System.err.println("ERROR: unknown login type. Please report this to a mentor.");
								break;	
							}
							
							
						} else {										// Unknown tag
							System.out.println("Hey!!! Your RFID tag: " + UID + " is not in the database. Please see a mentor! Thanks.");
						}
						
					}
	        	} finally {
	        		card.endExclusive();
	        		card.disconnect(false);						// Done with this card channel
	        		card = null; 
	        	}
			
			} // end while scan for cards on the terminal
	        System.err.println("Yikes! Shouldn't get here unless card reader was unplugged!");
		   
/*	 
	    	UserTag user = UserTags.getUser("D58BABD2");
	    	Debug.log("User is: " + user);	
	    	db.write(user.getUsername());
	    	user = UserTags.getUser("B5FCACD2");
	    	Debug.log("User is: " + user);	
	    	db.write(user.getUsername());
	    	user = UserTags.getUser("05FCACD2");
	    	Debug.log("User is: " + user);	
	    	db.write(user.getUsername());
	    	db.reportFromDB();
	*/    	
		} catch(Exception e) {
			System.err.println("Unknown error reading RFID: " + e.toString());
			e.printStackTrace(System.err);
	 	}


    	
	 } // end main

    
    /**
     * PJW: Make this the "inventory" process OR use the CLI opensc tool    
     */
    public static void tagInventory() {
		Card card = null;
		CardChannel channel = null;		
		// Command to get data from the card on the reader
		CommandAPDU command = new CommandAPDU(new byte[] { (byte) 0xFF, (byte) ISO7816.INS_GET_DATA, (byte) 0x00, (byte) 0x00, (byte) 0x00 });

		try {
	        
		    TerminalFactory factory = TerminalFactory.getDefault();
		    CardTerminals terminals = factory.terminals();
		    if (terminals.list().isEmpty()) { 
	            System.err.println("No readers found. Connect a reader and try again."); 
	            System.exit(1); 
	        } else {
	            Debug.log("RFID readers detected: " + terminals.list());
	        }
	
		    System.out.println("Place you card/tag/etc. on the reader to start"); 
			while (  (card = waitForCard(terminals)) != null ) {
	        	
	        	card.beginExclusive();							// Only our thread should process this card
	        	try {
	
					channel = card.getBasicChannel();
									
					ResponseAPDU response = channel.transmit(command);	// Get Data command returns the card UID
					Debug.log("Response: " + response.toString());
					   
					//if (response.getSW1() == 0x63 && response.getSW2() == 0x00)  System.out.println("Failed");
					if (response.getSW() != ISO7816.SW_NO_ERROR) {
						System.err.println("ERROR: Failed to read card. Error codes SW1+SW2: " + response.toString());
						System.err.println("Please tell a mentor!");
					} else {
						System.out.println("UID: " + bin2hex(response.getData()));
	        		}
	        	} finally {
	        		card.endExclusive();
	        		card.disconnect(false);						// Done with this card channel
	        		card = null; 
	        	}
			}
			System.err.println("Yikes! Shouldn't get here!");
	   
 
		} catch(Exception e) {
			System.err.println("Unknown error reading RFID: " + e.toString());
			e.printStackTrace(System.err);
	 	}
    	
    } // end method tagInventory
    
	/**
	 * Block until we get a card insertion event
	 * 
	 * @param terminals		We can have multiple readers. Scan all readers for cards
	 * @return				Connects to the card on the reader and returns the resulting Card object 
	 * @throws CardException
	 */
    private static Card waitForCard(CardTerminals terminals) 
            throws CardException { 
        while (true) { 
        	terminals.waitForChange(0);		// Block forever waiting for state change - no timeout
        	for (CardTerminal ct : terminals.list(CardTerminals.State.CARD_INSERTION)) { 
                return ct.connect("*"); 		// Connect via any available protocol (e.g. half or full duplex)
            } 
           
        } 
    } // end waitForCard
	
	
	
	/**
	 * Returns the hex-formatted version of the byte inputs. 
	 * Used to format the data returned from the card reader
	 * which is ISO 7816-4 based
	 * 
	 * @param data	Byte data from the card
	 * @return		hex-formatted string
	 */
	static String bin2hex(byte[] data) {
	    return String.format("%0" + (data.length * 2) + "X", new BigInteger(1,data));
	}

}

/**
 *  Name: 
 *  	Database - Our RFID reader needs a simple database. The original version use a google spreadsheet
 *  	and tried to update it in real time. That had a number of disadvantages, notably the reliability 
 *  	of the school's Internet access (problems with the Sonic firewall) and performance. 
 *  	The version of attendance system will use a local database. We can copy the data to a google
 *  	spreadsheet as needed. 
 *  
 *  	What DB? We want a simple embedded DB. There are MANY with varying feature and complexity. 
 *  	We choose KISS here and are using standard Berkley DB (cause that's all we need). Basically
 *  	B-tree indexes access. 
 *  
 *  	Our table will be indexed by day (primary key) and username (secondary key) 
 *  	The "value" data for each day
 *  		- username
 *  		- time in
 *  		- time out
 *  		- total time for today
 *  	For example:
 *  		Key: 					 Time In   Time Out	   Check-ins | Time Today
 *  		1/12/2017 Wolfe, Peter | 9:00	 | 18:00 	|  1		 | 9:00:00
 *  
 * 		If you log in/out multiple times on the same day there are several use-cases:
 * 		1) Single login/logout:
 * 			- The day's first login updates Time In
 * 			- A subsequent logout updates the number of check-ins
 * 			  and the Time Today (elapsed time) and clears the in/out fields.
 * 		2) Multiple login/logouts on the same day 
 * 			- The day's 1st login updates Time In
 * 			- A 1st logout updates, check-ins, Time Today and clears the in/out fields.
 *  		- Repeat for subsequent login/out pairs, adding to check-ins, Time Today each time.
 *  	3) Login in with no logout
 *  		- User's time-in updated but gets no credit (check-ins and Time Today never updated)
 *  		- Pjw: TODO: if student realizes, need to allow a manual (mentor approved) udpate. See below. 
 *  	4) Logout with no login
 *  		- User gets no credit. (check-ins and Time Today never updated). 
 *  		- Pjw: TODO: we should warn the user of this error and allow a manual (mentor approved)
 *  		  update of the database. Need to create a code path to allow that
 *  		  (e.g. some CLI DB maintenance tasks)
 *		PJW: The above implies we don't really need a "Time Out" field. For the single login/logout
 *		case it can be computed. The the multi-login/logout case it really applies only to the last
 *		logout so is not really useful. For the logout with no login case, it can be used forensically
 *		to make sure students are staying honest with their claims so leave it for now. 
 *
 *		PJW: Modeling question. Above I modeled this as a day has a set of users. 
 *		Could have done a user has a set of days. Not sure it really matters (?). Still have
 *		to index by a primary and secondary key. However, I'm not a DB guy...
 *
 *		There is an invaluable Berkely DB tutorial here: 
 *			http://www.oracle.com/technetwork/testcontent/o27berkeleydb-100623.html
 */


package rfid_reader;

public class Database {

	
	
	
	
} // end class Database

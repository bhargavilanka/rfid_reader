/**
 * 
 */
package rfid_reader;

/**
 *  Time log records that we write to our database
 *  Note that the Berkley DB is maintained by Sleepycat and licensed
 *  to Oracle for use with Java
 *  
 *  There are command line tools to load and dump the DB
 *  See: http://docs.oracle.com/cd/E17277_02/html/java/com/sleepycat/je/util/DbDump.html
 *  for an example:
 *   java { com.sleepycat.je.util.DbDump |
 *       -jar je-<version>.jar DbDump }
 *  	-h <dir>           # environment home directory
 * 		[-f <fileName>]     # output file, for non -rR dumps
 * 		[-l]                # list databases in the environment
 * 		[-p]                # output printable characters
 * 		[-r]                # salvage mode
 * 		[-R]                # aggressive salvage mode
 * 		[-d] <directory>    # directory for *.dump files (salvage mode)
 * 		[-s <databaseName>] # database to dump
 * 		[-v]                # verbose in salvage mode
 * 		[-V]                # print JE version number	
 *
 */

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
public class AttendanceRecord {

}

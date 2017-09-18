package rfid_reader;

public interface Constants {
	
	// Filenames
	public static final String USER_RFIDTAG_MAPPING = "data/FIRST Attendance - RFID tags.csv";
	public static final String TIMELOG = "data/FIRST Attendance - timelog.csv";
	public static final String DATABASE_DIR = "data/attendance_DB";

	
	// Other constants here
	public static final String dateTimeFormatPattern = "yyyy/MM/dd HH:mm:ss z";

	public enum LoginType {
		LOGIN, 
		LOGOUT,
		INVALID_TIME_SPAN		// Login/out pairs cannot span days...
	}
}


package application;
/**
 * 
 * This interface contains all the constants used to complete the commands on the server side.
 *
 */
public interface SavedItems {
	public static final String HOST = "localhost";
	public static final int PORT = 10004;
	public static final int START = 4;
	public static final int REGISTER = 1;
	public static final int ADDFUNDS = 2;
	public static final int GETBILLS = 3;
	public static final int LOGIN = 0;
	public static final int MANUALPAYMENT = 5;
	public static final int GETNOTICES = 6;
	public static final int ARCHIVENOTICE = 7;
	public static final int UNSUBSCRIBE = 11;
	public static final int SUBSCRIBE = 10;
	public static final int DELETESTATUS = 9;
	public static final int CHECKSTATUS = 8;
	public static final int GETPROVIDERS = 15;
	public static final int GETSTATUS = 14;
	public static final int SETSTATUS = 13;
	public static final int UPDATEACCPROV = 12;

}

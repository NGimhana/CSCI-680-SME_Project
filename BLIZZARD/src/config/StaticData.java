package config;

public class StaticData {
	
	//System.getProperty("user.dir");
	public static String BRICK_EXP ="/Users/bimalkadesilva/Documents/Fall2022/CSCI680-SME/Project/BLIZZARD-Replication-Package-ESEC-FSE2018";
	
	public static double SIGNIFICANCE_THRESHOLD = 0.001;
	public static int DOI_TOPK_THRESHOLD = 5;
	
	public static int MAX_PE_SUGGESTED_QUERY_LEN = 30;
	public static int MAX_NL_SUGGESTED_QUERY_LEN = 8;
	public static int MAX_ST_SUGGESTED_QUERY_LEN = 11;
	
	
	public static int MAX_QUERY_LEN = 1024;
	public static int MAX_ST_ENTRY_LEN = 10;
	public static int MAX_ST_THRESHOLD = 10;

	public static boolean DISCARD_TOO_GOOD = false;


	public static int CROWD_API_OCC_THRESHOLD = 5;

}

package sms.plateserv.InnoComm.model;

import java.util.HashMap;
import java.util.Map;

public class ErrorCodes {

	public static final String CODE_1704 = "1704";
	public static final String CODE_1705 = "1705";
	public static final String CODE_1706 = "1706";
	public static final String CODE_1707 = "1707";
	public static final String CODE_1708 = "1708";
	public static final String CODE_1709 = "1709";
	public static final String CODE_1710 = "1710";
	public static final String CODE_1725 = "1725";
	public static final String CODE_1726 = "1726";
	public static final String CODE_1025 = "1025";
	public static final String CODE_1026 = "1026";

	public static Map<String, String> map = new HashMap<>();

	static {
		map.put(CODE_1704, "Invalid value in type field");
		map.put(CODE_1705, "Invalid message");
		map.put(CODE_1706, "Invalid destination");
		map.put(CODE_1707, "Invalid Source");
		map.put(CODE_1708, "Invalid value in \"dir\" field");
		map.put(CODE_1709, "User validation failed");
		map.put(CODE_1710, "Internal Error");
		map.put(CODE_1025, "Insufficient Credit User");
		map.put(CODE_1026, "Insufficient Credit Reseller");
	}

}
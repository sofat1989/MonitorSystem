package my.ebay.MonitorSystem;

public class Utils {
	static boolean debugflag = true;
	public static void debugprint(String str) {
		if (debugflag) {
			System.out.println(str);
		}
	}
	public static void resultprint(String str) {
		System.out.println(str);
	}
	public static void printStack(Exception e) {
		if (debugflag)
			e.printStackTrace(System.err);
	}
}
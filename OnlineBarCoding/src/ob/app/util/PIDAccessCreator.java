package ob.app.util;

public class PIDAccessCreator {
	public static String creasteAccessPID(int pid) {
		String procesID = "";

		if (pid > 199999) {
			pid = pid - 200000;
			procesID = "P-0" + Integer.toString(pid);
		} else {
			procesID = "P-" + Integer.toString(pid);
		}
		return procesID;
	}
}

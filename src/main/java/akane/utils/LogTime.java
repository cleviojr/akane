package akane.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogTime {

	public static String getTime() {
		return new SimpleDateFormat("[HH:mm:ss]").format(new Date()) + " [Akane] ";
	}

}

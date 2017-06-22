package com.madhouse.platform.smartexchange.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LogUtils {

	public static String getDetailException(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
	
}

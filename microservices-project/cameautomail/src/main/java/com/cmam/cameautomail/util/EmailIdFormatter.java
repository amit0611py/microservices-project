package com.cmam.cameautomail.util;

import java.util.Arrays;

public class EmailIdFormatter {
	
	public static String format(String emailIds) {
		return Arrays.toString(
				Arrays.stream(emailIds.trim().split(", ")).map(id -> "\""+ id + "\"").toArray()
				);
	}

}

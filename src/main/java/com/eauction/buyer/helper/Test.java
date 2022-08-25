package com.eauction.buyer.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test {

	public static void main(String[] args) {
		System.err.println("getCurrentDateTime " +getCurrentDateTime());
		System.out.println("getCurrentDateTime " +getCurrentDateTime(LocalDateTime.now()));

	}
	
	public static LocalDateTime getCurrentDateTime() {

		LocalDateTime now = LocalDateTime.now();  
        //DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		return now;//.parse(now, format);
	}
	
	public static String getCurrentDateTime(LocalDateTime now) {
		
		//LocalDateTime now = LocalDateTime.now();  
        DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String formatDateTime = now.format(format);  
        return formatDateTime;
	}

}

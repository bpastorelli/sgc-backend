package br.com.sgc.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.activation.MimetypesFileTypeMap;

public class Utils {
	
	public static String dateFormat(Date date, String format) {
		
		LocalDate dateFormat = convertToLocalDate(date);
		DateTimeFormatter formatador = 
		  DateTimeFormatter.ofPattern(format);
		return dateFormat.format(formatador);
	}
	
	public static String dateFormat(LocalDate date, String format) {
		
		DateTimeFormatter formatador = 
		  DateTimeFormatter.ofPattern(format);
		return date.format(formatador);
	}
	
	public static LocalDate convertToLocalDate(Date dateToConvert) {
	    return Instant.ofEpochMilli(dateToConvert.getTime())
	      .atZone(ZoneId.systemDefault())
	      .toLocalDate();
	}
	

	public static String getFileTypeByMimetypesFileTypeMap(final String fileName){    
	
	    final MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
	    return fileTypeMap.getContentType(fileName);
	
	}
	
	public static boolean compareDate(Date date1, Date date2) {
		
		int result = date1.compareTo(date2);
		return result == 0 ? true : false;
		
	}
	
	public static Date parseDate(String date) {
	    
		try {
	        return new SimpleDateFormat("dd/MM/yyyy").parse(date);
	    } catch (ParseException e) {
	        return null;
	    }
	}
	
	public static Date parseDate(LocalDate date) {
	    
	     return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	     
	}
	
	public static LocalDate parseDate(Date date) {
		
		return date.toInstant()
			      .atZone(ZoneId.systemDefault())
			      .toLocalDate();
		
	}
	
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
	
}

package br.com.sgc.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

public class Utils {
	
	public static String dateFormat(Date date, String format) {
		
		LocalDateTime dateFormat = convertToLocalDateTime(date);
		DateTimeFormatter formatador = 
		  DateTimeFormatter.ofPattern(format);
		return dateFormat.format(formatador);
	}
	
	public static String dateFormat(LocalDate date, String format) {
		
		DateTimeFormatter formatador = 
		  DateTimeFormatter.ofPattern(format);
		return date.format(formatador);
	}
	
	public static String dateFormat(LocalDateTime date, String format) {
		
		DateTimeFormatter formatador = 
		  DateTimeFormatter.ofPattern(format);
		return date.format(formatador);
	}
	
	public static LocalDate convertToLocalDate(Date dateToConvert) {
	    return Instant.ofEpochMilli(dateToConvert.getTime())
	      .atZone(ZoneId.systemDefault())
	      .toLocalDate();
	}

	public static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
	    return Instant.ofEpochMilli(dateToConvert.getTime())
	      .atZone(ZoneId.systemDefault())
	      .toLocalDateTime();
	}
	
	public static LocalDateTime convertToLocalDateTime(LocalDate dateToConvert) {
		
		LocalDateTime dt = dateToConvert.atStartOfDay();
		
	    return dt;
	}
	
	public static Date convertToDate(LocalDate date) {
		
		ZoneId defaultZoneId = ZoneId.systemDefault();
		
		return Date.from(date.atStartOfDay(defaultZoneId).toInstant());
	}
	
	public static Long convertToLong(LocalDateTime date) {
		
		Long dt = date.getLong(ChronoField.DAY_OF_YEAR);
		
	    return dt;
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
	
	public static LocalDateTime convertToLocalDateTime(CharSequence dateToConvert) {
	    
		return LocalDateTime.parse(dateToConvert);
		
	}
	
	public static LocalDateTime convertToLocalDateTime(String dateToConvert) {
	    
		return LocalDateTime.parse(dateToConvert);
		
	}
	
	public static LocalDate parseDate(Date date) {
		
		return date.toInstant()
			      .atZone(ZoneId.systemDefault())
			      .toLocalDate();
		
	}
	
	public static LocalDateTime ajustaData(LocalDate date) {
		
		LocalDateTime date1 = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0, 0);
		
		return date1;
		
	}
	
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    public static String formatPlaca(String placa) {
    	
    	String p1 = null;
    	String p2 = null;
    	
    	if(placa != null && !placa.isBlank()) {
    		p1 = placa.substring(0, 3);
    		p2 = placa.substring(3, 7);
    		placa = p1.concat("-").concat(p2);
    	}else {
    		placa = "";
    	}
		
		return placa;
    }
    
    public static <T> List<T> getPage(List<T> sourceList, int page, int pageSize) {
        if(pageSize <= 0 || page <= 0) {
            throw new IllegalArgumentException("invalid page size: " + pageSize);
        }
        
        int fromIndex = (page - 1) * pageSize;
        if(sourceList == null || sourceList.size() <= fromIndex){
            return Collections.emptyList();
        }
        
        // toIndex exclusive
        return sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size()));
    }
    
    public static String formatNumber(BigDecimal valor, String format) {
    	
    	DecimalFormat df2 = new DecimalFormat(format);
    	
    	return df2.format(valor);
    }
    
    public static String formatNumber(BigDecimal valor) {
    	
    	return formatNumber(valor, "#,###.00");
    }
	
}

package br.com.sgc.utils;

import java.math.BigDecimal;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import br.com.sgc.enums.DataTypeEnum;

public class WorkbookUtils {
	
	
	@SuppressWarnings("unchecked")
	public static <T> T getCellValue(int rowIndex, int col, XSSFSheet worksheet, DataTypeEnum dataType) {
		
		XSSFRow row = worksheet.getRow(rowIndex);
		CellType getType = worksheet.getRow(rowIndex).getCell(col).getCellType();
		
		T value = null;
		
		if(getType.equals(CellType.STRING)) {
			value = (T)row.getCell(col).getStringCellValue();	
		}else if(getType.equals(CellType.NUMERIC)) {
			switch (dataType) {
				case BIG_DECIMAL:
					value = (T)BigDecimal.valueOf(row.getCell(col).getNumericCellValue());
					break;
				case DATE:
					value = (T)row.getCell(col).getLocalDateTimeCellValue();
					break;
				case CPF:
					value = (T)Long.valueOf((long)row.getCell(col).getNumericCellValue()).toString();
					break;
			default:
				break;
					
			}
		}
		
		return value;
	}

}

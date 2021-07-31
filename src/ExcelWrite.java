import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWrite {
	
	
	public void createDailyExcelSheet(ArrayList<Employee> team, Date date) {
		
	}
	
	
	
	
	
	
	////////////////////////////////////////////////////////////////
	//					outputExcel								  //	
	// Method takes in arrays in their organized format and 	  //
	// outputs them to a new excel document (daily schedules)	  //
	////////////////////////////////////////////////////////////////
	@SuppressWarnings("deprecation")
	public void outputExcel(int weekday, String[] sections, String[] mobile, ArrayList<Employee> team, String date) throws IOException{
		File src = new File("src\\Daily Master.xlsx");
		FileInputStream fis = new FileInputStream(src); 
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sheet1 = wb.getSheetAt(0); 
		
		//local variables
		//int finalRowNum = this.getEmployeeNames(weekday).length; 
		//(use length of array to determine number of rows in excel 
		//sheet, NOT USED, format problem after row shift)
		
		int rowNum = 5;
		String fileName = "";
		String cellData = "";
		Cell cell = sheet1.getRow(0).getCell(0);
		int columnSection = 0;
		int columnName = 2;
		int columnShift = 3;
		int columnBreak1 = 4;
		int columnBreak2 = 5;
		int columnMobileResp = 6;
		
		String[] arraySections = sections;
		String[] arrayMobile = mobile;
		
		//determine file name for daily excel sheet
		if(weekday == 3)
		fileName = "MONDAY";
		else if(weekday == 4)
		fileName = "TUESDAY";
		else if(weekday == 5)
		fileName = "WEDNESDAY";
		else if(weekday == 6)
		fileName = "THURSDAY";
		else if(weekday == 7)
		fileName = "FRIDAY";
		else if(weekday == 8)
		fileName = "SATURDAY";
		else
		fileName = "SUNDAY";
		
		// Create a new font and alter it.
		Font font = wb.createFont();
		font.setFontHeightInPoints((short)14);
		font.setFontName("Century Gothic");
		
		// Sets new font and cell style appropriate for output
		XSSFCellStyle style = wb.createCellStyle();
		style.setFont(font);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		
		// Sets new font and cell style appropriate for output (with green background)
		XSSFCellStyle style2 = wb.createCellStyle();
		style2.setFont(font);
		style2.setAlignment(CellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style2.setBorderBottom(CellStyle.BORDER_THIN);
		style2.setBorderTop(CellStyle.BORDER_MEDIUM);
		style2.setBorderRight(CellStyle.BORDER_THIN);
		style2.setBorderLeft(CellStyle.BORDER_THIN);
		style2.setFillForegroundColor(new XSSFColor(new java.awt.Color(204, 255, 204)));
		style2.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		// Output day of the week
		cell = sheet1.getRow(2).getCell(5);
		cell.setCellValue(fileName);
		cell.setCellStyle(style2);
		
		//Output the date
		cell = sheet1.getRow(3).getCell(5);
		cell.setCellValue(date);
		cell.setCellStyle(style2);
		
		// Output Sections
		rowNum = 5;
		for(int i = 0; i < arraySections.length; i++){
			cell = sheet1.getRow(rowNum).getCell(columnSection);
			cell.setCellValue(arraySections[i]);
			cell.setCellStyle(style);
			rowNum++;
		}
		
		// Output names and breaks
		rowNum = 5;
		for(int h = 0; h < team.size(); h++){
			cellData = sheet1.getRow(rowNum).getCell(columnSection).getStringCellValue();
			for(Employee output : team){
				if(cellData.equals(output.getSection())){
					//output names
					cell = sheet1.getRow(rowNum).getCell(columnName);
					cell.setCellValue(output.getName());
					//output shift times
					cell = sheet1.getRow(rowNum).getCell(columnShift);
					cell.setCellValue(output.getShift());
					//output Break 1
					cell = sheet1.getRow(rowNum).getCell(columnBreak1);
					if(output.isPartTime){
						if(output.getFifteen()){
							cell.setCellValue(output.getFirstBreak() + "(15m)");
						}
						else
							cell.setCellValue(output.getFirstBreak());
					}
					else{
						cell.setCellValue(output.getFirstBreak());
					}
					//output break 2
					cell = sheet1.getRow(rowNum).getCell(columnBreak2);
					if(output.isPartTime){
						if(!output.getFifteen()){
							cell.setCellValue(output.getSecondBreak() + "(15m)");
						}
						else
							cell.setCellValue(output.getSecondBreak());
					}
					else
						cell.setCellValue(output.getSecondBreak());
					//set cell Style
					cell.setCellStyle(style);
				}
			}
			rowNum++;
		}
		
		// Output mobile responders
		rowNum = 5;
		for(int i = 0; i < arrayMobile.length; i++){
		cell = sheet1.getRow(rowNum).getCell(columnMobileResp);
		cell.setCellValue(arrayMobile[i]);
		cell.setCellStyle(style);
		rowNum++;
		}
		
		//get desktop location
		String userHomeFolder = System.getProperty("user.home");
		FileOutputStream fileOut = new FileOutputStream(new File(userHomeFolder + "//Desktop//" + fileName + ".xlsx"));
		wb.write(fileOut);
		fileOut.close();
		System.out.println(fileName + " file output complete");
		
		fis.close(); 
		wb.close();
	}
	
	////////////////////////////////////////////////////////////////
	//					setColor								  //	
	// Method creates a custom color by replacing LAVENDER with a //
	// custom color. 											  //
	////////////////////////////////////////////////////////////////
	public HSSFColor setColor(HSSFWorkbook workbook, byte r,byte g, byte b){
		HSSFPalette palette = workbook.getCustomPalette();
		HSSFColor hssfColor = null;
		try 
		{
			hssfColor= palette.findColor(r, g, b); 
			if (hssfColor == null )
			{
			    palette.setColorAtIndex(HSSFColor.LAVENDER.index, r, g,b);
			    hssfColor = palette.getColor(HSSFColor.LAVENDER.index);
			}
		} 
		catch (Exception e) 
		{
			System.out.println("Exception caught in setColor method!");
		}

		 return hssfColor;
		}

}

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.*;

public class ExcelRead {

	private static final String OFF = "OFF";
	private static final String PTO = "PTO";
	private static final String LOA = "LOA";
	private static final String LEAD = "LEAD";
	private final String SWING = "Swing";
	private final String DAY = "Day";
	private final String GRAVE = "Grave";
	private String filePath = "";
	private ArrayList<Employee> team = new ArrayList<Employee>();
	private File src; 
	private FileInputStream fis; 
	private XSSFWorkbook wb; 
	private XSSFSheet sheet1; 
	private DataFormatter formatter; 

	//constructor, initialize variables
	public ExcelRead(String filePath) throws IOException {
		this.filePath = filePath;
		this.src = new File(filePath); 
		this.fis = new FileInputStream(src); 
		this.wb = new XSSFWorkbook(fis); 
		this.sheet1 = wb.getSheetAt(0); 
		this.formatter = new DataFormatter(); 
	}
	
	public void setTeam(String shift, String weekday) throws IOException{
		int shiftRow = getShiftRow(shift);
		//String scheduleDate = parseDate(date); TODO
		String scheduleDate = "7-Jun";  //hardcoded test case
		Row dateRow = sheet1.getRow(shiftRow + 1);
		int dateColumn = getDateColumn(scheduleDate, dateRow);
		
		
		
		
		
		
		
	}
	
	private ArrayList<Employee> readEmployees(Row dateRow, int dateColumn){
		
		
		
		
		return team;
	}
	
	//returns column number for specified date
	//if not found, returns -1
	private int getDateColumn(String date, Row dateRow) {
		int dateColumn = -1;
		
		for(Cell cell : dateRow) {
			if(cell.getStringCellValue().equals(date)) {
				dateColumn = cell.getColumnIndex();
			}
		}
		return dateColumn;
	}

	//returns row number of shift on excel sheet
	//if not found, returns -1
	private int getShiftRow(String shift) {
		int rowNum = -1;
		for(Row row : sheet1) {
			for(Cell cell : row) {
				if (formatter.formatCellValue(cell).contains(shift)){
                    rowNum = row.getRowNum();
                }
			}
		}
		return rowNum;
	}
	
	
	//converts date object into Day#-Month format for excel schedule
	//TODO determine format Swing date widget sends date information
	private String parseDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);  //monday/tuesday/wednesday
		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
		String month = new SimpleDateFormat("MMM").format(c.getTime());
		
		return dayOfMonth + "-" + month;
	}
	
	private int getDateColumn(int shiftRow, String date) {
		int columnNum = -1;
		int dateRow = shiftRow + 1;
		
		for(Row row : sheet1) {
			for(Cell cell : row) {
				if (formatter.formatCellValue(cell).contains(date)){
					columnNum = row.getRowNum();
                }
			}
		}
		return columnNum;
	}
	
	
	
	
	
	
	
	
	
	

	////////////////////////////////////////////////////////////////
	//					setAllEmployeeNames                		  //	
	// Method gets the names of all employees currently employed  //
	// and listed on the excel sheet for that shift(i.e swing)    //
	////////////////////////////////////////////////////////////////
	public void setAllEmployeeNames() throws IOException{
		File src = new File(filePath); 
		FileInputStream fis = new FileInputStream(src); 
		XSSFWorkbook wb = new XSSFWorkbook(fis); 
		XSSFSheet sheet1 = wb.getSheetAt(0); 
		DataFormatter formatter = new DataFormatter();

		//local variables
		int startingRow = 0;
		String nameData;
		Pattern p = Pattern.compile(", ([A-Za-z]+)");

		//find startingRow, row will contain number 1 next to employee name
		while(isNotOne){
			numCell = sheet1.getRow(startingRow).getCell(inLeftMostColumn);
			String rowInfo = formatter.formatCellValue(numCell);
			if(rowInfo.equals(ONE)){
				isNotOne = false;
			}
			startingRow++;
		}
		this.setStartingRow(startingRow - 1);

		//get all employee names starting at starting row, ending at blank cell(exception)
		while (!isEndOfRow){
			try{
				for(int i = 0; i < allNames.length; i++){
					nameData = sheet1.getRow(startingRow - 1).getCell(1).getStringCellValue();
					namesAndBadge[i] = nameData;
					Matcher m = p.matcher(nameData);
					m.find();
					allNames[i] = m.group(1);
					startingRow+=1;
				}
			}
			catch(Exception e)
			{isEndOfRow = true;}
		}
		isEndOfRow = false;
		wb.close(); 
	}

	//sets all employee hours, used to determine partTime status
	public void setAllEmployeeHours() throws IOException{
		File src = new File(filePath); 
		FileInputStream fis = new FileInputStream(src); 
		XSSFWorkbook wb = new XSSFWorkbook(fis); 
		XSSFSheet sheet1 = wb.getSheetAt(0); 
		DataFormatter formatter = new DataFormatter();
		//local variables
		int startingRow = 0;
		String hourData;
		isNotOne = true;
		
		//find startingRow, row will contain number 1 next to employee name
		while(isNotOne){
			numCell = sheet1.getRow(startingRow).getCell(inLeftMostColumn);
			String rowInfo = formatter.formatCellValue(numCell);
			if(rowInfo.equals(ONE)){
				isNotOne = false;
			}
			startingRow++;
		}
		this.setStartingRow(startingRow - 1);

		//get all employee hours starting at starting row, ending at blank cell(exception)
		while (!isEndOfRow){
			try{
				for(int i = 0; i < allNames.length; i++){
					hourData = sheet1.getRow(startingRow - 1).getCell(2).getStringCellValue();
					allHours[i] = hourData;
					startingRow++;
				}
			}
			catch(Exception e)
			{isEndOfRow = true;}
		}
		isEndOfRow = false;
		wb.close(); 
	}

	////////////////////////////////////////////////////////////////
	//					setEmployeeNames                		  //	
	// Method reads excel sheet and gets the names of the         //
	// employees working for the day passed through the parameter //
	////////////////////////////////////////////////////////////////
	public void setEmployeeNames(int weekday) throws IOException{
		File src = new File("D:\\Eclipse\\Schedule\\Weekly.xlsx"); 
		FileInputStream fis = new FileInputStream(src); 
		XSSFWorkbook wb = new XSSFWorkbook(fis); 
		XSSFSheet sheet1 = wb.getSheetAt(0); 

		//declare and initialize local variables
		String nameData;
		int nameCell = 1;
		Pattern p = Pattern.compile(", ([A-Za-z]+)");
		int k = 0;

		//Loop, get names, partially fill array with correct name format (first name only)
		while (!isEndOfRow){
			try{
				String dayData = sheet1.getRow(startingRow).getCell(weekday).getStringCellValue();

				if(dayData.equals(OFF) || dayData.equals(PTO) || dayData.equals(LOA) || dayData.contains(DUAL_RATE)){
					System.out.println("Employee off, continue");
					startingRow+=1;
					System.out.println("starting row ++ = " + startingRow);
					k+=1;
					continue;
				}
				else{
					nameData = sheet1.getRow(startingRow).getCell(nameCell).getStringCellValue();
					System.out.println("Shift is for swing, name = " + nameData);
				}

				Matcher m = p.matcher(nameData);
				m.find();
				System.out.println("Array Index = " + (startingRow-k-9));
				names[weekday-3][startingRow-k-9] = m.group(1);
				startingRow+=1;

			}
			catch(IllegalStateException e){
				System.out.println("End of excel file reached!");
				isEndOfRow = true;
			}
		}
		isEndOfRow = false;
		wb.close(); 
	}

	////////////////////////////////////////////////////////////////
	//					setWorkHours	                		  //	
	// Method reads excel sheet and gets the shift hours of the   //
	// employees working for the specified weekday				  //
	////////////////////////////////////////////////////////////////
	public void setEmployeeHours(int weekday) throws IOException{
		File src = new File(filePath);
		FileInputStream fis = new FileInputStream(src); 
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sheet1 = wb.getSheetAt(0); 
		DataFormatter formatter = new DataFormatter();  //converts numerical cell to string

		//declare and initialize local variables
		int index = 0;
		startingRow = 0;
		isNotOne = true;

		//find startingRow, row will contain number 1 next to employee name
		while(isNotOne){
			numCell = sheet1.getRow(startingRow).getCell(inLeftMostColumn);
			String rowInfo = formatter.formatCellValue(numCell);
			if(rowInfo.equals(ONE)){
				isNotOne = false;
			}
			startingRow++;
		}
		this.setStartingRow(startingRow - 1);

		while (!isEndOfRow){
			try{
				String dayData = sheet1.getRow(startingRow).getCell(weekday).getStringCellValue();

				if(dayData.equals(OFF) || dayData.equals(PTO) || dayData.equals(LOA) || dayData.contains(DUAL_RATE)){
					startingRow += 1;
					continue;
				}
				else
					times[weekday-3][index] = sheet1.getRow(startingRow).getCell(weekday).getStringCellValue();
				index+=1;
				startingRow+=1;
			}
			catch(Exception e)
			{isEndOfRow = true;}
		}
		isEndOfRow = false;
		wb.close(); //close wb, prevent memory leak
	}


	////////////////////////////////////////////////////////////////
	//					setSections		                		  //	
	// Method sets the sections based on the number of employees  //
	// available for any given day								  //
	////////////////////////////////////////////////////////////////
	public void setSections(ArrayList<Employee> team, int weekday) throws IOException{
		int scheduleSize = team.size();

		if(scheduleSize == 13)
			sections[weekday-3] = new String[] {"Front Floater", "1", "2", "3", "Breaker Front", 
					"Back Floater", "4", "5", "8", "9", "6", "7", "Breaker Back"};
		else if(scheduleSize == 12)
			sections[weekday-3] = new String[] {"1", "2", "3", "Breaker Front",
					"Float All", "Breaker Back", "4", "5", "8", "9", "6", "7"};
		else if(scheduleSize == 11)
			sections[weekday-3] = new String[] {"1", "2", "3", "Breaker Front",
					"4", "5", "8", "9", "6", "7", "Breaker Back"};
		else if(scheduleSize == 10)
			sections[weekday-3] = new String[] {"1", "2", "3", "Breaker All", "4", "5", "8", "9", "6", "7"};
		else if(scheduleSize == 9)
			sections[weekday-3] = new String[] {"1", "2", "3", "4", "5", "8", "9", "6", "7"};
		else if(scheduleSize == 8)
			sections[weekday-3] = new String[] {"1,2", "2,1", "3,6,7", "4,5", "5,4", "8,9", "9,8", "6,7,3"};
		else if(scheduleSize == 7)
			sections[weekday-3] = new String[] {"1,2", "2,3", "4", "5", "8", "9", "6,7"};
		else if(scheduleSize == 6)
			sections[weekday-3] = new String[] {"1,2", "2,3", "Breaker All", "4,5", "8,9", "6,7"};
		else if(scheduleSize == 5)
			sections[weekday-3] = new String[] {"1,2", "2,3", "4,5", "8,9", "6,7"};
		else if(scheduleSize == 4)
			sections[weekday-3] = new String[] {"1,2,3", "6,7", "4,5", "8,9"};
		else if(scheduleSize == 3)
			sections[weekday-3] = new String[] {"1,2,3", "6,7,NSA", "4,5,8,9"};
	}

	////////////////////////////////////////////////////////////////
	//					setMobile		                		  //	
	// Method sets the mobile responders based on the number      //
	// of employees available for any given day					  //
	////////////////////////////////////////////////////////////////
	public void setMobile(ArrayList<Employee> team, int weekday) throws IOException{
		int scheduleSize = team.size();

		if(scheduleSize == 13)
			mobile[weekday-3] = new String[] {"1-3", "1,2,3", "1,2,3", "1,2,3", "1,2,3",
					"4-9", "4,5", "5,4", "8,9", "9,8", "6,7", "7,6", "4-9"};
		else if(scheduleSize == 12)
			mobile[weekday-3] = new String[] {"1,2,3", "1,2,3", "1,2,3", "1,2,3", 
					"All", "4-9", "4,5", "5,4", "8,9", "9,8", "6,7", "7,6"};
		else if(scheduleSize == 11)
			mobile[weekday-3] = new String[] {"1,2,3", "1,2,3", "1,2,3", "1,2,3",
					"4,5", "5,4", "8,9", "9,8", "6,7", "7,6", "4-9"};
		else if(scheduleSize == 10)
			mobile[weekday-3] = new String[] {"1,2,3", "1,2,3", "1,2,3", "All",
					"4,5", "5,4", "8,9", "9,8", "6,7", "7,6"};
		else if(scheduleSize == 9)
			mobile[weekday-3] = new String[] {"1,2,3", "1,2,3", "1,2,3", "4,5",
					"5,4", "8,9", "9,8", "6,7", "7,6"};
		else if(scheduleSize == 8)
			mobile[weekday-3] = new String[] {"1,2,3", "1,2,3", "3,6,7", "4,5", "5,4", "8,9", "9,8", "6,7,3"};
		else if(scheduleSize == 7)
			mobile[weekday-3] = new String[] {"1,2,3", "1,2,3", "4,5", "5,4,8", "8,9,5", "9,8,5", "6,7,3"};
		else if(scheduleSize == 6)
			mobile[weekday-3] = new String[] {"1,2,3", "1,2,3", "All", "4,5,8", "8,9,5", "6,7,3"};
		else if(scheduleSize == 5)
			mobile[weekday-3] = new String[] {"ALL", "ALL", "ALL", "ALL", "ALL"};
		else if(scheduleSize == 4)
			mobile[weekday-3] = new String[] {"ALL", "ALL", "ALL", "ALL"};
		else if(scheduleSize == 3)
			mobile[weekday-3] = new String[] {"ALL", "ALL", "ALL"};
	}

	////////////////////////////////////////////////////////////////
	//					setRowCount			               		  //	
	// Method makes sure the number of rows in employee column is //
	// equal to the number of employees working that day		  //
	////////////////////////////////////////////////////////////////
	public void setRowCount() throws IOException{
		File src = new File(filePath);
		FileInputStream fis = new FileInputStream(src);
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sheet1 = wb.getSheetAt(0); 

		int startingRow = 5;
		String cellString = "";
		String host = "Casino Host";

		//Counts rows in weekly schedule excel file and stops at "Casino Host" string
		while (!isEndOfRow){
			try{
				for(int i = 0; i < 30; i++){
					cellString = sheet1.getRow(startingRow).getCell(0).getStringCellValue();
					startingRow++;
					rowCount++;
					if(cellString.equals(host)){
						isEndOfRow = true;
						break;
					}
				}
			}
			catch(Exception e)
			{isEndOfRow = true;}
		}
		isEndOfRow = false;
		rowCount = rowCount - 5;
		wb.close();
	}

	////////////////////////////////////////////////////////////////
	//					setDate			                		  //	
	// Sets the date for the current weekday					  //
	////////////////////////////////////////////////////////////////
	public void setDate(int column) throws IOException{
		File src = new File(filePath); 
		FileInputStream fis = new FileInputStream(src); 
		XSSFWorkbook wb = new XSSFWorkbook(fis); 
		XSSFSheet sheet1 = wb.getSheetAt(0); 
		DataFormatter formatter = new DataFormatter();  //converts numerical cell to string

		Cell dateInt = sheet1.getRow(1).getCell(column);
		String dateString = formatter.formatCellValue(dateInt);

		String[] parts = dateString.split("-");
		String day = parts[0];
		String month = parts[1];

		date = day + "-" + month + "-" + new SimpleDateFormat("YYYY").format(new Date());;
	}

	////////////////////////////////////////////////////////////////
	//					getAllEmployeeNames                		  //	
	// Method returns an array of the names of all employees on   //
	// the excel sheet, working or off shift(i.e. swing) 		  //
	////////////////////////////////////////////////////////////////
	public String[] getAllEmployeeNames(){
		int nulls = 0;

		for(int i = 0; i < allNames.length; i++){
			if(allNames[i] != null)
				continue;
			else
				nulls++;
		}

		int newLength = allNames.length - nulls;
		String[] newAllNames = new String[newLength];

		for(int j = 0; j < newLength; j++){
			newAllNames[j] = allNames[j];
		}
		return newAllNames;
	}
	
	//get all employee hours, from column 2 on excel sheet
	public String[] getAllHours(){
		int nulls = 0;

		for(int i = 0; i < allHours.length; i++){
			if(allHours[i] != null)
				continue;
			else
				nulls++;
		}

		int newLength = allHours.length - nulls;
		String[] newAllHours = new String[newLength];

		for(int j = 0; j < newLength; j++){
			newAllHours[j] = allHours[j];
		}
		return newAllHours;
	}

	////////////////////////////////////////////////////////////////
	//					getEmployeeNames                		  //	
	// Method returns an array of the names of employees working  //
	// for that day, parallel array with times[]                  //
	////////////////////////////////////////////////////////////////
	public String[] getEmployeeNames(int weekday){
		int nulls = 0;

		for(int i = 0; i < names[weekday-3].length; i++){
			if(names[weekday-3][i] != null)
				continue;
			else
				nulls++;
		}

		int newLength = (names[weekday-3].length) - nulls;
		String[] newNames = new String[newLength];

		for(int j = 0; j < newLength; j++){
			newNames[j] = names[weekday-3][j];
		}
		return newNames;
	}


	////////////////////////////////////////////////////////////////
	//					getEmployeeHours                		  //	
	// Method returns an array of the shift times the employees   //
	// are working for that day, parallel array with names[]      //
	////////////////////////////////////////////////////////////////
	public String[] getEmployeeHours(int weekday){
		int nulls = 0;

		//counts number of null values in array
		for(int i = 0; i < times[weekday-3].length; i++){
			if(times[weekday-3][i] != null)
				continue;
			else
				nulls++;
		}

		//creates new array without the null values
		int newLength = (times[weekday-3].length) - nulls;
		String[] newTimes = new String[newLength];

		for(int j = 0; j < newLength; j++){
			newTimes[j] = times[weekday-3][j];
		}
		return newTimes;
	}

	////////////////////////////////////////////////////////////////
	//					formatShiftTimes			              //	
	// Removes the 'a' and 'p' from shift times (from Master) and //
	// creates a new array with the formatted shift times		  //
	////////////////////////////////////////////////////////////////
	public String[] formatEmployeeHours(String[] shiftTimes){
		String unformatTime = "";
		int length = shiftTimes.length;
		String[] shiftTimesFinal = new String[length];
		for(int i = 0; i < shiftTimes.length; i++){
			unformatTime = shiftTimes[i];
			String newTime = "";
			for(int j = 0; j < unformatTime.length(); j++){
				char c = unformatTime.charAt(j);
				if(c == 'a' || c == 'p')
					continue;
				else
					newTime += c;
			}
			shiftTimesFinal[i] = newTime;
		}
		return shiftTimesFinal;
	}

	////////////////////////////////////////////////////////////////
	//					getSections			               		  //	
	// Method returns an array of the sections the employees      //
	// are working in that day								      //
	////////////////////////////////////////////////////////////////
	public String[] getSections(int weekday){
		return sections[weekday-3];
	}

	////////////////////////////////////////////////////////////////
	//					getMobile			               		  //	
	// Method returns an array of the mobile values the employees //
	// are using for that day, parallel array with getSections[]  //
	////////////////////////////////////////////////////////////////
	public String[] getMobile(int weekday){
		return mobile[weekday-3];
	}

	////////////////////////////////////////////////////////////////
	//					getRowCount		               		      //	
	// Method returns an the row count for number of employees    //
	////////////////////////////////////////////////////////////////
	public int getRowCount(){
		return rowCount;
	}

	////////////////////////////////////////////////////////////////
	//					getDate			                		  //	
	// Methods returns the date for the current weekday			  //
	////////////////////////////////////////////////////////////////
	public String getDate(){
		return date;
	}

	////////////////////////////////////////////////////////////////
	//					getAllNamesAndBadges                	  //	
	// Method returns an array of the names of all employees on   //
	// the excel sheet, working or off shift(i.e. swing) 		  //
	////////////////////////////////////////////////////////////////
	public String[] getAllNamesAndBadges(){
		int nulls = 0;

		for(int i = 0; i < namesAndBadge.length; i++){
			if(namesAndBadge[i] != null)
				continue;
			else
				nulls++;
		}

		int newLength = namesAndBadge.length - nulls;
		String[] newAllNames = new String[newLength];

		for(int j = 0; j < newLength; j++){
			newAllNames[j] = namesAndBadge[j];
		}
		return newAllNames;
	}

	//set File path
	public void setFilePath(String absoluteFilePath){
		filePath = absoluteFilePath;
	}

	//get file path
	public String getFilePath(){
		return filePath;
	}
}
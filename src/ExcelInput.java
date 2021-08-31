import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

public class ExcelInput {

	private static final String OFF = "OFF";
	private static final String PTO = "PTO";
	private static final String LOA = "LOA";
	private static final String LEAD = "LEAD";
	private final String SWING = "Swing";
	private final String DAY = "Day";
	private final String GRAVE = "Grave";
	private String filePath = "";
	private ArrayList<String> dates = new ArrayList<String>();
	private ArrayList<Employee> team = new ArrayList<Employee>();
	private File src; 
	private FileInputStream fis; 
	private XSSFWorkbook wb; 
	private XSSFSheet sheet1; 
	private DataFormatter formatter; 

	//constructor, initialize variables
	public ExcelInput(String filePath) throws IOException {
		this.filePath = filePath;
		this.src = new File(filePath); 
		this.fis = new FileInputStream(src); 
		this.wb = new XSSFWorkbook(fis); 
		this.sheet1 = wb.getSheetAt(0); 
		this.formatter = new DataFormatter(); 
		setDates();
	}
	
	
	//reads excel sheet and populates the date arraylist with
	//dates #day-Month contained in sheet for that week
	private void setDates() {
		int shiftRow = getShiftRow(DAY);
		Row dateRow = sheet1.getRow(shiftRow + 1);
		for(Cell cell : dateRow) {
			dates.add(formatter.formatCellValue(cell));
		}
	}

	
	//reads excel sheet and populates team arraylist with
	//employee's for that shift and day
	//employee's names are read from excel sheet and files are
	//de-serialized/created based on employee name
	public void setTeam(String shift, String date) throws IOException{
		int employeeRow = getShiftRow(shift) + 2;
		//Get shift row
		//get column index by finding corresponding date AFTER shift row
		//start at shift row + 2 (skip date and weekday rows)
		//iterate through the rest of the rows, getting employee data
		//until cell == "total"
		
		//then get leadership team
		
		
		
		
		
		
		
		//team = readEmployees(dateRow, dateColumn);
		
		
		
		
		
	}

	//returns row number of shift on excel sheet
	//if not found, returns -1
	private int getShiftRow(String shift) {
		System.out.println("IN SHIFTROW METHOD");
		int rowNum = -1;
		
		outerloop:
		for(Row row : sheet1) {
			for(Cell cell : row) {
				if (formatter.formatCellValue(cell).contains(shift)){
                    rowNum = row.getRowNum();
					System.out.println("Cell info: " + cell.getStringCellValue() + " in row " + rowNum);
                    break outerloop;
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
	
	//set File path
	public void setFilePath(String absoluteFilePath){
		filePath = absoluteFilePath;
	}

	//get file path
	public String getFilePath(){
		return filePath;
	}
}
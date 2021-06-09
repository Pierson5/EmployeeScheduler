import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


//Serializes employee's files. Rotation values are then loaded to use the
//next time a schedule is being created. 
public class Serialize {

	static String employeeName;
	static String filename;
	static String fileLocation;

	////////////////////////////////////////////////////////////////
	//					saveEmployeeFiles			              //	
	// Takes team of employee objects and serializes them to	  //
	// employee folder.											  //
	////////////////////////////////////////////////////////////////
	public static void saveEmployeeFiles(ArrayList<Employee> team){
		for(Employee toSave : team){
			employeeName = toSave.getFullName();
			filename = employeeName + ".ser";
			fileLocation = "src\\Employee Files\\" + filename;
			try{
				ObjectOutputStream fileOut = new ObjectOutputStream(new FileOutputStream(fileLocation));
				fileOut.writeObject(toSave);
				System.out.println("File saved for " + employeeName);
				fileOut.close();
			} catch(FileNotFoundException e){
				e.printStackTrace();
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	////////////////////////////////////////////////////////////////
	//					saveEmployee    			              //	
	// Saves single employee 									  //
	////////////////////////////////////////////////////////////////
	public static void saveEmployee(Employee employee){
		employeeName = employee.getFullName();
		filename = employeeName + ".ser";
		fileLocation = "src\\Employee Files\\" + filename;
		try{
			ObjectOutputStream fileOut = new ObjectOutputStream(new FileOutputStream(fileLocation));
			fileOut.writeObject(employee);
			System.out.println("File saved for " + employeeName);
			fileOut.close();
		} catch(FileNotFoundException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}


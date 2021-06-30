import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class main {
	
	private static String empFilePath = "";
	private static String excelFilePath = "";
	
	public static void main(String[] args) {
		//Create directories for employee files and excel files if
		//not already created. Created in same directory program is run.
	    File currentFolder = new File(System.getProperty("user.dir"));
        File employeeFolder = new File(currentFolder, "EmployeeFiles");
        if (!employeeFolder.exists()) {
        	System.out.println("Creating Employee Directory");
        	employeeFolder.mkdir();
        	System.out.println("Directory created successfully in: " + employeeFolder.getAbsolutePath());
        }
        //Create excel directory
        File excelFolder = new File(currentFolder, "Excel");
        if (!excelFolder.exists()) {
        	System.out.println("Creating Excel Directory");
        	excelFolder.mkdir();
        	System.out.println("Directory created successfully in: " + excelFolder.getAbsolutePath());
        }
        empFilePath = employeeFolder.getAbsolutePath();
        excelFilePath = excelFolder.getAbsolutePath();
		
        
        //attempt to deserialize employee files TODO
        
        
        
		//Test cases, load employee files (15 employees + 2 Leads)
		Employee a = new Employee("Jeremy", "P");
		Employee b = new Employee("Jamie", "Ale");
		Employee c = new Employee("Jeslee", "Cac");
		Employee d = new Employee("Ryan", "Cry");
		Employee e = new Employee("Sara", "Yn");
		Employee f = new Employee("Estevan", "Nee");
		Employee g = new Employee("Ruth", "Ela");
		Employee h = new Employee("Arvydas", "Sts");
		
		Employee i = new Employee("Jocelyne", "Bon");
		Employee j = new Employee("Jose", "Quz");
		Employee k = new Employee("Matt", "Zk");
		Employee l = new Employee("Julia", "Dle");
		Employee m = new Employee("Aidas", "Ss");
		Employee n = new Employee("Erika", "Bt");
		Employee o = new Employee("Oscar", "Ma");
		Employee r = new Employee("Tim", "Smith");
		
		//leads
		Employee p = new Employee("Alenefer", "Js");
		Employee q = new Employee("Alejandro", "Her");
		
		//assign TEST shift start times
		String SIX = "6:00pm";
		String SEVEN = "7:00pm";
		a.setStartTime(SEVEN);
		b.setStartTime(SEVEN);
		c.setStartTime(SIX);
		d.setStartTime(SEVEN);
		e.setStartTime(SIX);
		f.setStartTime(SEVEN);
		g.setStartTime(SEVEN);
		h.setStartTime(SIX);
		
		i.setStartTime(SIX);
		j.setStartTime(SEVEN);
		k.setStartTime(SIX);
		l.setStartTime(SIX);
		m.setStartTime(SEVEN);
		n.setStartTime(SEVEN);
		o.setStartTime(SEVEN);
		
		r.setStartTime(SIX);
		
		//Instantiate sections 
		//West side
		Section one = new Section("1");
		Section two = new Section("2");
		Section twoThree = new Section("2.3");
		Section three = new Section("3");
		Section nine = new Section("9");
		Section nineHighLimit = new Section("9.HL");
		Section highLimit1 = new Section("HL");
		Section highLimit2 = new Section("HL");
		
		//East side
		Section four = new Section("4");
		Section five = new Section("5");
		Section fiveSix = new Section("5.6");
		Section six = new Section("6");
		Section seven = new Section("7");
		Section eight = new Section("8");
		Section floatEast = new Section("EAST");
		
		//Float
		Section floatSection = new Section("FLOAT");
		
		//Add sections to arraylist
		ArrayList<Section> forFifteenEmployees = new ArrayList<Section>();
		forFifteenEmployees.add(one);
		forFifteenEmployees.add(two);
		forFifteenEmployees.add(twoThree);
		forFifteenEmployees.add(three);
		forFifteenEmployees.add(nine);
		forFifteenEmployees.add(nineHighLimit);
		forFifteenEmployees.add(highLimit1);
		forFifteenEmployees.add(highLimit2);
		
		forFifteenEmployees.add(four);
		forFifteenEmployees.add(five);
		forFifteenEmployees.add(fiveSix);
		forFifteenEmployees.add(six);
		forFifteenEmployees.add(seven);
		forFifteenEmployees.add(eight);
		forFifteenEmployees.add(floatEast);
		
		forFifteenEmployees.add(floatSection);
		
		
		//Create floor plan
		FloorPlan swing1 = new FloorPlan("swing1", 
				forFifteenEmployees.size(), forFifteenEmployees);
		
		//add floor plan to hashtable
		FloorPlan.addFloorPlan(swing1);
		
		//TODO
		//test toString for hashtable
		System.out.println("=============CURRENT FLOOR PLAN=====================\n");
		FloorPlan.displayFloorPlans();
		
		
		//relationship test, k has relationship with o and vice versa
		k.setRelationship(o);
			
		//Group into "team"
		ArrayList<Employee> team = new ArrayList<Employee>();
		team.add(a); team.add(b); team.add(c); team.add(d); team.add(e); team.add(f);
		team.add(g); team.add(h); team.add(i); team.add(j); team.add(k); team.add(l); 
		team.add(m); team.add(n); team.add(o); team.add(r);
		
		//ASSIGN SECTIONS TEST
		swing1.scheduleSections(team);
		swing1.staggerStartTimes(team);
		swing1.balanceSections(team);
		swing1.scheduleBreaks(team);
		
		//test toString and section assigning. 
		for(Employee emp : team) {
			System.out.println(emp.toString());
			System.out.println("\n");
			
			emp.saveFile(empFilePath);
			
		}
 	}
}

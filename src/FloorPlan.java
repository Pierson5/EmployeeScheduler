import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;

public class FloorPlan {

	//store floorPlans here
	private static Hashtable<String, FloorPlan> floorPlans = 
			new Hashtable<String, FloorPlan>();

	private int numOfEmployees;
	private String name;
	private ArrayList<Section> sections;
	private HashMap<String, Integer> eastMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> westMap = new HashMap<String, Integer>();

	public FloorPlan(String name, int numOfEmployees, ArrayList<Section> sections) {
		this.setName(name);
		this.setNumOfEmployees(numOfEmployees);
		this.setSections(sections);
		//setEastAndWest();
	}

	public int getNumOfEmployees() {
		return numOfEmployees;
	}

	public void setNumOfEmployees(int numOfEmployees) {
		this.numOfEmployees = numOfEmployees;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Section> getSections() {
		return sections;
	}

	public void setSections(ArrayList<Section> sections2) {
		sections = sections2;
	}

	//TODO return array of sections as a string [1, 2, 3, etc...]
	public String toString() {
		return "";
	}

	public static boolean addFloorPlan(FloorPlan newPlan) {
		//check if entry is already in hashtable
		if(floorPlans.containsKey(newPlan.name)){
			System.out.println("An entry by the name " + newPlan.name + " already exists");
			return false;
		}
		else {
			floorPlans.put(newPlan.name, newPlan);
			return true;
		}
	}


	//goes through each floorPlan and calls my toString method to display
	public static void displayFloorPlans() {
		System.out.println(floorPlans.toString());
	}


	// 1. Check relationship status, if both employees are working, assign to separate: East/West Side
	// 2. Check start time, sort employees into two groups, 6pm / 7pm. (early/late)
	// 3. Alternate between 6pm and 7pm, sending to east or west side
	// 4. Check rotational values for the employees, lowest value (index) for that section gets assigned
	public void assignSections(ArrayList<Employee> team) {

		String shiftTimeKey = "";

		//Shuffle employees, to select random employee
		//get shift start time, check hashmap, schedule to side with lower # of employees with same shift time
		//iterate through employee rotation times and schedule to highest priority on that side
		//if float, no need to check times, assign highest priority employee (or stop at top priority)
		//Employee with correct start time found, compare both employee to determine who has highest rotation priority
		//End of iteration, assign employee with highest priority to that section, update hashmap counter

		Collections.shuffle(team);

		scheduleRelationships(team);

		//assign stand alone section/s (FLOAT), if not already assigned in relationship method
		for(Section section : sections) {
			if(section.getName().equals("FLOAT")) {
				System.out.println("ASSIGNING FLOAT SECTION");
				if(!section.isAssigned()) {
					assignTopPriority(team, section);
				}
			}
		}


		//assign sections to rest of team on East and West sides of casino
		for(Employee empToAssign : team) {

			//check if employee is already assigned a section
			if(!empToAssign.isAssignedSection()) {
				shiftTimeKey = empToAssign.getStartTime();
				//check east and west map to determine which side of casino to assign employee
				//check if key is in hashmap, if not, initialize value to 0
				eastMap.putIfAbsent(shiftTimeKey, 0);
				westMap.putIfAbsent(shiftTimeKey, 0);

				
				
				//Schedule East Side
				if(eastMap.get(shiftTimeKey) < westMap.get(shiftTimeKey)) {
					System.out.println("Scheduling East side, < West");

					for(Section section : sections) {
						Boolean isAssigned = false;
						
						//Check if Section is on the East side
						if(section.isEast() && !section.isAssigned()) {

							System.out.println("Scheduling " + section.getName());

							//compare section to employees in team with the same start time as above. 
							//if there are no other employees with the same start time, schedule highest priority
							for(Employee empToCompare : team) {
								//employees must have same start time to balance east and west side
								if(empToCompare.getStartTime().equals(shiftTimeKey) && 
								  !empToCompare.isAssignedSection() && 
								  !empToCompare.equals(empToAssign)) {
									Employee employee;
									//get employee with highest priority and assign that employee the section
									employee = empWithHighestPriority(empToAssign, empToCompare, section);

									System.out.println("Comparing1: " + empToAssign.getFirstName() + " with: " + empToCompare.getFirstName());
									System.out.println("Winner1: " + employee.getFirstName() + " is scheduled section: " + section.getName());
									
									employee.setSection(section);
									updateFloorPlanTime(employee);
									isAssigned = true;
									break;
								}
							}
							if(isAssigned == false) { //employee start time is unique, assign to available section
								empToAssign.setSection(section);
								updateFloorPlanTime(empToAssign);
							}
							break;
						}
					}
				}
				
				
				//Schedule West Side
				else if(eastMap.get(shiftTimeKey) > westMap.get(shiftTimeKey)) {
					System.out.println("Scheduling West side, < East");

					for(Section section : sections) {
						Boolean isAssigned = false;
						
						
						//Check if Section is on the East side
						if(!section.isEast() && !section.isAssigned()) {

							System.out.println("Scheduling " + section.getName());

							//compare section to employees in team with the same start time as above. 
							//if there are no other employees with the same start time, schedule highest priority
							for(Employee empToCompare : team) {
								//employees must have same start time to balance east and west side
								if(empToCompare.getStartTime().equals(shiftTimeKey) && 
								  !empToCompare.isAssignedSection() && 
								  !empToCompare.equals(empToAssign)) {
									Employee employee;
									//get employee with highest priority and assign that employee the section
									employee = empWithHighestPriority(empToAssign, empToCompare, section);
									System.out.println("Comparing2: " + empToAssign.getFirstName() + " with: " + empToCompare.getFirstName());
									System.out.println("Winner2: " + employee.getFirstName() + " is scheduled section: " + section.getName());
									
									employee.setSection(section);
									updateFloorPlanTime(employee);
									isAssigned = true;
									break;
								}
							}
							if(isAssigned == false) { //employee start time is unique, assign to available section
								empToAssign.setSection(section);
								updateFloorPlanTime(empToAssign);
							}
							break;
						}
					}
				}
				
				
				//both sides have equal number(or zero) employees with that shift time
				//assign highest priority from available sections
				else { 
					System.out.println("Both sides equal");
					for(Section section : sections) {
						if(!section.isAssigned()) {
							System.out.println("Assigning SECTION " + section.getName());
							empToAssign.setSection(section);
							updateFloorPlanTime(empToAssign);
							break;
						}
					}
				}
			}
		}
	}

	//Returns the employee with the highest priority of a given section
	//if equal, returns emp1
	//if sections don't match with rotation values, returns null
	private Employee empWithHighestPriority(Employee emp1, Employee emp2, Section section) {
		Section[] rotationValues = emp1.getRotationValues();
		Section[] rotationValues2 = emp2.getRotationValues();
	
		for(int i = 0; i < rotationValues.length; i++) {
			if(section.getName().contains(rotationValues[i].getName())) {
				return emp1;
			}
			else if(section.getName().contains(rotationValues2[i].getName())) {
				return emp2;
			}
		}
		System.out.println("SECTION NOT FOUND IN empWithHighestPriority method in FloorPlan Class***************");
		return null;
	}

	private void scheduleRelationships(ArrayList<Employee> team) {
		String alreadyScheduled = "";
		//check if employee is in a relationship, if so, assign relationed employees to opposite ends of casino
		for(Employee employee : team) {

			System.out.println("Checking employee: " + employee.getFullName());
			System.out.println("Employee hasRelationship? :  " + employee.hasRelationship());

			if(employee.hasRelationship()) {
				System.out.println(employee.getFullName() + "has relationship, assigining section:::::::::::::::");
				//This will skip the relationed employee further down the ArrayList once already scheduled
				if(employee.getFullName().equals(alreadyScheduled)) {
					System.out.println(employee.getFullName() + " is already scheduled, skipping iteration");
					continue;
				}
				//otherwise schedule the employee who has the relationship with this employee 
				else {
					scheduleOppositeSides(employee, employee.getRelationshipEmployee());
					alreadyScheduled = employee.getRelationshipEmployee().getFullName();
				}
			}
		}
	}


	//Takes 2 employees and schedules them to opposite sides of the casino
	//compares priorities of both employees, highest combination of east/west side
	//and schedules accordingly
	private void scheduleOppositeSides(Employee employee, Employee relationshipEmployee) {
		//Compare priorities, if same side, iterate down priority list of both until opposite side is found. Schedule employee 
		//to that side, other employee gets top priority of other side
		//update hashmap/floor with start times count and increment 
		int iterations1 = 0;
		int iterations2 = 0;

		Section[] employeeRotation1 = employee.getRotationValues();
		Section[] employeeRotation2 = relationshipEmployee.getRotationValues();

		Section empOneTopPriority = employeeRotation1[0];
		Section empTwoTopPriority = employeeRotation2[0];
		Section empOneSecondPriority = null;
		Section empTwoSecondPriority = null;

		//if top priorities are on different sides, schedule those sections
		if(empOneTopPriority.isEast() && !empTwoTopPriority.isEast()) {
			//match top priorities with available section
			for(Section section : sections) {
				if(section.getName().contains(empOneTopPriority.getName())) {
					employee.setSection(section);
				}
				if(section.getName().contains(empTwoTopPriority.getName())) {
					relationshipEmployee.setSection(section);
				}
			}
		}

		//if both top priorities are on the same casino side, iterate through rotations values
		//until opposite sides are reached, record # of iterations taken to reach opposite sides
		else {
			for(int i = 0; i < employeeRotation2.length; i++) {
				//if they are still on the east side, check the next one
				if(empOneTopPriority.isEast() == employeeRotation2[i].isEast()) {
					iterations1++;
					continue;
				}
				//sections are different, keep track of position so highest priorities of both
				//employees are assigned
				else {
					empTwoSecondPriority = employeeRotation2[i];
					break;
				}
			}
			//reverse order, check employee 2 against employee 1 rotation values
			for(int i = 0; i < employeeRotation1.length; i++) {
				//if they are still on the east side, check the next one
				if(empTwoTopPriority.isEast() == employeeRotation1[i].isEast()) {
					iterations2++;
					continue;
				}
				//sections are different, keep track of position so highest priorities of both
				//employees are assigned
				else {
					empOneSecondPriority = employeeRotation1[i];
					break;
				}
			}

			//compare how many iterations were used to find sections on different sides
			//The lowest iterations = highest priority of both employees, assign sections
			if(iterations1 <= iterations2) {
				//iterations1 is top priority, assign sections
				for(Section section : sections) {
					if(section.getName().contains(empOneTopPriority.getName())) {
						employee.setSection(section);
					}
					if(section.getName().contains(empTwoSecondPriority.getName())) {
						relationshipEmployee.setSection(section);
					}
				}
			}
			else {
				//iterations2 is top priority
				for(Section section : sections) {
					if(section.getName().contains(empOneSecondPriority.getName())) {
						employee.setSection(section);
					}
					if(section.getName().contains(empTwoSecondPriority.getName())) {
						relationshipEmployee.setSection(section);
					}
				}
			}
		}


		//display results of method TESTING
		System.out.println("EMP 1 rotation vals:   " + employee.rotationValuesToString());
		System.out.println("EMP 2 rotation vals:   " + relationshipEmployee.rotationValuesToString());

		System.out.println(employee.getFullName() + " (1) assigned to section " + employee.getSection().toString());
		System.out.println(relationshipEmployee.getFullName() + " (2) assigned to section " + relationshipEmployee.getSection().toString());

		//update start time counter for assigned sections
		updateFloorPlanTime(employee);
		updateFloorPlanTime(relationshipEmployee);
	}



	//Updates # of employees scheduled to east or west side
	//based on shift start times
	private void updateFloorPlanTime(Employee employee) {
		System.out.println("/n CURRENT MAPS");
		System.out.println("\n\nEAST\n\n" + eastMap.toString());
		System.out.println("WEST\n\n" + westMap.toString());
		Section sectionToUpdate = employee.getSection();
		String sectionKey = employee.getStartTime();

		System.out.println(employee.getFirstName() + " is adding " + employee.getStartTime() + " to section: " + employee.getSection());

		if(sectionToUpdate.isEast()) {
			System.out.println("Incrementing east");
			//if map contain key (start time), increment counter
			if(eastMap.containsKey(sectionKey)) {
				eastMap.put(sectionKey, eastMap.get(sectionKey) + 1);
			}
			//else, map codes not contain value, add new entry with count of 1
			else {
				eastMap.put(sectionKey, 1);
			}
		}

		//else, its on west side (or float)
		else {
			if(sectionToUpdate.getName().equals("FLOAT")) {
				System.out.println("Float, no incrementation");
				//do nothing, do not add to map
			}
			else { //else, its on west side, update westMap
				//if map contain key (start time), increment counter
				if(westMap.containsKey(sectionKey)) {
					System.out.println("Incrementing west side");
					westMap.put(sectionKey, westMap.get(sectionKey) + 1);
				}
				//else, map codes not contain value, add new entry with count of 1
				else {
					westMap.put(sectionKey, 1);
				}
			}
		}

		//display updated hashmaps
		System.out.println("\nUPDATED MAPS");
		System.out.println("\nEAST\n" + eastMap.toString());
		System.out.println("\nWEST\n" + westMap.toString());

	}


	//assigns section to employee with highest priority out of given team of employees
	//TODO assumes team is not empty
	private void assignTopPriority(ArrayList<Employee> team, Section toAssign) {
		int priorityIndex = 50;
		int topPriorityIndex = 50;
		Employee currentTopPriority = null;
		Section[] rotationVals;
		for(Employee employee : team) {
			//skip employees already assigned sections
			if(!employee.isAssignedSection()) {
				rotationVals = employee.getRotationValues();
				//iterate through rotation values
				for(int i = 0; i < rotationVals.length; i++) {
					if(rotationVals[i].getName().equals(toAssign.getName())) {
						priorityIndex = i;
						if(priorityIndex < topPriorityIndex) {
							topPriorityIndex = priorityIndex;
							currentTopPriority = employee;
						}
					}
				}
			}
		}
		System.out.println("assignTopPriority::: " + currentTopPriority.getFirstName() + " Section: " + toAssign.getName());
		currentTopPriority.setSection(toAssign);
		toAssign.isAssigned = true;
	}
	

	//go through assigned sections and compare priority values, swap if both
	//employees have higher priorities than the other's section
	public void balanceSection(ArrayList<Employee> team) {
		
	}
}

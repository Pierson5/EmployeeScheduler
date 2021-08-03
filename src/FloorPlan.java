import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Queue;

import org.apache.poi.util.SystemOutLogger;

public class FloorPlan {
	private int numOfEmployees;
	private String name;
	private ArrayList<Section> sections;
	private HashMap<String, Integer> eastMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> westMap = new HashMap<String, Integer>();

	public FloorPlan(String name, int numOfEmployees, ArrayList<Section> sections) {
		this.setName(name);
		this.setNumOfEmployees(numOfEmployees);
		this.setSections(sections);
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

	// TODO
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String floorPlanTitle = "\nSECTIONS\n";
		sb.append(floorPlanTitle);
		for (Section section : sections) {
			sb.append("\n" + section.getName() + ",  isAssigned: " + section.isAssigned());
			if (section.isAssigned()) {
				sb.append(" with: " + section.getAssignedEmployee().getFirstName());
			}
		}
		return sb.toString();
	}

	// Shuffle employees, to select random employee
	// get employee shift start time, check hashmap, schedule to appropriate side to
	// keep both sides balanced
	// iterate through employee rotation times and schedule employee with highest
	// priority on that side
	// if section is float, no need to check times, assign highest priority employee
	// (or stop at top priority)
	// Employee with correct start time found, compare both employee to determine
	// who has highest rotation priority
	// End of iteration, assign employee with highest priority to that section,
	// update hashmap counter
	public void scheduleSections(ArrayList<Employee> team) {
		westMap.clear();
		eastMap.clear();
		String shiftTimeKey = "";

		Collections.shuffle(team);

		scheduleRelationships(team);

		// assign stand alone section/s (FLOAT), if not already assigned in relationship
		// method
		for (Section section : sections) {
			if (section.getName().equals("FLOAT")) {
				System.out.println("ASSIGNING FLOAT SECTION");
				if (!section.isAssigned()) {
					assignTopPriority(team, section);
				}
			}
		}

		// assign sections to rest of team on East and West sides of casino
		for (Employee empToAssign : team) {

			// check if employee is already assigned a section
			if (!empToAssign.isAssignedSection()) {
				shiftTimeKey = empToAssign.getStartTime();
				// check east and west map to determine which side of casino to assign employee
				// check if key is in hashmap, if not, initialize value to 0
				eastMap.putIfAbsent(shiftTimeKey, 0);
				westMap.putIfAbsent(shiftTimeKey, 0);

				// Schedule East Side
				if (eastMap.get(shiftTimeKey) < westMap.get(shiftTimeKey)) {
					System.out.println("Scheduling East side, < West");

					for (Section section : sections) {
						Boolean hasUniqueStartTime = false;

						// Check if Section is on the East side
						if (section.isEast() && !section.isAssigned()) {

							System.out.println("Scheduling " + section.getName());

							// compare section to employees in team with the same start time as above.
							// if there are no other employees with the same start time, schedule highest
							// priority
							for (Employee empToCompare : team) {
								// employees must have same start time to balance east and west side
								if (empToCompare.getStartTime().equals(shiftTimeKey)
										&& !empToCompare.isAssignedSection() && !empToCompare.equals(empToAssign)) {
									Employee employee;
									// get employee with highest priority and assign that employee the section
									employee = empWithHighestPriority(empToAssign, empToCompare, section);

									System.out.println("Comparing1: " + empToAssign.getFirstName() + " with: "
											+ empToCompare.getFirstName());
									System.out.println("Winner1: " + employee.getFirstName() + " is scheduled section: "
											+ section.getName());

									employee.setSection(section);
									updateFloorPlanTime(employee);
									hasUniqueStartTime = true;
									break;
								}
							}
							if (hasUniqueStartTime == false) { // employee start time is unique, assign to available
																// section
								empToAssign.setSection(section);
								updateFloorPlanTime(empToAssign);
							}
							break;
						}
					}
				}

				// Schedule West Side
				else if (eastMap.get(shiftTimeKey) > westMap.get(shiftTimeKey)) {
					System.out.println("Scheduling West side, < East");

					for (Section section : sections) {
						Boolean isAssigned = false;

						// Check if Section is on the East side
						if (!section.isEast() && !section.isAssigned()) {

							System.out.println("Scheduling " + section.getName());

							// compare section to employees in team with the same start time as above.
							// if there are no other employees with the same start time, schedule highest
							// priority
							for (Employee empToCompare : team) {
								// employees must have same start time to balance east and west side
								if (empToCompare.getStartTime().equals(shiftTimeKey)
										&& !empToCompare.isAssignedSection() && !empToCompare.equals(empToAssign)) {
									Employee employee;
									// get employee with highest priority and assign that employee the section
									employee = empWithHighestPriority(empToAssign, empToCompare, section);
									System.out.println("Comparing2: " + empToAssign.getFirstName() + " with: "
											+ empToCompare.getFirstName());
									System.out.println("Winner2: " + employee.getFirstName() + " is scheduled section: "
											+ section.getName());

									employee.setSection(section);
									updateFloorPlanTime(employee);
									isAssigned = true;
									break;
								}
							}
							if (isAssigned == false) { // employee start time is unique, assign to available section
								empToAssign.setSection(section);
								updateFloorPlanTime(empToAssign);
							}
							break;
						}
					}
				}

				// both sides have equal number(or zero) employees with that shift time
				// assign highest priority from available sections
				else {
					System.out.println("Both sides equal, assigning employee: " + empToAssign.getFirstName());
					for (Section section : sections) {
						if (!section.isAssigned()) {
							System.out.println("Assigning SECTION " + section.getName());
							empToAssign.setSection(section);
							updateFloorPlanTime(empToAssign);
							break;
						}
					}
				}
			}
		}
		// assign any sections/employees that did not get assigned in main algorith,
		// due to uneven number of east and west side sections
		checkForUnassignedSections(team);
	}

	// simulated annealing variation to find best/good enough schedule
	// assigns temp/scores to priority values, higher value = smaller temp/score
	// reject schedules with a "high temp/score", recreating schedule and optimizing
	// until optimal schedule (most employees with highest priorities scheduled) is
	// found
	public void createOptimalSchedule(ArrayList<Employee> team) {
		double acceptableScore = 3.0;
		double currentScore = 0.0;
		double topScore = 10;
		int time = 0;
		ArrayList<Employee> currentOptimal = team;
		boolean noSolutionFound = true;

		while (noSolutionFound) {
			// clear previously scheduled sections
			for (Employee emp : team) {
				emp.clearAssignedSection();
			}

			System.out.println("\n\n\nScheduling Team");
			scheduleSections(team);
			staggerStartTimes(team);
			balanceSections(team);

			currentScore = calculateAverageScore(team);

			// save schedule with lowest score
			if (currentScore < topScore) {
				currentOptimal = team;
				topScore = currentScore;
			}
			System.out.println("Current score = " + currentScore);

			if (currentScore < acceptableScore) {
				noSolutionFound = false;
				team = currentOptimal;
			}

			time++;
			if (time % 50 == 0) {
				System.out.println("50 attempts without solution, increasing temp");
				acceptableScore++;
			}
		}
		System.out.println("OUT OF WHILE LOOP, current = " + currentScore + "  acceptable: " + acceptableScore);
	}

	// get
	public double calculateAverageScore(ArrayList<Employee> team) {
		double totalScore = 0.0;

		for (Employee emp : team) {
			if (emp.priorityFromAssignedSection() < 3) {
				totalScore += 1;
			} else if (emp.priorityFromAssignedSection() > 3 && emp.priorityFromAssignedSection() < 5) {
				totalScore += 10;
			} else if (emp.priorityFromAssignedSection() > 5 && emp.priorityFromAssignedSection() < 8) {
				totalScore += 20;
			} else {
				totalScore += 40;
			}
		}
		System.out.println("Calculating score, Total: " + totalScore + ", teamSize: " + team.size());
		return totalScore / team.size();
	}

	public void checkForUnassignedSections(ArrayList<Employee> team) {
		for (Employee emp : team) {
			if (!emp.isAssignedSection()) {
				for (Section section : sections) {
					if (!section.isAssigned) {
						emp.setSection(section);
					}
				}
			}
		}
	}

	// Iterates through each section, if 3 sections in a row
	// contain employees with the same start time (meaning they are scheduled
	// next to each other) this will interview with break scheduling. Method
	// swaps 3rd employee with same start time with employee containing a different
	// start time.
	public void staggerStartTimes(ArrayList<Employee> team) {
		int counter = 0;
		String previousEmployeeStartTime = "";
		String startTime = "";
		final int MAX_REPEAT_START_TIMES = 3;
		for (int i = 0; i < sections.size(); i++) {
			startTime = sections.get(i).getAssignedEmployee().getStartTime();
			if (startTime.equals(previousEmployeeStartTime)) {
				counter++;
			}
			// continue iteration from where sections loop left off, looking
			// ahead for employee with a different start time to swap with.
			if (counter == MAX_REPEAT_START_TIMES) {
				for (int j = i + 1; j < sections.size(); j++) {
					// start times differ and are on the same side of casino. (i.e. do not swap HL
					// and 4 [different sides, each side already balanced as much as possible])

					System.out.println("CHECKING SECTION " + sections.get(j).getName());
					System.out.println("\nASSIGNED TO: " + sections.get(j).getAssignedEmployee().getFullName());

					if (!sections.get(j).getAssignedEmployee().getStartTime().equals(startTime)
							&& sections.get(j).isEast() == sections.get(i).isEast()
							&& !sections.get(j).getAssignedEmployee().hasRelationship()) {
						swapSections(sections.get(i).getAssignedEmployee(), sections.get(j).getAssignedEmployee());
						counter = 0; // reset counter
					} else
						continue;
				}
			} else
				previousEmployeeStartTime = startTime;
		}
	}

	// Schedules break times based on employee start time.
	// Employees with the same start time are scheduled breaks
	// in cyclical fashion. I.E. if available times are [10pm, 10:30pm]
	// emp1 = 10pm, emp2 = 1030pm, emp3 = back to 10pm
	// Times are stored in groups of 3, 1st break, 2nd break, 3rd break.
	public void scheduleBreaks(ArrayList<Employee> team) {

		HashMap<String, Queue<LocalTime>> availableBreakTimes = new HashMap<String, Queue<LocalTime>>();

		for (Section section : sections) {
			Employee employee = section.getAssignedEmployee();
			String employeeStartTime = employee.getStartTime();
			LocalTime startTime = stringToLocalTime(employeeStartTime);
			Queue<LocalTime> availableTimes = new ArrayDeque<LocalTime>();

			// attempt to pull break times from map, if not there, create new list
			// for that start time.
			if (!availableBreakTimes.containsKey(employeeStartTime)) {
				for (int i = 0; i < team.size() / 3; i++) {
					// 2h, 4, 6h
					availableTimes.add(startTime.plusMinutes((long) 120));
					availableTimes.add(startTime.plusMinutes((long) 240));
					availableTimes.add(startTime.plusMinutes((long) 360));

					// 2h 30m, 4h 30m, 6h 30m
					availableTimes.add(startTime.plusMinutes((long) 150));
					availableTimes.add(startTime.plusMinutes((long) 270));
					availableTimes.add(startTime.plusMinutes((long) 390));

					// 2h 15m, 4h, 6h 15m
					availableTimes.add(startTime.plusMinutes((long) 135));
					availableTimes.add(startTime.plusMinutes((long) 240));
					availableTimes.add(startTime.plusMinutes((long) 375));

					// 2h 45m, 5h, 6h 45m
					availableTimes.add(startTime.plusMinutes((long) 165));
					availableTimes.add(startTime.plusMinutes((long) 300));
					availableTimes.add(startTime.plusMinutes((long) 405));
				}
				availableBreakTimes.put(employeeStartTime, availableTimes);
			}
			employee.setFirstBreak(availableBreakTimes.get(employeeStartTime).poll().toString());
			employee.setSecondBreak(availableBreakTimes.get(employeeStartTime).poll().toString());
			employee.setThirdBreak(availableBreakTimes.get(employeeStartTime).poll().toString());
		}
	}

	public LocalTime stringToLocalTime(String time) {
		DateTimeFormatter fmt = new DateTimeFormatterBuilder().parseCaseInsensitive() // ignore lowercase am/pm
				.appendPattern("h:mma") // time format
				.toFormatter(Locale.ENGLISH); // set locale
		return LocalTime.parse(time, fmt);
	}

	// Returns the employee with the highest priority of a given section
	// if equal, returns emp1
	// if sections don't match with rotation values, returns null
	private Employee empWithHighestPriority(Employee emp1, Employee emp2, Section section) {
		Section[] rotationValues = emp1.getRotationValues();
		Section[] rotationValues2 = emp2.getRotationValues();

		for (int i = 0; i < rotationValues.length; i++) {
			if (section.getName().contains(rotationValues[i].getName())) {
				return emp1;
			} else if (section.getName().contains(rotationValues2[i].getName())) {
				return emp2;
			}
		}
		System.out.println("SECTION NOT FOUND IN empWithHighestPriority method in FloorPlan Class***************");
		return null;
	}

	private void scheduleRelationships(ArrayList<Employee> team) {
		String alreadyScheduled = "";
		// check if employee is in a relationship, if so, assign relationed employees to
		// opposite ends of casino
		for (Employee employee : team) {

			System.out.println("Checking employee: " + employee.getFullName());
			System.out.println("Employee hasRelationship? :  " + employee.hasRelationship());

			if (employee.hasRelationship()) {
				System.out.println(employee.getFullName() + "has relationship, assigining section:::::::::::::::");
				// This will skip the relationed employee further down the ArrayList once
				// already scheduled
				if (employee.getFullName().equals(alreadyScheduled)) {
					System.out.println(employee.getFullName() + " is already scheduled, skipping iteration");
					continue;
				}
				// otherwise schedule the employee who has the relationship with this employee
				else {
					scheduleOppositeSides(employee, employee.getRelationshipEmployee());
					alreadyScheduled = employee.getRelationshipEmployee().getFullName();
				}
			}
		}
	}

	// Takes 2 employees and schedules them to opposite sides of the casino
	private void scheduleOppositeSides(Employee employee, Employee relationshipEmployee) {
		Section[] employeeRotation1 = employee.getRotationValues();
		Section[] employeeRotation2 = relationshipEmployee.getRotationValues();
		Section empOneTopPriority = employeeRotation1[0];
		Section empTwoTopPriority = employeeRotation2[0];

		// Employee's top priorities on different sides of the casino
		if ((empOneTopPriority.isEast() && !empTwoTopPriority.isEast())
				|| (empTwoTopPriority.isEast() && !empOneTopPriority.isEast())
						&& !empOneTopPriority.getName().equals(empTwoTopPriority.getName())) {

			System.out.println("IN FIRST IF");
			// match top priorities with available section
			for (Section section : sections) {
				if (section.getName().contains(empOneTopPriority.getName())) {
					employee.setSection(section);
					break;
				}
			}

			for (Section section : sections) {
				if (section.getName().contains(empTwoTopPriority.getName())) {
					relationshipEmployee.setSection(section);
					break;
				}
			}
		}

		// both employees top priorities on the same side, schedule top priority of one
		// and assign top most priority of other on the opposite side
		else {
			System.out.println("IN SECOND IF");

			// schedule empOne's top priority
			for (Section section : sections) {
				if (section.getName().contains(empOneTopPriority.getName())) {
					employee.setSection(section);
					System.out.println(employee.getFirstName() + " scheduled to " + section.getName());
				}
			}

			// emp is scheduled east, find top priority west for emp2
			// else, emp1 is scheduled west, find top priority east for emp2
			if (employee.getSection().isEast()) {
				System.out.println("SCHEDULING " + relationshipEmployee.getFirstName() + " To WEST");
				// schedule empTwo top west side priority
				for (Section rotationSection : employeeRotation2) {
					if (!rotationSection.isEast() || rotationSection.getName().contains("FLOAT")) {
						System.out.println("In west side IF statement checking " + rotationSection.getName());
						// highest priority west side found, assign to emp2
						for (Section section : sections) {
							if (section.isAssigned()) {
								System.out.println(
										"SECTION ALREADY ASSIGNED TO " + section.getAssignedEmployee().getFirstName());
								break;
							} else if (section.getName().contains(rotationSection.getName())) {
								System.out.println("Scheduling " + relationshipEmployee.getFirstName() + " to "
										+ section.getName());
								relationshipEmployee.setSection(section);
							}
						}
					}
				}
			} else {
				// emp1 scheduled west, schedule emp2 to the east
				for (Section rotationSection : employeeRotation2) {
					System.out.println("Scheduling east side:  " + rotationSection.isEast());
					if (rotationSection.isEast() || rotationSection.getName().contains("FLOAT")) {
						// highest priority east side found, assign to emp2
						for (Section section : sections) {
							System.out.println(
									"Checking section: " + section.getName() + " is assigned? " + section.isAssigned());
							if (section.getName().contains(rotationSection.getName()) && !section.isAssigned()) {
								relationshipEmployee.setSection(section);
							}
						}
						break;
					}
				}
			}
		}
		// display results of method TESTING
		System.out.println(employee.getFirstName() + " rotation vals:   " + employee.rotationValuesToString());
		System.out.println(relationshipEmployee.getFirstName() + " rotation vals:   "
				+ relationshipEmployee.rotationValuesToString());

		System.out.println(employee.getFullName() + " (1) assigned to section " + employee.getSection().toString());
		System.out.println(relationshipEmployee.getFullName() + " (2) assigned to section "
				+ relationshipEmployee.getSection().toString());

		// update start time counter for assigned sections
		updateFloorPlanTime(employee);
		updateFloorPlanTime(relationshipEmployee);
	}

	// Updates # of employees scheduled to east or west side
	// based on shift start times
	private void updateFloorPlanTime(Employee employee) {
		System.out.println("/n CURRENT MAPS");
		System.out.println("\n\nEAST\n\n" + eastMap.toString());
		System.out.println("WEST\n\n" + westMap.toString());
		Section sectionToUpdate = employee.getSection();
		String sectionKey = employee.getStartTime();

		System.out.println(employee.getFirstName() + " is adding " + employee.getStartTime() + " to section: "
				+ employee.getSection());

		if (sectionToUpdate.isEast()) {
			System.out.println("Incrementing east");
			// if map contain key (start time), increment counter
			if (eastMap.containsKey(sectionKey)) {
				eastMap.put(sectionKey, eastMap.get(sectionKey) + 1);
			}
			// else, map codes not contain value, add new entry with count of 1
			else {
				eastMap.put(sectionKey, 1);
			}
		}

		// else, its on west side (or float)
		else {
			if (sectionToUpdate.getName().equals("FLOAT")) {
				System.out.println("Float, no incrementation");
				// do nothing, do not add to map
			} else { // else, its on west side, update westMap
						// if map contain key (start time), increment counter
				if (westMap.containsKey(sectionKey)) {
					System.out.println("Incrementing west side");
					westMap.put(sectionKey, westMap.get(sectionKey) + 1);
				}
				// else, map codes not contain value, add new entry with count of 1
				else {
					westMap.put(sectionKey, 1);
				}
			}
		}

		// display updated hashmaps
		System.out.println("\nUPDATED MAPS");
		System.out.println("\nEAST\n" + eastMap.toString());
		System.out.println("\nWEST\n" + westMap.toString());
		System.out.println(this.toString());

	}

	// assigns section to employee with highest priority out of given team of
	// employees
	// TODO error checking: assumes team is not empty
	private void assignTopPriority(ArrayList<Employee> team, Section toAssign) {
		int priorityIndex = 50;
		int topPriorityIndex = 50;
		Employee currentTopPriority = null;
		Section[] rotationVals;
		for (Employee employee : team) {
			// skip employees already assigned sections
			if (!employee.isAssignedSection()) {
				rotationVals = employee.getRotationValues();
				// iterate through rotation values
				for (int i = 0; i < rotationVals.length; i++) {
					if (rotationVals[i].getName().equals(toAssign.getName())) {
						priorityIndex = i;
						if (priorityIndex < topPriorityIndex) {
							topPriorityIndex = priorityIndex;
							currentTopPriority = employee;
						}
					}
				}
			}
		}
		System.out.println(
				"assignTopPriority::: " + currentTopPriority.getFirstName() + " Section: " + toAssign.getName());
		currentTopPriority.setSection(toAssign);
		toAssign.isAssigned = true;
	}

	// go through assigned sections and compare priority values, swap if both
	// employees have higher priorities than the other's section
	public void balanceSections(ArrayList<Employee> team) {
		String startTime1 = "";
		String startTime2 = "";
		Section emp1Section;
		Section emp2Section;

		for (Employee employee1 : team) {
			for (Employee employee2 : team) {
				startTime1 = employee1.getStartTime();
				startTime2 = employee2.getStartTime();
				if (startTime1 == startTime2) {
					emp1Section = employee1.getSection();
					emp2Section = employee2.getSection();

					System.out.println("Comparing: " + employee1.getFirstName());
					System.out.println("with: " + employee2.getFirstName());

					// do not move employees with relationships
					// Employee1 section priority for Employee2's section is higher (or equal to)
					// than Employee2 and higher than current priority.
					// Employee2 section priority for Employee1's section is higher (or equal to)
					// than Employee1 and higher than current priority.
					if (!(employee1.hasRelationship() || employee2.hasRelationship())
							&& (employee1.sectionPriority(emp2Section) <= employee2.sectionPriority(emp2Section))
							&& employee2.sectionPriority(emp1Section) <= employee1.sectionPriority(emp1Section)) {
						System.out.println(
								"\n\nSwapping***** " + employee1.getFullName() + " and " + employee2.getFullName());
						System.out.println(employee1.getFirstName() + " in section " + employee1.getSection().getName()
								+ " has a priority of " + employee1.sectionPriority(emp1Section));
						System.out.println(employee2.getFirstName() + " in section " + employee2.getSection().getName()
								+ " has a priority of " + employee2.sectionPriority(emp2Section));
						System.out.println(employee1.getFirstName() + " for section " + employee2.getSection().getName()
								+ " has a priority of " + employee1.sectionPriority(emp2Section));
						System.out.println(employee2.getFirstName() + " for section " + employee1.getSection().getName()
								+ " has a priority of " + employee2.sectionPriority(emp1Section));

						swapSections(employee1, employee2);
					}
				}
			}
		}
	}

	private void swapSections(Employee emp1, Employee emp2) {
		Section temp = emp1.getSection();
		emp1.setSection(emp2.getSection());
		System.out.println(emp1.getFirstName() + " is now section " + emp2.getSection().getName());
		emp2.setSection(temp);
		System.out.println(emp2.getFirstName() + " is now section " + temp.getName());

	}
}

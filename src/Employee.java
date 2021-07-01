import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

public class Employee implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int badgeNum;
	private String firstName;
	private String lastName;
	private Section section;
	private String startTime;
	private Employee relationship;
	private boolean hasRelationship;
	private boolean isAssignedSection;
	private String firstBreak;
	private String secondBreak;
	private String thirdBreak;

	// Array Locations
	// 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12
	// 1, 2, 3, 4, 5, 6, 7, 8, 9, HL, EAST, WEST, FLOAT
	int NUM_OF_SECTIONS = 13;

	// employee rotation values
	Section[] rotationValues = new Section[NUM_OF_SECTIONS];

	// default employee constructor
	public Employee() {
		this.setFirstName("FirstName");
		this.setLastName("LastName");
		this.instantiateRotationPriority();
		this.hasRelationship = false;
		this.isAssignedSection = false;
	}

	/*
	 * Construct employee object with name
	 */
	public Employee(String firstName, String lastName) {
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.instantiateRotationPriority();
		this.hasRelationship = false;
		this.isAssignedSection = false;
	}

	/*
	 * Construct employee object with name and relationship status
	 */
	public Employee(String firstName, String lastName, Employee relationshipWithThisEmployee) {
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.instantiateRotationPriority();
		this.hasRelationship = false;
		this.setRelationship(relationshipWithThisEmployee);
	}
	
	/*
	 * Copy constructor
	 */
	public Employee(Employee emp) {
		this.setFirstName(emp.getFirstName());
		this.setLastName(emp.getLastName());
		this.instantiateRotationPriority();
		this.hasRelationship = false;
		this.isAssignedSection = false;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
		this.isAssignedSection = true;
		section.isAssigned = true;
		section.setAssignedEmployee(this);
	}

	public boolean isAssignedSection() {
		return isAssignedSection;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public int getBadgeNum() {
		return badgeNum;
	}

	public void setBadgeNum(int badgeNum) {
		this.badgeNum = badgeNum;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}

	public boolean hasRelationship() {
		return hasRelationship;
	}

	public Employee getRelationshipEmployee() {
		return relationship;
	}

	public void setRelationship(Employee employee) {
		this.relationship = employee;
		employee.relationship = this;
		this.hasRelationship = true;
		employee.hasRelationship = true;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public Section[] getRotationValues() {
		return rotationValues;
	}

	public void setRotationValues(Section[] rotationPriority) {
		this.rotationValues = rotationPriority;
	}

	public String getFirstBreak() {
		return firstBreak;
	}

	public void setFirstBreak(String firstBreak) {
		this.firstBreak = firstBreak;
	}

	public String getSecondBreak() {
		return secondBreak;
	}

	public void setSecondBreak(String secondBreak) {
		this.secondBreak = secondBreak;
	}

	public String getThirdBreak() {
		return thirdBreak;
	}

	public void setThirdBreak(String thirdBreak) {
		this.thirdBreak = thirdBreak;
	}
	
	//Resets assigned section and breaktimes
	//used for re-serializing employee object
	//so old data is not used in current scheduling
	public void resetEmployeeValues() {
		this.isAssignedSection = false;
	}

	public String rotationValuesToString() {
		String result = "\n";

		for (Section sect : rotationValues) {
			result += sect.getName() + ": " + sect.getRotationValue() + "\n";
		}

		return result;
	}

	public String toString() {
		return "\nName: " + this.getFullName() + "\nAssigned Section = " + this.section.getName() + 
				"\nStart Time: " + this.getStartTime() +
				"\nBreaks: " + this.getFirstBreak() + "  "
				+ this.getSecondBreak() + "  " + this.getThirdBreak() +
				"\nRotation Values = " + Arrays.toString(this.getRotationValues());
				

	}

	// returns employee rotation index of parameter "section"
	// Lower value = higher priority, with 0 being highest priority
	// returns -1 if not found
	public int sectionPriority(Section section) {
		String sectionName = section.getName();
		int sectionPriority = -1;

		for (int i = 0; i < rotationValues.length; i++) {
			if (sectionName.contains(rotationValues[i].getName())) {
				sectionPriority = i;
			}
		}
		return sectionPriority;
	}

	// updates employee rotation value after being assigned section/s
	public void updateRotation() {
		String sectionName = this.section.getName();
		int sectionIndex = 0;
		double sectionValue = 0.0;

		// if working multiple sections, divide rotation value by number of
		// sections worked.
		// i.e. if working 9.HL, and rotation value is +1, rotation value will be 0.5
		// for 9 and HL
		if (sectionName.length() > 1 && !sectionName.equals("EAST") && !sectionName.equals("WEST")
				&& !sectionName.equals("FLOAT")) {

			// split string up at period delimiter
			String[] sections = sectionName.split("\\.");

			// iterate through tokens, calculate new priority value, add to old priority
			// value
			for (String i : sections) {
				sectionIndex = Section.nameToRotationIndex(i);
				sectionValue = 1.0 / sections.length;

				System.out.println(this.getFirstName() + " Adding " + sectionValue + " to section " + i);

				this.rotationValues[sectionIndex].addToRotationValue(sectionValue);
			}
		}

		// section length string = 1, only one section to update, no calculations needed
		else {
			sectionIndex = Section.nameToRotationIndex(sectionName);

			System.out.println(this.getFirstName() + " Adding " + 1.0 + " to section " + sectionName);

			this.rotationValues[sectionIndex].addToRotationValue(1.0);
		}
	}

	// Sorts an employee's rotation values in descending order,
	// used to assign sections based on highest values of that employee
	public void sortRotation() {

		// anonymous class with Lambda expression
		Comparator<Section> descRotationValue = (o1, o2) -> {
			// ternary operator
			return o1.getRotationValue() > o2.getRotationValue() ? 1 : -1;
		};
		// Arrays.sort produces ascending order, reverse the comparator to
		// produce descending order
		Arrays.sort(rotationValues, descRotationValue.reversed());
	}

	private void instantiateRotationPriority() {
		rotationValues[0] = new Section("1");
		rotationValues[1] = new Section("2");
		rotationValues[2] = new Section("3");
		rotationValues[3] = new Section("4");
		rotationValues[4] = new Section("5");
		rotationValues[5] = new Section("6");
		rotationValues[6] = new Section("7");
		rotationValues[7] = new Section("8");
		rotationValues[8] = new Section("9");
		rotationValues[9] = new Section("HL");
		rotationValues[10] = new Section("EAST");
		rotationValues[11] = new Section("WEST");
		rotationValues[12] = new Section("FLOAT");
	}

	////////////////////////////////////////////////////////////////
	// saveFile //
	// Serializes employee file/rotation values //
	////////////////////////////////////////////////////////////////
	public void saveFile(String filePath) {
		String employeeName = this.getFullName();
		String fileName = employeeName + ".ser";
		File outputFile = new File(filePath, fileName);
		try {
			ObjectOutputStream fileOut = new ObjectOutputStream(new FileOutputStream(outputFile));
			fileOut.writeObject(this);
			fileOut.close();
			System.out.println(this.getFullName() + "'s file has been saved.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	////////////////////////////////////////////////////////////////
	// loadFile //
	// De-serializes employee file/rotation values //
	////////////////////////////////////////////////////////////////
	public static Employee loadFile(String filePath, String fullName) {
		Employee emp = null;
		String filename = fullName + ".ser";
		filePath = filePath + File.separator + filename;
		
		try
        {   
            // Reading employee object from file
            FileInputStream file = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(file);
              
            // deserialization of object
            emp = (Employee)in.readObject();
              
            //reset old data
            
            emp.resetEmployeeValues();
            
            in.close();
            file.close();
              
            System.out.println("Object has been deserialized ");
        }
          
        catch(IOException e)
        {
        	e.printStackTrace();
            System.out.println("IOException is caught during deserialization");
        }
          
        catch(ClassNotFoundException e)
        {
            System.out.println("ClassNotFoundException is caught during deserialization");
        }
		return emp;
	}
}

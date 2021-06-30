import java.io.Serializable;

public class Section implements Serializable{
	private static final long serialVersionUID = 1L;
	
	String name;
	double rotationValue;
	boolean isAssigned;
	Employee assignedToSection;
	
	
	//default
	public Section() {
		this.name = "Section";
		this.rotationValue = 0.0;
		this.isAssigned = false;
	}
	
	//constructor
	public Section(String name) {
		setName(name);
		this.rotationValue = 0.0;
		this.isAssigned = false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getRotationValue() {
		return rotationValue;
	}

	public void setRotationValue(double priority) {
		this.rotationValue = priority;
	}
	
	public void setAssignedEmployee(Employee employee) {
		assignedToSection = employee;
	}
	
	public Employee getAssignedEmployee() {
		return assignedToSection;
	}
	
	public boolean isAssigned() {
		return isAssigned;
	}

	public void setAssigned(boolean isAssigned) {
		this.isAssigned = isAssigned;
	}
	
	public void addToRotationValue(double value) {
		this.rotationValue += value;
	}
	
	public String toString() {
		return "\nSection = " + name + "\nRotationValue = " + rotationValue + "\nisAssigned = " + isAssigned;
	}
	
	//Returns true if section is on the East side of the casino
	//East side is sections 4, 5, 6, 7, 8
	public Boolean isEast() {
		if(this.getName().contains("4") || 
		   this.getName().contains("5")	||
		   this.getName().contains("6") ||
		   this.getName().contains("7") ||
		   this.getName().contains("8") ||
		   this.getName().contains("EAST")){
			return true;
		}
		else
			return false;
	}
	
	
	// Array Locations
	//Index 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,    10,  11,   12
	//Name  1, 2, 3, 4, 5, 6, 7, 8, 9, HL, EAST, WEST, FLOAT
	public static int nameToRotationIndex(String name) {
		if (name == "HL") 
			return 9;
		else if (name == "EAST") 
			return 10;
		else if (name == "WEST")
			return 11;
		else if (name == "FLOAT")
			return 12;
		else
			return Integer.parseInt(name) - 1; 
	}

	
}

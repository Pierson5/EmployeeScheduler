import java.util.ArrayList;

public class main {

	public static void main(String[] args) {
		
		//Test cases, load employee files (15 employees + 2 Leads)
		Employee a = new Employee("Jeremy", "Pierson");
		Employee b = new Employee("Jamie", "Almajose");
		Employee c = new Employee("Jeslee", "Cachola");
		Employee d = new Employee("Ryan", "Crawley");
		Employee e = new Employee("Sara", "Yoon");
		Employee f = new Employee("Estevan", "Negrete");
		Employee g = new Employee("Ruth", "Eloriaga");
		Employee h = new Employee("Arvydas", "Stropus");
		
		Employee i = new Employee("Jocelyne", "Bryson");
		Employee j = new Employee("Jose", "Quinonez");
		Employee k = new Employee("Matt", "Zulik");
		Employee l = new Employee("Julia", "Depasquale");
		Employee m = new Employee("Aidas", "Stropus");
		Employee n = new Employee("Erika", "Billot");
		Employee o = new Employee("Oscar", "Mojica");
		Employee r = new Employee("Tim", "Smith");
		
		//leads
		Employee p = new Employee("Alenefer", "Jaimes");
		Employee q = new Employee("Alejandro", "Hernandez");
		
		
		String test1 = "5";
		String test2 = "5.6";
		
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
		
		
		//relationship test, only have to assign one relationship status
		//will assign the other relationship status automatically
		k.setRelationship(o);
			
		//Group into "team"
		//TODO might be more efficient to use hashtable here, not arrayList
		//especially when iterating through to set breaktimes, might have
		//to revisit the same employee multiple times. 
		ArrayList<Employee> team = new ArrayList<Employee>();
		team.add(a); team.add(b); team.add(c); team.add(d); team.add(e); team.add(f);
		team.add(g); team.add(h); team.add(i); team.add(j); team.add(k); team.add(l); 
		team.add(m); team.add(n); team.add(o); team.add(r);
		
		//ASSIGN SECTIONS TEST
		swing1.assignSections(team);
		
		//test toString and section assigning. 
		for(int z = 0; z < team.size(); z++) {
			System.out.println(team.get(z).toString());
			System.out.println("\n");
		}
	}
}

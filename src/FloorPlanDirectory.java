import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;

public class FloorPlanDirectory implements Serializable {
	private static final long serialVersionUID = 6041908346969426129L;
	private static Hashtable<String, FloorPlan> floorPlans;

	public FloorPlanDirectory() {
		setFloorPlans(new Hashtable<String, FloorPlan>());
	}

	public Hashtable<String, FloorPlan> getFloorPlans() {
		return floorPlans;
	}

	public void setFloorPlans(Hashtable<String, FloorPlan> floorPlans) {
		FloorPlanDirectory.floorPlans = floorPlans;
	}

	// Add floor plan to
	public boolean addFloorPlan(FloorPlan newPlan) {
		// check if entry is already in hashtable
		if (floorPlans.containsKey(newPlan.getName())) {
			System.out.println("An entry by the name " + newPlan.getName() + " already exists");
			return false;
		} else {
			floorPlans.put(newPlan.getName(), newPlan);
			return true;
		}
	}

	public boolean removeFloorPlan(FloorPlan toRemove) {
		String key = toRemove.getName();
		// check if entry is already in hashtable
		if (floorPlans.containsKey(key)) {
			floorPlans.remove(key);
			return true;
		} else {
			System.out.println(key + " not found in FloorPlanDirectory");
			return false;
		}
	}

	public String toString() {
		floorPlans.forEach((k, v) -> System.out.println("Key : " + k + ", Value : " + v));
		return null;
	}

	// Serializes floor plan directory
	public void saveFile(String filePath) {
		String fileName = "FloorPlanDirectory.ser";
		File outputFile = new File(filePath, fileName);
		try {
			ObjectOutputStream fileOut = new ObjectOutputStream(new FileOutputStream(outputFile));
			fileOut.writeObject(this);
			fileOut.close();
			System.out.println("FloorPlanDirectory has been saved.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// De-serializes floor plan directory
	@SuppressWarnings("unchecked")
	public static Hashtable<String, FloorPlan> loadFile(String filePath) {
		Hashtable<String, FloorPlan> floorPlanDirectory = null;
		String filename = "FloorPlanDirectory.ser";
		filePath = filePath + File.separator + filename;

		try {
			FileInputStream file = new FileInputStream(filePath);
			ObjectInputStream in = new ObjectInputStream(file);

			floorPlanDirectory = (Hashtable<String, FloorPlan>) in.readObject();

			in.close();
			file.close();

			System.out.println("Object has been deserialized ");
		}

		catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOException is caught during deserialization");
		}

		catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException is caught during deserialization");
		}
		return floorPlanDirectory;
	}
}

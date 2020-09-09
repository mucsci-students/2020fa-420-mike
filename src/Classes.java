import java.util.ArrayList;

public class Classes {
	static ArrayList<Entity> entities;
	static ArrayList<Relationship> relationships;

	//********************************************************//
	// Constructors                                           //
	//********************************************************//
	public Classes() {
		entities = new ArrayList<Entity>();
		relationships = new ArrayList<Relationship>();
	}
	
	//********************************************************//
	// Class Functions                                        //
	//********************************************************//
	public boolean createClass(String name) {
		for (Entity e : entities) {
			if (e.name.equals(name)) {
				return false;
			}
		}
		return entities.add(new Entity(name));
	}

	public boolean renameClass(String target, String newname) {
		int index = entities.indexOf(new Entity(target));
		if (index >= 0) {
			entities.get(index).name = newname;
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteClass(String target) {
		return entities.remove(new Entity(target));
	}

	//********************************************************//
	// Attribute Functions                                    //
	//********************************************************//
	public boolean createAttribute(String targetclass, String attribute) {
		Entity e;
		for (int index = 0; index < entities.size(); ++index) {
			e = entities.get(index);
			// If target found.
			if (e.name.equals(targetclass)) {
				// Create the attribute.
				return e.createAttribute(attribute);
			}
		}
		// Target not found.
		return false;
	}

	public boolean renameAttribute(String targetclass, String targetattribute, String newattribute)
	{
		Entity e;
		for (int index = 0; index < entities.size(); ++index) {
			e = entities.get(index);
			// If target found.
			if (e.name.equals(targetclass)) {
				// Try to rename the target attribute.
				return e.renameAttribute(targetattribute, newattribute);
			}
		}
		// Target not found.
		return false;
	}
	
	public boolean deleteAttribute(String targetclass, String targetattribute)
	{
		Entity e;
		for (int index = 0; index < entities.size(); ++index) {
			e = entities.get(index);
			// If target found.
			if (e.name.equals(targetclass)) {
				// Try to rename the target attribute.
				return e.deleteAttribute(targetattribute);
			}
		}
		// Target not found.
		return false;
	}
}

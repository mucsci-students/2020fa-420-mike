import java.util.ArrayList;

public class Classes {
	static ArrayList<Entity> entities;
	static ArrayList<Relationship> relationships;

	// ********************************************************//
	// Constructors //
	// ********************************************************//
	
	public Classes() {
		entities = new ArrayList<Entity>();
		relationships = new ArrayList<Relationship>();
	}

	// ********************************************************//
	// Class Functions //
	// ********************************************************//
	
	public boolean createClass(String name) {
		Entity e = new Entity(name);
		if (entities.contains(e)) {
			return false;
		}
		return entities.add(e);
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
		ArrayList<Relationship> temp = new ArrayList<Relationship>();
		int index = entities.indexOf(new Entity(target));
		
		if (index >= 0) {
			// Removing target
			entities.remove(index);
			
			// Removing all relationships associated with target.
			// Serial removal in the worst-case is O(RN), while serial adding is O(N-R)
			for (Relationship r : relationships) {
				if (!r.class1.equals(target) && !r.class2.equals(target)) {
					temp.add(r);
				}
			}
			relationships = temp;
			return true;
		}
		// Target not found.
		return false;
	}

	// ********************************************************//
	// Attribute Functions //
	// ********************************************************//
	
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

	public boolean renameAttribute(String targetclass, String targetattribute, String newattribute) {
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

	public boolean deleteAttribute(String targetclass, String targetattribute) {
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

	// ********************************************************//
	// Relationship Functions //
	// ********************************************************//

	public boolean createRelationship(String name, String class1, String class2) {
		Relationship r = new Relationship(name, class1, class2);
		if (relationships.contains(r)) {
			return false;
		}
		return relationships.add(r);
	}

	public boolean deleteRelationship(String name, String class1, String class2) {
		Relationship r = new Relationship(name, class1, class2);
		return relationships.remove(r);
	}

	// ********************************************************//
	// Member Functions //
	// ********************************************************//

	public void clear() {
		entities.clear();
		relationships.clear();
	}
}

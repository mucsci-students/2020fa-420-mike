import java.util.Objects;
import java.util.ArrayList;

public class Entity {
	String name;
	ArrayList<String> attributes;

	public Entity() {
		attributes = new ArrayList<String>();
	}

	public Entity(String newname) {
		name = newname;
		attributes = new ArrayList<String>();
	}
	
	public boolean equals(Object obj) {
		// Identity check
		if (this == obj) {
			return true;
		}
		// Null check
		if (obj == null) {
			return false;
		}
		// Type check
		if (getClass() != obj.getClass()) {
			return false;
		}
		// Equality check
		Entity other = (Entity) obj;
		return Objects.equals(this.name, other.name);
	}

	//********************************************************//
	// Attribute Functions                                    //
	//********************************************************//
	
	
	public boolean createAttribute(String attribute) {
		return attributes.add(attribute);
	}	

	public boolean renameAttribute(String target, String newattribute) {
		int index = attributes.indexOf(target);
		if (index >= 0) {
			attributes.set(index, newattribute);
			return true;
		}
		return false;
	}

	public boolean deleteAttribute(String target) {
		return attributes.remove(target);
	}
}

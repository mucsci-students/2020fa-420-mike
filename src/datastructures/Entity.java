
package datastructures;

import java.util.Objects;
import java.util.ArrayList;

public class Entity {
	private String name;
	private ArrayList<String> attributes;

	//*********************************************************//
	// Constructor //
	//*********************************************************//
	
	public Entity(String newname) {
		name = newname;
		attributes = new ArrayList<String>();
	}
	
	//*********************************************************//
	// Accessors & Mutator //
	//*********************************************************//
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String newname)
	{
		name = newname;
	}
	
	public ArrayList<String> getAttributes()
	{
		return attributes;
	}
	
	//*********************************************************//
	// Equals Function //
	//*********************************************************//
	
	public boolean equals(Object obj) {
		// Instance check
		if (obj instanceof Entity)
		{
			// Equality check
			Entity other = (Entity) obj;
			return Objects.equals(this.name, other.name);
		}
		return false;
	}

	//*********************************************************//
	// Attribute Functions //
	//*********************************************************//
	
	public boolean createAttribute(String attribute) {
		if (attributes.contains(attribute))
		{
			return false;
		}
		return attributes.add(attribute);
	}	

	public boolean renameAttribute(String target, String newattribute) {
        if (attributes.contains(newattribute))
        {
            return false;
        }
		
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

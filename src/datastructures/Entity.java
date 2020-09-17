package datastructures;

import java.util.Objects;
import java.util.ArrayList;

public class Entity {
	private String name;
	private ArrayList<String> fields;
	private ArrayList<String> methods;

	//*********************************************************//
	// Constructor //
	//*********************************************************//
	
	public Entity(String newname) {
		name = newname;
		fields = new ArrayList<String>();
		methods = new ArrayList<String>();
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
	
	public ArrayList<String> getFields()
	{
		return fields;
	}
	
	public ArrayList<String> getMethods()
	{
		return methods;
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
	// Field Functions //
	//*********************************************************//
	
	public boolean createField(String field) {
		if (fields.contains(field))
		{
			return false;
		}
		return fields.add(field);
	}
	
	public boolean renameField(String target, String newfield) {
        if (fields.contains(newfield))
        {
            return false;
        }
		
		int index = fields.indexOf(target);
		if (index >= 0) {
			fields.set(index, newfield);
			return true;
		}
		return false;
	}
	
	public boolean deleteField(String target) {
		return fields.remove(target);
	}
	
	//*********************************************************//
	// Field Functions //
	//*********************************************************//
	
	public boolean createMethod(String method) {
		if (methods.contains(method))
		{
			return false;
		}
		return methods.add(method);
	}	

	public boolean renameMethod(String target, String newmethod) {
        if (methods.contains(newmethod))
        {
            return false;
        }
		
		int index = methods.indexOf(target);
		if (index >= 0) {
			methods.set(index, newmethod);
			return true;
		}
		return false;
	}
	
	public boolean deleteMethod(String target) {
		return methods.remove(target);
	}
}

package mike.datastructures;

import java.util.Objects;
import java.util.ArrayList;

public class Entity {
	private String name;
	private ArrayList<Field> fields;
	private ArrayList<Method> methods;
	private int xLocation;
	private int yLocation;

	//*********************************************************//
	// Constructor //
	//*********************************************************//
	
	public Entity(String newname) {
		name = newname;
		fields = new ArrayList<Field>();
		methods = new ArrayList<Method>();
		xLocation = 0;
		yLocation = 0;
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
	
	public ArrayList<Field> getFields()
	{
		return fields;
	}
	
	public ArrayList<Method> getMethods()
	{
		return methods;
	}
	
	public int getXLocation()
	{
		return xLocation;
	}
	
	public void setXLocation(int newX)
	{
		xLocation = newX;
	}
	
	public int getYLocation()
	{
		return yLocation;
	}
	
	public void setYLocation(int newY)
	{
		yLocation = newY;
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
			//check names
			return Objects.equals(this.name, other.name);
		}
		return false;
	}

	//*********************************************************//
	// Field Functions //
	//*********************************************************//
	
	public boolean createField(String name, String type) {
		if(containsField(name)){
			return false; //already contains field
		}
		return fields.add(new Field(name, type));
	}
	
	public boolean renameField(String target, String newfield) {
		if(containsField(newfield)){
			//new field already exists
			return false;
		}

		for (Field f : fields) {
			// If target found.
			if (f.getName().equals(target)) {
				//rename the target field.
				f.setName(newfield);
				return true;
			}
		}
		// Target not found.
		return false;
	}
	
	public boolean deleteField(String target) {
		return fields.remove(new Field(target, "int"));
	}
	
	//*********************************************************//
	// Method Functions //
	//*********************************************************//
	
	public boolean createMethod(String method, String type) {
		if(containsMethod(method)){
			return false; //already contains method
		}
		return methods.add(new Method(method, type));
	}	

	public boolean renameMethod(String target, String newmethod) {
		if(containsMethod(newmethod)){
			//new method already exists
			return false;
		}

		for (Method m : methods) {
			// If target found.
			if (m.getName().equals(target)) {
				//rename method.
				m.setName(newmethod);
				return true;
			}
		}
		// Target not found.
		return false;
	}
	
	public boolean deleteMethod(String target) {
		return methods.remove(new Method(target, "int"));
	}

	//*********************************************************//
	// Parameter Functions //
	//*********************************************************//

	public boolean createParameter(String method, String name, String type){
		for (Method m : methods) {
			// If target found.
			if (m.getName().equals(method)) {
				// Try to create the parameter.
				return m.createParameter(name, type);
			}
		}
		// Target not found.
		return false;
	}

	public boolean renameParameter(String method, String name, String newname){
		for (Method m : methods) {
			// If target found.
			if (m.getName().equals(method)) {
				//try to rename parameter.
				return m.renameParameter(name, newname);
			}
		}
		// Target not found.
		return false;
	}

	public boolean deleteParameter(String method, String name){
		for (Method m : methods) {
			// If target found.
			if (m.getName().equals(method)) {
				//try to delete parameter.
				return m.deleteParameter(name);
			}
		}
		// Target not found.
		return false;
	}

	/* helper/member functions */

	/* search if a field already exists */
	public boolean containsField(String name){
		for(Field f : fields){
			if(f.getName().equals(name)){
				return true; //found field
			}
		}
		return false;
	}

	/* copy a field and its properties */
	public Field copyField(String name){
		for(Field f : fields){
			if(f.getName().equals(name)){
				return f;
			}
		}
		return null;
	}

	/* search if a method already exists */
	public boolean containsMethod(String name){
		for(Method m : methods){
			if(m.getName().equals(name)){
				return true; //found method
			}
		}
		return false;
	}

	/* copy a method and its properties */
	public Method copyMethod(String name){
		for(Method m : methods){
			if(m.getName().equals(name)){
				return m;
			}
		}
		return null;
	}

}


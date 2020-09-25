package mike.datastructures;

import java.util.Objects;
import java.util.ArrayList;

public class Entity {
	private String name;
	private ArrayList<Field> fields;
	private ArrayList<Method> methods;

	//*********************************************************//
	// Constructor //
	//*********************************************************//
	
	public Entity(String newname) {
		name = newname;
		fields = new ArrayList<Field>();
		methods = new ArrayList<Method>();
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
		if(searchField(name)){
			return false; //already contains field
		}
		return fields.add(new Field(name, type));
	}
	
	public boolean renameField(String target, String newfield) {
		if(searchField(newfield)){
			//new field already exists
			return false;
		}
		Field f;
		for (int index = 0; index < fields.size(); ++index) {
			f = fields.get(index);
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
		return fields.remove(copyField(target));
	}
	
	//*********************************************************//
	// Method Functions //
	//*********************************************************//
	
	public boolean createMethod(String method, String type) {
		if(searchMethod(method)){
			return false; //already contains method
		}
		return methods.add(new Method(method, type));
	}	

	public boolean renameMethod(String target, String newmethod) {
		if(searchMethod(newmethod)){
			//new method already exists
			return false;
		}
		Method m;
		for (int index = 0; index < methods.size(); ++index) {
			m = methods.get(index);
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
		return methods.remove(copyMethod(target));
	}

	//*********************************************************//
	// Parameter Functions //
	//*********************************************************//

	public boolean createParameter(String method, String name, String type){
		Method m;
		for (int index = 0; index < methods.size(); ++index) {
			m = methods.get(index);
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
		Method m;
		for (int index = 0; index < methods.size(); ++index) {
			m = methods.get(index);
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
		Method m;
		for (int index = 0; index < methods.size(); ++index) {
			m = methods.get(index);
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
	public boolean searchField(String name){
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
	public boolean searchMethod(String name){
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


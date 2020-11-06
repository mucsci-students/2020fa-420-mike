package mike.datastructures;

import java.util.ArrayList;
import mike.datastructures.Relationship.Type;

public class Model {
    private static ArrayList<Entity> entities;
    private static ArrayList<Relationship> relationships;
    
    // *********************************************************//
    // Constructor //
    // *********************************************************//

    public Model() {
        entities = new ArrayList<Entity>();
        relationships = new ArrayList<Relationship>();
    }

    // *********************************************************//
    // Accessors //
    // *********************************************************//

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public ArrayList<Relationship> getRelationships() {
        return relationships;
    }

    // *********************************************************//
    // Class Functions //
    // *********************************************************//

    public boolean createClass(String name) {
        if(name == null){
            return false;
        }
        Entity e = new Entity(name);
        if (entities.contains(e)) {
            // Duplicate found.
            return false;
        }
        return entities.add(e);
    }

    public boolean renameClass(String target, String newname) {
        if(containsEntity(newname)) {
        	return false; // Already contains desired name
        }

        int index = entities.indexOf(new Entity(target));

        if (index < 0) {
            return false; // Target not found.
        }

        // Changing entity name.
        entities.get(index).setName(newname);

        // Changing entity's name in relationships.
        for (Relationship r : relationships) {
            if (r.getFirstClass().equals(target)) {
                r.setFirstClass(newname);
            }
            if (r.getSecondClass().equals(target)) {
                r.setSecondClass(newname);
            }
        }
        return true;
    }

    public boolean deleteClass(String target) {
        int index = entities.indexOf(new Entity(target));

        if (index < 0) {
            // Target not found.
            return false;
        }

        // Removing target
        entities.remove(index);

        ArrayList<Relationship> temp = new ArrayList<Relationship>();

        // Removing all relationships associated with target.
        // Serial removal in the worst-case is O(RN), while serial adding is O(N-R)
        for (Relationship r : relationships) {
            if (!r.getFirstClass().equals(target) && !r.getSecondClass().equals(target)) {
                temp.add(r);
            }
        }
        relationships = temp;
        return true;
    }

    // *********************************************************//
    // Field Functions //
    // *********************************************************//

    public boolean createField(String targetclass, String field, String type, String visType) {
        for (Entity e : entities) {
            // If target found.
            if (e.getName().equals(targetclass)) {
                // Create the attribute.
                return e.createField(field, type, visType);
            }
        }
        // Target not found.
        return false;
    }

    public boolean renameField(String targetclass, String targetfield, String newfield) {
        for (Entity e : entities) {
            // If target found.
            if (e.getName().equals(targetclass)) {
                // Try to rename the target attribute.
                return e.renameField(targetfield, newfield);
            }
        }
        // Target not found.
        return false;
    }
    
    public boolean changeFieldVis(String targetclass, String targetfield, String newVis) {
	for (Entity e: entities) {
	    // If target found.
	    if (e.getName().equals(targetclass)) {
		// Try to change the visibility.
		return e.changeFieldVis(targetfield, newVis);
	    }
	}
	// Target not found
	return false;
    }

    public boolean deleteField(String targetclass, String targetfield) {
        for (Entity e : entities) {
            // If target found.
            if (e.getName().equals(targetclass)) {
                // Try to rename the target attribute.
                return e.deleteField(targetfield);
            }
        }
        // Target not found.
        return false;
    }

    // *********************************************************//
    // Method Functions //
    // *********************************************************//

    public boolean createMethod(String targetclass, String method, String type, String visType) {
        for (Entity e : entities) {
            // If target found.
            if (e.getName().equals(targetclass)) {
                // Create the attribute.
                return e.createMethod(method, type, visType);
            }
        }
        // Target not found.
        return false;
    }

    public boolean renameMethod(String targetclass, String targetmethod, String newmethod) {
        for (Entity e : entities) {
            // If target found.
            if (e.getName().equals(targetclass)) {
                // Try to rename the target attribute.
                return e.renameMethod(targetmethod, newmethod);
            }
        }
        // Target not found.
        return false;
    }
    
    public boolean changeMethodVis(String targetclass, String targetmethod, String newVis) {
	for (Entity e: entities) {
	    // If target found.
	    if (e.getName().equals(targetclass)) {
		// Try to change the visibility.
		return e.changeMethodVis(targetmethod, newVis);
	    }
	}
	// Target not found
	return false;
    }

    public boolean deleteMethod(String targetclass, String targetmethod) {
        for (Entity e : entities) {
            // If target found.
            if (e.getName().equals(targetclass)) {
                // Try to rename the target attribute.
                return e.deleteMethod(targetmethod);
            }
        }
        // Target not found.
        return false;
    }

    // *********************************************************//
    // Relationship Functions //
    // *********************************************************//

    public boolean createRelationship(Type name, String class1, String class2) {
        if(name == null){
            return false;
        }

        if(!containsEntity(class1) || !containsEntity(class2)) {
        	//class1 or class2 does not exist
        	return false;
        }
    	
    	for (Relationship r : relationships) {
            if (r.getFirstClass().equals(class1) && r.getSecondClass().equals(class2)) {
                // There already exists a relationship between those two classes in this direction.
                return false;
            }
        }
        return relationships.add(new Relationship(name, class1, class2));
    }

    public boolean deleteRelationship(Type name, String class1, String class2) {
        Relationship r = new Relationship(name, class1, class2);
        return relationships.remove(r);
    }

    // *********************************************************//
    // Parameter Functions //
    // *********************************************************//

    public boolean createParameter(String className, String method, String name, String type){
        for (Entity e : entities) {
            // If target found.
            if (e.getName().equals(className)) {
                // Try to rename the target attribute.
                return e.createParameter(method, name, type);
            }
        }
        // Target not found.
        return false;
    }

    public boolean renameParameter(String className, String method, String name, String newname){
        for (Entity e : entities) {
            // If target found.
            if (e.getName().equals(className)) {
                // Try to rename the target attribute.
                return e.renameParameter(method, name, newname);
            }
        }
        // Target not found.
        return false;
    }

    public boolean deleteParameter(String className, String method, String name){
        for (Entity e : entities) {
            // If target found.
            if (e.getName().equals(className)) {
                // Try to rename the target attribute.
                return e.deleteParameter(method, name);
            }
        }
        // Target not found.
        return false;
    }

    // *********************************************************//
    // Member Functions //
    // *********************************************************//

    public void clear() {
        entities.clear();
        relationships.clear();
    }

    public boolean empty(){
        return entities.isEmpty();
    }

    // *********************************************************//
    // Helper Functions (for testing mainly) //
    // *********************************************************//

    /* Entity functions */

    public boolean containsEntity(String name)
    {
        for (Entity e : entities)
        {
            if(e.getName().equals(name))
            {
                return true;
            }
        }
        return false;
    }

    //copies an attribute entirely
    //if not found, returns null
    public Entity copyEntity(String name)
    {
        for (Entity e : entities)
        {
            if(e.getName().equals(name))
            {
                return e;
            }
        }
        return null;
    }

    // *********************************************************//
    // Relationship Helper Functions (for testing mainly) //
    // *********************************************************//

    public boolean containsRelationship(Type name, String class1, String class2)
    {
        for (Relationship r : relationships)
        {
            if(r.getName() == name && r.getFirstClass().equals(class1) && r.getSecondClass().equals(class2))
            {
                return true;
            }
        }
        return false;
    }

    //return relationship or null if not found
    public Relationship getRelationship(Type name, String class1, String class2)
    {
        for (Relationship r : relationships)
        {
            if(r.getName() == name && r.getFirstClass().equals(class1) && r.getSecondClass().equals(class2))
            {
                return r;
            }
        }
        return null;
    }

}


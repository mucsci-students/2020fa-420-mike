package mike.datastructures;

import java.util.ArrayList;

public class Classes {
    private static ArrayList<Entity> entities;
    private static ArrayList<Relationship> relationships;

    // *********************************************************//
    // Constructor //
    // *********************************************************//

    public Classes() {
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
        Entity e = new Entity(name);
        if (entities.contains(e)) {
            // Duplicate found.
            return false;
        }
        return entities.add(e);
    }

    public boolean renameClass(String target, String newname) {
        if(searchEntity(newname)) {
        	return false; // Already contains desired name
        }

        Entity e = copyEntity(target);
        int index = entities.indexOf(e);

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
        Entity e = copyEntity(target);
        int index = entities.indexOf(e);

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

    public boolean createField(String targetclass, String field, String type) {
        Entity e;
        for (int index = 0; index < entities.size(); ++index) {
            e = entities.get(index);
            // If target found.
            if (e.getName().equals(targetclass)) {
                // Create the attribute.
                return e.createField(field, type);
            }
        }
        // Target not found.
        return false;
    }

    public boolean renameField(String targetclass, String targetfield, String newfield) {
        Entity e;
        for (int index = 0; index < entities.size(); ++index) {
            e = entities.get(index);
            // If target found.
            if (e.getName().equals(targetclass)) {
                // Try to rename the target attribute.
                return e.renameField(targetfield, newfield);
            }
        }
        // Target not found.
        return false;
    }

    public boolean deleteField(String targetclass, String targetfield) {
        Entity e;
        for (int index = 0; index < entities.size(); ++index) {
            e = entities.get(index);
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

    public boolean createMethod(String targetclass, String method, String type) {
        Entity e;
        for (int index = 0; index < entities.size(); ++index) {
            e = entities.get(index);
            // If target found.
            if (e.getName().equals(targetclass)) {
                // Create the attribute.
                return e.createMethod(method, type);
            }
        }
        // Target not found.
        return false;
    }

    public boolean renameMethod(String targetclass, String targetmethod, String newmethod) {
        Entity e;
        for (int index = 0; index < entities.size(); ++index) {
            e = entities.get(index);
            // If target found.
            if (e.getName().equals(targetclass)) {
                // Try to rename the target attribute.
                return e.renameMethod(targetmethod, newmethod);
            }
        }
        // Target not found.
        return false;
    }

    public boolean deleteMethod(String targetclass, String targetmethod) {
        Entity e;
        for (int index = 0; index < entities.size(); ++index) {
            e = entities.get(index);
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

    public boolean createRelationship(String name, String class1, String class2) {
        if(!searchEntity(class1) || !searchEntity(class2)) {
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

    public boolean deleteRelationship(String name, String class1, String class2) {
        Relationship r = new Relationship(name, class1, class2);
        return relationships.remove(r);
    }

    // *********************************************************//
    // Parameter Functions //
    // *********************************************************//

    public boolean createParameter(String className, String method, String name, String type){
        Entity e;
        for (int index = 0; index < entities.size(); ++index) {
            e = entities.get(index);
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
        Entity e;
        for (int index = 0; index < entities.size(); ++index) {
            e = entities.get(index);
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
        Entity e;
        for (int index = 0; index < entities.size(); ++index) {
            e = entities.get(index);
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
        return entities.isEmpty() && relationships.isEmpty();
    }

    // *********************************************************//
    // Helper Functions (for testing mainly) //
    // *********************************************************//

    /* Entity functions */

    public boolean searchEntity(String name)
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

    public boolean searchRelationship(String name, String class1, String class2)
    {
        for (Relationship r : relationships)
        {
            if(r.getName().equals(name) && r.getFirstClass().equals(class1) && r.getSecondClass().equals(class2))
            {
                return true;
            }
        }
        return false;
    }

    //return relationship or null if not found
    public Relationship getRelationship(String name, String class1, String class2)
    {
        for (Relationship r : relationships)
        {
            if(r.getName().equals(name) && r.getFirstClass().equals(class1) && r.getSecondClass().equals(class2))
            {
                return r;
            }
        }
        return null;
    }

}


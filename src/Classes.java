package datastructures;

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
        int index = entities.indexOf(new Entity(target));

        if (index < 0) {
            // Target not found.
            return false;
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

        if (index > 0) {
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
    // Attribute Functions //
    // *********************************************************//

    public boolean createAttribute(String targetclass, String attribute) {
        Entity e;
        for (int index = 0; index < entities.size(); ++index) {
            e = entities.get(index);
            // If target found.
            if (e.getName().equals(targetclass)) {
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
            if (e.getName().equals(targetclass)) {
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
            if (e.getName().equals(targetclass)) {
                // Try to rename the target attribute.
                return e.deleteAttribute(targetattribute);
            }
        }
        // Target not found.
        return false;
    }

    // *********************************************************//
    // Relationship Functions //
    // *********************************************************//

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
                Entity ret = new Entity(name);
                //loop through attributes
                for (String s : e.getAttributes())
                {
                    ret.createAttribute(s);
                }
                return ret;
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

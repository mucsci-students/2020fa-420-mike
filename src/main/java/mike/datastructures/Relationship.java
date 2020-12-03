package mike.datastructures;

import java.util.Objects;

public class Relationship {
    private Type name;
    private String class1;
    private String class2;

    public enum Type {
	COMPOSITION, AGGREGATION, REALIZATION, INHERITANCE
    }

    // *********************************************************//
    // Constructors //
    // *********************************************************//

    public Relationship(Type newname, String newclass1, String newclass2) {
	name = newname;
	class1 = newclass1;
	class2 = newclass2;
    }

    // *********************************************************//
    // Accessors & Mutators //
    // *********************************************************//

    public Type getName() {
	return name;
    }

    public String getFirstClass() {
	return class1;
    }

    public String getSecondClass() {
	return class2;
    }

    public void setName(Type newname) {
	name = newname;
    }

    public void setFirstClass(String newclass1) {
	class1 = newclass1;
    }

    public void setSecondClass(String newclass2) {
	class2 = newclass2;
    }

    // *********************************************************//
    // Equals Function //
    // *********************************************************//

    public boolean equals(Object obj) {
	// Instance check
	if (obj instanceof Relationship) {
	    // Equality check
	    Relationship other = (Relationship) obj;
	    return Objects.equals(this.name, other.name) && Objects.equals(this.class1, other.class1)
		    && Objects.equals(this.class2, other.class2);
	}
	return false;
    }

}

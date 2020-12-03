package mike.datastructures;

import java.util.Objects;

/**
 * Serves as a base for fields, methods, and parameters This is because they all
 * contain a name and a type
 *
 * @author Stefan Gligorevic
 */
public class Formal {
    private String name;
    private String type;

    public Formal(String name, String type) {
	this.name = name;
	this.type = type;
    }

    /* Getters and setters */
    public String getName() {
	return name;
    }

    public String getType() {
	return type;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setType(String type) {
	this.type = type;
    }

    /* Equals method */
    public boolean equals(Object obj) {
	// Instance check
	if (obj instanceof Formal) {
	    // Equality check
	    Formal other = (Formal) obj;
	    // only doing by name because we do not allow duplicate names
	    return Objects.equals(this.name, other.name);
	}
	return false;
    }

}

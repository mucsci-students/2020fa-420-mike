import java.util.Objects;

public class Relationship {
	String name;
	String class1;
	String class2;

	public Relationship(String newname, String newclass1, String newclass2)
	{
		name = newname;
		class1 = newclass1;
		class2 = newclass2;
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
		Relationship other = (Relationship) obj;
		return Objects.equals(this.name, other.name)
				&& Objects.equals(this.class1, other.class1)
				&& Objects.equals(this.class2, other.class2);
	}
	
	
}

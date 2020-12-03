package mike.datastructures;

public class Parameter extends Formal {

    public Parameter(String name, String type) {
	super(name, type);
    }

    public Parameter(Parameter copyparam) {
	super(copyparam.getName(), copyparam.getType());
    }

}

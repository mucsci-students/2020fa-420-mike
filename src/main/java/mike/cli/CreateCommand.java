package mike.cli;

import mike.datastructures.Model;
import mike.datastructures.Entity.visibility;
import mike.datastructures.Relationship.Type;

public class CreateCommand extends CommandObj {

    public CreateCommand(Model m, String[] com, boolean p) {
	super(m, com, p);
    }

    public boolean execute() {
	if (commands.length < 3) {
	    System.out.println(errorMessage + commandUsage[2] + commandUsage[3] + commandUsage[4] + commandUsage[5]
		    + commandUsage[6] + "\n");
	    return prompt;
	}
	if (commands[1].equals("class")) {
	    if (commands.length != 3) {
		System.out.println(errorMessage + commandUsage[2] + "\n");
		return prompt;
	    }
	    if (model.createClass(commands[2])) {
		return true;
	    } else {
		System.out.println("\nCreate class failed. Make sure the class name doesn't already exist.\n");
	    }
	} else if (commands[1].equals("field")) {
	    if (commands.length != 6) {
		System.out.println(errorMessage + commandUsage[3] + "\n");
		return prompt;
	    } else if (checkVis(commands[3]) == null) {
		System.out.println("\nInvalid visibility type. Valid types are public, private, or protected.\n");
		return prompt;
	    }
	    if (model.createField(commands[2], commands[5], commands[4], commands[3])) {
		return true;
	    } else {
		System.out.println(
			"\nCreate field failed. Make sure the field doesn't already exist and the class name does exist.\n");
	    }
	} else if (commands[1].equals("method")) {
	    if (commands.length != 6) {
		System.out.println(errorMessage + commandUsage[4] + "\n");
		return prompt;
	    } else if (checkVis(commands[3]) == null) {
		System.out.println("\nInvalid visibility type. Valid types are public, private, or protected.\n");
		return prompt;
	    }
	    if (model.createMethod(commands[2], commands[5], commands[4], commands[3])) {
		return true;
	    } else {
		System.out.println(
			"\nCreate method failed. Make sure the method doesn't already exist and the class name does exist.\n");
	    }
	} else if (commands[1].equals("relationship")) {
	    if (commands.length != 5) {
		System.out.println(errorMessage + commandUsage[5] + "\n");
		return prompt;
	    }
	    if (model.createRelationship(checkEnum(commands[2].toUpperCase()), commands[3], commands[4])) {
		return true;
	    } else {
		System.out.println(
			"\nCreate relationship failed. Make sure the classes exist, the relationship type is valid, and that it is not a duplicate.\n");
	    }
	} else if (commands[1].equals("parameter")) {
	    if (commands.length != 6) {
		System.out.println(errorMessage + commandUsage[6] + "\n");
		return prompt;
	    }
	    if (model.createParameter(commands[2], commands[3], commands[5], commands[4])) {
		return true;
	    } else {
		System.out.println(
			"\nCreate parameter failed. Make sure the class exists, method exists, and the parameter does NOT exist.\n");
	    }
	} else {
	    System.out.println(errorMessage + commandUsage[2] + commandUsage[3] + commandUsage[4] + commandUsage[5]
		    + commandUsage[6] + "\n");
	}
	return prompt;
    }

    // Return enum type that user requested, null if invalid
    private static Type checkEnum(String command) {
	switch (command) {
	case "REALIZATION":
	    return Type.REALIZATION;
	case "AGGREGATION":
	    return Type.AGGREGATION;
	case "COMPOSITION":
	    return Type.COMPOSITION;
	case "INHERITANCE":
	    return Type.INHERITANCE;
	default:
	    return null;
	}
    }

    // Return enum type of visibility user requested, null if invalid
    private visibility checkVis(String visType) {
	visType = visType.toUpperCase();
	switch (visType) {
	case "PUBLIC":
	    return visibility.PUBLIC;
	case "PRIVATE":
	    return visibility.PRIVATE;
	case "PROTECTED":
	    return visibility.PROTECTED;
	default:
	    return null;
	}
    }

}

package mike;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import mike.controller.Controller;
import mike.datastructures.*;
import mike.datastructures.Relationship.Type;
import mike.view.GUIView;
import mike.view.ViewTemplate;

import java.util.ArrayList;

import javax.swing.JLabel;

@SuppressWarnings("unchecked")
public class HelperMethods {

    // Main load function. Calls loadClasses and loadRelationships
    public static void load(Path userPath, Model userClasses, Controller control, ViewTemplate view)
	    throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException {

	userClasses.clear();

	File directory = new File(userPath.toString());

	// Parse the JSON file and get an array of the classes
	Object obj = new JSONParser().parse(new FileReader(directory));

	// Variable initialization. If the types look stupid that's because they are.
	JSONObject javaObj = (JSONObject) obj;
	JSONArray list = (JSONArray) javaObj.get("Classes");
	ArrayList<JSONObject> objList = new ArrayList<JSONObject>();

	loadClasses(userClasses, javaObj, list, objList, control, view);

	// Clear out variables for reuse
	list = (JSONArray) javaObj.get("Relationships");
	objList.clear();

	loadRelationships(userClasses, javaObj, list, objList, view);
    }

    // Takes all of the information from the classes in the JSON file,
    // and inserts them to the currently running program
    private static void loadClasses(Model userClasses, JSONObject javaObj, JSONArray list,
	    ArrayList<JSONObject> objList, Controller control, ViewTemplate view) {
	// Add class names and attributes from JSONArray list to loadFile
	for (int x = 0; x < list.size(); ++x) {
	    // Get class from JSONArray list, add to ArrayList objList (type conversions)
	    Object entity = list.get(x);
	    objList.add((JSONObject) entity);

	    // Extract name of class, add to loadFile
	    String className = (String) objList.get(x).get("className");
	    userClasses.createClass(className);

	    // Extract all fields of associated class, add to loadFile
	    JSONArray classFields = (JSONArray) objList.get(x).get("fields");
	    for (int y = 0; y < classFields.size(); ++y) {
		JSONObject field = (JSONObject) classFields.get(y);
		String fieldName = field.get("fieldName").toString();
		String fieldType = field.get("fieldType").toString();
		String fieldVis = field.get("fieldVis").toString();

		userClasses.createField(className, fieldName, fieldType, fieldVis);
	    }

	    // Extract all methods of associated class, add to loadFile
	    JSONArray classMethods = (JSONArray) objList.get(x).get("methods");
	    for (int y = 0; y < classMethods.size(); ++y) {
		JSONObject method = (JSONObject) classMethods.get(y);
		String methodName = method.get("methodName").toString();
		String methodType = method.get("methodType").toString();
		String methodVis = method.get("methodVis").toString();

		userClasses.createMethod(className, methodName, methodType, methodVis);
		JSONArray methodParam = (JSONArray) method.get("Parameters");
		for (int z = 0; z < methodParam.size(); ++z) {
		    JSONObject param = (JSONObject) methodParam.get(z);
		    String paramName = param.get("paramName").toString();
		    String paramType = param.get("paramType").toString();

		    userClasses.createParameter(className, methodName, paramName, paramType);
		}
	    }
	    // Extract name of class, add to loadFile
	    Entity e = userClasses.getEntities().get(x);
	    Long location = (Long) objList.get(x).get("xPosition");
	    e.setXLocation(Math.toIntExact(location));
	    location = (Long) objList.get(x).get("yPosition");
	    e.setYLocation(Math.toIntExact(location));
	    if (ViewTemplate.isGUI()) {
		((GUIView) view).showClass(e, control);
	    }
	}
	if (ViewTemplate.isGUI()) {
	    for (Entity curEntity : userClasses.getEntities()) {
		JLabel curLabel = ((GUIView) view).getEntityLabels().get(curEntity.getName());
		curLabel.setLocation(curEntity.getXLocation(), curEntity.getYLocation());
	    }
	}

    }

    // Takes all of the information from the relationships in the JSON file,
    // and inserts them into the currently running program
    private static void loadRelationships(Model userClasses, JSONObject javaObj, JSONArray list,
	    ArrayList<JSONObject> objList, ViewTemplate view) {
	// Add relationship and class names from JSONArray list to loadFile
	for (int x = 0; x < list.size(); ++x) {

	    // Get relationship from JSONArray list, add to ArrayList objList (type
	    // conversions)
	    Object entity = list.get(x);
	    objList.add((JSONObject) entity);

	    // Extract name of relationship and classes, add to loadFile
	    String relationName = (String) objList.get(x).get("relationName");
	    String classOne = (String) objList.get(x).get("ClassOne");
	    String classTwo = (String) objList.get(x).get("ClassTwo");
	    userClasses.createRelationship(checkEnum(relationName.toUpperCase()), classOne, classTwo);
	    if (ViewTemplate.isGUI()) {
		((GUIView) view).createRelationship(checkEnum(relationName.toUpperCase()), classOne, classTwo,
			userClasses);
	    }

	}
    }

    // Main save function. Calls saveClasses and saveRelationships
    public static void save(Path directory, Model userClasses) throws IOException {
	JSONObject saveFile = new JSONObject();

	saveFile = saveClasses(saveFile, userClasses);
	saveFile = saveRelationships(saveFile, userClasses);

	writeFile(saveFile, directory);
    }

    // Creates a JSONObject for the classes and saves it to the saveFile
    private static JSONObject saveClasses(JSONObject saveFile, Model userClasses) {
	JSONArray allClasses = new JSONArray();

	for (Entity entity : userClasses.getEntities()) {
	    JSONObject singleClass = new JSONObject();

	    singleClass.put("className", entity.getName());

	    // Create an array of fields for the class
	    JSONArray fields = new JSONArray();
	    for (Field field : entity.getFields()) {
		JSONObject oneField = new JSONObject();
		oneField.put("fieldVis", field.getVisibility().toString());
		oneField.put("fieldType", field.getType());
		oneField.put("fieldName", field.getName());
		fields.add(oneField);
	    }
	    singleClass.put("fields", fields);

	    // Create an array of methods for the class
	    JSONArray methods = new JSONArray();
	    for (Method method : entity.getMethods()) {
		JSONObject oneMethod = new JSONObject();
		oneMethod.put("methodVis", method.getVisibility().toString());
		oneMethod.put("methodType", method.getType());
		oneMethod.put("methodName", method.getName());

		JSONArray parameters = new JSONArray();
		for (Parameter param : method.getParameters()) {
		    JSONObject oneParam = new JSONObject();
		    oneParam.put("paramType", param.getType());
		    oneParam.put("paramName", param.getName());
		    parameters.add(oneParam);
		}
		oneMethod.put("Parameters", parameters);
		methods.add(oneMethod);
	    }
	    singleClass.put("methods", methods);

	    singleClass.put("xPosition", entity.getXLocation());
	    singleClass.put("yPosition", entity.getYLocation());

	    // Add class with it's name and attributes to the JSONArray allClasses
	    allClasses.add(singleClass);
	}
	saveFile.put("Classes", allClasses);

	return saveFile;
    }

    // Creates a JSONObject for the relationships and saves it to the saveFile
    private static JSONObject saveRelationships(JSONObject saveFile, Model userClasses) {
	JSONArray allRelationships = new JSONArray();

	for (Relationship relation : userClasses.getRelationships()) {
	    // Create JSONObject for a single relationship
	    JSONObject relationObj = new JSONObject();

	    relationObj.put("relationName", relation.getName().toString());
	    relationObj.put("ClassOne", relation.getFirstClass());
	    relationObj.put("ClassTwo", relation.getSecondClass());

	    // Add relationship with it's name and related classes to the JSONArray
	    // allRelationships
	    allRelationships.add(relationObj);
	}
	saveFile.put("Relationships", allRelationships);

	return saveFile;
    }

    // Creates a file if it does not exist and writes saveFile to the file
    private static void writeFile(JSONObject saveFile, Path directory) throws IOException {
	File fileDirectory = new File(directory.toString());
	fileDirectory.createNewFile();

	// Converts JSONObject and adds it to file
	String fullJSONString = saveFile.toString();
	FileWriter myWriter = new FileWriter(fileDirectory);
	myWriter.write(fullJSONString);
	myWriter.close();
    }

    // Lists all of the classes and their respective attributes
    public static void listClasses(Model userClasses) {
	System.out.println("Classes:");
	for (Entity curEntity : userClasses.getEntities()) {
	    System.out.println("	" + curEntity.getName() + ":");
	    System.out.print("		fields:  [ ");

	    // Prints out all of the fields
	    ArrayList<Field> fields = curEntity.getFields();
	    for (int x = 0; x < fields.size(); x++) {

		System.out.print("(" + fields.get(x).getVisibility().toString().toLowerCase() + ") " + fields.get(x).getType() + " " + fields.get(x).getName());
		if (x != fields.size() - 1) {
		    System.out.print(", ");
		}
	    }
	    System.out.println(" ]");

	    // Prints out all of the methods
	    System.out.print("		methods: [ ");
	    for (int x = 0; x < curEntity.getMethods().size(); x++) {
		Method curMethod = curEntity.getMethods().get(x);
		System.out.print("(" + curMethod.getVisibility().toString().toLowerCase() + ") " + curMethod.getType() + " " + curMethod.getName() + " -- {");

		// Loop through parameters of method
		ArrayList<Parameter> parameters = curMethod.getParameters();
		for (Parameter p : parameters) {
		    System.out.print("(" + p.getType() + ") " + p.getName());
		    // Print comma if not last parameter
		    if (!p.equals(parameters.get(parameters.size() - 1))) {
			System.out.print(", ");
		    }
		}
		System.out.print("}");

		// Print comma if not last method
		if (x != curEntity.getMethods().size() - 1) {
		    System.out.print(",\n			   ");
		}
	    }
	    System.out.println(" ]");
	}
    }

    // Lists all of the relationships and the classes they are pointing to
    public static void listRelationships(Model userClasses) {
	System.out.println("Relationships:");
	for (Relationship relation : userClasses.getRelationships()) {
	    System.out.println(
		    "   -- " + relation.getName() + ": " + relation.getFirstClass() + "--" + relation.getSecondClass());
	}
    }

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

}

package mike.datastructures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import mike.HelperMethods;

@SuppressWarnings("unchecked")
public class Memento {
    private Model m;

    public Memento() {
    }

    public Memento(Model m) {
	set(m);
    }

    public void set(Model m) {
	this.m = m;
    }

    public Model getModel() {
	return this.m;
    }

    public void load(Path path)
	    throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException {

	m.clear();

	File directory = new File(path.toString());

	// Parse the JSON file and get an array of the classes
	Object obj = new JSONParser().parse(new FileReader(directory));

	// Variable initialization. If the types look stupid that's because they are.
	JSONObject javaObj = (JSONObject) obj;
	JSONArray list = (JSONArray) javaObj.get("Classes");
	ArrayList<JSONObject> objList = new ArrayList<JSONObject>();

	loadClasses(javaObj, list, objList);

	// Clear out variables for reuse
	list = (JSONArray) javaObj.get("Relationships");
	objList.clear();

	loadRelationships(javaObj, list, objList);
    }

    private void loadClasses(JSONObject javaObj, JSONArray list, ArrayList<JSONObject> objList) {
	// Add class names and attributes from JSONArray list to loadFile
	for (int x = 0; x < list.size(); ++x) {
	    // Get class from JSONArray list, add to ArrayList objList (type conversions)
	    Object entity = list.get(x);
	    objList.add((JSONObject) entity);

	    // Extract name of class, add to loadFile
	    String className = (String) objList.get(x).get("className");
	    m.createClass(className);

	    // Extract all fields of associated class, add to loadFile
	    JSONArray classFields = (JSONArray) objList.get(x).get("fields");
	    for (int y = 0; y < classFields.size(); ++y) {
		JSONObject field = (JSONObject) classFields.get(y);
		String fieldName = field.get("fieldName").toString();
		String fieldType = field.get("fieldType").toString();
		String fieldVis = field.get("fieldVis").toString();

		m.createField(className, fieldName, fieldType, fieldVis);
	    }

	    // Extract all methods of associated class, add to loadFile
	    JSONArray classMethods = (JSONArray) objList.get(x).get("methods");
	    for (int y = 0; y < classMethods.size(); ++y) {
		JSONObject method = (JSONObject) classMethods.get(y);
		String methodName = method.get("methodName").toString();
		String methodType = method.get("methodType").toString();
		String methodVis = method.get("methodVis").toString();

		m.createMethod(className, methodName, methodType, methodVis);
		JSONArray methodParam = (JSONArray) method.get("Parameters");
		for (int z = 0; z < methodParam.size(); ++z) {
		    JSONObject param = (JSONObject) methodParam.get(z);
		    String paramName = param.get("paramName").toString();
		    String paramType = param.get("paramType").toString();

		    m.createParameter(className, methodName, paramName, paramType);
		}
	    }
	    // Extract name of class, add to loadFile
	    Entity e = m.getEntities().get(x);
	    Long location = (Long) objList.get(x).get("xPosition");
	    e.setXLocation(Math.toIntExact(location));
	    location = (Long) objList.get(x).get("yPosition");
	    e.setYLocation(Math.toIntExact(location));
	}
    }

    private void loadRelationships(JSONObject javaObj, JSONArray list, ArrayList<JSONObject> objList) {
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
	    m.createRelationship(HelperMethods.checkEnum(relationName.toUpperCase()), classOne, classTwo);

	}
    }

    public void save(Path directory) throws IOException {
	JSONObject saveFile = new JSONObject();

	saveFile = saveClasses(saveFile);
	saveFile = saveRelationships(saveFile);

	writeFile(saveFile, directory);
    }

    private JSONObject saveClasses(JSONObject saveFile) {
	JSONArray allClasses = new JSONArray();

	for (Entity entity : m.getEntities()) {
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

    private JSONObject saveRelationships(JSONObject saveFile) {
	JSONArray allRelationships = new JSONArray();

	for (Relationship relation : m.getRelationships()) {
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

    private void writeFile(JSONObject saveFile, Path directory) throws IOException {
	File fileDirectory = new File(directory.toString());
	fileDirectory.createNewFile();

	// Converts JSONObject and adds it to file
	String fullJSONString = saveFile.toString();
	FileWriter myWriter = new FileWriter(fileDirectory);
	myWriter.write(fullJSONString);
	myWriter.close();
    }
}
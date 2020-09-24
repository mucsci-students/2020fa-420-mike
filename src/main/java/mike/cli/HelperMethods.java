package mike.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import mike.datastructures.Classes;
import mike.datastructures.Entity;
import mike.datastructures.Relationship;

import java.util.ArrayList;
	
@SuppressWarnings("unchecked")
public class HelperMethods {

	// Main load function. Calls loadClasses and loadRelationships
	public static void load(String userPath, Classes userClasses) throws FileNotFoundException, IOException, ParseException {
		
		userClasses.clear();

		File directory = new File(userPath);

		// Parse the JSON file and get an array of the classes
		Object obj = new JSONParser().parse(new FileReader(directory));
	        
	    // Variable initialization. If the types look stupid that's because they are.
	    JSONObject javaObj = (JSONObject) obj;
	    JSONArray list = (JSONArray) javaObj.get("Classes");
	    ArrayList<JSONObject> objList = new ArrayList<JSONObject>();
	        
	    loadClasses(userClasses, javaObj, list, objList);
	   
	    // Clear out variables for reuse
	    list = (JSONArray) javaObj.get("Relationships");
	    objList.clear();
	        
	    loadRelationships(userClasses, javaObj, list, objList);
	}

	// Takes all of the information from the classes in the JSON file,
	//	and inserts them to the currently running program
	private static void loadClasses(Classes userClasses, JSONObject javaObj, JSONArray list, ArrayList<JSONObject> objList) {
		// Add class names and attributes from JSONArray list to loadFile
	    for (int x = 0; x < list.size(); ++x){
	        	
	    	// Get class from JSONArray list, add to ArrayList objList (type conversions)
	    	Object entity = list.get(x);
	    	objList.add((JSONObject) entity);
	            
	    	// Extract name of class, add to loadFile
	    	String className = (String) objList.get(x).get("className");
	    	userClasses.createClass(className);
	        	
	    	// Extract all fields of associated class, add to loadFile
	    	ArrayList<String> classFields = (ArrayList<String>) objList.get(x).get("fields");
	    	for(int y = 0; y < classFields.size(); ++y) {
	    		userClasses.createField(className, classFields.get(y));
	    	}
	    	
	    	// Extract all methods of associated class, add to loadFile
	    	ArrayList<String> classMethods = (ArrayList<String>) objList.get(x).get("methods");
	    	for(int y = 0; y < classMethods.size(); ++y) {
	    		userClasses.createMethod(className, classMethods.get(y));
	    	}
	    }
	}
		
	// Takes all of the information from the relationships in the JSON file,
	//	and inserts them into the currently running program
	private static void loadRelationships(Classes userClasses, JSONObject javaObj, JSONArray list, ArrayList<JSONObject> objList) {
		// Add relationship and class names from JSONArray list to loadFile
	    for (int x = 0; x < list.size(); ++x){
	        	
	    	// Get relationship from JSONArray list, add to ArrayList objList (type conversions)
	        Object entity = list.get(x);
	        objList.add((JSONObject) entity);
	            
	        // Extract name of relationship and classes, add to loadFile
	        String relationName = (String) objList.get(x).get("relationName");
	        String classOne = (String) objList.get(x).get("ClassOne");
	        String classTwo = (String) objList.get(x).get("ClassTwo");
	        userClasses.createRelationship(relationName, classOne, classTwo);
	    }	
	}	

	// Main save function. Calls saveClasses and saveRelationships
	public static void save (String filename, String directory, Classes userClasses) throws IOException {
		File newFile = new File(directory);
		System.out.println();
		
		// If directory given does not exist, put in user.dir
		if(!newFile.isDirectory())
		{
			directory = System.getProperty("user.dir");
			System.out.println("Directory does not exist.");
		}

		JSONObject saveFile = new JSONObject();
		
		saveFile = saveClasses(saveFile, userClasses);
		saveFile = saveRelationships(saveFile, userClasses);
		
		directory += ("\\" + filename);
		writeFile(saveFile, directory);
		System.out.println("File saved to:" + directory +"\n");
	}

	// Creates a JSONObject for the classes and saves it to the saveFile
	private static JSONObject saveClasses (JSONObject saveFile, Classes userClasses) {	
		JSONArray allClasses = new JSONArray();
		
		for(Entity entity : userClasses.getEntities())
		{
			JSONObject singleClass = new JSONObject();
			
			singleClass.put("className", entity.getName());
			
			// Create an array of fields for the class
			JSONArray fields = new JSONArray();
			for(String field : entity.getFields()) 
			{
				fields.add(field);
			}
			singleClass.put("fields", fields);
			
			// Create an array of methods for the class
			JSONArray methods = new JSONArray();
			for(String method : entity.getMethods()) 
			{
				methods.add(method);
			}
			singleClass.put("methods", methods);
			
			// Add class with it's name and attributes to the JSONArray allClasses
			allClasses.add(singleClass);	
		}
		saveFile.put("Classes", allClasses);
		
		return saveFile;
	}
	
	// Creates a JSONObject for the relationships and saves it to the saveFile
	private static JSONObject saveRelationships (JSONObject saveFile, Classes userClasses) {
		JSONArray allRelationships = new JSONArray();
		
		for(Relationship relation : userClasses.getRelationships())
		{
			// Create JSONObject for a single relationship
			JSONObject relationObj = new JSONObject();
			
			relationObj.put("relationName", relation.getName());
			relationObj.put("ClassOne", relation.getFirstClass());
			relationObj.put("ClassTwo", relation.getSecondClass());
			
			// Add relationship with it's name and related classes to the JSONArray allRelationships
			allRelationships.add(relationObj);		
		}
		saveFile.put("Relationships", allRelationships);
		
		return saveFile;
	}
	
	// Creates a file if it does not exist and writes saveFile to the file
	private static void writeFile (JSONObject saveFile, String directory) throws IOException {
		File fileDirectory = new File(directory);
		fileDirectory.createNewFile();
	    
	    // Converts JSONObject and adds it to file
	    String fullJSONString = saveFile.toString();
	    FileWriter myWriter = new FileWriter(fileDirectory);
		myWriter.write(fullJSONString);
		myWriter.close();
	}

	// Lists all of the classes and their respective attributes
	public static void listClasses(Classes userClasses)
	{
		System.out.println("Classes:");
		for(Entity curEntity : userClasses.getEntities())
		{			
			System.out.println("	" + curEntity.getName() + ":");
			System.out.print("		fields: [ ");
				
			//Prints out all of the fields
			for(int x = 0; x < curEntity.getFields().size(); x++)
			{
				System.out.print(curEntity.getFields().get(x));
				if(x != curEntity.getFields().size()-1)
				{
					System.out.print(", ");
				}
			}
			System.out.println(" ]");
			
			//Prints out all of the methods
			System.out.print("		methods: [ ");
			for(int x = 0; x < curEntity.getMethods().size(); x++)
			{
				System.out.print(curEntity.getMethods().get(x));
				if(x != curEntity.getMethods().size()-1)
				{
					System.out.print(", ");
				}
			}
			System.out.println(" ]");
		}
	}
		
	// Lists all of the relationships and the classes they are pointing to
	public static void listRelationships(Classes userClasses)
	{
		System.out.println("Relationships:");
		for(Relationship relation : userClasses.getRelationships())
		{
			System.out.println("   -- " + relation.getName() + ": " + relation.getFirstClass() + "--" + relation.getSecondClass());
		}
	} 
}


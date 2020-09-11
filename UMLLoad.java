import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class UMLLoad {
	
	public static void load(String userPath) throws FileNotFoundException, IOException, ParseException {

		File directory = new File(userPath);

		// Parse the JSON file and get an array of the classes
		Object obj = new JSONParser().parse(new FileReader(directory));
        Classes loadFile = new Classes();
        
        // Variable initialization. If the types look stupid that's because they are.
        JSONObject javaObj = (JSONObject) obj;
        JSONArray list = (JSONArray) javaObj.get("Classes");
        ArrayList<JSONObject> objList = new ArrayList<JSONObject>();
        
        loadClasses(loadFile, javaObj, list, objList);
   
        // Clear out variables for reuse
        list = (JSONArray) javaObj.get("Relationships");
        objList.clear();
        
        loadRelationships(loadFile, javaObj, list, objList);
	}

	// Takes all of the information from the classes in the JSON file,
	//	and inserts them to the currently running program
	private static Classes loadClasses(Classes loadFile, JSONObject javaObj, JSONArray list, ArrayList<JSONObject> objList) {
		 // Add class names and attributes from JSONArray list to loadFile
        for (int x = 0; x < list.size(); ++x){
        	
        	// Get class from JSONArray list, add to ArrayList objList (type conversions)
        	Object entity = list.get(x);
            objList.add((JSONObject) entity);
            
            // Extract name of class, add to loadFile
            String className = (String) objList.get(x).get("className");
            loadFile.createClass(className);
        	
        	// Extract all attributes of associated class, add to loadFile
        	ArrayList<String> classAttributes = (ArrayList<String>) objList.get(x).get("attributes");
        	for(int y = 0; y < classAttributes.size(); ++y) {
        		loadFile.createAttribute(className, classAttributes.get(y));
        	}
        }
		return loadFile;
	}
	
	// Takes all of the information from the relationships in the JSON file,
	//	and inserts them into the currently running program
	private static Classes loadRelationships(Classes loadFile, JSONObject javaObj, JSONArray list, ArrayList<JSONObject> objList) {
        // Add relationship and class names from JSONArray list to loadFile
        for (int x = 0; x < list.size(); ++x){
        	
        	// Get relationship from JSONArray list, add to ArrayList objList (type conversions)
        	Object entity = list.get(x);
            objList.add((JSONObject) entity);
            
            // Extract name of relationship and classes, add to loadFile
            String relationName = (String) objList.get(x).get("relationName");
            String classOne = (String) objList.get(x).get("ClassOne");
            String classTwo = (String) objList.get(x).get("ClassTwo");
            loadFile.createRelationship(relationName, classOne, classTwo);
        }	
		return loadFile;
	}	
}

package mike;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import mike.controller.ControllerType;
import mike.controller.GUIController;
import mike.datastructures.*;
import mike.datastructures.Relationship.Type;
import mike.view.GUIView;
import mike.view.ViewTemplate;

import java.util.ArrayList;

import javax.swing.JLabel;

@SuppressWarnings("unchecked")
public class HelperMethods {

    // Main load function. Calls loadClasses and loadRelationships
    public static void load(File file, Model userClasses, ControllerType control, ViewTemplate view)
            throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException {

        // Parse the JSON file and get an array of the classes
        Object obj = new JSONParser().parse(new FileReader(file));

        userClasses.clear();

        // Variable initialization. If the types look stupid that's because they are.
        JSONObject javaObj = (JSONObject) obj;
        JSONArray list = (JSONArray) javaObj.get("Classes");
        ArrayList<JSONObject> objList = new ArrayList<JSONObject>();

        loadClasses(userClasses, javaObj, list, objList, (GUIController) control, view);

        // Clear out variables for reuse
        list = (JSONArray) javaObj.get("Relationships");
        objList.clear();

        loadRelationships(userClasses, javaObj, list, objList, view);
    }

    // Takes all of the information from the classes in the JSON file,
    // and inserts them to the currently running program
    private static void loadClasses(Model userClasses, JSONObject javaObj, JSONArray list,
                                    ArrayList<JSONObject> objList, ControllerType control, ViewTemplate view) {
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
            Long xLocation = (Long) objList.get(x).get("xPosition");
            Long yLocation = (Long) objList.get(x).get("yPosition");
            e.setLocation(Math.toIntExact(xLocation), Math.toIntExact(yLocation));
            if (ViewTemplate.isGUI()) {
                ((GUIView) view).showClass(e, (GUIController) control);
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
    public static void save(File file, Model userClasses) throws IOException {
        JSONObject saveFile = new JSONObject();

        saveFile = saveClasses(saveFile, userClasses);
        saveFile = saveRelationships(saveFile, userClasses);

        writeFile(saveFile, file);
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
    private static void writeFile(JSONObject saveFile, File file) throws IOException {
        file.createNewFile();

        // Converts JSONObject and adds it to file
        String fullJSONString = saveFile.toString();
        FileWriter myWriter = new FileWriter(file);
        myWriter.write(fullJSONString);
        myWriter.close();
    }

    // Lists all of the classes and their respective attributes
    public static void listClasses(Model userClasses) {
        System.out.println("Classes:");
        for(Entity curEntity : userClasses.getEntities()) {
            int longestStringLength = getLongestStringInClass(curEntity).length();
            String divider = "+";
            String spaces = " ";

            //add bars for divider and top and bottom string
            //number of bars = longest string + 2 (for spacing)
            for(int i = 0; i < longestStringLength + 2; ++i) {
                divider += "-";
            }
            divider += "+";

            //print top string
            System.out.println("\t" + divider);

            //print class name
            String className = curEntity.getName();
            spaces = calculateSpaces(className, longestStringLength);
            System.out.println("\t| " + curEntity.getName() + spaces + "|");

            //print divider
            System.out.println("\t" + divider);

            //print fields
            String fieldsString = "Fields:";
            spaces = calculateSpaces(fieldsString, longestStringLength);
            System.out.println("\t| " + fieldsString + spaces + "|");

            ArrayList<Field> fields = curEntity.getFields();
            for(Field f : fields) {
                String fieldToString = f.getVisibility().toString() + " " + f.getType() + " " + f.getName();
                spaces = calculateSpaces(fieldToString, longestStringLength);
                System.out.println("\t| " + fieldToString + spaces + "|");
            }

            //print divider
            System.out.println("\t" + divider);

            //print methods
            String methodsString = "Methods:";
            spaces = calculateSpaces(methodsString, longestStringLength);
            System.out.println("\t| " + methodsString + spaces + "|");

            ArrayList<Method> methods = curEntity.getMethods();
            for(Method m : methods) {
                String methodToString = m.getVisibility().toString() + " " + m.getType() + " " + m.getName() + "(";
                //Add parameters to method string
                ArrayList<Parameter> parameters = m.getParameters();
                for (Parameter p : parameters) {
                    methodToString += p.getType() + " " + p.getName();
                    //Add comma if not last parameter
                    if (!p.equals(parameters.get(parameters.size() - 1))) {
                        methodToString += ", ";
                    }
                }
                methodToString += ")";
                spaces = calculateSpaces(methodToString, longestStringLength);
                System.out.println("\t| " + methodToString + spaces + "|");
            }

            //print bottom string
            System.out.println("\t" + divider);

        }
        /* i tried ...
        //loop through classes
        for (Entity curEntity : userClasses.getEntities()) {
			int longestStringLength = getLongestStringInClass(curEntity).length();
			String bars = "", spaces = "";

			//print top bar underscores based on length of longest string + spaces and |s
            //tab ~= 7 spaces
        	for(int i = 0; i < longestStringLength + 2; ++i) {
				bars += "_";
			}
            System.out.println("\t" + bars);
        	//System.out.println("\t|\t" + spaces + "\t|");

            //update spaces for class name
            for(int i = curEntity.getName().length(); i <longestStringLength; ++i) {
                spaces += " ";
            }

        	//print class name
        	System.out.println("\t| " + curEntity.getName() + spaces + " |");
        	System.out.println("\t|" + bars + "|");

            //update spaces for fields: string
            spaces = "";
            for(int i = 8; i <longestStringLength; ++i) {
                spaces += " ";
            }

        	//print fields
			System.out.println("\t| Fields: " + spaces + " |");
			ArrayList<Field> fields = curEntity.getFields();
			for(Field f : fields) {
                //update spaces for each field
                spaces = "";
                for(int i = f.getName().length(); i <longestStringLength; ++i) {
                    spaces += " ";
                }
				System.out.println("\t| " + f.getVisibility().toString().toLowerCase() + " "
						+ f.getType() + " " + f.getName() + spaces + " |");
			}

			System.out.println("\t|" + bars + "|");

            //update spaces for fields: string
            spaces = "";
            for(int i = 9; i <longestStringLength; ++i) {
                spaces += " ";
            }

			//print methods
			System.out.println("\t| Methods: " + spaces + " |");
			ArrayList<Method> methods = curEntity.getMethods();
			for(Method m : methods) {
			    String currString = m.getVisibility().toString().toLowerCase() + " "
                        + m.getType() + " " + m.getName() + "(";
				System.out.print("\t| " + currString);

				//print parameters for each method
                ArrayList<Parameter> parameters = m.getParameters();
                for (Parameter p : parameters) {
                    currString += p.getType() + " " + p.getName();
                    System.out.print(currString);
                    // Print comma if not last parameter
                    if (!p.equals(parameters.get(parameters.size() - 1))) {
                        currString += ", ";
                        System.out.print(currString);
                    }
                }

                //update spaces for each method
                spaces = "";
                for(int i = currString.length() + 1; i <longestStringLength; ++i) {
                    spaces += " ";
                }

                System.out.print(")" + spaces + "|\n");
			}

			System.out.println("\t|" + bars + "|");

		}
		*/
        /*
        for (Entity curEntity : userClasses.getEntities()) {
            System.out.println("	" + curEntity.getName() + ":");
            System.out.print("		fields:  [ ");

            // Prints out all of the fields
            ArrayList<Field> fields = curEntity.getFields();
            for (int x = 0; x < fields.size(); x++) {

                System.out.print("(" + fields.get(x).getVisibility().toString().toLowerCase() + ") "
                        + fields.get(x).getType() + " " + fields.get(x).getName());
                if (x != fields.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println(" ]");

            // Prints out all of the methods
            System.out.print("		methods: [ ");
            for (int x = 0; x < curEntity.getMethods().size(); x++) {
                Method curMethod = curEntity.getMethods().get(x);
                System.out.print("(" + curMethod.getVisibility().toString().toLowerCase() + ") " + curMethod.getType()
                        + " " + curMethod.getName() + " -- {");

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
        */
    }

    // Lists all of the relationships and the classes they are pointing to
    public static void listRelationships(Model userClasses) {
        System.out.println("Relationships:");
        for (Relationship relation : userClasses.getRelationships()) {
            System.out.println(
                    "   -- " + relation.getName() + ": " + relation.getFirstClass() + "--" + relation.getSecondClass());
        }
    }

    public static Type checkEnum(String command) {
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

    //calculates the number of spaces needed based on a string and the biggest string's size
    //returns the spaces as a string
    //to be used for formatting the box for list classes in the CLI
    private static String calculateSpaces(String smallString, int maxStringLength) {
        String spaces = " ";
        int numSpaces = maxStringLength - smallString.length();
        while(numSpaces-- > 0) {
            spaces += " ";
        }
        return spaces;
    }

    //calculates the longest string length in the class and returns that string
    //to be used for formatting the box for list classes in the CLI
    private static String getLongestStringInClass(Entity entity) {
        String longestString = "Methods:";

        //look at fields
        for(Field f : entity.getFields()) {
            String currString = f.getVisibility().toString() + " " + f.getType() + " " + f.getName();
            if(currString.length() > longestString.length()) {
                longestString = currString;
            }
        }

        //look at methods
        for(Method m : entity.getMethods()) {
            String currString = m.getVisibility().toString() + " " + m.getType() + " " + m.getName() + "(";
            //have to remember parameters!
            ArrayList<Parameter> parameters = m.getParameters();
            for(Parameter p : parameters) {
                currString += p.getType() + " " + p.getName();
                //if not the last parameter add a comma
                if(!p.equals(parameters.get(parameters.size() - 1))) {
                    currString += ", ";
                }
            }
            currString += ")";
            if(currString.length() > longestString.length()) {
                longestString = currString;
            }
        }

        return longestString;
    }

}

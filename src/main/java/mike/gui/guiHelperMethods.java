package mike.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import mike.datastructures.Classes;
import mike.datastructures.Entity;
import mike.datastructures.Field;
import mike.datastructures.Method;
import mike.datastructures.Parameter;
import mike.datastructures.Relationship;
import mike.datastructures.Relationship.Type;

@SuppressWarnings("unchecked")
public class guiHelperMethods {
	public static void createClass(Classes classes) {
		JTextField name = new JTextField(20);

		// Create a panel containing a drop-down box and text field
		JPanel inputFields = new JPanel();
		inputFields.add(new JLabel("Enter a Class name: "));
		inputFields.add(name);

		// Ask for input with inputFields
		int result = JOptionPane.showConfirmDialog(null, inputFields, "Create Class", JOptionPane.OK_CANCEL_OPTION);

		if (result == 0) {
			classes.createClass(name.getText());
			View.showClass(classes.copyEntity(name.getText()));
		}
	}

	public static void renameClass(Classes classes, JComboBox<String> list) {
		JTextField rename = new JTextField(20);

		// Create a panel containing a drop-down box and text field
		JPanel inputFields = new JPanel();
		inputFields.add(new JLabel("Choose a Class: "));
		inputFields.add(list);
		enterInput("Rename Class: ", inputFields, rename);

		// Ask for input with inputFields
		int result = JOptionPane.showConfirmDialog(null, inputFields, "Rename Class", JOptionPane.OK_CANCEL_OPTION);

		if (result == 0) {
			String oldname = list.getSelectedItem().toString();
			classes.renameClass(oldname, rename.getText());
			View.updateClass(oldname, classes.copyEntity(rename.getText()));
		}
	}

	public static void deleteClass(Classes classes, JComboBox<String> list) {
		// Create a panel containing a drop-down box
		JPanel inputFields = new JPanel();
		inputFields.add(list);

		// Ask for input with inputFields
		int result = JOptionPane.showConfirmDialog(null, inputFields, "Delete Class", JOptionPane.OK_CANCEL_OPTION);

		if (result == 0) {
			String name = list.getSelectedItem().toString();
			classes.deleteClass(name);
			View.deleteClass(name);
		}
	}

	public static void createOrDeleteRelation(Classes classes, JComboBox<String> list, String function,
			String[] entityStrings) {
		JRadioButton composition = new JRadioButton("Composition");
		composition.setActionCommand("a");
		composition.setSelected(true);

		JRadioButton aggregation = new JRadioButton("Aggregation");
		aggregation.setActionCommand("b");

		JRadioButton realization = new JRadioButton("Realization");
		realization.setActionCommand("c");

		JRadioButton inheritance = new JRadioButton("Inheritance");
		inheritance.setActionCommand("d");

		// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(composition);
		group.add(aggregation);
		group.add(realization);
		group.add(inheritance);

		// Create a panel containing a drop-down box
		JPanel inputFields = new JPanel();
		inputFields.add(new JLabel("Choose the Relationship Type: "));
		inputFields.add(composition);
		inputFields.add(aggregation);
		inputFields.add(realization);
		inputFields.add(inheritance);
		inputFields.add(Box.createHorizontalStrut(15)); // a spacer

		inputFields.add(new JLabel("Choose a Class 1: "));
		inputFields.add(list);
		inputFields.add(Box.createHorizontalStrut(15)); // a spacer
		JComboBox<String> list2 = new JComboBox<>(entityStrings);
		inputFields.add(new JLabel("Choose a Class 2: "));
		inputFields.add(list2);

		// Ask for input with inputFields
		int result = -1;
		if (function.equals("Create Relationship")) {
			result = JOptionPane.showConfirmDialog(null, inputFields, "Create Relationship",
					JOptionPane.OK_CANCEL_OPTION);
		} else if (function.equals("Delete Relationship")) {
			result = JOptionPane.showConfirmDialog(null, inputFields, "Delete Relationship",
					JOptionPane.OK_CANCEL_OPTION);
		}

		Type name;
		if (composition.isSelected()) {
			name = Type.COMPOSITION;
		} else if (aggregation.isSelected()) {
			name = Type.AGGREGATION;
		} else if (realization.isSelected()) {
			name = Type.REALIZATION;
		} else {
			name = Type.INHERITANCE;
		}

		if (result == 0) {
			if (function.equals("Create Relationship")) {
				classes.createRelationship(name, list.getSelectedItem().toString(), list2.getSelectedItem().toString());
			} else {
				classes.deleteRelationship(name, list.getSelectedItem().toString(), list2.getSelectedItem().toString());
			}
		}
	}

	public static void createFieldsOrMethods(Classes classes, JComboBox<String> list, String attribute) {
		JTextField type = new JTextField(20);
		JTextField name = new JTextField(20);

		// Create a panel containing a drop-down box and text field
		JPanel inputFields = new JPanel();
		inputFields.add(new JLabel("Choose a Class: "));
		inputFields.add(list);
		enterInput("Enter a " + attribute + " Type: ", inputFields, type);
		enterInput("Enter a " + attribute + " Name: ", inputFields, name);

		// Ask for input with inputFields
		int result = JOptionPane.showConfirmDialog(null, inputFields, "Create " + attribute,
				JOptionPane.OK_CANCEL_OPTION);

		if (result == 0) {
			String entityname = list.getSelectedItem().toString();
			if (attribute.equals("Field")) {
				classes.createField(entityname, name.getText(), type.getText());
			} else {
				classes.createMethod(entityname, name.getText(), type.getText());
			}
			View.updateClass(entityname, classes.copyEntity(entityname));
		}
	}

	public static void renameFieldsOrMethods(Classes classes, JComboBox<String> list, JFrame frame, String attribute) {
		// Create a window asking for which class to choose from
		JComboBox<String> finaleList = inputClass(classes.getEntities(), frame, attribute, false,
				"Rename " + attribute);
		if (finaleList == null) {
			return;
		}
		// Create Drop-down box of existing classes
		JComboBox<String> List2 = findStuff(classes, finaleList.getSelectedItem().toString(), attribute, false);
		JTextField rename = new JTextField(20);

		// Create a panel containing a drop-down box and text field
		JPanel inputFields2 = new JPanel();
		inputFields2.add(new JLabel("Choose a " + attribute + ": "));
		inputFields2.add(List2);
		enterInput("Enter a New " + attribute + " Name: ", inputFields2, rename);

		// Ask for input with inputFields
		int result2 = JOptionPane.showConfirmDialog(null, inputFields2, "Rename " + attribute,
				JOptionPane.OK_CANCEL_OPTION);

		if (result2 == 0) {
			String entityname = list.getSelectedItem().toString();
			if (attribute.equals("Field")) {
				classes.renameField(entityname, List2.getSelectedItem().toString(), rename.getText());
			} else {
				classes.renameMethod(entityname, List2.getSelectedItem().toString(), rename.getText());
			}

			View.updateClass(entityname, classes.copyEntity(entityname));
		}
	}

	public static void deleteFieldsOrMethods(Classes classes, JComboBox<String> list, JFrame frame, String attribute) {
		// Create a window asking for which class to choose from
		JComboBox<String> finaleList = inputClass(classes.getEntities(), frame, attribute, false,
				"Delete " + attribute);
		if (finaleList == null) {
			return;
		}
		// Create Drop-down box of existing classes
		JComboBox<String> List2 = findStuff(classes, finaleList.getSelectedItem().toString(), attribute, false);

		// Create a panel containing a drop-down box and text field
		JPanel inputFields2 = new JPanel();
		inputFields2.add(new JLabel("Choose a " + attribute + ": "));
		inputFields2.add(List2);

		// Ask for input with inputFields
		int result2 = JOptionPane.showConfirmDialog(null, inputFields2, "Delete " + attribute,
				JOptionPane.OK_CANCEL_OPTION);

		if (result2 == 0) {
			String entityname = list.getSelectedItem().toString();
			if (attribute.equals("Field")) {
				classes.deleteField(entityname, List2.getSelectedItem().toString());
			} else {
				classes.deleteMethod(entityname, List2.getSelectedItem().toString());
			}
			View.updateClass(entityname, classes.copyEntity(entityname));
		}
	}

	public static void createParameter(Classes classes, JComboBox<String> list, JFrame frame) {
		// Create a window asking for which class to choose from
		JComboBox<String> finaleList = inputClass(classes.getEntities(), frame, "Method", false, "Create Parameter");
		if (finaleList == null) {
			return;
		}
		// Create Drop-down box of existing classes
		JComboBox<String> methodList = findStuff(classes, finaleList.getSelectedItem().toString(), "Method", false);
		JTextField type = new JTextField(20);
		JTextField name = new JTextField(20);
		
		// Create a panel containing a drop-down box and text field
		JPanel inputFields2 = new JPanel();
		inputFields2.add(new JLabel("Choose a Method: "));
		inputFields2.add(methodList);
		enterInput("Enter a Parameter Type: ", inputFields2, type);
		enterInput("Enter a Parameter Name: ", inputFields2, name);

		// Ask for input with inputFields
		int result2 = JOptionPane.showConfirmDialog(null, inputFields2, "Create Parameter",
				JOptionPane.OK_CANCEL_OPTION);

		if (result2 == 0) {
			String entityname = list.getSelectedItem().toString();
			classes.createParameter(finaleList.getSelectedItem().toString(), methodList.getSelectedItem().toString(),
					name.getText(), type.getText());
			View.updateClass(entityname, classes.copyEntity(entityname));
		}
	}

	public static void renameParameter(Classes classes, JComboBox<String> list, JFrame frame) {
		// Create a window asking for which class to choose from
		JComboBox<String> finaleList = inputClass(classes.getEntities(), frame, "Method", true, "Rename Parameter");
		if (finaleList == null) {
			return;
		}
		// Create Drop-down box of existing classes
		JComboBox<String> methodList = findStuff(classes, finaleList.getSelectedItem().toString(), "Method", true);

		// Create a panel containing a drop-down box and text field
		JPanel inputFields2 = new JPanel();
		inputFields2.add(new JLabel("Choose a Method: "));
		inputFields2.add(methodList);

		// Ask for input with inputFields
		int result2 = JOptionPane.showConfirmDialog(null, inputFields2, "Rename Parameter",
				JOptionPane.OK_CANCEL_OPTION);
		if (result2 != 0) {
			return;
		}

		Entity e = null;
		for (int x = 0; x < classes.getEntities().size(); ++x) {
			if (finaleList.getSelectedItem() == classes.getEntities().get(x).getName()) {
				e = classes.getEntities().get(x);
			}
		}
		Method m = null;
		for (int x = 0; x < e.getMethods().size(); ++x) {
			if (methodList.getSelectedItem() == e.getMethods().get(x).getName()) {
				m = e.getMethods().get(x);
			}
		}
		String[] p = new String[m.getParameters().size()];
		for (int x = 0; x < m.getParameters().size(); ++x) {
			p[x] = m.getParameters().get(x).getName();
		}

		JComboBox<String> parameterList = new JComboBox<String>(p);
		JTextField name = new JTextField(20);

		JPanel inputFields3 = new JPanel();
		inputFields3.add(new JLabel("Choose a Parameter:"));
		inputFields3.add(parameterList);
		enterInput("Enter a Parameter name: ", inputFields2, name);

		// Ask for input with inputFields
		int result3 = JOptionPane.showConfirmDialog(null, inputFields3, "Rename Parameter",
				JOptionPane.OK_CANCEL_OPTION);

		if (result3 == 0) {
			String entityname = list.getSelectedItem().toString();
			classes.renameParameter(finaleList.getSelectedItem().toString(), methodList.getSelectedItem().toString(),
					parameterList.getSelectedItem().toString(), name.getText());
			View.updateClass(entityname, classes.copyEntity(entityname));
		}
	}

	public static void deleteParameter(Classes classes, JComboBox<String> list, JFrame frame) {
		// Create a window asking for which class to choose from
		JComboBox<String> finaleList = inputClass(classes.getEntities(), frame, "Method", true, "Delete Parameter");
		if (finaleList == null) {
			return;
		}
		// Create Drop-down box of existing classes
		JComboBox<String> methodList = findStuff(classes, finaleList.getSelectedItem().toString(), "Method", true);

		// Create a panel containing a drop-down box and text field
		JPanel inputFields2 = new JPanel();
		inputFields2.add(new JLabel("Choose a Method: "));
		inputFields2.add(methodList);

		// Ask for input with inputFields
		int result2 = JOptionPane.showConfirmDialog(null, inputFields2, "Delete Parameter",
				JOptionPane.OK_CANCEL_OPTION);
		if (result2 != 0) {
			return;
		}

		Entity e = null;
		for (int x = 0; x < classes.getEntities().size(); ++x) {
			if (finaleList.getSelectedItem() == classes.getEntities().get(x).getName()) {
				e = classes.getEntities().get(x);
			}
		}
		Method m = null;
		for (int x = 0; x < e.getMethods().size(); ++x) {
			if (methodList.getSelectedItem() == e.getMethods().get(x).getName()) {
				m = e.getMethods().get(x);
			}
		}
		String[] p = new String[m.getParameters().size()];
		for (int x = 0; x < m.getParameters().size(); ++x) {
			p[x] = m.getParameters().get(x).getName();
		}

		JComboBox<String> parameterList = new JComboBox<String>(p);

		JPanel inputFields3 = new JPanel();
		inputFields3.add(new JLabel("Choose a Parameter to Delete:"));
		inputFields3.add(parameterList);

		// Ask for input with inputFields
		int result3 = JOptionPane.showConfirmDialog(null, inputFields3, "Delete Parameter",
				JOptionPane.OK_CANCEL_OPTION);

		if (result3 == 0) {
			String entityname = list.getSelectedItem().toString();
			classes.deleteParameter(finaleList.getSelectedItem().toString(), methodList.getSelectedItem().toString(),
					parameterList.getSelectedItem().toString());
			View.updateClass(entityname, classes.copyEntity(entityname));
		}
	}

	private static JComboBox<String> inputClass(ArrayList<Entity> entities, JFrame frame, String type, Boolean opt,
			String function) {
		// Create a panel containing a drop-down box and text field
		JComboBox<String> finaleList = findUsefulClasses(entities, frame, type, opt);
		if (finaleList == null) {
			return null;
		}
		JPanel inputFields = new JPanel();
		inputFields.add(new JLabel("Choose a Class: "));
		inputFields.add(finaleList);

		// Ask for input with inputFields
		int result = JOptionPane.showConfirmDialog(null, inputFields, function, JOptionPane.OK_CANCEL_OPTION);
		if (result != 0) {
			return null;
		}
		return finaleList;
	}

	// Finds fields/methods that are in a specific class (if opt == true, then it
	// only finds methods with parameters)
	private static JComboBox<String> findStuff(Classes test, String inEntity, String type, Boolean opt) {
		// Create Drop-down box of existing classes
		ArrayList<Entity> entities2 = test.getEntities();
		String[] attributeStrings = null;
		for (int x = 0; x < entities2.size(); ++x) {
			if (entities2.get(x).getName() == inEntity) {
				Entity entFound = entities2.get(x);

				if (type == "Field") {
					attributeStrings = new String[entFound.getFields().size()];
					for (int y = 0; y < entFound.getFields().size(); ++y) {
						attributeStrings[y] = entFound.getFields().get(y).getName();
					}
				} else if (type == "Method") {

					if (opt == true) {
						int amount = 0;
						for (int y = 0; y < entFound.getMethods().size(); ++y) {
							if (entFound.getMethods().get(y).getParameters().size() != 0) {
								amount++;
							}
						}
						attributeStrings = new String[amount];
						for (int y = 0; y < entFound.getMethods().size(); ++y) {
							if (entFound.getMethods().get(y).getParameters().size() != 0) {
								attributeStrings[y] = entFound.getMethods().get(y).getName();
							}
						}
					} else if (opt == false) {
						attributeStrings = new String[entFound.getMethods().size()];
						for (int y = 0; y < entFound.getMethods().size(); ++y) {
							attributeStrings[y] = entFound.getMethods().get(y).getName();
						}
					}
				}
			}
		}

		JComboBox<String> list = new JComboBox<>(attributeStrings);
		return list;
	}

	// Finds classes that have fields/methods in them
	private static JComboBox<String> findUsefulClasses(ArrayList<Entity> entities, JFrame frame, String type,
			Boolean opt) {
		// Create Drop-down box of existing classes
		ArrayList<Entity> finale = new ArrayList<Entity>();
		if (type == "Field") {
			for (int x = 0; x < entities.size(); ++x) {
				Entity re = entities.get(x);

				if (re.getFields().size() != 0) {
					finale.add(re);
				}
			}
		} else if (type == "Method" && opt == false) {
			for (int x = 0; x < entities.size(); ++x) {
				Entity re = entities.get(x);
				if (re.getMethods().size() != 0) {
					finale.add(re);
				}
			}
		} else if (type == "Method" && opt == true) {
			for (int x = 0; x < entities.size(); ++x) {
				Entity re = entities.get(x);
				if (re.getMethods().size() != 0 && re.getMethods().get(x).getParameters().size() != 0) {
					finale.add(re);
				}
			}
		}

		if (finale.size() == 0) {
			if (type.equals("Field")) {
				JOptionPane.showMessageDialog(frame, "There are no fields!");
			} else if (type.equals("Method")) {
				JOptionPane.showMessageDialog(frame, "There are no methods!");
			}

			return null;
		}

		String[] entityStrings3 = new String[finale.size()];
		for (int x = 0; x < finale.size(); ++x) {
			entityStrings3[x] = finale.get(x).getName();
		}
		JComboBox<String> finaleList = new JComboBox<>(entityStrings3);

		return finaleList;
	}

	public static void enterInput(String ask, JPanel inputFields, JTextField box) {
		inputFields.add(Box.createHorizontalStrut(15)); // a spacer
		inputFields.add(new JLabel(ask));
		inputFields.add(box);
	}

	// Main load function. Calls loadClasses and loadRelationships
	public static void load(String userPath, Classes userClasses)
			throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException {

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
	// and inserts them to the currently running program
	private static void loadClasses(Classes userClasses, JSONObject javaObj, JSONArray list,
			ArrayList<JSONObject> objList) {
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

				userClasses.createField(className, fieldName, fieldType);
			}

			// Extract all methods of associated class, add to loadFile
			JSONArray classMethods = (JSONArray) objList.get(x).get("methods");
			for (int y = 0; y < classMethods.size(); ++y) {
				JSONObject method = (JSONObject) classMethods.get(y);
				String methodName = method.get("methodName").toString();
				String methodType = method.get("methodType").toString();

				userClasses.createMethod(className, methodName, methodType);

				JSONArray methodParam = (JSONArray) method.get("Parameters");
				for (int z = 0; z < methodParam.size(); ++z) {
					JSONObject param = (JSONObject) methodParam.get(z);
					String paramName = param.get("paramName").toString();
					String paramType = param.get("paramType").toString();

					userClasses.createParameter(className, methodName, paramName, paramType);
				}
			}

		}
	}

	// Takes all of the information from the relationships in the JSON file,
	// and inserts them into the currently running program
	private static void loadRelationships(Classes userClasses, JSONObject javaObj, JSONArray list,
			ArrayList<JSONObject> objList) {
		// Add relationship and class names from JSONArray list to loadFile
		for (int x = 0; x < list.size(); ++x) {

			// Get relationship from JSONArray list, add to ArrayList objList (type
			// conversions)
			Object entity = list.get(x);
			objList.add((JSONObject) entity);

			// Extract name of relationship and classes, add to loadFile
			Type relationName = (Type) objList.get(x).get("relationName");
			String classOne = (String) objList.get(x).get("ClassOne");
			String classTwo = (String) objList.get(x).get("ClassTwo");
			userClasses.createRelationship(relationName, classOne, classTwo);
		}
	}

	// Main save function. Calls saveClasses and saveRelationships
	public static void save(String filename, String directory, Classes userClasses) throws IOException {
		File newFile = new File(directory);
		// If directory given does not exist, put in user.dir
		if (!newFile.isDirectory()) {
			directory = System.getProperty("user.dir");
			System.out.println("Directory does not exist.");
		}

		JSONObject saveFile = new JSONObject();
		saveFile = saveClasses(saveFile, userClasses);
		saveFile = saveRelationships(saveFile, userClasses);

		directory += ("\\" + filename);
		writeFile(saveFile, directory);
		System.out.println("File saved to:" + directory);
	}

	// Creates a JSONObject for the classes and saves it to the saveFile
	private static JSONObject saveClasses(JSONObject saveFile, Classes userClasses) {
		JSONArray allClasses = new JSONArray();

		for (Entity entity : userClasses.getEntities()) {
			JSONObject singleClass = new JSONObject();

			singleClass.put("className", entity.getName());

			// Create an array of fields for the class
			JSONArray fields = new JSONArray();
			for (Field field : entity.getFields()) {
				JSONObject oneField = new JSONObject();
				oneField.put("fieldType", field.getType());
				oneField.put("fieldName", field.getName());
				fields.add(oneField);
			}
			singleClass.put("fields", fields);

			// Create an array of methods for the class
			JSONArray methods = new JSONArray();
			for (Method method : entity.getMethods()) {
				JSONObject oneMethod = new JSONObject();
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

			// Add class with it's name and attributes to the JSONArray allClasses
			allClasses.add(singleClass);
		}
		saveFile.put("Classes", allClasses);

		return saveFile;
	}

	// Creates a JSONObject for the relationships and saves it to the saveFile
	private static JSONObject saveRelationships(JSONObject saveFile, Classes userClasses) {
		JSONArray allRelationships = new JSONArray();

		for (Relationship relation : userClasses.getRelationships()) {
			// Create JSONObject for a single relationship
			JSONObject relationObj = new JSONObject();

			relationObj.put("relationName", relation.getName());
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
	private static void writeFile(JSONObject saveFile, String directory) throws IOException {
		File fileDirectory = new File(directory);
		fileDirectory.createNewFile();

		// Converts JSONObject and adds it to file
		String fullJSONString = saveFile.toString();
		FileWriter myWriter = new FileWriter(fileDirectory);
		myWriter.write(fullJSONString);
		myWriter.close();
	}

	// Lists all of the classes and their respective attributes
	public static void listClasses(Classes userClasses) {
		System.out.println("Classes:");
		for (Entity curEntity : userClasses.getEntities()) {
			System.out.println("	" + curEntity.getName() + ":");
			System.out.print("		fields:  [ ");

			// Prints out all of the fields
			ArrayList<Field> fields = curEntity.getFields();
			for (int x = 0; x < fields.size(); x++) {

				System.out.print("(" + fields.get(x).getType() + ") " + fields.get(x).getName());
				if (x != fields.size() - 1) {
					System.out.print(", ");
				}
			}
			System.out.println(" ]");
			// Prints out all of the methods
			System.out.print("		methods: [ ");
			for (int x = 0; x < curEntity.getMethods().size(); x++) {
				Method curMethod = curEntity.getMethods().get(x);
				System.out.print("(" + curMethod.getType() + ") " + curMethod.getName() + " -- {");

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
	public static void listRelationships(Classes userClasses) {
		System.out.println("Relationships:");
		for (Relationship relation : userClasses.getRelationships()) {
			System.out.println(
					"   -- " + relation.getName() + ": " + relation.getFirstClass() + "--" + relation.getSecondClass());
		}
	}
}

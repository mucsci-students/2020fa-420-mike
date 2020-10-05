package mike.GUI;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cli.HelperMethods;
import mike.datastructures.Classes;
import mike.datastructures.Entity;
import mike.datastructures.Method;
import mike.datastructures.Relationship.Type;
import GUI.view;

public class Controller extends HelperMethods{

	private static Classes userClasses;

	public Controller (Classes userClasses){
		this.userClasses = userClasses;
	}
   
	// Listen to any function calls
	public void saveListener(JButton save) {
		save.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  String directory = System.getProperty("user.dir");
			  try {
				  save("uml.json", directory, userClasses);
			  }  catch (IOException e1) {
				  e1.printStackTrace();
			  }
		  }
		});
	}
	
	public void saveAsListener(JButton saveAs) {
		saveAs.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  String directory = System.getProperty("user.dir");
			  try {	
				  save("uml.json", directory, userClasses);
			  }  catch (IOException e1) {
				  e1.printStackTrace();
			  }
		  }
		});
	}

	// Listen to any function calls
	public void loadListener(JButton load) {
		load.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  String directory = System.getProperty("user.dir");
			  try {
				  directory += "\\uml.json";
				   load(directory, userClasses);
			  }  catch (Exception e1) {
				  e1.printStackTrace();
			  }
		  }
		});
	}
	
	// Listen to any function calls
	public void treeListener(JTree tree, JFrame frame, HashMap<String, JLabel> entityLabels, JPanel centerPanel) {
		tree.addTreeSelectionListener(new TreeSelectionListener() {
		    public void valueChanged(TreeSelectionEvent e) {
		        functionCalled(tree, userClasses, frame, entityLabels, centerPanel);
		    }
		});
	}
	
	public static void functionCalled(JTree tree, Classes test, JFrame frame, HashMap<String, JLabel> entityLabels, JPanel centerPanel) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        
        /* if nothing is selected */ 
        if (node == null) {
        	return;
        }
        
        tree.clearSelection();
        // Create Drop-down box of existing classes
    	ArrayList<Entity> entities = test.getEntities();
    	String[] entityStrings = new String[entities.size()];
    	for(int x = 0; x < entities.size(); ++x) {
    		entityStrings[x] = entities.get(x).getName();
    	}
    	JComboBox<String> list = new JComboBox<>(entityStrings);
    	
        /* React to the node selection. */
        if (node.toString() == "Create Class") {
        	JTextField name = new JTextField(20);
        	
        	// Create a panel containing a drop-down box and text field
        	JPanel inputFields = new JPanel();
        	inputFields.add(new JLabel("Enter a Class name: "));
        	inputFields.add(name);
        	
        	// Ask for input with inputFields
            int result = JOptionPane.showConfirmDialog(null, inputFields, 
                    "Create Class", JOptionPane.OK_CANCEL_OPTION);
        	
        	if(result == 0) {
        		test.createClass(name.getText());
        		view.showClass(userClasses.copyEntity(name.getText()));  
        	}
        }
        else if (node.toString() == "Rename Class"){
        	JTextField rename = new JTextField(20);
        	
        	// Create a panel containing a drop-down box and text field
        	JPanel inputFields = new JPanel();
        	inputFields.add(new JLabel("Choose a Class: "));
        	inputFields.add(list);
        	enterInput("Rename Class: ", inputFields, rename);	
        	
        	// Ask for input with inputFields
            int result = JOptionPane.showConfirmDialog(null, inputFields, 
                    "Rename Class", JOptionPane.OK_CANCEL_OPTION);
        
        	if (result == 0) {
        		test.renameClass(list.getSelectedItem().toString(), rename.getText());
        		JLabel classObj = entityLabels.remove(list.getSelectedItem());
            	classObj.setText(rename.getText());
            	entityLabels.put(rename.getText(), classObj);	
        	}
        	
        }
        else if (node.toString() == "Delete Class"){
        	// Create a panel containing a drop-down box
        	JPanel inputFields = new JPanel();
        	inputFields.add(list);
        	
        	// Ask for input with inputFields
            int result = JOptionPane.showConfirmDialog(null, inputFields, 
                    "Delete Class", JOptionPane.OK_CANCEL_OPTION);
        
        	if (result == 0) {
        		test.deleteClass(list.getSelectedItem().toString());
        		JLabel classObj = entityLabels.get(list.getSelectedItem());
        		centerPanel.remove(classObj);
        		centerPanel.validate();
        		centerPanel.repaint();
        	}	
        }
        else if (node.toString() == "Create Relationship" || node.toString() == "Delete Relationship"){
        	JRadioButton composition = new JRadioButton("Composition");
        	composition.setActionCommand("a");
        	composition.setSelected(true);

            JRadioButton aggregation = new JRadioButton("Aggregation");
            aggregation.setActionCommand("b");

            JRadioButton association = new JRadioButton("Association");
            association.setActionCommand("c");

            JRadioButton inheritance = new JRadioButton("Inheritance");
            inheritance.setActionCommand("d");
        	
            //Group the radio buttons.
            ButtonGroup group = new ButtonGroup();
            group.add(composition);
            group.add(aggregation);
            group.add(association);
            group.add(inheritance);
            
        	// Create a panel containing a drop-down box
        	JPanel inputFields = new JPanel();
        	inputFields.add(new JLabel("Choose the Relationship Type: "));
        	inputFields.add(composition);
        	inputFields.add(aggregation);
        	inputFields.add(association);
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
        	if(node.toString() == "Create Relationship"){
        		result = JOptionPane.showConfirmDialog(null, inputFields, 
                        "Create Relationship", JOptionPane.OK_CANCEL_OPTION);
        	}
        	else if (node.toString() == "Delete Relationship") {
        		result = JOptionPane.showConfirmDialog(null, inputFields, 
                        "Delete Relationship", JOptionPane.OK_CANCEL_OPTION);
        	}
            
            Type name;
            if(composition.isSelected()){
            	name = Type.COMPOSITION;
            }
            else if(aggregation.isSelected()){
            	name = Type.AGGREGATION;
            }
            else if(association.isSelected()){
            	name = Type.ASSOCIATION;
            }
            else {
            	name = Type.INHERITANCE;
            }
            
            
        	if (result == 0) {
        		if(node.toString() == "Create Relationship") {
        			test.createRelationship(name, list.getSelectedItem().toString(), list2.getSelectedItem().toString());        			
        		}
        		else {
        			test.deleteRelationship(name, list.getSelectedItem().toString(), list2.getSelectedItem().toString());
        		}
        	}	
        }
        else if (node.toString() == "Create Field"){
        	JTextField type = new JTextField(20);
        	JTextField name = new JTextField(20);
        	
        	// Create a panel containing a drop-down box and text field
        	JPanel inputFields = new JPanel();
        	inputFields.add(new JLabel("Choose a Class: "));
        	inputFields.add(list);
        	enterInput("Enter a field type: ", inputFields, type);
        	enterInput("Enter a field name: ", inputFields, name);
        	
        	// Ask for input with inputFields
            int result = JOptionPane.showConfirmDialog(null, inputFields, 
                    "Create Field", JOptionPane.OK_CANCEL_OPTION);
        
        	if (result == 0) {
        		test.createField(list.getSelectedItem().toString(), name.getText(), type.getText());
        	}
        }
        else if (node.toString() == "Rename Field"){
        	// Create a window asking for which class to choose from
        	JComboBox<String> finaleList = inputClass(entities, frame, "fields", false, "Rename Field");
        	if (finaleList == null) {
        		return;
        	}
        	// Create Drop-down box of existing classes
        	JComboBox<String> fieldList = findStuff(test, finaleList.getSelectedItem().toString(), "fields", false);
        	JTextField rename = new JTextField(20);
        	
        	// Create a panel containing a drop-down box and text field
        	JPanel inputFields2 = new JPanel();
        	inputFields2.add(new JLabel("Choose a field: "));
        	inputFields2.add(fieldList);
        	enterInput("Enter a new field name: ", inputFields2, rename);
        	
        	// Ask for input with inputFields
            int result2 = JOptionPane.showConfirmDialog(null, inputFields2, 
                    "Rename Field", JOptionPane.OK_CANCEL_OPTION);
        
        	if (result2 == 0) {
        		test.renameField(list.getSelectedItem().toString(), fieldList.getSelectedItem().toString(), rename.getText());
        	}
        }
        else if (node.toString() == "Delete Field"){  
        	// Create a window asking for which class to choose from
        	JComboBox<String> finaleList = inputClass(entities, frame, "fields", false, "Delete Field");
        	if (finaleList == null) {
        		return;
        	}
        	// Create Drop-down box of existing classes
        	JComboBox<String> fieldList = findStuff(test, finaleList.getSelectedItem().toString(), "fields", false);
        	
        	// Create a panel containing a drop-down box and text field
        	JPanel inputFields2 = new JPanel();
        	inputFields2.add(new JLabel("Choose a field: "));
        	inputFields2.add(fieldList);
        
        	// Ask for input with inputFields
            int result2 = JOptionPane.showConfirmDialog(null, inputFields2, 
                    "Delete Field", JOptionPane.OK_CANCEL_OPTION);
        
        	if (result2 == 0) {
        		test.deleteField(list.getSelectedItem().toString(), fieldList.getSelectedItem().toString());
        	}
        }
        else if (node.toString() == "Create Method"){
        	JTextField type = new JTextField(20);
        	JTextField name = new JTextField(20);
        	
        	// Create a panel containing a drop-down box and text field
        	JPanel inputFields = new JPanel();
        	inputFields.add(new JLabel("Choose a Class: "));
        	inputFields.add(list);
        	enterInput("Enter a Method Type: ", inputFields, type);
        	enterInput("Enter a Method name", inputFields, name);
        	
        	// Ask for input with inputFields
            int result = JOptionPane.showConfirmDialog(null, inputFields, 
                    "Create Method", JOptionPane.OK_CANCEL_OPTION);
        
        	if (result == 0) {
        		test.createMethod(list.getSelectedItem().toString(), name.getText(), type.getText());
        	}
        }
        else if (node.toString() == "Rename Method"){
        	// Create a window asking for which class to choose from
        	JComboBox<String> finaleList = inputClass(entities, frame, "methods", false, "Rename Method");
        	if (finaleList == null) {
        		return;
        	}
        	// Create Drop-down box of existing classes
        	JComboBox<String> methodList = findStuff(test, finaleList.getSelectedItem().toString(), "methods", false);
        	JTextField rename = new JTextField(20);
        	
        	// Create a panel containing a drop-down box and text field
        	JPanel inputFields2 = new JPanel();
        	inputFields2.add(new JLabel("Choose a Method: "));
        	inputFields2.add(methodList);
        	enterInput("Enter a new Method name: ", inputFields2, rename);
        	
        	// Ask for input with inputFields
            int result2 = JOptionPane.showConfirmDialog(null, inputFields2, 
                    "Rename Method", JOptionPane.OK_CANCEL_OPTION);
        
        	if (result2 == 0) {
        		test.renameMethod(list.getSelectedItem().toString(), methodList.getSelectedItem().toString(), rename.getText());
        	}
        }
        else if (node.toString() == "Delete Method"){
        	// Create a window asking for which class to choose from
        	JComboBox<String> finaleList = inputClass(entities, frame, "methods", false, "Delete Method");
        	if (finaleList == null) {
        		return;
        	}
        	// Create Drop-down box of existing classes
        	JComboBox<String> methodList = findStuff(test, finaleList.getSelectedItem().toString(), "methods", false);
        	
        	// Create a panel containing a drop-down box and text field
        	JPanel inputFields2 = new JPanel();
        	inputFields2.add(new JLabel("Choose a Method: "));
        	inputFields2.add(methodList);
        
        	// Ask for input with inputFields
            int result2 = JOptionPane.showConfirmDialog(null, inputFields2, 
                    "Delete Method", JOptionPane.OK_CANCEL_OPTION);
        
        	if (result2 == 0) {
        		test.deleteMethod(list.getSelectedItem().toString(), methodList.getSelectedItem().toString());
        	}
        }
        else if (node.toString() == "Create Parameter"){
        	// Create a window asking for which class to choose from
        	JComboBox<String> finaleList = inputClass(entities, frame, "methods", false, "Create Parameter");
        	if (finaleList == null) {
        		return;
        	}
        	// Create Drop-down box of existing classes
        	JComboBox<String> methodList = findStuff(test, finaleList.getSelectedItem().toString(), "methods", false);
        	JTextField type = new JTextField(20);
        	JTextField name = new JTextField(20);
        	
        	// Create a panel containing a drop-down box and text field
        	JPanel inputFields2 = new JPanel();
        	inputFields2.add(new JLabel("Choose a Method: "));
        	inputFields2.add(methodList);
        	enterInput("Enter a Parameter Type: ", inputFields2, type);
        	enterInput("Enter a Parameter Name: ", inputFields2, name);
        	
        	// Ask for input with inputFields
            int result2 = JOptionPane.showConfirmDialog(null, inputFields2, 
                    "Create Parameter", JOptionPane.OK_CANCEL_OPTION);
        
        	if (result2 == 0) {       		
        		test.createParameter(finaleList.getSelectedItem().toString(), methodList.getSelectedItem().toString(), name.getText(), type.getText());
        	}
        }
        else if (node.toString() == "Rename Parameter"){
        	// Create a window asking for which class to choose from
        	JComboBox<String> finaleList = inputClass(entities, frame, "methods", true, "Rename Parameter");
        	if (finaleList == null) {
        		return;
        	}
        	// Create Drop-down box of existing classes
        	JComboBox<String> methodList = findStuff(test, finaleList.getSelectedItem().toString(), "methods", true);
        	
        	// Create a panel containing a drop-down box and text field
        	JPanel inputFields2 = new JPanel();
        	inputFields2.add(new JLabel("Choose a Method: "));
        	inputFields2.add(methodList);
        	
        	// Ask for input with inputFields
            int result2 = JOptionPane.showConfirmDialog(null, inputFields2, 
                    "Rename Parameter", JOptionPane.OK_CANCEL_OPTION);
            if (result2 != 0) {
        		return;
        	}
        	
            Entity e = null;
            for(int x = 0; x < test.getEntities().size(); ++x){
            	if (finaleList.getSelectedItem() == test.getEntities().get(x).getName()) {
            		e = test.getEntities().get(x);
            	}
            }
            Method m = null;
            for(int x = 0; x < e.getMethods().size(); ++x){
            	if (methodList.getSelectedItem() == e.getMethods().get(x).getName()) {
            		m = e.getMethods().get(x);
            	}
            }
            String[] p = new String[m.getParameters().size()];
            for(int x = 0; x < m.getParameters().size(); ++x) {
            	p[x] = m.getParameters().get(x).getName();
            }
			
            JComboBox<String> parameterList = new JComboBox<String>(p);
        	JTextField name = new JTextField(20);
        	
        	JPanel inputFields3 = new JPanel();
        	inputFields3.add(new JLabel("Choose a Parameter:"));
        	inputFields3.add(parameterList);
        	enterInput("Enter a Parameter name: ", inputFields2, name);
        	
        	// Ask for input with inputFields
            int result3 = JOptionPane.showConfirmDialog(null, inputFields3, 
                    "Rename Parameter", JOptionPane.OK_CANCEL_OPTION);
        
        	if (result3 == 0) {       		
        		test.renameParameter(finaleList.getSelectedItem().toString(), methodList.getSelectedItem().toString(), parameterList.getSelectedItem().toString(), name.getText());
        	}
        }
        else if (node.toString() == "Delete Parameter"){        	
        	// Create a window asking for which class to choose from
        	JComboBox<String> finaleList = inputClass(entities, frame, "methods", true, "Delete Parameter");
        	if (finaleList == null) {
        		return;
        	}
        	// Create Drop-down box of existing classes
        	JComboBox<String> methodList = findStuff(test, finaleList.getSelectedItem().toString(), "methods", true);
        	
        	// Create a panel containing a drop-down box and text field
        	JPanel inputFields2 = new JPanel();
        	inputFields2.add(new JLabel("Choose a Method: "));
        	inputFields2.add(methodList);
        	
        	// Ask for input with inputFields
            int result2 = JOptionPane.showConfirmDialog(null, inputFields2, 
                    "Delete Parameter", JOptionPane.OK_CANCEL_OPTION);
            if (result2 != 0) {
        		return;
        	}
        	
            Entity e = null;
            for(int x = 0; x < test.getEntities().size(); ++x){
            	if (finaleList.getSelectedItem() == test.getEntities().get(x).getName()) {
            		e = test.getEntities().get(x);
            	}
            }
            Method m = null;
            for(int x = 0; x < e.getMethods().size(); ++x){
            	if (methodList.getSelectedItem() == e.getMethods().get(x).getName()) {
            		m = e.getMethods().get(x);
            	}
            }
            String[] p = new String[m.getParameters().size()];
            for(int x = 0; x < m.getParameters().size(); ++x) {
            	p[x] = m.getParameters().get(x).getName();
            }
			
            JComboBox<String> parameterList = new JComboBox<String>(p);
        	
        	JPanel inputFields3 = new JPanel();
        	inputFields3.add(new JLabel("Choose a Parameter to Delete:"));
        	inputFields3.add(parameterList);
        	
        	// Ask for input with inputFields
            int result3 = JOptionPane.showConfirmDialog(null, inputFields3, 
                    "Delete Parameter", JOptionPane.OK_CANCEL_OPTION);
        
        	if (result3 == 0) {       		
        		test.deleteParameter(finaleList.getSelectedItem().toString(), methodList.getSelectedItem().toString(), parameterList.getSelectedItem().toString());
        	}
        }
        else if (node.toString() == "list all"){        	
        	System.out.println();							
			listClasses(test);
			System.out.println();
			listRelationships(test);
			System.out.println();
        }
        
    }
 
 	private static JComboBox<String> inputClass(ArrayList<Entity> entities, JFrame frame, String type, Boolean opt, String function) {
 		// Create a panel containing a drop-down box and text field
    	JComboBox<String> finaleList = findUsefulClasses(entities, frame, type, opt);
    	if(finaleList == null) {
    		return null;
    	}
    	JPanel inputFields = new JPanel();
    	inputFields.add(new JLabel("Choose a Class: "));
    	inputFields.add(finaleList);
    	
    	// Ask for input with inputFields
        int result = JOptionPane.showConfirmDialog(null, inputFields, 
                function, JOptionPane.OK_CANCEL_OPTION);
    	if (result != 0) {
    		return null;
    	}
    	return finaleList;
 	}
 	
 	// Finds fields/methods that are in a specific class (if opt == true, then it only finds methods with parameters)
 	private static JComboBox<String> findStuff(Classes test, String inEntity, String type, Boolean opt) {
 		// Create Drop-down box of existing classes
    	ArrayList<Entity> entities2 = test.getEntities();
    	String[] attributeStrings = null;
    	for(int x = 0; x < entities2.size(); ++x) {
    		if(entities2.get(x).getName() == inEntity) {
    			Entity entFound = entities2.get(x);
    			
    			if(type == "fields") {
	    			attributeStrings = new String[entFound.getFields().size()];
	    			for(int y = 0; y < entFound.getFields().size(); ++y) {
	    				attributeStrings[y] = entFound.getFields().get(y).getName();
	    			}
    			}
    			else if (type == "methods") {
    				
    				if(opt == true) {
    					int amount = 0;
    					for(int y = 0; y < entFound.getMethods().size(); ++y) {
		    				if(entFound.getMethods().get(y).getParameters().size() != 0) {
		    					amount++;
		    				}
    					}
    					attributeStrings = new String[amount];
    					for(int y = 0; y < entFound.getMethods().size(); ++y) {
		    				if(entFound.getMethods().get(y).getParameters().size() != 0) {
		    					attributeStrings[y] = entFound.getMethods().get(y).getName();
		    				}
    					}
    				}
					else if (opt == false){
						attributeStrings = new String[entFound.getMethods().size()];
						for(int y = 0; y < entFound.getMethods().size(); ++y) {
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
 	private static JComboBox<String> findUsefulClasses (ArrayList<Entity> entities, JFrame frame, String type, Boolean opt) {
 		// Create Drop-down box of existing classes
    	ArrayList<Entity> finale = new ArrayList<Entity>();
    	if(type == "fields") {
    		for(int x = 0; x < entities.size(); ++x) {
        		Entity re = entities.get(x);
        		
        		if(re.getFields().size() != 0) {
        			finale.add(re);
        		}
        	}
    	}
    	else if (type == "methods" && opt == false) {
    		for(int x = 0; x < entities.size(); ++x) {
	    		Entity re = entities.get(x);
	    		if(re.getMethods().size() != 0) {
	    			finale.add(re);
	    		}
    		}
    	}
    	else if (type == "methods" && opt == true) {
    		for(int x = 0; x < entities.size(); ++x) {
	    		Entity re = entities.get(x);
	    		if(re.getMethods().size() != 0 && re.getMethods().get(x).getParameters().size() != 0) {
	    			finale.add(re);
	    		}
    		}
    	}
    	
    	if(finale.size() == 0){
    		if(type == "fields") {
    			JOptionPane.showMessageDialog(frame, "There are no fields!");
    		}
    		else if(type == "methods") {
    			JOptionPane.showMessageDialog(frame, "There are no methods!");
    		}
    		
    		return null;
    	}
    	
    	String[] entityStrings3 = new String[finale.size()];
    	for(int x = 0; x < finale.size(); ++x) {
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
}

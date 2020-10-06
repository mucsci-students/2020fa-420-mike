package mike.gui;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import mike.datastructures.Classes;
import mike.datastructures.Entity;

import mike.gui.guiHelperMethods;

public class Controller extends guiHelperMethods {

	private static Classes userClasses;

	public Controller (Classes userClasses){
		Controller.userClasses = userClasses;
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
	
	public static void functionCalled(JTree tree, Classes classes, JFrame frame, HashMap<String, JLabel> entityLabels, JPanel centerPanel) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		
		/* if nothing is selected */ 
		if (node == null) {
			return;
		}
		
		tree.clearSelection();
		// Create Drop-down box of existing classes
		ArrayList<Entity> entities = classes.getEntities();
		String[] entityStrings = new String[entities.size()];
		for(int x = 0; x < entities.size(); ++x) {
			entityStrings[x] = entities.get(x).getName();
		}
		JComboBox<String> list = new JComboBox<>(entityStrings);
		
		
		/* React to the node selection. */
		switch(node.toString()) {
			case "Create Class" : createClass(classes, userClasses); break;
			case "Rename Class" : renameClass(classes, list); break;
			case "Delete Class" : deleteClass(classes, list); break;
			case "Create Relationship" : createOrDeleteRelation(classes, list, node.toString(), entityStrings); break;
			case "Delete Relationship" : createOrDeleteRelation(classes, list, node.toString(), entityStrings); break;
			case "Create Field" : createFieldsOrMethods(classes, list, "Field"); break;
			case "Rename Field" : renameFieldsOrMethods(classes, list, frame, "Field"); break;
			case "Delete Field" : deleteFieldsOrMethods(classes, list, frame, "Field"); break;
			case "Create Method" : createFieldsOrMethods(classes, list, "Method"); break;
			case "Rename Method" : renameFieldsOrMethods(classes, list, frame, "Method"); break;
			case "Delete Method" : deleteFieldsOrMethods(classes, list, frame, "Method"); break;
			case "Create Parameter" : createParameter(classes, list, frame); break;
			case "Rename Parameter" : renameParameter(classes, list, frame); break;
			case "Delete Parameter" : deleteParameter(classes, list, frame); break;
			default : 
		}
   
		if (node.toString() == "list all"){        	
			System.out.println();							
			listClasses(classes);
			System.out.println();
			listRelationships(classes);
			System.out.println();
		}
		
	}
 
}

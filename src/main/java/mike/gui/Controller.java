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

public class Controller extends guiHelperMethods {

	private static Classes classes;

	public Controller (Classes classes){
		Controller.classes = classes;
	}
   
	// Listen to any function calls
	public void saveListener(JButton save) {
		save.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  String directory = System.getProperty("user.dir");
			  try {
				  save("uml.json", directory, classes);
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
				  save("uml.json", directory, classes);
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
				   load(directory, classes);
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
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				
				/* if nothing is selected */ 
				if (node == null) {
					return;
				}
				
				tree.clearSelection();
				
				switch(node.toString())
				{
					case "Class" : return;
					case "Relationship" : return;
					case "Field" : return;
					case "Method" : return;
					case "Parameter" : return;
					case "list" : return;
					default: break;
				}
				
				// Create Drop-down box of existing classes
				ArrayList<Entity> entities = classes.getEntities();
				String[] entityStrings = new String[entities.size()];
				for(int x = 0; x < entities.size(); ++x) {
					entityStrings[x] = entities.get(x).getName();
				}
				JComboBox<String> list = new JComboBox<>(entityStrings);
				
				
				/* React to the node selection. */
				switch(node.toString()) {
					case "Create Class" : createClass(classes); break;
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
					case "list all" :        	
							System.out.println();							
							listClasses(classes);
							System.out.println();
							listRelationships(classes);
							System.out.println();
							break;
					default : throw new RuntimeException("Unknown button pressed");
				}
				for(Entity curEntity : entities) {
					JLabel curLabel = entityLabels.get(curEntity.getName());
					curLabel.setLocation(curEntity.getXLocation(), curEntity.getYLocation());				
				}
			}
		});
	}
}

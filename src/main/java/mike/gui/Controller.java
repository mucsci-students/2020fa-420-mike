package mike.gui;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import mike.datastructures.Classes;
import mike.datastructures.Entity;

public class Controller extends guiHelperMethods {

	private static Classes classes = new Classes();
	private static View view;
	private static Boolean changed = false;
	private static int x_pressed = 0;
	private static int y_pressed = 0;
	private static Path path = null;
	
	public Controller (View.InterfaceType viewtype){
		view = new View(viewtype);
	}
	
	public static void clickClass(JLabel newview) {
		newview.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// catching the current values for x,y coordinates on screen
				if (e.getSource() == newview) {
					x_pressed = e.getX();
					y_pressed = e.getY();
				}
			}
		});
	}
		
	
	public static void moveClass(JLabel newview, Entity entity) {
		newview.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (e.getSource() == newview) {
					JComponent jc = (JComponent) e.getSource();
					jc.setLocation(jc.getX() + e.getX() - x_pressed, jc.getY() + e.getY() - y_pressed);
					entity.setXLocation(jc.getX() + e.getX() - x_pressed);
					entity.setYLocation(jc.getY() + e.getY() - y_pressed);
					changed = true;
				}
			}
		});
	}
		
	public static void exitListener(JFrame frame) throws HeadlessException {
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter()
	    {
	        //@Override
	        public void windowClosing(WindowEvent e)
	        {	
	        	if(changed){
	        		int n = JOptionPane.showConfirmDialog(
					    frame,
					    "You have unsaved changes.  Do you still want to exit?",
					    "Exit",
					    JOptionPane.YES_NO_OPTION);
					// Yes, exit
					if(n == 0) {
						e.getWindow().dispose();
					}	 
	        	} else {
	        		e.getWindow().dispose();
	        	}
				           
	        }
	    });
	}
	
	// Listen to any function calls
	public static void saveListener(JButton save, JFrame frame) {
		save.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  try {
				  if(path == null){
					  saveWithInput(frame);
				  }
				  else{
					  save(path, classes);
					  changed = false;
				  }
			  }  catch (IOException e1) {
				  e1.printStackTrace();
			  }
		  }
		});
	}
	
	public static void saveAsListener(JButton saveAs, JFrame frame) {
		saveAs.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  saveWithInput(frame);
		  }
		});
	}
	
	private static void saveWithInput(JFrame frame) {
		try {	
		  JTextField fileName = new JTextField(20);
		  JTextField directory = new JTextField(40);
		  
		  // Create a panel containing a drop-down box and text field
		  JPanel inputFields = new JPanel();
	 	  inputFields.add(new JLabel("Enter a File name: "));
		  inputFields.add(fileName);
		  inputFields.add(new JLabel("Enter a Directory (optional): "));
		  inputFields.add(directory);
		  
		  // Ask for input with inputFields
		  int result = JOptionPane.showConfirmDialog(null, inputFields, "Save As", JOptionPane.OK_CANCEL_OPTION);
		  if (result == 0) {
			  File file = new File(directory.getText() + "\\" + fileName.getText());
			  if(file.isDirectory() && file.isAbsolute()){
				  path = Paths.get(file.toString());
			  } else {
				  path = Paths.get(System.getProperty("user.dir") + directory.getText() + "\\" + fileName.getText());
				  file = new File(path.getParent().toString());
				  if(!file.isDirectory()){  
					  JOptionPane.showMessageDialog(frame, "Directory does not exist.  File saved to uml directory.");
				  }
			  }
			      
			  save(path, classes);
			  changed = false;
		  }
		 
	  }  catch (IOException e1) {
		  e1.printStackTrace();
	  }
  }
  
	
	// Listen to any function calls
	public static void loadListener(JButton load, HashMap<String, JLabel> entityLabels, JPanel centerPanel, JFrame frame) {
		load.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  try {
				  if(changed == true) {
					  int n = JOptionPane.showConfirmDialog(
							    frame,
							    "You have unsaved changes.  Do you still want to load a new file?",
							    "Exit",
							    JOptionPane.YES_NO_OPTION);
						// No, go back
						if(n == 1) {
							return;
						}	
				  }
				  
				  JTextField directory = new JTextField(40);
				  
				  // Create a panel containing a drop-down box and text field
				  JPanel inputFields = new JPanel();
				  inputFields.add(new JLabel("Enter a Directory: "));
				  inputFields.add(directory);
				  
				  // Ask for input with inputFields
				  int result = JOptionPane.showConfirmDialog(null, inputFields, "Load", JOptionPane.OK_CANCEL_OPTION);

				  if (result == 0) {				  
					  classes.empty();
					  centerPanel.removeAll();
					  centerPanel.repaint();
					  
					  File file = new File(directory.getText());
					  if(file.isAbsolute()){
						  path = Paths.get(directory.getText());
					  }
					  else {
						  path = Paths.get(System.getProperty("user.dir") + "\\" + directory.getText());  
					  }
					  
					  load(path, classes);
					  changed = false;
					  centerPanel.validate();
				  }
				 
				  
			  }  catch (Exception e1) {
				  e1.printStackTrace();
			  }
			  for(Entity curEntity : classes.getEntities()) {
					JLabel curLabel = entityLabels.get(curEntity.getName());
					curLabel.setLocation(curEntity.getXLocation(), curEntity.getYLocation());				
			  }
		  }
		});
	}
	
	// Listen to any function calls
	public static void treeListener(JTree tree, JFrame frame, HashMap<String, JLabel> entityLabels, JPanel centerPanel) {
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
					case "Rename Class" : renameClass(classes, entityStrings, frame); break;
					case "Delete Class" : deleteClass(classes, entityStrings, frame); break;
					case "Create Relationship" : createRelation(classes,  entityStrings, frame); break;
					case "Delete Relationship" : deleteRelation(classes, entityStrings, frame); break;
					case "Create Field" : createFieldsOrMethods(classes, entityStrings, "Field", frame); break;
					case "Rename Field" : renameFieldsOrMethods(classes, list, frame, "Field"); break;
					case "Delete Field" : deleteFieldsOrMethods(classes, list, frame, "Field"); break;
					case "Create Method" : createFieldsOrMethods(classes, entityStrings, "Method", frame); break;
					case "Rename Method" : renameFieldsOrMethods(classes, list, frame, "Method"); break;
					case "Delete Method" : deleteFieldsOrMethods(classes, list, frame, "Method"); break;
					case "Create Parameter" : createParameter(classes, list, frame); break;
					case "Rename Parameter" : renameParameter(classes, list, frame); break;
					case "Delete Parameter" : deleteParameter(classes, list, frame); break;
					default : throw new RuntimeException("Unknown button pressed");
				}
				for(Entity curEntity : entities) {
					JLabel curLabel = entityLabels.get(curEntity.getName());
					curLabel.setLocation(curEntity.getXLocation(), curEntity.getYLocation());
				}
				changed = true;
			}
		});
	}

}
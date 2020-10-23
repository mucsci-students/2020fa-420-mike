package mike.controller;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.nio.file.Path;
import java.util.ArrayList;

import mike.datastructures.Model;
import mike.datastructures.Entity;
import mike.gui.guiHelperMethods;
import mike.view.GUIView;

public class Controller {

	private static Boolean changed = false;
	private static Path path = null;
	private static Model model = new Model();
	
	public Controller (){
	}
	
	public static void classControls(JLabel box, Entity e) {
		ClassController.clickClass(box);
		ClassController.moveClass(box, e);
	}
	
	public void frameControls (JButton save, JButton saveAs, JButton load, JButton addClass, JButton editMode) {
		SaveController.saveListener(save);
		SaveController.saveAsListener(saveAs);
		LoadController.loadListener(load);
		FrameController.addClassListener(addClass);
		ClassController.editModeListener(editMode);
		FrameController.exitListener();
		FrameController.resizeListener();
		
	}


	public static Path getPath() {
		return path;
	}
	
	public static Boolean getChanged() {
		return changed;
	}
	
	public static Model getModel() {
		return model;
	}
	
	public static void setChanged(Boolean yes) {
		changed = yes;
	}
	
	public static void setPath(Path yes) {
		path = yes;
	}
		
	// Listen to any function calls
	public static void treeListener(JTree tree) {
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
				ArrayList<Entity> entities = model.getEntities();
				String[] entityStrings = new String[entities.size()];
				for(int x = 0; x < entities.size(); ++x) {
					entityStrings[x] = entities.get(x).getName();
				}				
				
				/* React to the node selection. */
				JFrame frame = GUIView.getFrame();
				switch(node.toString()) {
					case "Create Class" : guiHelperMethods.createClass(model, frame); break;
					case "Rename Class" : guiHelperMethods.renameClass(model, entityStrings, frame); break;
					case "Delete Class" : guiHelperMethods.deleteClass(model, entityStrings, frame); break;
					case "Create Relationship" : guiHelperMethods.createRelation(model,  entityStrings, frame); break;
					case "Delete Relationship" : guiHelperMethods.deleteRelation(model, entityStrings, frame); break;
					case "Create Field" : guiHelperMethods.createFieldsOrMethods(model, entityStrings, "Field", frame); break;
					case "Rename Field" : guiHelperMethods.renameFieldsOrMethods(model, frame, "Field"); break;
					case "Delete Field" : guiHelperMethods.deleteFieldsOrMethods(model, frame, "Field"); break;
					case "Create Method" : guiHelperMethods.createFieldsOrMethods(model, entityStrings, "Method", frame); break;
					case "Rename Method" : guiHelperMethods.renameFieldsOrMethods(model, frame, "Method"); break;
					case "Delete Method" : guiHelperMethods.deleteFieldsOrMethods(model, frame, "Method"); break;
					case "Create Parameter" : guiHelperMethods.createParameter(model, frame); break;
					case "Rename Parameter" : guiHelperMethods.renameParameter(model, frame); break;
					case "Delete Parameter" : guiHelperMethods.deleteParameter(model, frame); break;
					default : throw new RuntimeException("Unknown button pressed");
				}
				for(Entity curEntity : entities) {
					JLabel curLabel = GUIView.getEntityLabels().get(curEntity.getName());
					curLabel.setLocation(curEntity.getXLocation(), curEntity.getYLocation());
				}
				changed = true;
			}
		});
	}
	
}

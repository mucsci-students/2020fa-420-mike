package mike.controller;

import java.nio.file.Path;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mike.datastructures.Model;
import mike.datastructures.Entity;

public class Controller {

	private static Boolean changed = false;
	private static Path path = null;
	private static Model model = new Model();
	private static JLabel inClass = null;
	
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
	
	public void saveCancel (JButton save, JButton cancel, JButton xButton) {
		SaveCancel.saveClass(save);
		SaveCancel.cancelClass(cancel);
		SaveCancel.deleteEntity(xButton);
	}

	public void createField (JPanel panel) {
		CreateDeleteController.createField(panel);
	}
	
	public void createMethod (JPanel panel) {
		CreateDeleteController.createMethod(panel);
	}
	
	public void createParam (JPanel panel, String methodName) {
		CreateDeleteController.createParam(panel, methodName);
	}
	
	public void deleteField (JPanel panel) {
		CreateDeleteController.deleteField(panel);
	}
	
	public void deleteMethod (JPanel panel) {
		CreateDeleteController.deleteMethod(panel);
	}
	
	public void deleteParam (JPanel panel, String methodName) {
		CreateDeleteController.deleteParam(panel, methodName);
	}
	
	public static JLabel getinClass() {
		return inClass;
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
	
	public static void setinClass(JLabel no) {
		inClass = no;
	}
	
	public static void setChanged(Boolean yes) {
		changed = yes;
	}
	
	public static void setPath(Path yes) {
		path = yes;
	}
	
}

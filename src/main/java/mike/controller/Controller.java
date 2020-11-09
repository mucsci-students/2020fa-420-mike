
package mike.controller;

import java.nio.file.Path;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import mike.datastructures.Model;
import mike.view.GUIView;
import mike.view.ViewTemplate;
import mike.datastructures.Entity;

public class Controller {

	private boolean changed = false;
	private Path path = null;
	private Model Cmodel;
	private ViewTemplate view = null;
	private JLabel inClass = null;
	
	public Controller (Model model, ViewTemplate views){
	    Cmodel = model;
	    view = views;
	    JMenuBar menubar = ((GUIView) views).getMenuBar();
	    SaveController.saveListener((JButton) menubar.getComponent(0), this);
	    SaveController.saveAsListener((JButton) menubar.getComponent(1), this);
	    LoadController.loadListener((JButton) menubar.getComponent(2), this);
	    FrameController.addClassListener((JButton) menubar.getComponent(3), this);
	    ClassController.editModeListener((JButton) menubar.getComponent(4), this);
	    FrameController.exitListener(view, this);
	    FrameController.resizeListener(view);
	}
	
	public void classControls(JLabel box, Entity e) {
		ClassController.clickClass(box, this);
		ClassController.moveClass(box, e, view);
	}
	
	public void saveCancel (JButton save, JButton cancel, JButton xButton, JButton addRelation, JButton deleteRelation, Model backup) {
		SaveCancel.saveClass(save, this);
		SaveCancel.cancelClass(cancel, this, backup);
		SaveCancel.deleteEntity(xButton, this);
		ClassController.addRelationListener(addRelation, this);
		ClassController.deleteRelationListener(deleteRelation, this);
	}

	public void createField (JPanel panel) {
		CreateDeleteController.createFunction(panel, this, "field", null);
	}
	
	public void createMethod (JPanel panel) {
		CreateDeleteController.createFunction(panel, this, "method", null);
	}
	
	public void createParam (JPanel panel, String methodName) {
		CreateDeleteController.createFunction(panel, this, "parameter", methodName);
	}
	
	public void deleteField (JPanel panel) {
		CreateDeleteController.deleteFunction(panel, this, "field", null);
	}
	
	public void deleteMethod (JPanel panel) {
		CreateDeleteController.deleteFunction(panel, this, "method", null);
	}
	
	public void deleteParam (JPanel panel, String methodName) {
		CreateDeleteController.deleteFunction(panel, this, "parameter", methodName);
	}
	
	public JLabel getinClass() {
		return inClass;
	}
	
	public Path getPath() {
		return path;
	}
	
	protected Model getModel() {
	    return Cmodel;
	}
	
	protected ViewTemplate getView() {
	    return view;
	}
	
	public Boolean getChanged() {
		return changed;
	}
	
	public void setModel(Model m) {
		Cmodel = m;
	}
	
	public void setinClass(JLabel no) {
		inClass = no;
	}
	
	public void setChanged(Boolean yes) {
		changed = yes;
	}
	
	public void setPath(Path yes) {
		path = yes;
	}
	
}

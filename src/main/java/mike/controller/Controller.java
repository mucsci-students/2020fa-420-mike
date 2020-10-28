
package mike.controller;

import java.nio.file.Path;

import javax.swing.JButton;
import javax.swing.JLabel;
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
	    SaveController.saveListener((JButton) ((GUIView) views).getMenuBar().getComponent(0), this);
	    SaveController.saveAsListener((JButton) ((GUIView) views).getMenuBar().getComponent(1), this);
	    LoadController.loadListener((JButton) ((GUIView) views).getMenuBar().getComponent(2), this);
	    FrameController.addClassListener((JButton) ((GUIView) views).getMenuBar().getComponent(3), this);
	    ClassController.editModeListener((JButton) ((GUIView) views).getMenuBar().getComponent(4), this);
	    FrameController.exitListener(changed, view);
	    FrameController.resizeListener(view);
	}
	
	public void classControls(JLabel box, Entity e) {
		ClassController.clickClass(box, this);
		ClassController.moveClass(box, e, view);
	}
	
	public void frameControls (JButton[] buttons) {
		
	}
	
	public void saveCancel (JButton save, JButton cancel, JButton xButton, JButton addRelation, JButton deleteRelation) {
		SaveCancel.saveClass(save, this);
		SaveCancel.cancelClass(cancel, this);
		SaveCancel.deleteEntity(xButton, this);
		ClassController.addRelationListener(addRelation, this);
		ClassController.deleteRelationListener(deleteRelation, this);
	}

	public void createField (JPanel panel) {
		CreateDeleteController.createField(panel, this);
	}
	
	public void createMethod (JPanel panel) {
		CreateDeleteController.createMethod(panel, this);
	}
	
	public void createParam (JPanel panel, String methodName) {
		CreateDeleteController.createParam(panel, methodName, this);
	}
	
	public void deleteField (JPanel panel) {
		CreateDeleteController.deleteField(panel, this);
	}
	
	public void deleteMethod (JPanel panel) {
		CreateDeleteController.deleteMethod(panel, this);
	}
	
	public void deleteParam (JPanel panel, String methodName) {
		CreateDeleteController.deleteParam(panel, methodName, this);
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

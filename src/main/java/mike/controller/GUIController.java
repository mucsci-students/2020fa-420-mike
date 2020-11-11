package mike.controller;

import java.nio.file.Path;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import mike.datastructures.Entity;
import mike.datastructures.Model;
import mike.view.CLIView;
import mike.view.GUIView;
import mike.view.ViewTemplate;

public class GUIController extends ControllerType {
    Model model;
    GUIView view;
    private boolean changed;
    private Path path;
    private JLabel inClass;

    public GUIController(Model model, ViewTemplate view) {
	super();
	this.model = model;
	this.view = (GUIView) view.getViewinterface();
    }

    public void classControls(JLabel box, Entity e) {
	ClassController.clickClass(box, this);
	ClassController.moveClass(box, e, view);
    }

    public void saveCancel(JButton save, JButton cancel, JButton xButton, JButton addRelation, JButton deleteRelation,
	    Model backup) {
	SaveCancel.saveClass(save, this);
	SaveCancel.cancelClass(cancel, this, backup);
	SaveCancel.deleteEntity(xButton, this);
	ClassController.addRelationListener(addRelation, this);
	ClassController.deleteRelationListener(deleteRelation, this);
    }

    public void init() {
	JMenuBar menubar = ((GUIView) view).getMenuBar();
	SaveController.saveListener((JButton) menubar.getComponent(0), this);
	SaveController.saveAsListener((JButton) menubar.getComponent(1), this);
	LoadController.loadListener((JButton) menubar.getComponent(2), this);
	FrameController.addClassListener((JButton) menubar.getComponent(3), this);
	ClassController.editModeListener((JButton) menubar.getComponent(4), this);
	FrameController.exitListener(this.view, this);
	FrameController.resizeListener(this.view);
    }

    public void createField(JPanel panel) {
	CreateDeleteController.createFunction(panel, this, "field", null);
    }

    public void createMethod(JPanel panel) {
	CreateDeleteController.createFunction(panel, this, "method", null);
    }

    public void createParam(JPanel panel, String methodName) {
	CreateDeleteController.createFunction(panel, this, "parameter", methodName);
    }

    public void deleteField(JPanel panel) {
	CreateDeleteController.deleteFunction(panel, this, "field", null);
    }

    public void deleteMethod(JPanel panel) {
	CreateDeleteController.deleteFunction(panel, this, "method", null);
    }

    public void deleteParam(JPanel panel, String methodName) {
	CreateDeleteController.deleteFunction(panel, this, "parameter", methodName);
    }

    public JLabel getinClass() {
	return inClass;
    }

    public Path getPath() {
	return path;
    }

    protected Model getModel() {
	return model;
    }

    protected ViewTemplate getView() {
	return view;
    }

    public Boolean getChanged() {
	return changed;
    }

    public void setModel(Model m) {
	model = m;
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

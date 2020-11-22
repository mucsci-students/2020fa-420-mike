package mike.controller;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import mike.datastructures.Entity;
import mike.datastructures.Memento;
import mike.datastructures.Model;
import mike.gui.RedoAction;
import mike.gui.SaveAction;
import mike.gui.UndoAction;
import mike.view.GUIView;
import mike.view.ViewTemplate;

public class GUIController extends ControllerType {
    private Model model;
    private GUIView view;
    private boolean changed;
    private File file;
    private JLabel inClass;
    protected ArrayList<Memento> mementos;
    private int currMeme;

    public GUIController(Model model, ViewTemplate view) {
	super();
	this.model = model;
	this.view = (GUIView) view.getViewinterface();
	currMeme = 0;
	mementos = new ArrayList<Memento>();
	mementos.add(new Memento(this.model));
    }

    public void classControls(JLabel box, Entity e) {
	ClassController.clickClass(box, this);
	ClassController.moveClass(box, e, view);
    }

    public void saveCancel(JButton save, JButton cancel, JButton xButton, JButton addRelation, JButton deleteRelation) {
	SaveCancel.saveClass(save, this);
	SaveCancel.cancelClass(cancel, this);
	SaveCancel.deleteEntity(xButton, this);
	ClassController.addRelationListener(addRelation, this);
	ClassController.deleteRelationListener(deleteRelation, this);
    }

    public void init() {
	JMenuBar menubar = view.getMenuBar();
	SaveController.saveListener((JButton) menubar.getComponent(0), this);
	SaveController.saveAsListener((JButton) menubar.getComponent(1), this);
	LoadController.loadListener((JButton) menubar.getComponent(2), this);
	UndoRedoController.undoListener((JButton) menubar.getComponent(3), mementos, this);
	UndoRedoController.redoListener((JButton) menubar.getComponent(4), mementos, this);
	FrameController.addClassListener((JButton) menubar.getComponent(5), this);
	ClassController.editModeListener((JButton) menubar.getComponent(6), this);
	FrameController.exitListener(this.view, this);
	FrameController.resizeListener(this.view);
	
	Action save = new SaveAction(this);
	Action undo = new UndoAction(this);
	Action redo = new RedoAction(this);
	
	JComponent contentPane = ((JComponent) view.getFrame().getContentPane());
	contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "save");
	contentPane.getActionMap().put("save", save);

	contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "undo");
	contentPane.getActionMap().put("undo", undo);
	
	contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), "redo");
	contentPane.getActionMap().put("redo", redo);
	
    }

    protected void truncateMemes(int end) {
	if (currMeme < mementos.size() - end) {
	    for (int i = mementos.size() - end; i > currMeme; --i) {
		mementos.remove(i);
	    }
	}
    }

    public void newMeme(Memento meme) {
	truncateMemes(1);
	mementos.add(meme);
	this.model = meme.getModel();
	++currMeme;
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

    public File getFile() {
	return file;
    }

    public Model getModel() {
	return model;
    }

    public GUIView getView() {
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

    public void setFile(File yes) {
	file = yes;
    }

    public int getCurrMeme() {
	return currMeme;
    }

    public void setCurrMeme(int newCurrMeme) {
	currMeme = newCurrMeme;
    }

    public ArrayList<Memento> getMementos() {
	return mementos;
    }

    public void appendMementos(ArrayList<Memento> editModeMementos) {
	truncateMemes(1);
	mementos.addAll(editModeMementos);
	currMeme = mementos.size() - 1;
    }

}

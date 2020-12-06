package mike.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import mike.datastructures.Memento;
import mike.datastructures.Model;
import mike.gui.Line;
import mike.view.GUIView;
import mike.view.ViewInterface;
import mike.view.ViewTemplate;

public class GUIControllerTest {

    @Mock
    private JLayeredPane pane;
    @Mock
    private JScrollPane scrollPane;
    @Mock
    private JMenuBar menuBar;
    @Spy
    private HashMap<String, JLabel> entityLabels;
    @Spy
    private ArrayList<Line> relations;

    @Mock
    GUIView guiViewMock;
    @Mock
    GUIController control;
    
    private GUIController GUIControllerMock;
    private Model model;
    
    @Before
    public void setup() {
	MockitoAnnotations.openMocks(this);
	model = new Model();
	guiViewMock = new GUIView(entityLabels, pane, scrollPane, relations, menuBar);
    }
    

    @Test
    public void constructorTest() throws Exception {
	GUIControllerMock = new GUIController(model, guiViewMock);
    }

    @Test
    public void getterTest() throws Exception {
	GUIControllerMock = new GUIController(model, guiViewMock);
	
	assertEquals("inClass is not null", null, GUIControllerMock.getinClass());
	assertEquals("file is not null", null, GUIControllerMock.getFile());
	assertEquals("Model is not equal", model, GUIControllerMock.getModel());
	assertFalse("changed is true", GUIControllerMock.getChanged());
	assertEquals("Model is not equal", model, GUIControllerMock.getModel());
	
	JLabel no = new JLabel("NO");
	GUIControllerMock.setinClass(no);
	assertEquals("Label not changed", no, GUIControllerMock.getinClass());
	
	Model model2 = new Model();
	model2.createClass("c1");
	GUIControllerMock.setModel(model2);
	assertEquals("Model is not equal", model2, GUIControllerMock.getModel());
	
	GUIControllerMock.setChanged(true);
	assertTrue("changed is false", GUIControllerMock.getChanged());
	assertEquals("currMeme is not 0", 0, GUIControllerMock.getCurrMeme());
	GUIControllerMock.setCurrMeme(1);
	assertEquals("currMeme is not 1", 1, GUIControllerMock.getCurrMeme());
	
	ArrayList<Memento> memes = new ArrayList<Memento>();
	memes.add(new Memento(model2));
	GUIControllerMock.appendMementos(memes);
	assertEquals("Mementos is wrong", 2, GUIControllerMock.getMementos().size());
    }
    
    @Test
    public void classControlsTest() throws Exception {
	GUIControllerMock = new GUIController(model, guiViewMock);
	
	JLabel box = new JLabel("c1");
	model.createClass("c1");
	GUIControllerMock.classControls(box, model.copyEntity("c1"));
    }
    
    @Test
    public void saveCancelTest() throws Exception {
	GUIControllerMock = new GUIController(model, guiViewMock);
	
	JButton save = new JButton();
	JButton cancel = new JButton();
	JButton xButton = new JButton();
	JButton addRelation = new JButton();
	JButton deleteRelation = new JButton();
	GUIControllerMock.saveCancel(save, cancel, xButton, addRelation, deleteRelation);
    }
    
    @Test
    public void createDeleteTest() throws Exception {
	GUIControllerMock = new GUIController(model, guiViewMock);
	JPanel panel = new JPanel();
	for(int x = 0; x < 7; ++x) {
	    panel.add(new JButton());
	}
	GUIControllerMock.createField(panel);
	GUIControllerMock.createMethod(panel);
	GUIControllerMock.createParam(panel, "m1");
	GUIControllerMock.deleteField(panel);
	GUIControllerMock.deleteMethod(panel);
	GUIControllerMock.deleteParam(panel, "m1");
    }
    
    
    @Test
    public void MementosTest() throws Exception {
	GUIControllerMock = new GUIController(model, guiViewMock);
	
	
	Model model2 = new Model();
	model2.createClass("c1");
	
	//ArrayList<Memento> memes = new ArrayList<Memento>();
	
	Memento meme = new Memento(model2);
	//memes.add(new Memento(model2));
	//GUIControllerMock.appendMementos(memes);
	//assertEquals("Mementos is wrong", 2, GUIControllerMock.getMementos().size());
	
	GUIControllerMock.newMeme(meme);
	GUIControllerMock.newMeme(meme);
	GUIControllerMock.newMeme(meme);
	GUIControllerMock.setCurrMeme(1);
	GUIControllerMock.truncateMemes(1);
    }
}
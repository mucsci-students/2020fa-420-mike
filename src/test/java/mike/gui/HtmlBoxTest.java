package mike.gui;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import mike.controller.GUIController;
import mike.datastructures.Model;
import mike.view.GUIView;

public class HtmlBoxTest {

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

    private Model model;

    @Before
    public void setup() {
	MockitoAnnotations.openMocks(this);
	guiViewMock = new GUIView(entityLabels, pane, scrollPane, relations, menuBar);
	model = new Model();
	control = new GUIController(model, guiViewMock);
    }

    @Test
    public void constructorTest() throws Exception {
	// Empty class
	model.createClass("c1");
	String text = "<html><b>c1</b><br/></html>";
	HtmlBox htmlbox = new HtmlBox(model.copyEntity("c1"), control);
	assertEquals("Box name not set", "c1", htmlbox.getBox().getName());
	assertEquals("Text not correct", text, htmlbox.getBox().getText());
	
	// One field and method
	model.createField("c1", "f1", "int", "private");
	model.createMethod("c1", "m1", "int", "protected");
	text = "<html><b>c1</b><br/><hr><b>Fields:</b><br/>&emsp private int f1<br/><hr><b>"
		+ "Methods:</b><br/>&emsp protected int m1()<br/></html>";
	HtmlBox htmlbox1 = new HtmlBox(model.copyEntity("c1"), control);
	assertEquals("Box name not set", "c1", htmlbox1.getBox().getName());
	assertEquals("Text not correct", text, htmlbox1.getBox().getText());
	
	// One parameter
	model.createParameter("c1", "m1", "p1", "boolean");
	text = "<html><b>c1</b><br/><hr><b>Fields:</b><br/>&emsp private int f1<br/><hr><b>"
		+ "Methods:</b><br/>&emsp protected int m1(boolean p1)<br/></html>";
	HtmlBox htmlbox2 = new HtmlBox(model.copyEntity("c1"), control);
	assertEquals("Box name not set", "c1", htmlbox2.getBox().getName());
	assertEquals("Text not correct", text, htmlbox2.getBox().getText());
	
	// Multiple parameters
	model.createParameter("c1", "m1", "p2", "boolean");
	text = "<html><b>c1</b><br/><hr><b>Fields:</b><br/>&emsp private int f1<br/><hr><b>"
		+ "Methods:</b><br/>&emsp protected int m1(boolean p1, boolean p2)<br/></html>";
	HtmlBox htmlbox3 = new HtmlBox(model.copyEntity("c1"), control);
	assertEquals("Box name not set", "c1", htmlbox3.getBox().getName());
	assertEquals("Text not correct", text, htmlbox3.getBox().getText());
    }

}

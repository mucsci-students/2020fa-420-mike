package mike.view;

import static org.junit.Assert.*;

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
import mike.datastructures.Relationship;
import mike.datastructures.Relationship.Type;
import mike.gui.Line;

public class GUIViewTest {

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

    @Before
    public void setup() {
	MockitoAnnotations.openMocks(this);
    }

    @Test
    public void initTest() throws Exception {
	guiViewMock = new GUIView(entityLabels, pane, scrollPane, relations, menuBar);
    }

    @Test
    public void getterTest() throws Exception {
	guiViewMock = new GUIView(entityLabels, pane, scrollPane, relations, menuBar);

	assertFalse("entityLabels object is null.", guiViewMock.getEntityLabels().equals(null));
	assertFalse("relations object is null.", guiViewMock.getRelations().equals(null));
	assertFalse("pane object is null.", guiViewMock.getPane().equals(null));
	assertFalse("menuBar object is null.", guiViewMock.getMenuBar().equals(null));
    }

    @Test
    public void createClassTest() throws Exception {
	// Pre make things
	Model model = new Model();
	guiViewMock = new GUIView(entityLabels, pane, scrollPane, relations, menuBar);
	assertEquals("There are more or less than 0 entityLabels", 0, guiViewMock.getEntityLabels().size());

	// Make two classes
	model.createClass("c1");
	model.createClass("c2");
	guiViewMock.showClass(model.copyEntity("c1"), control);
	guiViewMock.showClass(model.copyEntity("c2"), control);
	assertEquals("There are more or less than 2 entityLabels", 2, guiViewMock.getEntityLabels().size());
    }

    @Test
    public void deleteClassTest() throws Exception {
	// Pre make things
	Model model = new Model();
	guiViewMock = new GUIView(entityLabels, pane, scrollPane, relations, menuBar);

	// Make two classes
	model.createClass("c1");
	model.createClass("c2");
	guiViewMock.showClass(model.copyEntity("c1"), control);
	guiViewMock.showClass(model.copyEntity("c2"), control);
	assertEquals("There are more or less than 2 entityLabels", 2, guiViewMock.getEntityLabels().size());

	// Delete two classes
	guiViewMock.deleteClass("c1");
	assertEquals("There is more or less than 1 entityLabel", 1, guiViewMock.getEntityLabels().size());
	guiViewMock.deleteClass("c2");
	assertEquals("There are more or less than 0 entityLabels", 0, guiViewMock.getEntityLabels().size());
    }

    @Test
    public void createRelationshipsTest() throws Exception {
	// Pre make things
	Model model = new Model();
	guiViewMock = new GUIView(entityLabels, pane, scrollPane, relations, menuBar);

	// Make two classes
	model.createClass("c1");
	model.createClass("c2");
	guiViewMock.showClass(model.copyEntity("c1"), control);
	guiViewMock.showClass(model.copyEntity("c2"), control);
	assertEquals("There are more or less than 2 entityLabels", 2, guiViewMock.getEntityLabels().size());

	// Make a relationship
	guiViewMock.createRelationship(Type.AGGREGATION, "c1", "c2", model);
	assertEquals("There is more or less than 1 relationship", 1, guiViewMock.getRelations().size());
	Relationship rel = new Relationship(Type.AGGREGATION, "c1", "c2");
	assertEquals("There are more or less than 2 relationships", rel,
		model.getRelationship(Type.AGGREGATION, "c1", "c2"));
    }

    @Test
    public void deleteLineTest() throws Exception {
	// Pre make things
	Model model = new Model();
	guiViewMock = new GUIView(entityLabels, pane, scrollPane, relations, menuBar);

	// Make two classes
	model.createClass("c1");
	model.createClass("c2");
	guiViewMock.showClass(model.copyEntity("c1"), control);
	guiViewMock.showClass(model.copyEntity("c2"), control);
	assertEquals("There are more or less than 2 entityLabels", 2, guiViewMock.getEntityLabels().size());

	// Make a relationship
	guiViewMock.createRelationship(Type.AGGREGATION, "c1", "c2", model);
	assertEquals("There are more or less than 1 relationship", 1, guiViewMock.getRelations().size());
	Relationship rel = new Relationship(Type.AGGREGATION, "c1", "c2");
	assertEquals("Relationships does not exist in model", rel, model.getRelationship(Type.AGGREGATION, "c1", "c2"));

	// Delete a Relationship
	guiViewMock.deleteLine("c1", "c2");
	assertEquals("There are more or less than 0 relationships", 0, guiViewMock.getRelations().size());
	assertEquals("There are more or less than 2 entityLabels", 2, guiViewMock.getEntityLabels().size());

	// Make a both recursive relationships to each entity
	guiViewMock.createRelationship(Type.COMPOSITION, "c1", "c1", model);
	assertEquals("There are more or less than 1 relationship", 1, guiViewMock.getRelations().size());
	Relationship rel2 = new Relationship(Type.COMPOSITION, "c1", "c1");
	assertEquals("Relationships does not exist in model", rel2,
		model.getRelationship(Type.COMPOSITION, "c1", "c1"));

	guiViewMock.createRelationship(Type.COMPOSITION, "c2", "c2", model);
	assertEquals("There are more or less than 2 relationship", 2, guiViewMock.getRelations().size());
	Relationship rel3 = new Relationship(Type.COMPOSITION, "c2", "c2");
	assertEquals("Relationships does not exist in model", rel3,
		model.getRelationship(Type.COMPOSITION, "c2", "c2"));

	// Delete a Relationship
	guiViewMock.deleteLine("c1", "c1");
	assertEquals("There are more or less than 1 relationships", 1, guiViewMock.getRelations().size());
	assertEquals("There are more or less than 2 entityLabels", 2, guiViewMock.getEntityLabels().size());

	// Random test
	model.createClass("c3");
	guiViewMock.showClass(model.copyEntity("c3"), control);
	guiViewMock.createRelationship(Type.COMPOSITION, "c3", "c2", model);
	assertEquals("There are more or less than 2 relationships", 2, guiViewMock.getRelations().size());
	Relationship rel4 = new Relationship(Type.COMPOSITION, "c3", "c2");
	assertEquals("Relationships does not exist in model", rel4,
		model.getRelationship(Type.COMPOSITION, "c3", "c2"));

	guiViewMock.deleteLine("c2", "c2");
	assertEquals("There are more or less than 1 relationships", 1, guiViewMock.getRelations().size());
	assertEquals("There are more or less than 3 entityLabels", 3, guiViewMock.getEntityLabels().size());
    }

    @Test
    public void deleteLinesTest() throws Exception {
	// Pre make things
	Model model = new Model();
	guiViewMock = new GUIView(entityLabels, pane, scrollPane, relations, menuBar);

	// Make two classes
	model.createClass("c1");
	model.createClass("c2");
	model.createClass("c3");
	model.createClass("c4");
	guiViewMock.showClass(model.copyEntity("c1"), control);
	guiViewMock.showClass(model.copyEntity("c2"), control);
	guiViewMock.showClass(model.copyEntity("c3"), control);
	guiViewMock.showClass(model.copyEntity("c4"), control);
	assertEquals("There are more or less than 4 entityLabels", 4, guiViewMock.getEntityLabels().size());

	// Make some relationships
	guiViewMock.createRelationship(Type.AGGREGATION, "c1", "c2", model);
	guiViewMock.createRelationship(Type.COMPOSITION, "c3", "c2", model);
	guiViewMock.createRelationship(Type.INHERITANCE, "c2", "c2", model);
	guiViewMock.createRelationship(Type.REALIZATION, "c4", "c1", model);
	guiViewMock.createRelationship(Type.AGGREGATION, "c4", "c4", model);
	assertEquals("There are more or less than 5 relationship", 5, guiViewMock.getRelations().size());

	// Delete lines
	guiViewMock.deleteLines("c1");
	assertEquals("There are more or less than 3 relationships", 3, guiViewMock.getRelations().size());
	assertEquals("There are more or less than 4 entityLabels", 4, guiViewMock.getEntityLabels().size());

	// Delete lines (only a recursive)
	guiViewMock.deleteLines("c4");
	assertEquals("There are more or less than 2 relationships", 2, guiViewMock.getRelations().size());
	assertEquals("There are more or less than 4 entityLabels", 4, guiViewMock.getEntityLabels().size());

	// Delete lines
	guiViewMock.deleteLines("c2");
	assertEquals("There are more or less than 0 relationships", 0, guiViewMock.getRelations().size());
	assertEquals("There are more or less than 4 entityLabels", 4, guiViewMock.getEntityLabels().size());

    }

    @Test
    public void editmodeTest() throws Exception {
	// Pre make things
	Model model = new Model();
	guiViewMock = new GUIView(entityLabels, pane, scrollPane, relations, menuBar);
	assertEquals("There are more or less than 0 entityLabels", 0, guiViewMock.getEntityLabels().size());

	// Make two classes
	model.createClass("c1");
	model.createClass("c2");
	guiViewMock.showClass(model.copyEntity("c1"), control);
	guiViewMock.showClass(model.copyEntity("c2"), control);
	guiViewMock.createRelationship(Type.AGGREGATION, "c1", "c2", model);

	Relationship rel = new Relationship(Type.AGGREGATION, "c1", "c2");
	System.out.println(entityLabels.get("c1"));
	guiViewMock.repaintEverything(model, control);
	assertEquals("There are more or less than 1 relationships in model", rel,
		model.getRelationship(Type.AGGREGATION, "c1", "c2"));
	assertEquals("There is more or less than 1 relationship in gui", 1, guiViewMock.getRelations().size());
	assertEquals("There are more or less than 2 entityLabels in gui", 2, guiViewMock.getEntityLabels().size());
    }

    @Test
    public void repaintLineTest() throws Exception {
	// Pre make things
	Model model = new Model();
	guiViewMock = new GUIView(entityLabels, pane, scrollPane, relations, menuBar);

	// Make two classes
	model.createClass("c1");
	model.createClass("c2");
	guiViewMock.showClass(model.copyEntity("c1"), control);
	guiViewMock.showClass(model.copyEntity("c2"), control);
	assertEquals("There are more or less than 2 entityLabels", 2, guiViewMock.getEntityLabels().size());

	// Make a relationship
	guiViewMock.createRelationship(Type.AGGREGATION, "c1", "c2", model);
	guiViewMock.repaintLine("c1");

	assertEquals("There is more or less than 1 relationship", 1, guiViewMock.getRelations().size());
	Relationship rel = new Relationship(Type.AGGREGATION, "c1", "c2");
	assertEquals("There are more or less than 2 relationships", rel,
		model.getRelationship(Type.AGGREGATION, "c1", "c2"));
    }

    @Test
    public void exitEditingClassTest() {
	// Pre make things
	Model model = new Model();
	guiViewMock = new GUIView(entityLabels, pane, scrollPane, relations, menuBar);

	// Make one class
	model.createClass("c1");
	model.createClass("c2");
	guiViewMock.showClass(model.copyEntity("c1"), control);
	guiViewMock.showClass(model.copyEntity("c2"), control);
	assertEquals("There are more or less than 2 entityLabels", 2, guiViewMock.getEntityLabels().size());

	// Make a relationship
	guiViewMock.createRelationship(Type.INHERITANCE, "c1", "c1", model);
	assertEquals("There is more or less than 1 relationships", 1, guiViewMock.getRelations().size());
	
	JLabel c1 = new JLabel();
	c1.setName("c1");
	guiViewMock.exitEditingClass(c1, control, model);
	assertEquals("There is more or less than 1 relationships", 1, guiViewMock.getRelations().size());
	assertEquals("There are more or less than 2 entityLabels", 2, guiViewMock.getEntityLabels().size());
    }

}

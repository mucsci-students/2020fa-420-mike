package mike.view;

import static org.junit.Assert.*;

import org.junit.Test;

import mike.controller.GUIController;
import mike.datastructures.Model;
import mike.datastructures.Relationship;
import mike.datastructures.Relationship.Type;

public class GUIViewTest {

    private GUIView guiViewMock;

    @Test
    public void initTest() throws Exception {
	guiViewMock = new GUIView("cut");
    }

    @Test
    public void getterTest() throws Exception {
	guiViewMock = new GUIView("cut");

	assertFalse("entityLabels object is null.", guiViewMock.getEntityLabels().equals(null));
	assertFalse("relations object is null.", guiViewMock.getRelations().equals(null));
	assertFalse("pane object is null.", guiViewMock.getPane().equals(null));
	assertFalse("menuBar object is null.", guiViewMock.getMenuBar().equals(null));
    }
    
    @Test
    public void createClassTest() throws Exception {
	// Pre make things
	Model model = new Model();
	ViewTemplate view = new ViewTemplate();
	GUIController control = new GUIController(model, view);
	guiViewMock = new GUIView("cut");
	
	// Make two classes
	model.createClass("c1");
	model.createClass("c2");
	guiViewMock.showClass(model.copyEntity("c1"), control);
	guiViewMock.showClass(model.copyEntity("c2"), control);
	assertEquals("", 2, guiViewMock.getEntityLabels().size());
    }
    
    @Test
    public void createRelationshipsTest() throws Exception {
	// Pre make things
	Model model = new Model();
	ViewTemplate view = new ViewTemplate();
	GUIController control = new GUIController(model, view);
	guiViewMock = new GUIView("cut");
	
	// Make two classes
	model.createClass("c1");
	model.createClass("c2");
	guiViewMock.showClass(model.copyEntity("c1"), control);
	guiViewMock.showClass(model.copyEntity("c2"), control);
	assertEquals("", 2, guiViewMock.getEntityLabels().size());
	
	// Make a relationship
	guiViewMock.createRelationship(Type.AGGREGATION, "c1", "c2", model);
	assertEquals("", 1, guiViewMock.getRelations().size());
	Relationship rel = new Relationship(Type.AGGREGATION, "c1", "c2");
	assertEquals("", rel, model.getRelationship(Type.AGGREGATION, "c1", "c2"));
    }
}
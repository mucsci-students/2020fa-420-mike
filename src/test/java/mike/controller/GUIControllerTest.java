package mike.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import mike.datastructures.Memento;
import mike.datastructures.Model;

public class GUIControllerTest {

    private GUIController GUIControllerMock;

    @Test
    public void initTest() throws Exception {
	Model model = new Model();
	GUIControllerMock = new GUIController(model);
    }

    @Test
    public void getterTest() throws Exception {
	Model model = new Model();
	GUIControllerMock = new GUIController(model);
	
	assertEquals("inClass is not null", null, GUIControllerMock.getinClass());
	assertEquals("file is not null", null, GUIControllerMock.getFile());
	assertEquals("Model is not equal", model, GUIControllerMock.getModel());
	assertFalse("changed is true", GUIControllerMock.getChanged());
	assertEquals("Model is not equal", model, GUIControllerMock.getModel());
	
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
    
}

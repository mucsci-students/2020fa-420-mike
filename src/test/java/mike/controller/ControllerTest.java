package mike.controller;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import mike.datastructures.Model;
import mike.view.ViewTemplate;
import mike.view.ViewTemplate.InterfaceType;

public class ControllerTest {

    @Test
    public void CLIControllerCreationTest() throws IOException {
	Model m = new Model();
	ViewTemplate v = new ViewTemplate(InterfaceType.CLI);
	Controller c = new Controller(m, v);
	
	assertTrue("Controller equals null.", c != null);
    }

}

package mike.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mike.datastructures.Entity;
import mike.datastructures.Method;
import mike.datastructures.Model;
import mike.view.CLIView;
import mike.view.ViewTemplate;

public class RenameCommandTest {
    Model model;
    CLIView view;
    ViewTemplate viewTemp;
    CreateCommand command;
    boolean prompt;
    
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();

    private final PrintStream origOut = System.out;
    private final PrintStream origErr = System.err;
    
    @Before
    public void createCLI() throws IOException {
	System.setOut(new PrintStream(out));
	System.setErr(new PrintStream(err));
	model = new Model();
	viewTemp = new ViewTemplate(ViewTemplate.InterfaceType.CLI);
	view = (CLIView) viewTemp.getViewinterface();
	prompt = false;
    }
    
    @After
    public void cleanCLI() {
	System.setOut(origOut);
	System.setErr(origErr);
	String[] commands = {"sudo", "clear"};
	command = new CreateCommand(model, view, commands, prompt);
    }
    
    private void executeCommand(String[] commands) {
	RenameCommand create = new RenameCommand(model, view, commands, prompt);
	prompt = create.execute();
    }
    
    private void executeCreateCommand(String[] commands) {
	CreateCommand create = new CreateCommand(model, view, commands, prompt);
	prompt = create.execute();
    }
    
    @Test
    public void renameClassTest() {
        // Test setup of class creation
        String[] commands = {"create", "class", "c1"};
        executeCreateCommand(commands);
        commands[2] = "c2";
        executeCreateCommand(commands);

        assertTrue("CLI did not make a class; Should be class called c1.", model.containsEntity("c1"));
        assertTrue("CLI did not make a class; Should be class called c2.", model.containsEntity("c2"));

        // Test rename a class
        String[] rename = {"rename", "class", "c1", "c3"};
        executeCommand(rename);

        assertTrue("CLI did not rename a class; Should be class called c3.", model.containsEntity("c3"));
        assertFalse("CLI did not remove/rename old class; c1 should no longer exist.", model.containsEntity("c1"));

        // Test NOT renaming with invalid length
        String[] invalidLengthCommand = {"create", "class", "c3", "c1", "badLength"};
        executeCommand(invalidLengthCommand);

        assertFalse("CLI created a class with bad length; Class named 'c1' should not exist.", model.containsEntity("c1"));
        assertEquals("CLI made a class; Should be 2 classes.", 2, model.getEntities().size());


        // Test rename a NONEXISTENT class
        rename[2] = "c4";
        rename[3] = "c5";
        executeCommand(rename);
        
        assertTrue("CLI renamed a class. c2 should exist.", model.containsEntity("c2"));
        assertTrue("CLI renamed a class. c3 should exist.", model.containsEntity("c3"));
        assertFalse("CLI created a new class. c4 should not exist.", model.containsEntity("c4"));
        assertFalse("CLI created a new class. c5 should not exist.", model.containsEntity("c5"));

        // Test rename an EXISTING class to another EXISTING class
        rename[2] = "c3";
        rename[3] = "c2";
        executeCommand(rename);
        
        assertTrue("CLI renamed a class. c2 should exist.", model.containsEntity("c2"));
        assertTrue("CLI renamed a class. c3 should exist.", model.containsEntity("c3"));
        assertEquals("CLI does not have 2 classes. Model should have two entities.", 2, model.getEntities().size());
    }
    
    @Test
    public void renameFieldTest() {
	String[] classCommands = {"create", "class", "c1"};
	executeCreateCommand(classCommands);
	Entity c1 = model.getEntities().get(0);
	String[] createFieldCommands = {"create", "field", "c1", "public", "int", "f1"};
	executeCreateCommand(createFieldCommands);
	
	// Rename one field
	String[] renameFieldCommands = {"rename", "field", "c1", "f1", "newf1"};
	executeCommand(renameFieldCommands);
	assertFalse("c1 still has a f1; Should not have any more f1.", c1.containsField("f1"));
	assertTrue("c1 still has a f1; Should not have any more f1.", c1.containsField("newf1"));
	assertEquals("c1 has more or less than one fields.", 1, c1.getFields().size());
	
	// Rename multiple fields
	createFieldCommands[5] = "f2";
	executeCreateCommand(createFieldCommands);
	createFieldCommands[5] = "f3";
	executeCreateCommand(createFieldCommands);
	createFieldCommands[5] = "f4";
	executeCreateCommand(createFieldCommands);
	renameFieldCommands[3] = "f4";
	renameFieldCommands[4] = "newf4";
	executeCommand(renameFieldCommands);
	renameFieldCommands[3] = "f3";
	renameFieldCommands[4] = "newf3";
	executeCommand(renameFieldCommands);
	renameFieldCommands[3] = "f2";
	renameFieldCommands[4] = "newf2";
	executeCommand(renameFieldCommands);
	assertFalse("c1 still has field f2; Should not have it.", c1.containsField("f2"));
	assertFalse("c1 still has field f3; Should not have it.", c1.containsField("f3"));
	assertFalse("c1 still has field f4; Should not have it.", c1.containsField("f4"));
	assertTrue("c1 doesn't have field newf2; Should have it.", c1.containsField("newf2"));
	assertTrue("c1 doesn't have field newf3; Should have it.", c1.containsField("newf3"));
	assertTrue("c1 doesn't have field newf4; Should have it.", c1.containsField("newf4"));
	assertEquals("c1 has more or less than four fields.", 4, c1.getFields().size());
	
	// Rename field with wrong length of commands
	String[] invalidLengthCommand = {"rename", "field", "c1", "newf1", "tooLong", "reallyLong"};
	executeCommand(invalidLengthCommand);
	assertTrue("c1 no longer has newf1; Should still have it after an error.", c1.containsField("newf1"));
	assertFalse("c1 renamed field newf1; Shouldn't have since parameters are invalid.", c1.containsField("tooLong"));
	assertFalse("c1 renamed field newf1; Shouldn't have since parameters are invalid.", c1.containsField("reallyLong"));
	assertEquals("c1 has more or less than four fields.", 4, c1.getFields().size());
	
	// Rename field with non-existing class
	renameFieldCommands[2] = "c3";
	renameFieldCommands[3] = "newf1";
	renameFieldCommands[4] = "wrongf1";
	executeCommand(renameFieldCommands);
	assertTrue("c1 no longer has newf1; Should still have it after an error.", c1.containsField("newf1"));
	assertFalse("c1 contains wrongf1; Shouldn't have since class c3 doesn't exist.", c1.containsField("wrongf1"));
	assertEquals("CLI has more or less than one classes.", 1, model.getEntities().size());
	assertEquals("c1 has more or less than four fields.", 4, c1.getFields().size());
	
	// Rename field with non-existing field
	renameFieldCommands[2] = "c1";
	renameFieldCommands[3] = "f5";
	executeCommand(renameFieldCommands);
	assertFalse("CLI broke; Should not have", c1.containsField("f5"));
	assertFalse("c1 contains wrongf1; Should not since f5 doesn't exist", c1.containsField("wrongf1"));
	assertEquals("c1 has more or less than 4 fields.", 4, c1.getFields().size());	

	// Rename field in separate classes (with different or same name fields)
	classCommands[2] = "c2";
	executeCreateCommand(classCommands);
	Entity c2 = model.getEntities().get(1);
	createFieldCommands[2] = "c2";
	createFieldCommands[5] = "newf1";
	executeCreateCommand(createFieldCommands);
	createFieldCommands[5] = "f5";
	executeCreateCommand(createFieldCommands);
	renameFieldCommands[2] = "c2";
	renameFieldCommands[3] = "f5";
	renameFieldCommands[4] = "newf5";
	executeCommand(renameFieldCommands);
	assertFalse("c2 has a field named f5.", c2.containsField("f5"));
	assertTrue("c2 does not have a field named newf5.", c2.containsField("newf5"));
	assertEquals("c1 made more or less than four fields.", 4, c1.getFields().size());
	assertEquals("c2 made more or less than two fields.", 2, c2.getFields().size());
	renameFieldCommands[3] = "newf1";
	renameFieldCommands[4] = "newf3";
	executeCommand(renameFieldCommands);
	assertFalse("c2 has a field named newf1.", c2.containsField("newf1"));
	assertTrue("c2 does not have a field named newf3.", c2.containsField("newf3"));
	assertTrue("c1 does not have a field named newf3.", c1.containsField("newf3"));
	assertEquals("c1 made more or less than four fields.", 4, c1.getFields().size());
	assertEquals("c2 made more or less than two fields.", 2, c2.getFields().size());
	
	// Rename to an existing field
	renameFieldCommands[3] = "newf3";
	renameFieldCommands[4] = "newf5";
	executeCommand(renameFieldCommands);
	assertTrue("c2 does not have a field named newf5.", c2.containsField("newf5"));
	assertTrue("c2 does not have a field named newf3.", c2.containsField("newf3"));
	assertTrue("c1 does not have a field named newf3.", c1.containsField("newf3"));
	assertEquals("c1 made more or less than four fields.", 4, c1.getFields().size());
	assertEquals("c2 made more or less than two fields.", 2, c2.getFields().size());
	
    }
    
    @Test
    public void renameMethodTest() {
	String[] classCommands = {"create", "class", "c1"};
	executeCreateCommand(classCommands);
	Entity c1 = model.getEntities().get(0);
	String[] createMethodCommands = {"create", "method", "c1", "public", "int", "m1"};
	executeCreateCommand(createMethodCommands);
	
	// Rename one method
	String[] renameMethodCommands = {"rename", "method", "c1", "m1", "newm1"};
	executeCommand(renameMethodCommands);
	assertFalse("c1 still has a m1; Should not have any more m1.", c1.containsMethod("m1"));
	assertTrue("c1 still has a m1; Should not have any more m1.", c1.containsMethod("newm1"));
	assertEquals("c1 has more or less than one methods.", 1, c1.getMethods().size());
	
	// Rename multiple methods
	createMethodCommands[5] = "m2";
	executeCreateCommand(createMethodCommands);
	createMethodCommands[5] = "m3";
	executeCreateCommand(createMethodCommands);
	createMethodCommands[5] = "m4";
	executeCreateCommand(createMethodCommands);
	renameMethodCommands[3] = "m4";
	renameMethodCommands[4] = "newm4";
	executeCommand(renameMethodCommands);
	renameMethodCommands[3] = "m3";
	renameMethodCommands[4] = "newm3";
	executeCommand(renameMethodCommands);
	renameMethodCommands[3] = "m2";
	renameMethodCommands[4] = "newm2";
	executeCommand(renameMethodCommands);
	assertFalse("c1 still has method m2; Should not have it.", c1.containsMethod("m2"));
	assertFalse("c1 still has method m3; Should not have it.", c1.containsMethod("m3"));
	assertFalse("c1 still has method m4; Should not have it.", c1.containsMethod("m4"));
	assertTrue("c1 doesn't have method newm2; Should have it.", c1.containsMethod("newm2"));
	assertTrue("c1 doesn't have method newm3; Should have it.", c1.containsMethod("newm3"));
	assertTrue("c1 doesn't have method newm4; Should have it.", c1.containsMethod("newm4"));
	assertEquals("c1 has more or less than four methods.", 4, c1.getMethods().size());
	
	// Rename method with wrong length of commands
	String[] invalidLengthCommand = {"rename", "method", "c1", "newm1", "tooLong", "reallyLong"};
	executeCommand(invalidLengthCommand);
	assertTrue("c1 no longer has newm1; Should still have it after an error.", c1.containsMethod("newm1"));
	assertFalse("c1 renamed method newm1; Shouldn't have since parameters are invalid.", c1.containsMethod("tooLong"));
	assertFalse("c1 renamed method newm1; Shouldn't have since parameters are invalid.", c1.containsMethod("reallyLong"));
	assertEquals("c1 has more or less than four methods.", 4, c1.getMethods().size());
	
	// Rename method with non-existing class
	renameMethodCommands[2] = "c3";
	renameMethodCommands[3] = "newm1";
	renameMethodCommands[4] = "wrongm1";
	executeCommand(renameMethodCommands);
	assertTrue("c1 no longer has newm1; Should still have it after an error.", c1.containsMethod("newm1"));
	assertFalse("c1 contains wrongm1; Shouldn't have since class c3 doesn't exist.", c1.containsMethod("wrongm1"));
	assertEquals("CLI has more or less than one classes.", 1, model.getEntities().size());
	assertEquals("c1 has more or less than four methods.", 4, c1.getMethods().size());
	
	// Rename method with non-existing method
	renameMethodCommands[2] = "c1";
	renameMethodCommands[3] = "m5";
	executeCommand(renameMethodCommands);
	assertFalse("CLI broke; Should not have", c1.containsMethod("m5"));
	assertFalse("c1 contains wrongm1; Should not since m5 doesn't exist", c1.containsMethod("wrongm1"));
	assertEquals("c1 has more or less than 4 method", 4, c1.getMethods().size());	

	// Rename method in separate classes (with different or same name methods)
	classCommands[2] = "c2";
	executeCreateCommand(classCommands);
	Entity c2 = model.getEntities().get(1);
	createMethodCommands[2] = "c2";
	createMethodCommands[5] = "newm1";
	executeCreateCommand(createMethodCommands);
	createMethodCommands[5] = "m5";
	executeCreateCommand(createMethodCommands);
	renameMethodCommands[2] = "c2";
	renameMethodCommands[3] = "m5";
	renameMethodCommands[4] = "newm5";
	executeCommand(renameMethodCommands);
	
	assertFalse("c2 has a method named m5.", c2.containsMethod("m5"));
	assertTrue("c2 does not have a method named newm5.", c2.containsMethod("newm5"));
	assertEquals("c1 made more or less than four classes.", 4, c1.getMethods().size());
	assertEquals("c2 made more or less than two classes.", 2, c2.getMethods().size());
	renameMethodCommands[3] = "newm1";
	renameMethodCommands[4] = "newm3";
	executeCommand(renameMethodCommands);
	assertFalse("c2 has a method named newm1.", c2.containsMethod("newm1"));
	assertTrue("c2 does not have a method named newm3.", c2.containsMethod("newm3"));
	assertTrue("c1 does not have a method named newm3.", c1.containsMethod("newm3"));
	assertEquals("c1 made more or less than four classes.", 4, c1.getMethods().size());
	assertEquals("c2 made more or less than two classes.", 2, c2.getMethods().size());
	
	// Rename to an existing method
	renameMethodCommands[3] = "newm3";
	renameMethodCommands[4] = "newm5";
	executeCommand(renameMethodCommands);
	assertTrue("c2 does not have a method named newm5.", c2.containsMethod("newm5"));
	assertTrue("c2 does not have a method named newm3.", c2.containsMethod("newm3"));
	assertTrue("c1 does not have a method named newm3.", c1.containsMethod("newm3"));
	assertEquals("c1 made more or less than four classes.", 4, c1.getMethods().size());
	assertEquals("c2 made more or less than two classes.", 2, c2.getMethods().size());
	
    }
    
    @Test
    public void renameParameterTest() {
	String[] classCommands = {"create", "class", "c1"};
	executeCreateCommand(classCommands);
	Entity c1 = model.getEntities().get(0);
	String[] methodCommands = {"create", "method", "c1", "public", "int", "m1"};
	executeCreateCommand(methodCommands);
	Method m1 = c1.getMethods().get(0);
	String[] parameterCommands = {"create", "parameter", "c1", "m1", "int", "p1"};
	executeCreateCommand(parameterCommands);
	
	// Rename one parameter
	String[] renameParameterCommands = {"rename", "parameter", "c1", "m1", "p1", "newp1"};
	executeCommand(renameParameterCommands);
	assertTrue("CLI did not rename a parameter; Should be one parameter called newp1.", m1.containsParameter("newp1"));
	assertFalse("CLI did not get rid of p1.", m1.containsParameter("p1"));
	assertEquals("c1 made more or less than one methods.", 1, c1.getMethods().size());
	assertEquals("m1 made more or less than one parameter.", 1, m1.getParameters().size());
	
	// Rename back to original name
	renameParameterCommands[4] = "newp1";
	renameParameterCommands[5] = "p1";
	executeCommand(renameParameterCommands);
	assertFalse("CLI did not rename a parameter; Should be one parameter called p1.", m1.containsParameter("newp1"));
	assertTrue("CLI did not get change to p1.", m1.containsParameter("p1"));
	assertEquals("c1 made more or less than one methods.", 1, c1.getMethods().size());
	assertEquals("m1 made more or less than one parameter.", 1, m1.getParameters().size());
	
	// Rename multiple parameters
	parameterCommands[5] = "p2";
	executeCreateCommand(parameterCommands);
	parameterCommands[5] = "p3";
	executeCreateCommand(parameterCommands);
	parameterCommands[5] = "p4";
	executeCreateCommand(parameterCommands);
	renameParameterCommands[4] = "p2";
	renameParameterCommands[5] = "newp2";
	executeCommand(renameParameterCommands);
	renameParameterCommands[4] = "p3";
	renameParameterCommands[5] = "newp3";
	executeCommand(renameParameterCommands);
	renameParameterCommands[4] = "p4";
	renameParameterCommands[5] = "newp4";
	executeCommand(renameParameterCommands);
	assertFalse("CLI did not get rid of p2.", m1.containsParameter("p2"));
	assertFalse("CLI did not get rid of p3.", m1.containsParameter("p3"));
	assertFalse("CLI did not get rid of p4.", m1.containsParameter("p4"));
	assertTrue("CLI did not make a parameter; Should be one parameter called newp2.", m1.containsParameter("newp2"));
	assertTrue("CLI did not make a parameter; Should be one parameter called newp3.", m1.containsParameter("newp3"));
	assertTrue("CLI did not make a parameter; Should be one parameter called newp4.", m1.containsParameter("newp4"));
	assertEquals("m1 has more or less than 4 parameters", 4, m1.getParameters().size());
	
	// Rename parameter with wrong length of commands
	String[] invalidLengthCommand = {"rename", "parameter", "c1", "m1", "newp4", "tooLong", "reallyLong"};
	executeCommand(invalidLengthCommand);
	assertTrue("CLI deleted a parameter; Shouldn't have since too many arguments.", m1.containsParameter("newp4"));
	assertFalse("CLI made a parameter; Shouldn't have since too many arguments.", m1.containsParameter("tooLong"));
	assertFalse("CLI made a paremeter; Shouldn't have since too many arguments.", m1.containsParameter("reallyLong"));
	assertEquals("CLI made a parameter; Shouldn't have since too many arguments.", 4, m1.getParameters().size());
	
	// Rename parameter with non-existing class
	renameParameterCommands[2] = "c3";
	renameParameterCommands[4] = "newp4";
	renameParameterCommands[5] = "p5";
	executeCommand(renameParameterCommands);
	assertEquals("CLI has more or less than one class", 1, model.getEntities().size());
	assertTrue("m1 no longer has new p4.", m1.containsParameter("newp4"));
	assertFalse("CLI made a parameter in a class that doesn't exist; Should not have", m1.containsParameter("p5"));
	assertEquals("m1 has more or less than 4 methods.", 4, m1.getParameters().size());
	
	// Rename parameter with non-existing method
	renameParameterCommands[2] = "c1";
	renameParameterCommands[3] = "m2";
	executeCommand(renameParameterCommands);
	assertEquals("CLI has more or less than one class", 1, model.getEntities().size());
	assertEquals("CLI has more or less than one method", 1, c1.getMethods().size());
	assertTrue("c1 no longer contains method m1", c1.containsMethod("m1"));
	assertTrue("m1 no longer has new p4.", m1.containsParameter("newp4"));
	assertFalse("CLI made a parameter in a method that doesn't exist; Should not have", m1.containsParameter("p5"));
	assertEquals("m1 has more or less than 4 methods.", 4, m1.getParameters().size());
	
	// Rename parameter with non-existing parameter
	renameParameterCommands[3] = "m1";
	renameParameterCommands[4] = "p5";
	renameParameterCommands[5] = "newp5";
	executeCommand(renameParameterCommands);
	assertFalse("CLI broke; Should not have", m1.containsParameter("p5"));
	assertFalse("CLI broke; Should not have", m1.containsParameter("newp5"));
	assertEquals("m1 has more or less than 4 parameters", 4, m1.getParameters().size());
	
	// Rename parameter with same name
	renameParameterCommands[3] = "m1";
	renameParameterCommands[4] = "newp2";
	renameParameterCommands[5] = "p1";
	executeCommand(renameParameterCommands);
	assertTrue("CLI broke; should not have", m1.containsParameter("p1"));
	assertTrue("CLI got rid of newp2; should not have", m1.containsParameter("newp2"));
	assertEquals("CLI made a parameter; Parameter name already existed.", 4, m1.getParameters().size());
	
	// Rename Parameter in separate classes/methods (with same and/or different names)
	methodCommands[5] = "m2";
	executeCreateCommand(methodCommands);
	classCommands[2] = "c2";
	executeCreateCommand(classCommands);
	Entity c2 = model.getEntities().get(1);
	methodCommands[2] = "c2";
	methodCommands[5] = "m3";
	executeCreateCommand(methodCommands);
	parameterCommands[3] = "m2";
	executeCreateCommand(parameterCommands);
	parameterCommands[2] = "c2";
	parameterCommands[3] = "m3";
	executeCreateCommand(parameterCommands);
	parameterCommands[5] = "newp1";
	executeCreateCommand(parameterCommands);
	renameParameterCommands[3] = "m2";
	renameParameterCommands[4] = "p4";
	renameParameterCommands[5] = "newp4";
	executeCommand(renameParameterCommands);
	renameParameterCommands[2] = "c2";
	renameParameterCommands[3] = "m3";
	renameParameterCommands[4] = "p4";
	renameParameterCommands[5] = "newp4";
	executeCommand(renameParameterCommands);
	renameParameterCommands[4] = "newp1";
	renameParameterCommands[5] = "p5";
	executeCommand(renameParameterCommands);	
	assertTrue("CLI broke; should not have.", m1.containsParameter("newp4"));
	assertEquals("m1 has more or less than four parameters.", 4, m1.getParameters().size());
	assertFalse("CLI did not get rid of a parameter in m2; Should be one parameter called newp4 in m2.", c1.getMethods().get(1).containsParameter("p4"));
	assertTrue("CLI did not rename a parameter in m2; Should be one parameter called newp4 in m2.", c1.getMethods().get(1).containsParameter("newp4"));
	assertEquals("m2 has more or less than one parameter.", 1, c1.getMethods().get(1).getParameters().size());
	assertFalse("CLI did not get rid of a parameter in m3; Should be a parameter called newp4 in m3.", c2.getMethods().get(0).containsParameter("p4"));
	assertTrue("CLI did not rename a parameter in m3; Should be a parameter called newp4 in m3.", c2.getMethods().get(0).containsParameter("newp4"));
	assertFalse("CLI did not get rid of a parameter in m3; Should be a parameter called np5 in m3.", c2.getMethods().get(0).containsParameter("newp5"));
	assertTrue("CLI did not rename a parameter in m3; Should be a parameter called p5 in m3.", c2.getMethods().get(0).containsParameter("p5"));
	assertEquals("m3 has more or less than two parameter.", 2, c2.getMethods().get(0).getParameters().size());
	
    }
    
}

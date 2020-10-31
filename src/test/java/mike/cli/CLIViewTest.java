package mike.testcases;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mike.datastructures.Model;
import mike.datastructures.Entity;
import mike.datastructures.Method;
import mike.datastructures.Relationship.Type;

import mike.view.CLIView;
import mike.view.ViewTemplate;

public class CLIViewTest {
    
    Model model;
    CLIView cli;
    
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();

    private final PrintStream origOut = System.out;
    private final PrintStream origErr = System.err;
    
    @Before
    public void createCLI() throws IOException {
	System.setOut(new PrintStream(out));
	System.setErr(new PrintStream(err));
	model = new Model();
	cli = (CLIView) new ViewTemplate(ViewTemplate.InterfaceType.CLI, model).getViewinterface();
    }
    
    @After
    public void cleanCLI() {
	System.setOut(origOut);
	System.setErr(origErr);
	String[] commands = {"sudo", "clear"};
	cli.evaluateCommand(commands);
    }
    
    @Test
    public void initClassTest(){
	assertTrue("CLI is null; Should not be null.", cli != null);
	assertTrue("CLI's model is not empty; Should be empty.", cli.getCLIModel().empty());
    }
    
    @Test
    public void createClassTest() {
	String[] commands = {"create", "class", "c1"};
	cli.evaluateCommand(commands);
	assertTrue("CLI did not make a class; Should be one class called c1.", model.containsEntity("c1"));
	assertEquals("CLI made more or less than one class", 1, model.getEntities().size());
	
	commands[2] = "c2";
	cli.evaluateCommand(commands);
	commands[2] = "c3";
	cli.evaluateCommand(commands);
	commands[2] = "c4";
	cli.evaluateCommand(commands);
	commands[2] = "c5";
	cli.evaluateCommand(commands);
	
	assertTrue("CLI did not make a class; Should be class called c2.", model.containsEntity("c2"));
	assertTrue("CLI did not make a class; Should be class called c3.", model.containsEntity("c3"));
	assertTrue("CLI did not make a class; Should be class called c4.", model.containsEntity("c4"));
	assertTrue("CLI did not make a class; Should be class called c5.", model.containsEntity("c5"));
	assertEquals("CLI did not make classes; Should be 5 classes.", 5, model.getEntities().size());
	
	// Test NOT creating with invalid length
        String[] invalidLengthCommand = {"create", "class", "badLength", "Yes."};
        cli.evaluateCommand(invalidLengthCommand);

        assertFalse("CLI created a class with bad length; Class named 'badLength' should not exist.", model.containsEntity("badLength"));
        assertEquals("CLI made a class; Should be 5 classes.", 5, model.getEntities().size());

        // Test NOT making an EXISTING class
        commands[2] = "c1";
        cli.evaluateCommand(commands);

        assertEquals("CLI created a class that already existed; Length should be 5.", 5, model.getEntities().size());
    }
    
    @Test
    public void renameClassTest() {
        // Test setup of class creation
        String[] commands = {"create", "class", "c1"};
        cli.evaluateCommand(commands);
        commands[2] = "c2";
        cli.evaluateCommand(commands);

        assertTrue("CLI did not make a class; Should be class called c1.", model.containsEntity("c1"));
        assertTrue("CLI did not make a class; Should be class called c2.", model.containsEntity("c2"));

        // Test rename a class
        String[] rename = {"rename", "class", "c1", "c3"};
        cli.evaluateCommand(rename);

        assertTrue("CLI did not rename a class; Should be class called c3.", model.containsEntity("c3"));
        assertFalse("CLI did not remove/rename old class; c1 should no longer exist.", model.containsEntity("c1"));

        // Test NOT renaming with invalid length
        String[] invalidLengthCommand = {"create", "class", "c3", "c1", "badLength"};
        cli.evaluateCommand(invalidLengthCommand);

        assertFalse("CLI created a class with bad length; Class named 'c1' should not exist.", model.containsEntity("c1"));
        assertEquals("CLI made a class; Should be 2 classes.", 2, model.getEntities().size());


        // Test rename a NONEXISTENT class
        rename[2] = "c4";
        rename[3] = "c5";
        cli.evaluateCommand(rename);

        assertTrue("CLI renamed a class. c2 should exist.", model.containsEntity("c2"));
        assertTrue("CLI renamed a class. c3 should exist.", model.containsEntity("c3"));
        assertFalse("CLI created a new class. c4 should not exist.", model.containsEntity("c4"));
        assertFalse("CLI created a new class. c5 should not exist.", model.containsEntity("c5"));

        // Test rename an EXISTING class to another EXISTING class
        rename[2] = "c3";
        rename[3] = "c2";
        cli.evaluateCommand(rename);

        assertTrue("CLI renamed a class. c2 should exist.", model.containsEntity("c2"));
        assertTrue("CLI renamed a class. c3 should exist.", model.containsEntity("c3"));
        assertEquals("CLI does not have 2 classes. Model should have two entities.", 2, model.getEntities().size());
    }
    
    @Test
    public void deleteClassTest() {
        // Test setup of class creation
        String[] commands = {"create", "class", "c1"};
        cli.evaluateCommand(commands);
        commands[2] = "c2";
        cli.evaluateCommand(commands);

        assertTrue("CLI did not make a class; Should be class called c1.", model.containsEntity("c1"));
        assertTrue("CLI did not make a class; Should be class called c2.", model.containsEntity("c2"));

        // Test deleting a class
        String[] delete = {"delete", "class", "c1"};
        cli.evaluateCommand(delete);

        assertFalse("CLI did not delete a class; c1 should not exist.", model.containsEntity("c1"));
        assertTrue("CLI did something to another class; c2 should be okay.", model.containsEntity("c2"));

        // Test NOT deleting with invalid length
        String[] invalidLengthCommand = {"create", "class", "c2", "badLength"};
        cli.evaluateCommand(invalidLengthCommand);

        assertTrue("CLI deleted a class with bad length; Class named 'c2' should exist.", model.containsEntity("c2"));

        // Test NOT deleting a NONEXISTENT class
        cli.evaluateCommand(delete);

        assertTrue("CLI did deleted a class it wasn't supposed to; Class 'c2' should exist.", model.containsEntity("c2"));

        //Test deleting classes that have relationships
        //Ensure relationships are also deleted

    }
 
    //**********************************************************************************************//
    //**                                FIELD TESTS                                               **//
    //**                         (CREATE, RENAME, DELETE)                                         **//
    //**********************************************************************************************//
    
    @Test
    public void createFieldTest() {
	String[] classCommands = {"create", "class", "c1"};
	cli.evaluateCommand(classCommands);
	Entity c1 = model.getEntities().get(0);
	
	// Create one field
	String[] fieldCommands = {"create", "field", "c1", "int", "f1"};
	cli.evaluateCommand(fieldCommands);
	assertTrue("CLI did not make a field; Should be one field called f1.", c1.containsField("f1"));
	assertEquals("c1 made more or less than one field.", 1, c1.getFields().size());
	
	// Create multiple fields
	fieldCommands[4] = "f2";
	cli.evaluateCommand(fieldCommands);
	fieldCommands[4] = "f3";
	cli.evaluateCommand(fieldCommands);
	fieldCommands[4] = "f4";
	cli.evaluateCommand(fieldCommands);
	assertTrue("CLI did not make a field; Should be one field called f2.", c1.containsField("f2"));
	assertTrue("CLI did not make a field; Should be one field called f3.", c1.containsField("f3"));
	assertTrue("CLI did not make a field; Should be one field called f4.", c1.containsField("f4"));
	assertEquals("c1 has more or less than 4 fields", 4, c1.getFields().size());
	
	// Create field with wrong length of commands
	String[] invalidLengthCommand = {"create", "field", "c1", "int", "tooLong", "badLength"};
	cli.evaluateCommand(invalidLengthCommand);
	assertFalse("CLI made a field; Shouldn't have since too many arguments.", c1.containsField("badLength"));
	assertFalse("CLI made a field; Shouldn't have since too many arguments.", c1.containsField("tooLong"));
	assertEquals("CLI made a field; Shouldn't have since too many arguments.", 4, c1.getFields().size());
	
	// Create field with non-existing class
	fieldCommands[2] = "c3";
	fieldCommands[4] = "f6";
	cli.evaluateCommand(fieldCommands);
	assertEquals("CLI has more or less than one class", 1, model.getEntities().size());
	assertFalse("CLI made a field in a class that doesn't exist; Should not have", c1.containsField("f6"));
	assertEquals("c1 has more or less than 4 fields.", 4, c1.getFields().size());
	
	// Create field with same name
	fieldCommands[4] = "f1";
	cli.evaluateCommand(fieldCommands);
	assertEquals("CLI made a field; Field name already existed.", 4, c1.getFields().size());
	
	// Create field in separate classes
	classCommands[2] = "c2";
	cli.evaluateCommand(classCommands);
	Entity c2 = model.getEntities().get(1);
	fieldCommands[2] = "c2";
	fieldCommands[4] = "f5";
	cli.evaluateCommand(fieldCommands);
	assertTrue("CLI did not make a field; Should be one field called f5.", c2.containsField("f5"));
	assertEquals("c2 has more or less than one field.", 1, c2.getFields().size());
	
	// Can create the same field name in another class
	fieldCommands[4] = "f1";
	cli.evaluateCommand(fieldCommands);
	assertTrue("CLI did not make a field; Should be one field called f1.", c2.containsField("f1"));
	assertTrue("CLI deleted field f5; f5 should still exist.", c2.containsField("f5"));
	assertEquals("c2 has more or less than two fields.", 2, c2.getFields().size());

	// Can not create a field with same name, but different type
	fieldCommands[3] = "int";
	fieldCommands[4] = "f5";
	cli.evaluateCommand(fieldCommands);
	assertTrue("CLI broke", c2.containsField("f5"));
	assertEquals("c2 has more or less than two fields.", 2, c2.getFields().size());
    }

    @Test
    public void renameFieldTest() {
	String[] classCommands = {"create", "class", "c1"};
	cli.evaluateCommand(classCommands);
	Entity c1 = model.getEntities().get(0);
	String[] createFieldCommands = {"create", "field", "c1", "int", "f1"};
	cli.evaluateCommand(createFieldCommands);
	
	// Rename one field
	String[] renameFieldCommands = {"rename", "field", "c1", "f1", "newf1"};
	cli.evaluateCommand(renameFieldCommands);
	assertFalse("c1 still has a f1; Should not have any more f1.", c1.containsField("f1"));
	assertTrue("c1 still has a f1; Should not have any more f1.", c1.containsField("newf1"));
	assertEquals("c1 has more or less than one fields.", 1, c1.getFields().size());
	
	// Rename multiple fields
	createFieldCommands[4] = "f2";
	cli.evaluateCommand(createFieldCommands);
	createFieldCommands[4] = "f3";
	cli.evaluateCommand(createFieldCommands);
	createFieldCommands[4] = "f4";
	cli.evaluateCommand(createFieldCommands);
	renameFieldCommands[3] = "f4";
	renameFieldCommands[4] = "newf4";
	cli.evaluateCommand(renameFieldCommands);
	renameFieldCommands[3] = "f3";
	renameFieldCommands[4] = "newf3";
	cli.evaluateCommand(renameFieldCommands);
	renameFieldCommands[3] = "f2";
	renameFieldCommands[4] = "newf2";
	cli.evaluateCommand(renameFieldCommands);
	assertFalse("c1 still has field f2; Should not have it.", c1.containsField("f2"));
	assertFalse("c1 still has field f3; Should not have it.", c1.containsField("f3"));
	assertFalse("c1 still has field f4; Should not have it.", c1.containsField("f4"));
	assertTrue("c1 doesn't have field newf2; Should have it.", c1.containsField("newf2"));
	assertTrue("c1 doesn't have field newf3; Should have it.", c1.containsField("newf3"));
	assertTrue("c1 doesn't have field newf4; Should have it.", c1.containsField("newf4"));
	assertEquals("c1 has more or less than four fields.", 4, c1.getFields().size());
	
	// Rename field with wrong length of commands
	String[] invalidLengthCommand = {"rename", "field", "c1", "newf1", "tooLong", "reallyLong"};
	cli.evaluateCommand(invalidLengthCommand);
	assertTrue("c1 no longer has newf1; Should still have it after an error.", c1.containsField("newf1"));
	assertFalse("c1 renamed field newf1; Shouldn't have since parameters are invalid.", c1.containsField("tooLong"));
	assertFalse("c1 renamed field newf1; Shouldn't have since parameters are invalid.", c1.containsField("reallyLong"));
	assertEquals("c1 has more or less than four fields.", 4, c1.getFields().size());
	
	// Rename field with non-existing class
	renameFieldCommands[2] = "c3";
	renameFieldCommands[3] = "newf1";
	renameFieldCommands[4] = "wrongf1";
	cli.evaluateCommand(renameFieldCommands);
	assertTrue("c1 no longer has newf1; Should still have it after an error.", c1.containsField("newf1"));
	assertFalse("c1 contains wrongf1; Shouldn't have since class c3 doesn't exist.", c1.containsField("wrongf1"));
	assertEquals("CLI has more or less than one classes.", 1, model.getEntities().size());
	assertEquals("c1 has more or less than four fields.", 4, c1.getFields().size());
	
	// Rename field with non-existing field
	renameFieldCommands[2] = "c1";
	renameFieldCommands[3] = "f5";
	cli.evaluateCommand(renameFieldCommands);
	assertFalse("CLI broke; Should not have", c1.containsField("f5"));
	assertFalse("c1 contains wrongf1; Should not since f5 doesn't exist", c1.containsField("wrongf1"));
	assertEquals("c1 has more or less than 4 fields.", 4, c1.getFields().size());	

	// Rename field in separate classes (with different or same name fields)
	classCommands[2] = "c2";
	cli.evaluateCommand(classCommands);
	Entity c2 = model.getEntities().get(1);
	createFieldCommands[2] = "c2";
	createFieldCommands[4] = "newf1";
	cli.evaluateCommand(createFieldCommands);
	createFieldCommands[4] = "f5";
	cli.evaluateCommand(createFieldCommands);
	renameFieldCommands[2] = "c2";
	renameFieldCommands[3] = "f5";
	renameFieldCommands[4] = "newf5";
	cli.evaluateCommand(renameFieldCommands);
	assertFalse("c2 has a field named f5.", c2.containsField("f5"));
	assertTrue("c2 does not have a field named newf5.", c2.containsField("newf5"));
	assertEquals("c1 made more or less than four fields.", 4, c1.getFields().size());
	assertEquals("c2 made more or less than two fields.", 2, c2.getFields().size());
	renameFieldCommands[3] = "newf1";
	renameFieldCommands[4] = "newf3";
	cli.evaluateCommand(renameFieldCommands);
	assertFalse("c2 has a field named newf1.", c2.containsField("newf1"));
	assertTrue("c2 does not have a field named newf3.", c2.containsField("newf3"));
	assertTrue("c1 does not have a field named newf3.", c1.containsField("newf3"));
	assertEquals("c1 made more or less than four fields.", 4, c1.getFields().size());
	assertEquals("c2 made more or less than two fields.", 2, c2.getFields().size());
	
	// Rename to an existing field
	renameFieldCommands[3] = "newf3";
	renameFieldCommands[4] = "newf5";
	cli.evaluateCommand(renameFieldCommands);
	assertTrue("c2 does not have a field named newf5.", c2.containsField("newf5"));
	assertTrue("c2 does not have a field named newf3.", c2.containsField("newf3"));
	assertTrue("c1 does not have a field named newf3.", c1.containsField("newf3"));
	assertEquals("c1 made more or less than four fields.", 4, c1.getFields().size());
	assertEquals("c2 made more or less than two fields.", 2, c2.getFields().size());
	
    }
    
    @Test
    public void deleteFieldTest() {
	String[] classCommands = {"create", "class", "c1"};
	cli.evaluateCommand(classCommands);
	Entity c1 = model.getEntities().get(0);
	String[] createFieldCommands = {"create", "field", "c1", "int", "f1"};
	cli.evaluateCommand(createFieldCommands);
	
	// Delete one field
	String[] deleteFieldCommands = {"delete", "field", "c1", "f1"};
	cli.evaluateCommand(deleteFieldCommands);
	assertFalse("CLI still has a field; Should not have any more fields.", c1.containsField("f1"));
	assertEquals("c1 has more or less than zero fields.", 0, c1.getFields().size());
	
	// Delete multiple fields
	createFieldCommands[4] = "f2";
	cli.evaluateCommand(createFieldCommands);
	createFieldCommands[4] = "f3";
	cli.evaluateCommand(createFieldCommands);
	createFieldCommands[4] = "f4";
	cli.evaluateCommand(createFieldCommands);
	deleteFieldCommands[3] = "f4";
	cli.evaluateCommand(deleteFieldCommands);
	deleteFieldCommands[3] = "f3";
	cli.evaluateCommand(deleteFieldCommands);
	deleteFieldCommands[3] = "f2";
	cli.evaluateCommand(deleteFieldCommands);
	assertFalse("CLI still has a field; Should not have any more fields.", c1.containsField("f2"));
	assertFalse("CLI still has a field; Should not have any more fields.", c1.containsField("f3"));
	assertFalse("CLI still has a field; Should not have any more fields.", c1.containsField("f4"));
	assertEquals("c1 has more or less than zero fields.", 0, c1.getFields().size());
	
	// Delete field with wrong length of commands
	createFieldCommands[4] = "f5";
	cli.evaluateCommand(createFieldCommands);
	String[] invalidLengthCommand = {"delete", "field", "c1", "f5", "tooLong"};
	cli.evaluateCommand(invalidLengthCommand);
	assertTrue("CLI deleted field f5; Shouldn't have since parameters are invalid.", c1.containsField("f5"));
	assertEquals("CLI deleted a field; Shouldn't have since too many arguments.", 1, c1.getFields().size());
	
	// Delete field with non-existing class
	deleteFieldCommands[2] = "c3";
	deleteFieldCommands[3] = "f5";
	cli.evaluateCommand(deleteFieldCommands);
	assertTrue("CLI deleted a field in a class that doesn't exist; Should not have", c1.containsField("f5"));
	assertEquals("c1 has more or less than 1 fields.", 1, c1.getFields().size());
	
	// Delete field with non-existing field
	deleteFieldCommands[2] = "c1";
	deleteFieldCommands[3] = "f6";
	cli.evaluateCommand(deleteFieldCommands);
	assertFalse("CLI broke; Should not have", c1.containsField("f6"));
	assertTrue("CLI deleted the wrong field when the inserted field doesn't exist", c1.containsField("f5"));
	assertEquals("c1 has more or less than 1 field", 1, c1.getFields().size());	
	
	// Delete field in separate classes
	classCommands[2] = "c2";
	cli.evaluateCommand(classCommands);
	Entity c2 = model.getEntities().get(1);
	createFieldCommands[2] = "c2";
	createFieldCommands[4] = "f5";
	cli.evaluateCommand(createFieldCommands);
	createFieldCommands[4] = "f6";
	cli.evaluateCommand(createFieldCommands);
	deleteFieldCommands[2] = "c2";
	deleteFieldCommands[3] = "f5";
	cli.evaluateCommand(deleteFieldCommands);
	assertFalse("CLI did not delete a field; Should have deleted f5 in c2.", c2.containsField("f5"));
	assertTrue("CLI deleted a field; Should have deleted f5 in c1.", c1.containsField("f5"));
	assertEquals("c1 made more or less than one field.", 1, c1.getFields().size());
	assertEquals("c2 made more or less than one field.", 1, c2.getFields().size());
	
    }
    
    //**********************************************************************************************//
    //**                                METHOD TESTS                                              **//
    //**                         (CREATE, RENAME, DELETE)                                         **//
    //**********************************************************************************************//
    
    @Test
    public void createMethodTest() {
	String[] classCommands = {"create", "class", "c1"};
	cli.evaluateCommand(classCommands);
	Entity c1 = model.getEntities().get(0);
	
	// Create one method
	String[] methodCommands = {"create", "method", "c1", "int", "m1"};
	cli.evaluateCommand(methodCommands);
	assertTrue("CLI did not make a method; Should be one method called m1.", c1.containsMethod("m1"));
	assertEquals("c1 made more or less than one method.", 1, c1.getMethods().size());
	
	// Create multiple methods
	methodCommands[4] = "m2";
	cli.evaluateCommand(methodCommands);
	methodCommands[4] = "m3";
	cli.evaluateCommand(methodCommands);
	methodCommands[4] = "m4";
	cli.evaluateCommand(methodCommands);
	assertTrue("CLI did not make a method; Should be one method called m2.", c1.containsMethod("m2"));
	assertTrue("CLI did not make a method; Should be one method called m3.", c1.containsMethod("m3"));
	assertTrue("CLI did not make a method; Should be one method called m4.", c1.containsMethod("m4"));
	assertEquals("c1 has more or less than 4 methods", 4, c1.getMethods().size());
	
	// Create method with wrong length of commands
	String[] invalidLengthCommand = {"create", "method", "c1", "int", "tooLong", "badLength"};
	cli.evaluateCommand(invalidLengthCommand);
	assertFalse("CLI made a method; Shouldn't have since too many arguments.", c1.containsMethod("badLength"));
	assertFalse("CLI made a method; Shouldn't have since too many arguments.", c1.containsMethod("tooLong"));
	assertEquals("CLI made a method; Shouldn't have since too many arguments.", 4, c1.getMethods().size());
	
	// Create method with non-existing class
	methodCommands[2] = "c3";
	methodCommands[4] = "m6";
	cli.evaluateCommand(methodCommands);
	assertEquals("CLI has more or less than one class", 1, model.getEntities().size());
	assertFalse("CLI made a method in a class that doesn't exist; Should not have", c1.containsMethod("m6"));
	assertEquals("c1 has more or less than 4 methods.", 4, c1.getMethods().size());
	
	// Create method with same name
	methodCommands[4] = "m1";
	cli.evaluateCommand(methodCommands);
	assertEquals("CLI made a method; Method name already existed.", 4, c1.getMethods().size());
	
	// Create method in separate classes
	classCommands[2] = "c2";
	cli.evaluateCommand(classCommands);
	Entity c2 = model.getEntities().get(1);
	methodCommands[2] = "c2";
	methodCommands[4] = "m5";
	cli.evaluateCommand(methodCommands);
	assertTrue("CLI did not make a method; Should be one method called m5.", c2.containsMethod("m5"));
	assertEquals("c2 has more or less than one method.", 1, c2.getMethods().size());
	
	// Can create the same method name in another class
	methodCommands[4] = "m1";
	cli.evaluateCommand(methodCommands);
	assertTrue("CLI did not make a method; Should be one method called m1.", c2.containsMethod("m1"));
	assertTrue("CLI deleted method m5; m5 should still exist.", c2.containsMethod("m5"));
	assertEquals("c2 has more or less than two methods.", 2, c2.getMethods().size());

	// Can not create a method with same name, but different type
	methodCommands[3] = "int";
	methodCommands[4] = "m5";
	cli.evaluateCommand(methodCommands);
	assertTrue("CLI broke", c2.containsMethod("m5"));
	assertEquals("c2 has more or less than two methods.", 2, c2.getMethods().size());
    }

    @Test
    public void renameMethodTest() {
	String[] classCommands = {"create", "class", "c1"};
	cli.evaluateCommand(classCommands);
	Entity c1 = model.getEntities().get(0);
	String[] createMethodCommands = {"create", "method", "c1", "int", "m1"};
	cli.evaluateCommand(createMethodCommands);
	
	// Rename one method
	String[] renameMethodCommands = {"rename", "method", "c1", "m1", "newm1"};
	cli.evaluateCommand(renameMethodCommands);
	assertFalse("c1 still has a m1; Should not have any more m1.", c1.containsMethod("m1"));
	assertTrue("c1 still has a m1; Should not have any more m1.", c1.containsMethod("newm1"));
	assertEquals("c1 has more or less than one methods.", 1, c1.getMethods().size());
	
	// Rename multiple methods
	createMethodCommands[4] = "m2";
	cli.evaluateCommand(createMethodCommands);
	createMethodCommands[4] = "m3";
	cli.evaluateCommand(createMethodCommands);
	createMethodCommands[4] = "m4";
	cli.evaluateCommand(createMethodCommands);
	renameMethodCommands[3] = "m4";
	renameMethodCommands[4] = "newm4";
	cli.evaluateCommand(renameMethodCommands);
	renameMethodCommands[3] = "m3";
	renameMethodCommands[4] = "newm3";
	cli.evaluateCommand(renameMethodCommands);
	renameMethodCommands[3] = "m2";
	renameMethodCommands[4] = "newm2";
	cli.evaluateCommand(renameMethodCommands);
	assertFalse("c1 still has method m2; Should not have it.", c1.containsMethod("m2"));
	assertFalse("c1 still has method m3; Should not have it.", c1.containsMethod("m3"));
	assertFalse("c1 still has method m4; Should not have it.", c1.containsMethod("m4"));
	assertTrue("c1 doesn't have method newm2; Should have it.", c1.containsMethod("newm2"));
	assertTrue("c1 doesn't have method newm3; Should have it.", c1.containsMethod("newm3"));
	assertTrue("c1 doesn't have method newm4; Should have it.", c1.containsMethod("newm4"));
	assertEquals("c1 has more or less than four methods.", 4, c1.getMethods().size());
	
	// Rename method with wrong length of commands
	String[] invalidLengthCommand = {"rename", "method", "c1", "newm1", "tooLong", "reallyLong"};
	cli.evaluateCommand(invalidLengthCommand);
	assertTrue("c1 no longer has newm1; Should still have it after an error.", c1.containsMethod("newm1"));
	assertFalse("c1 renamed method newm1; Shouldn't have since parameters are invalid.", c1.containsMethod("tooLong"));
	assertFalse("c1 renamed method newm1; Shouldn't have since parameters are invalid.", c1.containsMethod("reallyLong"));
	assertEquals("c1 has more or less than four methods.", 4, c1.getMethods().size());
	
	// Rename method with non-existing class
	renameMethodCommands[2] = "c3";
	renameMethodCommands[3] = "newm1";
	renameMethodCommands[4] = "wrongm1";
	cli.evaluateCommand(renameMethodCommands);
	assertTrue("c1 no longer has newm1; Should still have it after an error.", c1.containsMethod("newm1"));
	assertFalse("c1 contains wrongm1; Shouldn't have since class c3 doesn't exist.", c1.containsMethod("wrongm1"));
	assertEquals("CLI has more or less than one classes.", 1, model.getEntities().size());
	assertEquals("c1 has more or less than four methods.", 4, c1.getMethods().size());
	
	// Rename method with non-existing method
	renameMethodCommands[2] = "c1";
	renameMethodCommands[3] = "m5";
	cli.evaluateCommand(renameMethodCommands);
	assertFalse("CLI broke; Should not have", c1.containsMethod("m5"));
	assertFalse("c1 contains wrongm1; Should not since m5 doesn't exist", c1.containsMethod("wrongm1"));
	assertEquals("c1 has more or less than 4 method", 4, c1.getMethods().size());	

	// Rename method in separate classes (with different or same name methods)
	classCommands[2] = "c2";
	cli.evaluateCommand(classCommands);
	Entity c2 = model.getEntities().get(1);
	createMethodCommands[2] = "c2";
	createMethodCommands[4] = "newm1";
	cli.evaluateCommand(createMethodCommands);
	createMethodCommands[4] = "m5";
	cli.evaluateCommand(createMethodCommands);
	renameMethodCommands[2] = "c2";
	renameMethodCommands[3] = "m5";
	renameMethodCommands[4] = "newm5";
	cli.evaluateCommand(renameMethodCommands);
	
	assertFalse("c2 has a method named m5.", c2.containsMethod("m5"));
	assertTrue("c2 does not have a method named newm5.", c2.containsMethod("newm5"));
	assertEquals("c1 made more or less than four classes.", 4, c1.getMethods().size());
	assertEquals("c2 made more or less than two classes.", 2, c2.getMethods().size());
	renameMethodCommands[3] = "newm1";
	renameMethodCommands[4] = "newm3";
	cli.evaluateCommand(renameMethodCommands);
	assertFalse("c2 has a method named newm1.", c2.containsMethod("newm1"));
	assertTrue("c2 does not have a method named newm3.", c2.containsMethod("newm3"));
	assertTrue("c1 does not have a method named newm3.", c1.containsMethod("newm3"));
	assertEquals("c1 made more or less than four classes.", 4, c1.getMethods().size());
	assertEquals("c2 made more or less than two classes.", 2, c2.getMethods().size());
	
	// Rename to an existing method
	renameMethodCommands[3] = "newm3";
	renameMethodCommands[4] = "newm5";
	cli.evaluateCommand(renameMethodCommands);
	assertTrue("c2 does not have a method named newm5.", c2.containsMethod("newm5"));
	assertTrue("c2 does not have a method named newm3.", c2.containsMethod("newm3"));
	assertTrue("c1 does not have a method named newm3.", c1.containsMethod("newm3"));
	assertEquals("c1 made more or less than four classes.", 4, c1.getMethods().size());
	assertEquals("c2 made more or less than two classes.", 2, c2.getMethods().size());
	
    }
    
    @Test
    public void deleteMethodTest() {
	String[] classCommands = {"create", "class", "c1"};
	cli.evaluateCommand(classCommands);
	Entity c1 = model.getEntities().get(0);
	String[] createMethodCommands = {"create", "method", "c1", "int", "m1"};
	cli.evaluateCommand(createMethodCommands);
	
	// Delete one method
	String[] deleteMethodCommands = {"delete", "method", "c1", "m1"};
	cli.evaluateCommand(deleteMethodCommands);
	assertFalse("CLI still has a method; Should not have any more methods.", c1.containsMethod("m1"));
	assertEquals("c1 has more or less than zero methods.", 0, c1.getMethods().size());
	
	// Delete multiple methods
	createMethodCommands[4] = "m2";
	cli.evaluateCommand(createMethodCommands);
	createMethodCommands[4] = "m3";
	cli.evaluateCommand(createMethodCommands);
	createMethodCommands[4] = "m4";
	cli.evaluateCommand(createMethodCommands);
	deleteMethodCommands[3] = "m4";
	cli.evaluateCommand(deleteMethodCommands);
	deleteMethodCommands[3] = "m3";
	cli.evaluateCommand(deleteMethodCommands);
	deleteMethodCommands[3] = "m2";
	cli.evaluateCommand(deleteMethodCommands);
	assertFalse("CLI still has a method; Should not have any more methods.", c1.containsMethod("m2"));
	assertFalse("CLI still has a method; Should not have any more methods.", c1.containsMethod("m3"));
	assertFalse("CLI still has a method; Should not have any more methods.", c1.containsMethod("m4"));
	assertEquals("c1 has more or less than zero methods.", 0, c1.getMethods().size());
	
	// Delete method with wrong length of commands
	createMethodCommands[4] = "m5";
	cli.evaluateCommand(createMethodCommands);
	String[] invalidLengthCommand = {"delete", "method", "c1", "m5", "tooLong"};
	cli.evaluateCommand(invalidLengthCommand);
	assertTrue("CLI deleted method m5; Shouldn't have since parameters are invalid.", c1.containsMethod("m5"));
	assertEquals("CLI deleted a method; Shouldn't have since too many arguments.", 1, c1.getMethods().size());
	
	// Delete method with non-existing class
	deleteMethodCommands[2] = "c3";
	deleteMethodCommands[3] = "m5";
	cli.evaluateCommand(deleteMethodCommands);
	assertTrue("CLI deleted a method in a class that doesn't exist; Should not have", c1.containsMethod("m5"));
	assertEquals("c1 has more or less than 1 methods.", 1, c1.getMethods().size());
	
	// Delete method with non-existing method
	deleteMethodCommands[2] = "c1";
	deleteMethodCommands[3] = "m6";
	cli.evaluateCommand(deleteMethodCommands);
	assertFalse("CLI broke; Should not have", c1.containsMethod("m6"));
	assertTrue("CLI deleted the wrong method when the inserted method doesn't exist", c1.containsMethod("m5"));
	assertEquals("c1 has more or less than 1 method", 1, c1.getMethods().size());	
	
	// Delete method in separate classes
	classCommands[2] = "c2";
	cli.evaluateCommand(classCommands);
	Entity c2 = model.getEntities().get(1);
	createMethodCommands[2] = "c2";
	createMethodCommands[4] = "m5";
	cli.evaluateCommand(createMethodCommands);
	createMethodCommands[4] = "m6";
	cli.evaluateCommand(createMethodCommands);
	deleteMethodCommands[2] = "c2";
	deleteMethodCommands[3] = "m5";
	cli.evaluateCommand(deleteMethodCommands);
	assertFalse("CLI did not delete a method; Should have deleted m5 in c2.", c2.containsMethod("m5"));
	assertTrue("CLI deleted a method; Should have deleted m5 in c1.", c1.containsMethod("m5"));
	assertEquals("c1 made more or less than one class.", 1, c1.getMethods().size());
	assertEquals("c2 made more or less than one class.", 1, c2.getMethods().size());
	
    }
    
    //**********************************************************************************************//
    //**                              PARAMETER TESTS                                             **//
    //**                         (CREATE, RENAME, DELETE)                                         **//
    //**********************************************************************************************//
    
    @Test
    public void createParameterTest() {
	String[] classCommands = {"create", "class", "c1"};
	cli.evaluateCommand(classCommands);
	Entity c1 = model.getEntities().get(0);
	String[] methodCommands = {"create", "method", "c1", "int", "m1"};
	cli.evaluateCommand(methodCommands);
	Method m1 = c1.getMethods().get(0);
	
	// Create one parameter
	String[] parameterCommands = {"create", "parameter", "c1", "m1", "String", "p1"};
	cli.evaluateCommand(parameterCommands);
	assertTrue("CLI did not make a parameter; Should be one parameter called p1.", m1.containsParameter("p1"));
	assertEquals("c1 made more or less than one methods.", 1, c1.getMethods().size());
	assertEquals("m1 made more or less than one parameter.", 1, m1.getParameters().size());
	
	// Create multiple parameters
	parameterCommands[5] = "p2";
	cli.evaluateCommand(parameterCommands);
	parameterCommands[5] = "p3";
	cli.evaluateCommand(parameterCommands);
	parameterCommands[5] = "p4";
	cli.evaluateCommand(parameterCommands);
	assertTrue("CLI did not make a parameter; Should be one parameter called p2.", m1.containsParameter("p2"));
	assertTrue("CLI did not make a parameter; Should be one parameter called p3.", m1.containsParameter("p3"));
	assertTrue("CLI did not make a parameter; Should be one parameter called p4.", m1.containsParameter("p4"));
	assertEquals("m1 has more or less than 4 parameters", 4, m1.getParameters().size());
	
	// Create parameter with wrong length of commands
	String[] invalidLengthCommand = {"create", "parameter", "c1", "m1", "int", "p5", "tooLong"};
	cli.evaluateCommand(invalidLengthCommand);
	assertFalse("CLI made a method; Shouldn't have since too many arguments.", m1.containsParameter("p5"));
	assertFalse("CLI made a method; Shouldn't have since too many arguments.", m1.containsParameter("tooLong"));
	assertEquals("CLI made a method; Shouldn't have since too many arguments.", 4, m1.getParameters().size());
	
	// Create parameter with non-existing class
	parameterCommands[2] = "c3";
	parameterCommands[5] = "p5";
	cli.evaluateCommand(parameterCommands);
	assertEquals("CLI has more or less than one class", 1, model.getEntities().size());
	assertFalse("CLI made a parameter in a class that doesn't exist; Should not have", m1.containsParameter("p5"));
	assertEquals("m1 has more or less than 4 methods.", 4, m1.getParameters().size());
	
	// Create parameter with non-existing method
	parameterCommands[2] = "c1";
	parameterCommands[3] = "m2";
	cli.evaluateCommand(parameterCommands);
	assertEquals("CLI has more or less than one class", 1, model.getEntities().size());
	assertEquals("CLI has more or less than one class", 1, c1.getMethods().size());
	assertTrue("c1 no longer contains method m1", c1.containsMethod("m1"));
	assertFalse("CLI made a parameter in a method that doesn't exist; Should not have", m1.containsParameter("p5"));
	assertEquals("m1 has more or less than 4 methods.", 4, m1.getParameters().size());
	
	// Create parameter with same name
	parameterCommands[3] = "m1";
	parameterCommands[5] = "p1";
	cli.evaluateCommand(methodCommands);
	assertTrue("CLI broke; should not have", m1.containsParameter("p1"));
	assertEquals("CLI made a parameter; Parameter name already existed.", 4, m1.getParameters().size());
	
	// Create Parameter in separate classes/methods (with same and/or different names)
	methodCommands[4] = "m2";
	cli.evaluateCommand(methodCommands);
	classCommands[2] = "c2";
	cli.evaluateCommand(classCommands);
	Entity c2 = model.getEntities().get(1);
	methodCommands[2] = "c2";
	methodCommands[4] = "m3";
	cli.evaluateCommand(methodCommands);
	parameterCommands[3] = "m2";
	cli.evaluateCommand(parameterCommands);
	parameterCommands[2] = "c2";
	parameterCommands[3] = "m3";
	cli.evaluateCommand(parameterCommands);
	parameterCommands[5] = "newp1";
	cli.evaluateCommand(parameterCommands);
	assertTrue("CLI broke; should not have.", m1.containsParameter("p1"));
	assertEquals("m1 has more or less than four parameters.", 4, m1.getParameters().size());
	assertTrue("CLI did not make a parameter in m2; Should be one parameter called p1 in m2.", c1.getMethods().get(1).containsParameter("p1"));
	assertEquals("m2 has more or less than one parameter.", 1, c1.getMethods().get(1).getParameters().size());
	assertTrue("CLI did not make a parameter in m3; Should be one parameter called p1 in m3.", c2.getMethods().get(0).containsParameter("p1"));
	assertTrue("CLI did not make a parameter in m3; Should be one parameter called p1 in m3.", c2.getMethods().get(0).containsParameter("newp1"));
	assertEquals("m3 has more or less than two parameter.", 2, c2.getMethods().get(0).getParameters().size());
	
	// Create a parameter with same name, but different type
	parameterCommands[4] = "int";
	parameterCommands[5] = "newp1";
	cli.evaluateCommand(parameterCommands);
	assertTrue("CLI did not break", c2.getMethods().get(0).containsParameter("newp1"));
	assertEquals("m3 has more or less than two parameter.", 2, c2.getMethods().get(0).getParameters().size());
	
    }
    
    @Test
    public void renameParameterTest() {
	String[] classCommands = {"create", "class", "c1"};
	cli.evaluateCommand(classCommands);
	Entity c1 = model.getEntities().get(0);
	String[] methodCommands = {"create", "method", "c1", "int", "m1"};
	cli.evaluateCommand(methodCommands);
	Method m1 = c1.getMethods().get(0);
	String[] parameterCommands = {"create", "parameter", "c1", "m1", "int", "p1"};
	cli.evaluateCommand(parameterCommands);
	
	// Rename one parameter
	String[] renameParameterCommands = {"rename", "parameter", "c1", "m1", "p1", "newp1"};
	cli.evaluateCommand(renameParameterCommands);
	assertTrue("CLI did not rename a parameter; Should be one parameter called newp1.", m1.containsParameter("newp1"));
	assertFalse("CLI did not get rid of p1.", m1.containsParameter("p1"));
	assertEquals("c1 made more or less than one methods.", 1, c1.getMethods().size());
	assertEquals("m1 made more or less than one parameter.", 1, m1.getParameters().size());
	
	// Rename back to original name
	renameParameterCommands[4] = "newp1";
	renameParameterCommands[5] = "p1";
	cli.evaluateCommand(renameParameterCommands);
	assertFalse("CLI did not rename a parameter; Should be one parameter called p1.", m1.containsParameter("newp1"));
	assertTrue("CLI did not get change to p1.", m1.containsParameter("p1"));
	assertEquals("c1 made more or less than one methods.", 1, c1.getMethods().size());
	assertEquals("m1 made more or less than one parameter.", 1, m1.getParameters().size());
	
	// Rename multiple parameters
	parameterCommands[5] = "p2";
	cli.evaluateCommand(parameterCommands);
	parameterCommands[5] = "p3";
	cli.evaluateCommand(parameterCommands);
	parameterCommands[5] = "p4";
	cli.evaluateCommand(parameterCommands);
	renameParameterCommands[4] = "p2";
	renameParameterCommands[5] = "newp2";
	cli.evaluateCommand(renameParameterCommands);
	renameParameterCommands[4] = "p3";
	renameParameterCommands[5] = "newp3";
	cli.evaluateCommand(renameParameterCommands);
	renameParameterCommands[4] = "p4";
	renameParameterCommands[5] = "newp4";
	cli.evaluateCommand(renameParameterCommands);
	assertFalse("CLI did not get rid of p2.", m1.containsParameter("p2"));
	assertFalse("CLI did not get rid of p3.", m1.containsParameter("p3"));
	assertFalse("CLI did not get rid of p4.", m1.containsParameter("p4"));
	assertTrue("CLI did not make a parameter; Should be one parameter called newp2.", m1.containsParameter("newp2"));
	assertTrue("CLI did not make a parameter; Should be one parameter called newp3.", m1.containsParameter("newp3"));
	assertTrue("CLI did not make a parameter; Should be one parameter called newp4.", m1.containsParameter("newp4"));
	assertEquals("m1 has more or less than 4 parameters", 4, m1.getParameters().size());
	
	// Rename parameter with wrong length of commands
	String[] invalidLengthCommand = {"rename", "parameter", "c1", "m1", "newp4", "tooLong", "reallyLong"};
	cli.evaluateCommand(invalidLengthCommand);
	assertTrue("CLI deleted a parameter; Shouldn't have since too many arguments.", m1.containsParameter("newp4"));
	assertFalse("CLI made a parameter; Shouldn't have since too many arguments.", m1.containsParameter("tooLong"));
	assertFalse("CLI made a paremeter; Shouldn't have since too many arguments.", m1.containsParameter("reallyLong"));
	assertEquals("CLI made a parameter; Shouldn't have since too many arguments.", 4, m1.getParameters().size());
	
	// Rename parameter with non-existing class
	renameParameterCommands[2] = "c3";
	renameParameterCommands[4] = "newp4";
	renameParameterCommands[5] = "p5";
	cli.evaluateCommand(renameParameterCommands);
	assertEquals("CLI has more or less than one class", 1, model.getEntities().size());
	assertTrue("m1 no longer has new p4.", m1.containsParameter("newp4"));
	assertFalse("CLI made a parameter in a class that doesn't exist; Should not have", m1.containsParameter("p5"));
	assertEquals("m1 has more or less than 4 methods.", 4, m1.getParameters().size());
	
	// Rename parameter with non-existing method
	renameParameterCommands[2] = "c1";
	renameParameterCommands[3] = "m2";
	cli.evaluateCommand(renameParameterCommands);
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
	cli.evaluateCommand(renameParameterCommands);
	assertFalse("CLI broke; Should not have", m1.containsParameter("p5"));
	assertFalse("CLI broke; Should not have", m1.containsParameter("newp5"));
	assertEquals("m1 has more or less than 4 parameters", 4, m1.getParameters().size());
	
	// Rename parameter with same name
	renameParameterCommands[3] = "m1";
	renameParameterCommands[4] = "newp2";
	renameParameterCommands[5] = "p1";
	cli.evaluateCommand(renameParameterCommands);
	assertTrue("CLI broke; should not have", m1.containsParameter("p1"));
	assertTrue("CLI got rid of newp2; should not have", m1.containsParameter("newp2"));
	assertEquals("CLI made a parameter; Parameter name already existed.", 4, m1.getParameters().size());
	
	// Rename Parameter in separate classes/methods (with same and/or different names)
	methodCommands[4] = "m2";
	cli.evaluateCommand(methodCommands);
	classCommands[2] = "c2";
	cli.evaluateCommand(classCommands);
	Entity c2 = model.getEntities().get(1);
	methodCommands[2] = "c2";
	methodCommands[4] = "m3";
	cli.evaluateCommand(methodCommands);
	parameterCommands[3] = "m2";
	cli.evaluateCommand(parameterCommands);
	parameterCommands[2] = "c2";
	parameterCommands[3] = "m3";
	cli.evaluateCommand(parameterCommands);
	parameterCommands[5] = "newp1";
	cli.evaluateCommand(parameterCommands);
	renameParameterCommands[3] = "m2";
	renameParameterCommands[4] = "p4";
	renameParameterCommands[5] = "newp4";
	cli.evaluateCommand(renameParameterCommands);
	renameParameterCommands[2] = "c2";
	renameParameterCommands[3] = "m3";
	renameParameterCommands[4] = "p4";
	renameParameterCommands[5] = "newp4";
	cli.evaluateCommand(renameParameterCommands);
	renameParameterCommands[4] = "newp1";
	renameParameterCommands[5] = "p5";
	cli.evaluateCommand(renameParameterCommands);	
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
    
    @Test
    public void deleteParameterTest() {
	String[] classCommands = {"create", "class", "c1"};
	cli.evaluateCommand(classCommands);
	Entity c1 = model.getEntities().get(0);
	String[] methodCommands = {"create", "method", "c1", "int", "m1"};
	cli.evaluateCommand(methodCommands);
	Method m1 = c1.getMethods().get(0);
	String[] parameterCommands = {"create", "parameter", "c1", "m1", "int", "p1"};
	cli.evaluateCommand(parameterCommands);
	
	// Delete one parameter
	String[] deleteParameterCommands = {"delete", "parameter", "c1", "m1", "p1"};
	cli.evaluateCommand(deleteParameterCommands);
	assertFalse("CLI did not get rid of p1.", m1.containsParameter("p1"));
	assertEquals("c1 made more or less than one methods.", 1, c1.getMethods().size());
	assertEquals("m1 made more or less than zero parameter.", 0, m1.getParameters().size());
	
	// Delete multiple parameters
	parameterCommands[5] = "p1";
	cli.evaluateCommand(parameterCommands);
	parameterCommands[5] = "p2";
	cli.evaluateCommand(parameterCommands);
	parameterCommands[5] = "p3";
	cli.evaluateCommand(parameterCommands);
	deleteParameterCommands[4] = "p3";
	cli.evaluateCommand(deleteParameterCommands);
	deleteParameterCommands[4] = "p2";
	cli.evaluateCommand(deleteParameterCommands);
	deleteParameterCommands[4] = "p1";
	cli.evaluateCommand(deleteParameterCommands);
	assertFalse("CLI did not get rid of p2.", m1.containsParameter("p1"));
	assertFalse("CLI did not get rid of p3.", m1.containsParameter("p2"));
	assertFalse("CLI did not get rid of p4.", m1.containsParameter("p3"));
	assertEquals("m1 has more or less than zero parameters.", 0, m1.getParameters().size());
	
	// Delete parameter with wrong length of commands
	parameterCommands[5] = "p1";
	cli.evaluateCommand(parameterCommands);
	String[] invalidLengthCommand = {"delete", "parameter", "c1", "m1", "p1", "tooLong"};
	cli.evaluateCommand(invalidLengthCommand);
	assertTrue("CLI deleted a parameter; Shouldn't have since too many arguments.", m1.containsParameter("p1"));
	assertFalse("CLI made a parameter; Shouldn't have since too many arguments.", m1.containsParameter("tooLong"));
	assertEquals("m1 has more or less than one parameter.", 1, m1.getParameters().size());
	
	// Delete parameter with non-existing class
	deleteParameterCommands[2] = "c3";
	cli.evaluateCommand(deleteParameterCommands);
	assertEquals("CLI has more or less than one class", 1, model.getEntities().size());
	assertTrue("m1 no longer has a p1.", m1.containsParameter("p1"));
	assertEquals("m1 has more or less than 1 parameters.", 1, m1.getParameters().size());
	
	// Delete parameter with non-existing method
	deleteParameterCommands[2] = "c1";
	deleteParameterCommands[3] = "m2";
	cli.evaluateCommand(deleteParameterCommands);
	assertEquals("CLI has more or less than one class", 1, model.getEntities().size());
	assertEquals("CLI has more or less than one method", 1, c1.getMethods().size());
	assertTrue("c1 no longer contains method m1", c1.containsMethod("m1"));
	assertTrue("m1 no longer has new p4.", m1.containsParameter("p1"));
	assertEquals("m1 has more or less than one parameter.", 1, m1.getParameters().size());
	
	// Delete parameter with non-existing parameter
	deleteParameterCommands[3] = "m1";
	deleteParameterCommands[4] = "p2";
	cli.evaluateCommand(deleteParameterCommands);
	assertFalse("CLI broke; Should not have", m1.containsParameter("p2"));
	assertEquals("m1 has more or less than one parameters", 1, m1.getParameters().size());
	
	// Delete parameter in separate classes/methods (with same and/or different names)
	methodCommands[4] = "m2";
	cli.evaluateCommand(methodCommands);
	classCommands[2] = "c2";
	cli.evaluateCommand(classCommands);
	Entity c2 = model.getEntities().get(1);
	methodCommands[2] = "c2";
	methodCommands[4] = "m3";
	cli.evaluateCommand(methodCommands);
	parameterCommands[3] = "m2";
	cli.evaluateCommand(parameterCommands);
	parameterCommands[2] = "c2";
	parameterCommands[3] = "m3";
	cli.evaluateCommand(parameterCommands);
	parameterCommands[5] = "newp1";
	cli.evaluateCommand(parameterCommands);
	deleteParameterCommands[2] = "c2";
	deleteParameterCommands[3] = "m3";
	deleteParameterCommands[4] = "newp1";
	cli.evaluateCommand(deleteParameterCommands);
	deleteParameterCommands[4] = "p1";
	cli.evaluateCommand(deleteParameterCommands);
	deleteParameterCommands[2] = "c1";
	deleteParameterCommands[3] = "m2";
	cli.evaluateCommand(deleteParameterCommands);
	deleteParameterCommands[3] = "m1";
	cli.evaluateCommand(deleteParameterCommands);
	assertFalse("CLI broke; should not have.", m1.containsParameter("p1"));
	assertEquals("m1 has more or less than zero parameters.", 0, m1.getParameters().size());
	assertFalse("CLI did not get rid of a parameter in m2; Should be no more parameters in m2.", c1.getMethods().get(1).containsParameter("p1"));
	assertEquals("m2 has more or less than zero parameters.", 0, c1.getMethods().get(1).getParameters().size());
	assertFalse("CLI did not get rid of a parameter in m3; Should be no more parameters in m3.", c2.getMethods().get(0).containsParameter("p1"));
	assertFalse("CLI did not get rid of a parameter in m3; Should be no more parameters in m3.", c2.getMethods().get(0).containsParameter("newp1"));
	assertEquals("m3 has more or less than zero parameter.", 0, c2.getMethods().get(0).getParameters().size());
    	
    }
    
    //**********************************************************************************************//
    //**                             RELATIONSHIP TESTS                                           **//
    //**                              (CREATE, DELETE)                                            **//
    //**********************************************************************************************//

    @Test
    public void createRelationshipTest() {
        //create classes to have relationships
        String[] classes = {"create", "class", "c1"};
        cli.evaluateCommand(classes);
        classes[2] = "c2";
        cli.evaluateCommand(classes);
        classes[2] = "c3";
        cli.evaluateCommand(classes);

        //attempting to create relationships to non-existent classes
        String[] wrongRels = {"create", "relationship", "aggregation", "fake1", "fake2"};
        cli.evaluateCommand(wrongRels);
        assertTrue("Relationship was created with fake classes", model.getRelationships().isEmpty());

        //creating relationship with invalid type
        wrongRels[2] = "fake";
        wrongRels[3] = "c1";
        wrongRels[4] = "c2";
        cli.evaluateCommand(wrongRels);
        assertTrue("Relationship was created with invalid type", model.getRelationships().isEmpty());

        //create relationships of each type
        String[] relationships = {"create", "relationship", "aggregation", "c1", "c2"};
        cli.evaluateCommand(relationships);
        assertTrue("Aggregation relationship between c1 and c2 not found", model.containsRelationship(Type.AGGREGATION, "c1", "c2"));

        relationships[2] = "composition";
        relationships[3] = "c2";
        relationships[4] = "c3";

        cli.evaluateCommand(relationships);
        assertTrue("Composition relationship between c2 and c3 not found", model.containsRelationship(Type.COMPOSITION, "c2", "c3"));

        relationships[2] = "inheritance";
        relationships[3] = "c1";

        cli.evaluateCommand(relationships);
        assertTrue("Inheritance relationship between c1 and c3 not found", model.containsRelationship(Type.INHERITANCE, "c1", "c3"));

        //recursive relationship
        relationships[2] = "realization";
        relationships[3] = "c2";
        relationships[4] = "c2";

        cli.evaluateCommand(relationships);
        assertTrue("Recursive realization relationship associated with c2 not found", model.containsRelationship(Type.REALIZATION, "c2", "c2"));

        //attempting to create a relationship that already exists
        cli.evaluateCommand(relationships);
        assertEquals("Recursive realization c2 relationship created twice", 4, model.getRelationships().size());

        relationships[2] = "composition";
        assertEquals("New c2 recursive relationship created, even though c2 already has a recursive relationship", 4, model.getRelationships().size());
    }

    @Test
    public void deleteRelationshipTest() {
        //create classes to have relationships
        String[] classes = {"create", "class", "c1"};
        cli.evaluateCommand(classes);
        classes[2] = "c2";
        cli.evaluateCommand(classes);
        classes[2] = "c3";
        cli.evaluateCommand(classes);

        //create relationships (c1--c2, c2--c3, c3--c3)
        String[] relationships = {"create", "relationship", "aggregation", "c1", "c2"};
        cli.evaluateCommand(relationships);

        relationships[2] = "realization";
        relationships[3] = "c2";
        relationships[4] = "c3";
        cli.evaluateCommand(relationships);

        relationships[2] = "inheritance";
        relationships[3] = "c3";
        cli.evaluateCommand(relationships);

        //deleting non-existent relationships
        relationships[0] = "delete";
        relationships[2] = "fakeType";
        cli.evaluateCommand(relationships);
        assertTrue("c3 recursive relationship deleted despite fake relationship type", model.containsRelationship(Type.INHERITANCE, "c3", "c3"));

        relationships[2] = "realization";
        cli.evaluateCommand(relationships);
        assertTrue("Relationship c3--c3 deleted despite wrong relationship type given", model.containsRelationship(Type.INHERITANCE, "c3", "c3"));

        relationships[3] = "fake1";
        relationships[4] = "fake2";
        cli.evaluateCommand(relationships);
        assertEquals("Relationship deleted despite fake class names", 3, model.getRelationships().size());

        //deleting relationships
        relationships[2] = "realization";
        relationships[3] = "c2";
        relationships[4] = "c3";
        cli.evaluateCommand(relationships);
        assertFalse("Relationship c2--c3 failed to delete", model.containsRelationship(Type.REALIZATION, "c2", "c3"));
        assertEquals("Size of relationship list is wrong", 2, model.getRelationships().size());

        relationships[2] = "inheritance";
        relationships[3] = "c3";
        cli.evaluateCommand(relationships);
        assertFalse("Relationship c3--c3 failed to delete", model.containsRelationship(Type.INHERITANCE, "c3", "c3"));
        assertEquals("Size of relationship list is wrong", 1, model.getRelationships().size());
    }

    @Test
    public void clearTest() {
        // Make a whole bunch of stuff to be cleared.
        String[] classes = {"create", "class", "c1"};
        cli.evaluateCommand(classes);
        classes[2] = "c2";
        cli.evaluateCommand(classes);

        assertEquals("CLI did not make the classes; Should be 2.", 2, model.getEntities().size());

        String[] field = {"create", "field", "c1", "int", "f1"};
        cli.evaluateCommand(field);
        field[4] = "f2";
        cli.evaluateCommand(field);
        field[2] = "c2";
        field[4] = "f1";
        cli.evaluateCommand(field);

        assertTrue("CLI did not make 2 fields in c1; C1 should have f1 and f2.", model.getEntities().get(0).containsField("f1"));
        assertTrue("CLI did not make 2 fields in c1; C1 should have f1 and f2.", model.getEntities().get(0).containsField("f2"));
        assertTrue("CLI did not make 1 field in c2; C2 should have f1.", model.getEntities().get(1).containsField("f1"));

        String[] method = {"create", "method", "c1", "double", "m1"};
        cli.evaluateCommand(method);
        method[4] = "m2";
        cli.evaluateCommand(method);
        method[2] = "c2";
        method[4] = "m1";
        cli.evaluateCommand(method);

        assertTrue("CLI did not make 2 methods in c1; C1 should have m1 and m2.", model.getEntities().get(0).containsMethod("m1"));
        assertTrue("CLI did not make 2 methods in c1; C1 should have m1 and m2.", model.getEntities().get(0).containsMethod("m2"));
        assertTrue("CLI did not make 1 method in c2; C2 should have m1.", model.getEntities().get(1).containsMethod("m1"));

        String[] parameter = {"create", "parameter", "c1", "m1", "char", "p1"};
        cli.evaluateCommand(parameter);
        parameter[5] = "p2";
        cli.evaluateCommand(parameter);
        parameter[3] = "m2";
        parameter[5] = "p1";
        cli.evaluateCommand(parameter);
        parameter[2] = "c2";
        parameter[3] = "m1";
        cli.evaluateCommand(parameter);

        assertTrue("CLI did not make 2 parameters in c1 > m1; M1 should have p1 and p2.", model.getEntities().get(0).getMethods().get(0).containsParameter("p1"));
        assertTrue("CLI did not make 2 parameters in c1 > m1; M1 should have p1 and p2.", model.getEntities().get(0).getMethods().get(0).containsParameter("p2"));
        assertTrue("CLI did not make 1 parameter in c1 > m2; M2 should have p1.", model.getEntities().get(0).getMethods().get(1).containsParameter("p1"));
        assertTrue("CLI did not make 1 parameter in c2 > m1; M1 should have p1.", model.getEntities().get(1).getMethods().get(0).containsParameter("p1"));

        String[] relationship = {"create", "relationship", "realization", "c1", "c1"};
        cli.evaluateCommand(relationship);
        relationship[2] = "aggregation";
        relationship[4] = "c2";
        cli.evaluateCommand(relationship);

        assertTrue("CLI did not make a recursive relationship. There should be a realization from c1 to c1.", model.containsRelationship(Type.REALIZATION, "c1", "c1"));
        assertTrue("CLI did not make a normal relationship. There should be an aggregation from c1 to c2.", model.containsRelationship(Type.AGGREGATION, "c1", "c2"));


        String[] clear = {"sudo", "clear"};
        cli.evaluateCommand(clear);

        assertTrue("CLI's model is not empty; CLI's model should be empty.", model.empty());
    }
}

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
import mike.datastructures.Relationship.Type;
import mike.view.CLIView;
import mike.view.ViewTemplate;

public class CreateCommandTest {

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
    
    private void resetStreams() {
        out.reset();
        err.reset();
    }
    
    
    private void executeCommand(String[] commands) {
	CreateCommand create = new CreateCommand(model, view, commands, prompt);
	prompt = create.execute();
    }
    
    @Test
    public void createClassTest() {
	// Test creating one class
	String[] commands = {"create", "class", "c1"};
	executeCommand(commands);
	assertTrue("CLI did not make a class; Should be one class called c1.", model.containsEntity("c1"));
	assertEquals("CLI made more or less than one class", 1, model.getEntities().size());
	
	// Test creating 4 more classes
	commands[2] = "c2";
	executeCommand(commands);
	commands[2] = "c3";
	executeCommand(commands);
	commands[2] = "c4";
	executeCommand(commands);
	commands[2] = "c5";
	executeCommand(commands);
	
	assertTrue("CLI did not make a class; Should be class called c2.", model.containsEntity("c2"));
	assertTrue("CLI did not make a class; Should be class called c3.", model.containsEntity("c3"));
	assertTrue("CLI did not make a class; Should be class called c4.", model.containsEntity("c4"));
	assertTrue("CLI did not make a class; Should be class called c5.", model.containsEntity("c5"));
	assertEquals("CLI did not make classes; Should be 5 classes.", 5, model.getEntities().size());
	
	// Test NOT creating with invalid length
        String[] invalidLengthCommand = {"create", "class", "badLength", "Yes."};
        executeCommand(invalidLengthCommand);
        assertFalse("CLI created a class with bad length; Class named 'badLength' should not exist.", model.containsEntity("badLength"));
        assertEquals("CLI made a class; Should be 5 classes.", 5, model.getEntities().size());

        // Test NOT making an EXISTING class
        commands[2] = "c1";
        executeCommand(commands);
        assertEquals("CLI created a class that already existed; Length should be 5.", 5, model.getEntities().size());
    }
    
    @Test
    public void createFieldTest() {
	String[] classCommands = {"create", "class", "c1"};
	executeCommand(classCommands);
	Entity c1 = model.getEntities().get(0);
	
	// Create one field
	String[] fieldCommands = {"create", "field", "c1", "public", "int", "f1"};
	executeCommand(fieldCommands);
	assertTrue("CLI did not make a field; Should be one field called f1.", c1.containsField("f1"));
	assertEquals("c1 made more or less than one field.", 1, c1.getFields().size());
	
	// Create multiple fields
	fieldCommands[5] = "f2";
	executeCommand(fieldCommands);
	fieldCommands[5] = "f3";
	executeCommand(fieldCommands);
	fieldCommands[5] = "f4";
	executeCommand(fieldCommands);
	assertTrue("CLI did not make a field; Should be one field called f2.", c1.containsField("f2"));
	assertTrue("CLI did not make a field; Should be one field called f3.", c1.containsField("f3"));
	assertTrue("CLI did not make a field; Should be one field called f4.", c1.containsField("f4"));
	assertEquals("c1 has more or less than 4 fields", 4, c1.getFields().size());
	
	// Create field with wrong length of commands
	String[] invalidLengthCommand = {"create", "field", "c1", "public", "int", "tooLong", "badLength"};
	executeCommand(invalidLengthCommand);
	assertFalse("CLI made a field; Shouldn't have since too many arguments.", c1.containsField("badLength"));
	assertFalse("CLI made a field; Shouldn't have since too many arguments.", c1.containsField("tooLong"));
	assertEquals("CLI made a field; Shouldn't have since too many arguments.", 4, c1.getFields().size());
	
	// Create field with non-existing class
	fieldCommands[2] = "c3";
	fieldCommands[5] = "f6";
	executeCommand(fieldCommands);
	assertEquals("CLI has more or less than one class", 1, model.getEntities().size());
	assertFalse("CLI made a field in a class that doesn't exist; Should not have", c1.containsField("f6"));
	assertEquals("c1 has more or less than 4 fields.", 4, c1.getFields().size());
	
	// Create field with same name
	fieldCommands[5] = "f1";
	executeCommand(fieldCommands);
	assertEquals("CLI made a field; Field name already existed.", 4, c1.getFields().size());
	
	// Create field in separate classes
	classCommands[2] = "c2";
	executeCommand(classCommands);
	Entity c2 = model.getEntities().get(1);
	fieldCommands[2] = "c2";
	fieldCommands[5] = "f5";
	executeCommand(fieldCommands);
	assertTrue("CLI did not make a field; Should be one field called f5.", c2.containsField("f5"));
	assertEquals("c2 has more or less than one field.", 1, c2.getFields().size());
	
	// Can create the same field name in another class
	fieldCommands[5] = "f1";
	executeCommand(fieldCommands);
	assertTrue("CLI did not make a field; Should be one field called f1.", c2.containsField("f1"));
	assertTrue("CLI deleted field f5; f5 should still exist.", c2.containsField("f5"));
	assertEquals("c2 has more or less than two fields.", 2, c2.getFields().size());

	// Can not create a field with same name, but different type
	fieldCommands[4] = "int";
	fieldCommands[5] = "f5";
	executeCommand(fieldCommands);
	assertTrue("CLI broke", c2.containsField("f5"));
	assertEquals("c2 has more or less than two fields.", 2, c2.getFields().size());
    }
    
    @Test
    public void createMethodTest() {
	String[] classCommands = {"create", "class", "c1"};
	executeCommand(classCommands);
	Entity c1 = model.getEntities().get(0);
	
	// Create one method
	String[] methodCommands = {"create", "method", "c1", "private", "int", "m1"};
	executeCommand(methodCommands);
	assertTrue("CLI did not make a method; Should be one method called m1.", c1.containsMethod("m1"));
	assertEquals("c1 made more or less than one method.", 1, c1.getMethods().size());
	
	// Create multiple methods
	methodCommands[5] = "m2";
	executeCommand(methodCommands);
	methodCommands[5] = "m3";
	executeCommand(methodCommands);
	methodCommands[5] = "m4";
	executeCommand(methodCommands);
	assertTrue("CLI did not make a method; Should be one method called m2.", c1.containsMethod("m2"));
	assertTrue("CLI did not make a method; Should be one method called m3.", c1.containsMethod("m3"));
	assertTrue("CLI did not make a method; Should be one method called m4.", c1.containsMethod("m4"));
	assertEquals("c1 has more or less than 4 methods", 4, c1.getMethods().size());
	
	// Create method with wrong length of commands
	String[] invalidLengthCommand = {"create", "method", "c1", "public", "int", "tooLong", "badLength"};
	executeCommand(invalidLengthCommand);
	assertFalse("CLI made a method; Shouldn't have since too many arguments.", c1.containsMethod("badLength"));
	assertFalse("CLI made a method; Shouldn't have since too many arguments.", c1.containsMethod("tooLong"));
	assertEquals("CLI made a method; Shouldn't have since too many arguments.", 4, c1.getMethods().size());
	
	// Create method with non-existing class
	methodCommands[2] = "c3";
	methodCommands[5] = "m6";
	executeCommand(methodCommands);
	assertEquals("CLI has more or less than one class", 1, model.getEntities().size());
	assertFalse("CLI made a method in a class that doesn't exist; Should not have", c1.containsMethod("m6"));
	assertEquals("c1 has more or less than 4 methods.", 4, c1.getMethods().size());
	
	// Create method with same name
	methodCommands[5] = "m1";
	executeCommand(methodCommands);
	assertEquals("CLI made a method; Method name already existed.", 4, c1.getMethods().size());
	
	// Create method in separate classes
	classCommands[2] = "c2";
	executeCommand(classCommands);
	Entity c2 = model.getEntities().get(1);
	methodCommands[2] = "c2";
	methodCommands[5] = "m5";
	executeCommand(methodCommands);
	assertTrue("CLI did not make a method; Should be one method called m5.", c2.containsMethod("m5"));
	assertEquals("c2 has more or less than one method.", 1, c2.getMethods().size());
	
	// Can create the same method name in another class
	methodCommands[5] = "m1";
	executeCommand(methodCommands);
	assertTrue("CLI did not make a method; Should be one method called m1.", c2.containsMethod("m1"));
	assertTrue("CLI deleted method m5; m5 should still exist.", c2.containsMethod("m5"));
	assertEquals("c2 has more or less than two methods.", 2, c2.getMethods().size());

	// Can not create a method with same name, but different type
	methodCommands[4] = "int";
	methodCommands[5] = "m5";
	executeCommand(methodCommands);
	assertTrue("CLI broke", c2.containsMethod("m5"));
	assertEquals("c2 has more or less than two methods.", 2, c2.getMethods().size());
    }
    
    @Test
    public void createParameterTest() {
	String[] classCommands = {"create", "class", "c1"};
	executeCommand(classCommands);
	Entity c1 = model.getEntities().get(0);
	String[] methodCommands = {"create", "method", "c1", "protected", "int", "m1"};
	executeCommand(methodCommands);
	Method m1 = c1.getMethods().get(0);
	
	// Create one parameter
	String[] parameterCommands = {"create", "parameter", "c1", "m1", "String", "p1"};
	executeCommand(parameterCommands);
	assertTrue("CLI did not make a parameter; Should be one parameter called p1.", m1.containsParameter("p1"));
	assertEquals("c1 made more or less than one methods.", 1, c1.getMethods().size());
	assertEquals("m1 made more or less than one parameter.", 1, m1.getParameters().size());
	
	// Create multiple parameters
	parameterCommands[5] = "p2";
	executeCommand(parameterCommands);
	parameterCommands[5] = "p3";
	executeCommand(parameterCommands);
	parameterCommands[5] = "p4";
	executeCommand(parameterCommands);
	assertTrue("CLI did not make a parameter; Should be one parameter called p2.", m1.containsParameter("p2"));
	assertTrue("CLI did not make a parameter; Should be one parameter called p3.", m1.containsParameter("p3"));
	assertTrue("CLI did not make a parameter; Should be one parameter called p4.", m1.containsParameter("p4"));
	assertEquals("m1 has more or less than 4 parameters", 4, m1.getParameters().size());
	
	// Create parameter with wrong length of commands
	String[] invalidLengthCommand = {"create", "parameter", "c1", "m1", "int", "p5", "tooLong"};
	executeCommand(invalidLengthCommand);
	assertFalse("CLI made a method; Shouldn't have since too many arguments.", m1.containsParameter("p5"));
	assertFalse("CLI made a method; Shouldn't have since too many arguments.", m1.containsParameter("tooLong"));
	assertEquals("CLI made a method; Shouldn't have since too many arguments.", 4, m1.getParameters().size());
	
	// Create parameter with non-existing class
	parameterCommands[2] = "c3";
	parameterCommands[5] = "p5";
	executeCommand(parameterCommands);
	assertEquals("CLI has more or less than one class", 1, model.getEntities().size());
	assertFalse("CLI made a parameter in a class that doesn't exist; Should not have", m1.containsParameter("p5"));
	assertEquals("m1 has more or less than 4 methods.", 4, m1.getParameters().size());
	
	// Create parameter with non-existing method
	parameterCommands[2] = "c1";
	parameterCommands[3] = "m2";
	executeCommand(parameterCommands);
	assertEquals("CLI has more or less than one class", 1, model.getEntities().size());
	assertEquals("CLI has more or less than one class", 1, c1.getMethods().size());
	assertTrue("c1 no longer contains method m1", c1.containsMethod("m1"));
	assertFalse("CLI made a parameter in a method that doesn't exist; Should not have", m1.containsParameter("p5"));
	assertEquals("m1 has more or less than 4 methods.", 4, m1.getParameters().size());
	
	// Create parameter with same name
	parameterCommands[3] = "m1";
	parameterCommands[5] = "p1";
	executeCommand(methodCommands);
	assertTrue("CLI broke; should not have", m1.containsParameter("p1"));
	assertEquals("CLI made a parameter; Parameter name already existed.", 4, m1.getParameters().size());
	
	// Create Parameter in separate classes/methods (with same and/or different names)
	methodCommands[5] = "m2";
	executeCommand(methodCommands);
	classCommands[2] = "c2";
	executeCommand(classCommands);
	Entity c2 = model.getEntities().get(1);
	methodCommands[2] = "c2";
	methodCommands[5] = "m3";
	executeCommand(methodCommands);
	parameterCommands[3] = "m2";
	executeCommand(parameterCommands);
	parameterCommands[2] = "c2";
	parameterCommands[3] = "m3";
	executeCommand(parameterCommands);
	parameterCommands[5] = "newp1";
	executeCommand(parameterCommands);
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
	executeCommand(parameterCommands);
	assertTrue("CLI did not break", c2.getMethods().get(0).containsParameter("newp1"));
	assertEquals("m3 has more or less than two parameter.", 2, c2.getMethods().get(0).getParameters().size());
	
    }
    
    @Test
    public void createRelationshipTest() {
        //create classes to have relationships
        String[] classes = {"create", "class", "c1"};
        executeCommand(classes);
        classes[2] = "c2";
        executeCommand(classes);
        classes[2] = "c3";
        executeCommand(classes);

        //attempting to create relationships to non-existent classes
        String[] wrongRels = {"create", "relationship", "aggregation", "fake1", "fake2"};
        executeCommand(wrongRels);
        assertTrue("Relationship was created with fake classes", model.getRelationships().isEmpty());

        //creating relationship with invalid type
        wrongRels[2] = "fake";
        wrongRels[3] = "c1";
        wrongRels[4] = "c2";
        executeCommand(wrongRels);
        assertTrue("Relationship was created with invalid type", model.getRelationships().isEmpty());

        //create relationships of each type
        String[] relationships = {"create", "relationship", "aggregation", "c1", "c2"};
        executeCommand(relationships);
        assertTrue("Aggregation relationship between c1 and c2 not found", model.containsRelationship(Type.AGGREGATION, "c1", "c2"));

        relationships[2] = "composition";
        relationships[3] = "c2";
        relationships[4] = "c3";

        executeCommand(relationships);
        assertTrue("Composition relationship between c2 and c3 not found", model.containsRelationship(Type.COMPOSITION, "c2", "c3"));

        relationships[2] = "inheritance";
        relationships[3] = "c1";

        executeCommand(relationships);
        assertTrue("Inheritance relationship between c1 and c3 not found", model.containsRelationship(Type.INHERITANCE, "c1", "c3"));
        
        // Create relationship wrong length
        resetStreams();
        System.out.println("\nERROR: "
        	+ "Error in parsing command. Proper command usage is: \n"
        	+ "  create relationship <type> <class name1> <class name2>\n");
	String expected = out.toString();
	resetStreams();
	String[] createErr = { "create", "relationship", "WRONG" };
	executeCommand(createErr);
	assertEquals("relationship error message did not appear correctly.", expected, out.toString());
	resetStreams();
        
        //recursive relationship
        relationships[2] = "realization";
        relationships[3] = "c2";
        relationships[4] = "c2";

        executeCommand(relationships);
        assertTrue("Recursive realization relationship associated with c2 not found", model.containsRelationship(Type.REALIZATION, "c2", "c2"));

        //attempting to create a relationship that already exists
        executeCommand(relationships);
        assertEquals("Recursive realization c2 relationship created twice", 4, model.getRelationships().size());

        relationships[2] = "composition";
        assertEquals("New c2 recursive relationship created, even though c2 already has a recursive relationship", 4, model.getRelationships().size());
    }
    
    @Test
    public void createErrorTest() {
	System.out.println("\n"
		+ "ERROR: Error in parsing command. Proper command usage is: \n"
		+ "  create class <name>\n"
		+ "  create field <class name> <field visibility> <field type> <field name>\n"
		+ "  create method <class name> <method visibility> <method type> <method name>\n"
		+ "  create relationship <type> <class name1> <class name2>\n"
		+ "  create parameter <class name> <method> <parameter type> <parameter name>\n");
	String expected = out.toString();
	resetStreams();
	String[] createError = {"create", "class"};
	executeCommand(createError);
	String actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
	
	resetStreams();
	System.out.println("\n"
		+ "ERROR: Error in parsing command. Proper command usage is: \n"
		+ "  create class <name>\n"
		+ "  create field <class name> <field visibility> <field type> <field name>\n"
		+ "  create method <class name> <method visibility> <method type> <method name>\n"
		+ "  create relationship <type> <class name1> <class name2>\n"
		+ "  create parameter <class name> <method> <parameter type> <parameter name>\n");
	expected = out.toString();
	resetStreams();
	String[] createObjError = {"create", "ERROR", "test"};
	executeCommand(createObjError);
	actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
    }
    
    @Test
    public void createVisErrorTest() {
	System.out.println("\n"
		+ "ERROR: Invalid visibility type. Valid types are public, private, or protected.\n");
	String expected = out.toString();
	resetStreams();
	String[] createVisError = {"create", "field", "c1", "ERROR", "int", "f1"};
	executeCommand(createVisError);
	String actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
	
	resetStreams();
	System.out.println("\n"
		+ "ERROR: Invalid visibility type. Valid types are public, private, or protected.\n");
	expected = out.toString();
	resetStreams();
	createVisError[1] = "method";
	executeCommand(createVisError);
	actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
    }
    
}

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

public class DeleteCommandTest {

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
	String[] commands = { "sudo", "clear" };
	command = new CreateCommand(model, view, commands, prompt);
    }
    
    private void resetStreams() {
        out.reset();
        err.reset();
    }
    
    private void executeCreate(String[] commands) {
	CreateCommand create = new CreateCommand(model, view, commands, prompt);
	prompt = create.execute();
    }

    private void executeDelete(String[] commands) {
	DeleteCommand delete = new DeleteCommand(model, view, commands, prompt);
	prompt = delete.execute();
    }

    @Test
    public void deleteClassTest() {
	// Test setup of class creation
	String[] createClass = { "create", "class", "c1" };
	executeCreate(createClass);
	createClass[2] = "c2";
	executeCreate(createClass);

	assertTrue("CLI did not make a class; Should be class called c1.", model.containsEntity("c1"));
	assertTrue("CLI did not make a class; Should be class called c2.", model.containsEntity("c2"));

	// Test deleting a class
	String[] delete = { "delete", "class", "c1" };
	executeDelete(delete);

	assertFalse("CLI did not delete a class; c1 should not exist.", model.containsEntity("c1"));
	assertTrue("CLI did something to another class; c2 should be okay.", model.containsEntity("c2"));

	// Test NOT deleting with invalid length
	String[] invalidLengthCommand = { "delete", "class", "c2", "badLength" };
	executeDelete(invalidLengthCommand);

	assertTrue("CLI deleted a class with bad length; Class named 'c2' should exist.", model.containsEntity("c2"));

	// Test NOT deleting a NONEXISTENT class
	executeDelete(delete);

	assertFalse("CLI did not do something weird. c1 should not exist.", model.containsEntity("c1"));
	assertTrue("CLI deleted a class it wasn't supposed to; Class 'c2' should exist.", model.containsEntity("c2"));

	// Test deleting classes that have fields
	createClass[2] = "c1";
	executeCreate(createClass);
	String[] createField = { "create", "field", "c1", "public", "int", "f1" };
	executeCreate(createField);
	executeDelete(delete);

	assertFalse("CLI did not delete the class; c1 should not exist.", model.containsEntity("c1"));

	// Test deleting classes that have methods
	executeCreate(createClass);
	String[] createMethod = { "create", "method", "c1", "public", "int", "m1" };
	executeCreate(createMethod);
	executeDelete(delete);

	assertFalse("CLI did not delete the class. c1 should not exist.", model.containsEntity("c1"));

	// Test deleting classes with methods and parameters
	executeCreate(createClass);
	executeCreate(createMethod);
	String[] createParameter = { "create", "parameter", "c1", "m1", "int", "p1" };
	executeCreate(createParameter);
	executeDelete(delete);

	assertFalse("CLI did not delete the class. c1 should not exist.", model.containsEntity("c1"));

	// Test deleting class with a relationship
	executeCreate(createClass);
	String[] createRelationship = { "create", "relationship", "realization", "c1", "c2" };
	executeCreate(createRelationship);

	assertTrue("CLI did not create the relationship. Should be a relationship from c1 to c2.",
		model.containsRelationship(Type.REALIZATION, "c1", "c2"));

	executeDelete(delete);

	assertFalse("CLI did not delete the class. c1 should not exist.", model.containsEntity("c1"));
	assertFalse("CLI did not delete the relationship. Realization from c1 to c2 should not exist.",
		model.containsRelationship(Type.REALIZATION, "c1", "c2"));
    }

    @Test
    public void deleteFieldTest() {
	String[] classCommands = { "create", "class", "c1" };
	executeCreate(classCommands);
	Entity c1 = model.getEntities().get(0);
	String[] createFieldCommands = { "create", "field", "c1", "public", "int", "f1" };
	executeCreate(createFieldCommands);

	// Delete one field
	String[] deleteFieldCommands = { "delete", "field", "c1", "f1" };
	executeDelete(deleteFieldCommands);
	assertFalse("CLI still has a field; Should not have any more fields.", c1.containsField("f1"));
	assertEquals("c1 has more or less than zero fields.", 0, c1.getFields().size());

	// Delete multiple fields
	createFieldCommands[5] = "f2";
	executeCreate(createFieldCommands);
	createFieldCommands[5] = "f3";
	executeCreate(createFieldCommands);
	createFieldCommands[5] = "f4";
	executeCreate(createFieldCommands);
	deleteFieldCommands[3] = "f4";
	executeDelete(deleteFieldCommands);
	deleteFieldCommands[3] = "f3";
	executeDelete(deleteFieldCommands);
	deleteFieldCommands[3] = "f2";
	executeDelete(deleteFieldCommands);
	assertFalse("CLI still has a field; Should not have any more fields.", c1.containsField("f2"));
	assertFalse("CLI still has a field; Should not have any more fields.", c1.containsField("f3"));
	assertFalse("CLI still has a field; Should not have any more fields.", c1.containsField("f4"));
	assertEquals("c1 has more or less than zero fields.", 0, c1.getFields().size());

	// Delete field with wrong length of commands
	createFieldCommands[5] = "f5";
	executeCreate(createFieldCommands);
	String[] invalidLengthCommand = { "delete", "field", "c1", "f5", "tooLong" };
	executeDelete(invalidLengthCommand);
	assertTrue("CLI deleted field f5; Shouldn't have since parameters are invalid.", c1.containsField("f5"));
	assertEquals("CLI deleted a field; Shouldn't have since too many arguments.", 1, c1.getFields().size());

	// Delete field with non-existing class
	deleteFieldCommands[2] = "c3";
	deleteFieldCommands[3] = "f5";
	executeDelete(deleteFieldCommands);
	assertTrue("CLI deleted a field in a class that doesn't exist; Should not have", c1.containsField("f5"));
	assertEquals("c1 has more or less than 1 fields.", 1, c1.getFields().size());

	// Delete field with non-existing field
	deleteFieldCommands[2] = "c1";
	deleteFieldCommands[3] = "f6";
	executeDelete(deleteFieldCommands);
	assertFalse("CLI broke; Should not have", c1.containsField("f6"));
	assertTrue("CLI deleted the wrong field when the inserted field doesn't exist", c1.containsField("f5"));
	assertEquals("c1 has more or less than 1 field", 1, c1.getFields().size());

	// Delete field in separate classes
	classCommands[2] = "c2";
	executeCreate(classCommands);
	Entity c2 = model.getEntities().get(1);
	createFieldCommands[2] = "c2";
	createFieldCommands[5] = "f5";
	executeCreate(createFieldCommands);
	createFieldCommands[5] = "f6";
	executeCreate(createFieldCommands);
	deleteFieldCommands[2] = "c2";
	deleteFieldCommands[3] = "f5";
	executeDelete(deleteFieldCommands);
	assertFalse("CLI did not delete a field; Should have deleted f5 in c2.", c2.containsField("f5"));
	assertTrue("CLI deleted a field; Should have deleted f5 in c1.", c1.containsField("f5"));
	assertEquals("c1 made more or less than one field.", 1, c1.getFields().size());
	assertEquals("c2 made more or less than one field.", 1, c2.getFields().size());
    }

    @Test
    public void deleteMethodTest() {
	String[] classCommands = { "create", "class", "c1" };
	executeCreate(classCommands);
	Entity c1 = model.getEntities().get(0);
	String[] createMethodCommands = { "create", "method", "c1", "public", "int", "m1" };
	executeCreate(createMethodCommands);

	// Delete one method
	String[] deleteMethodCommands = { "delete", "method", "c1", "m1" };
	executeDelete(deleteMethodCommands);
	assertFalse("CLI still has a method; Should not have any more methods.", c1.containsMethod("m1"));
	assertEquals("c1 has more or less than zero methods.", 0, c1.getMethods().size());

	// Delete multiple methods
	createMethodCommands[5] = "m2";
	executeCreate(createMethodCommands);
	createMethodCommands[5] = "m3";
	executeCreate(createMethodCommands);
	createMethodCommands[5] = "m4";
	executeCreate(createMethodCommands);
	deleteMethodCommands[3] = "m4";
	executeDelete(deleteMethodCommands);
	deleteMethodCommands[3] = "m3";
	executeDelete(deleteMethodCommands);
	deleteMethodCommands[3] = "m2";
	executeDelete(deleteMethodCommands);
	assertFalse("CLI still has a method; Should not have any more methods.", c1.containsMethod("m2"));
	assertFalse("CLI still has a method; Should not have any more methods.", c1.containsMethod("m3"));
	assertFalse("CLI still has a method; Should not have any more methods.", c1.containsMethod("m4"));
	assertEquals("c1 has more or less than zero methods.", 0, c1.getMethods().size());

	// Delete method with wrong length of commands
	createMethodCommands[5] = "m5";
	executeCreate(createMethodCommands);
	String[] invalidLengthCommand = { "delete", "method", "c1", "m5", "tooLong" };
	executeDelete(invalidLengthCommand);
	assertTrue("CLI deleted method m5; Shouldn't have since parameters are invalid.", c1.containsMethod("m5"));
	assertEquals("CLI deleted a method; Shouldn't have since too many arguments.", 1, c1.getMethods().size());

	// Delete method with non-existing class
	deleteMethodCommands[2] = "c3";
	deleteMethodCommands[3] = "m5";
	executeDelete(deleteMethodCommands);
	assertTrue("CLI deleted a method in a class that doesn't exist; Should not have", c1.containsMethod("m5"));
	assertEquals("c1 has more or less than 1 methods.", 1, c1.getMethods().size());

	// Delete method with non-existing method
	deleteMethodCommands[2] = "c1";
	deleteMethodCommands[3] = "m6";
	executeDelete(deleteMethodCommands);
	assertFalse("CLI broke; Should not have", c1.containsMethod("m6"));
	assertTrue("CLI deleted the wrong method when the inserted method doesn't exist", c1.containsMethod("m5"));
	assertEquals("c1 has more or less than 1 method", 1, c1.getMethods().size());

	// Delete method in separate classes
	classCommands[2] = "c2";
	executeCreate(classCommands);
	Entity c2 = model.getEntities().get(1);
	createMethodCommands[2] = "c2";
	createMethodCommands[5] = "m5";
	executeCreate(createMethodCommands);
	createMethodCommands[5] = "m6";
	executeCreate(createMethodCommands);
	deleteMethodCommands[2] = "c2";
	deleteMethodCommands[3] = "m5";
	executeDelete(deleteMethodCommands);
	assertFalse("CLI did not delete a method; Should have deleted m5 in c2.", c2.containsMethod("m5"));
	assertTrue("CLI deleted a method; Should have deleted m5 in c1.", c1.containsMethod("m5"));
	assertEquals("c1 made more or less than one class.", 1, c1.getMethods().size());
	assertEquals("c2 made more or less than one class.", 1, c2.getMethods().size());
    }

    @Test
    public void deleteParameterTest() {
	String[] classCommands = { "create", "class", "c1" };
	executeCreate(classCommands);
	Entity c1 = model.getEntities().get(0);
	String[] methodCommands = { "create", "method", "c1", "public", "int", "m1" };
	executeCreate(methodCommands);
	Method m1 = c1.getMethods().get(0);
	String[] parameterCommands = { "create", "parameter", "c1", "m1", "int", "p1" };
	executeCreate(parameterCommands);

	// Delete one parameter
	String[] deleteParameterCommands = { "delete", "parameter", "c1", "m1", "p1" };
	executeDelete(deleteParameterCommands);
	assertFalse("CLI did not get rid of p1.", m1.containsParameter("p1"));
	assertEquals("c1 made more or less than one methods.", 1, c1.getMethods().size());
	assertEquals("m1 made more or less than zero parameter.", 0, m1.getParameters().size());

	// Delete multiple parameters
	parameterCommands[5] = "p1";
	executeCreate(parameterCommands);
	parameterCommands[5] = "p2";
	executeCreate(parameterCommands);
	parameterCommands[5] = "p3";
	executeCreate(parameterCommands);
	deleteParameterCommands[4] = "p3";
	executeDelete(deleteParameterCommands);
	deleteParameterCommands[4] = "p2";
	executeDelete(deleteParameterCommands);
	deleteParameterCommands[4] = "p1";
	executeDelete(deleteParameterCommands);
	assertFalse("CLI did not get rid of p2.", m1.containsParameter("p1"));
	assertFalse("CLI did not get rid of p3.", m1.containsParameter("p2"));
	assertFalse("CLI did not get rid of p4.", m1.containsParameter("p3"));
	assertEquals("m1 has more or less than zero parameters.", 0, m1.getParameters().size());

	// Delete parameter with wrong length of commands
	parameterCommands[5] = "p1";
	executeCreate(parameterCommands);
	String[] invalidLengthCommand = { "delete", "parameter", "c1", "m1", "p1", "tooLong" };
	executeDelete(invalidLengthCommand);
	assertTrue("CLI deleted a parameter; Shouldn't have since too many arguments.", m1.containsParameter("p1"));
	assertFalse("CLI made a parameter; Shouldn't have since too many arguments.", m1.containsParameter("tooLong"));
	assertEquals("m1 has more or less than one parameter.", 1, m1.getParameters().size());

	// Delete parameter with non-existing class
	deleteParameterCommands[2] = "c3";
	executeDelete(deleteParameterCommands);
	assertEquals("CLI has more or less than one class", 1, model.getEntities().size());
	assertTrue("m1 no longer has a p1.", m1.containsParameter("p1"));
	assertEquals("m1 has more or less than 1 parameters.", 1, m1.getParameters().size());

	// Delete parameter with non-existing method
	deleteParameterCommands[2] = "c1";
	deleteParameterCommands[3] = "m2";
	executeDelete(deleteParameterCommands);
	assertEquals("CLI has more or less than one class", 1, model.getEntities().size());
	assertEquals("CLI has more or less than one method", 1, c1.getMethods().size());
	assertTrue("c1 no longer contains method m1", c1.containsMethod("m1"));
	assertTrue("m1 no longer has new p4.", m1.containsParameter("p1"));
	assertEquals("m1 has more or less than one parameter.", 1, m1.getParameters().size());

	// Delete parameter with non-existing parameter
	deleteParameterCommands[3] = "m1";
	deleteParameterCommands[4] = "p2";
	executeDelete(deleteParameterCommands);
	assertFalse("CLI broke; Should not have", m1.containsParameter("p2"));
	assertEquals("m1 has more or less than one parameters", 1, m1.getParameters().size());

	// Delete parameter in separate classes/methods (with same and/or different
	// names)
	methodCommands[5] = "m2";
	executeCreate(methodCommands);
	classCommands[2] = "c2";
	executeCreate(classCommands);
	Entity c2 = model.getEntities().get(1);
	methodCommands[2] = "c2";
	methodCommands[5] = "m3";
	executeCreate(methodCommands);
	parameterCommands[3] = "m2";
	executeCreate(parameterCommands);
	parameterCommands[2] = "c2";
	parameterCommands[3] = "m3";
	executeCreate(parameterCommands);
	parameterCommands[5] = "newp1";
	executeCreate(parameterCommands);
	deleteParameterCommands[2] = "c2";
	deleteParameterCommands[3] = "m3";
	deleteParameterCommands[4] = "newp1";
	executeDelete(deleteParameterCommands);
	deleteParameterCommands[4] = "p1";
	executeDelete(deleteParameterCommands);
	deleteParameterCommands[2] = "c1";
	deleteParameterCommands[3] = "m2";
	executeDelete(deleteParameterCommands);
	deleteParameterCommands[3] = "m1";
	executeDelete(deleteParameterCommands);
	assertFalse("CLI broke; should not have.", m1.containsParameter("p1"));
	assertEquals("m1 has more or less than zero parameters.", 0, m1.getParameters().size());
	assertFalse("CLI did not get rid of a parameter in m2; Should be no more parameters in m2.",
		c1.getMethods().get(1).containsParameter("p1"));
	assertEquals("m2 has more or less than zero parameters.", 0, c1.getMethods().get(1).getParameters().size());
	assertFalse("CLI did not get rid of a parameter in m3; Should be no more parameters in m3.",
		c2.getMethods().get(0).containsParameter("p1"));
	assertFalse("CLI did not get rid of a parameter in m3; Should be no more parameters in m3.",
		c2.getMethods().get(0).containsParameter("newp1"));
	assertEquals("m3 has more or less than zero parameter.", 0, c2.getMethods().get(0).getParameters().size());
    }

    @Test
    public void deleteRelationshipTest() {
	// create classes to have relationships
	String[] classes = { "create", "class", "c1" };
	executeCreate(classes);
	classes[2] = "c2";
	executeCreate(classes);
	classes[2] = "c3";
	executeCreate(classes);

	// create relationships (c1--c2, c2--c3, c3--c3)
	String[] relationships = { "create", "relationship", "aggregation", "c1", "c2" };
	executeCreate(relationships);

	relationships[2] = "realization";
	relationships[3] = "c2";
	relationships[4] = "c3";
	executeCreate(relationships);

	relationships[2] = "inheritance";
	relationships[3] = "c3";
	executeCreate(relationships);

	// deleting non-existent relationships
	relationships[0] = "delete";
	relationships[2] = "fakeType";
	executeDelete(relationships);
	assertTrue("c3 recursive relationship deleted despite fake relationship type",
		model.containsRelationship(Type.INHERITANCE, "c3", "c3"));

	relationships[2] = "realization";
	executeDelete(relationships);
	assertTrue("Relationship c3--c3 deleted despite wrong relationship type given",
		model.containsRelationship(Type.INHERITANCE, "c3", "c3"));

	relationships[3] = "fake1";
	relationships[4] = "fake2";
	executeDelete(relationships);
	assertEquals("Relationship deleted despite fake class names", 3, model.getRelationships().size());

	// deleting relationships
	relationships[2] = "realization";
	relationships[3] = "c2";
	relationships[4] = "c3";
	executeDelete(relationships);
	assertFalse("Relationship c2--c3 failed to delete", model.containsRelationship(Type.REALIZATION, "c2", "c3"));
	assertEquals("Size of relationship list is wrong", 2, model.getRelationships().size());

	relationships[2] = "inheritance";
	relationships[3] = "c3";
	executeDelete(relationships);
	assertFalse("Relationship c3--c3 failed to delete", model.containsRelationship(Type.INHERITANCE, "c3", "c3"));
	assertEquals("Size of relationship list is wrong", 1, model.getRelationships().size());
    }
    
    @Test
    public void deleteRelationshipTypesTest() {
	// create classes to have relationships
	String[] classes = { "create", "class", "c1" };
	executeCreate(classes);
	classes[2] = "c2";
	executeCreate(classes);
	classes[2] = "c3";
	executeCreate(classes);

	// create relationships (c1--c2, c2--c3, c3--c3)
	String[] relationships = { "create", "relationship", "aggregation", "c1", "c2" };
	executeCreate(relationships);
	String[] relationships2 = { "create", "relationship", "inheritance", "c1", "c1" };
	executeCreate(relationships2);
	String[] relationships3 = { "create", "relationship", "composition", "c1", "c3" };
	executeCreate(relationships3);
	String[] relationships4 = { "create", "relationship", "realization", "c3", "c3" };
	executeCreate(relationships4);
	
	relationships[0] = "delete";
	executeDelete(relationships);
	relationships2[0] = "delete";
	executeDelete(relationships2);
	relationships3[0] = "delete";
	executeDelete(relationships3);
	relationships4[0] = "delete";
	executeDelete(relationships4);
	assertFalse("Relationship c1--c2 failed to delete", model.containsRelationship(Type.AGGREGATION, "c1", "c2"));
	assertFalse("Relationship c1--c1 failed to delete", model.containsRelationship(Type.INHERITANCE, "c1", "c1"));
	assertFalse("Relationship c1--c3 failed to delete", model.containsRelationship(Type.COMPOSITION, "c1", "c3"));
	assertFalse("Relationship c3--c3 failed to delete", model.containsRelationship(Type.REALIZATION, "c3", "c3"));
	assertEquals("Size of relationship list is wrong", 0, model.getRelationships().size());
    }
    
    @Test
    public void deleteErrorTest() {
	//create classes
        String[] classes = {"create", "class", "c1"};
        executeCreate(classes);
        classes[2] = "c2";
        executeCreate(classes);
        classes[2] = "c3";
        executeCreate(classes);
	
	System.out.println("\n"
		+ "ERROR: Error in parsing command. Proper command usage is: \n"
		+ "  delete class <name>\n"
		+ "  delete field <class name> <field name>\n"
		+ "  delete method <class name> <method name>\n"
		+ "  delete relationship <type> <class name1> <class name2>\n"
		+ "  delete parameter <class name> <method name>, <parameter name>\n");
	String expected = out.toString();
	resetStreams();
	String[] deleteError = {"delete", "class"};
	executeDelete(deleteError);
	String actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
	
	resetStreams();
	System.out.println("\n"
		+ "ERROR: Error in parsing command. Proper command usage is: \n"
		+ "  delete class <name>\n"
		+ "  delete field <class name> <field name>\n"
		+ "  delete method <class name> <method name>\n"
		+ "  delete relationship <type> <class name1> <class name2>\n"
		+ "  delete parameter <class name> <method name>, <parameter name>\n");
	expected = out.toString();
	resetStreams();
	String[] deleteObjError = {"delete", "ERROR", "test"};
	executeDelete(deleteObjError);
	actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
	
	resetStreams();
	System.out.println("\n"
		+ "ERROR: Error in parsing command. Proper command usage is: \n"
		+ "  delete relationship <type> <class name1> <class name2>\n");
	expected = out.toString();
	resetStreams();
	String[] deleteRelationError = {"delete", "relationship", "test"};
	executeDelete(deleteRelationError);
	actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
    }
    
}

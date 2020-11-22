package mike.cli;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mike.datastructures.Model;
import mike.view.CLIView;

public class SettypeTest {
    
    Path path;
    Model model;
    CLIView view;
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
	view = new CLIView();
    }
    
    @After
    public void cleanCLI() {
	System.setOut(origOut);
	System.setErr(origErr);
	model.empty();
    }
    
    private void resetStreams() {
        out.reset();
        err.reset();
    }
    
    private void executeCommand(String[] commands) {
	SettypeCommand settype = new SettypeCommand(model, view, commands, prompt);
	prompt = settype.execute();
    }
    
    private void executeCreateCommand(String[] commands) {
	CreateCommand create = new CreateCommand(model, view, commands, prompt);
	prompt = create.execute();
    }
    
    @Test
    public void settypeTest() {
	// Test creating one class
	String[] commands = {"create", "class", "c1"};
	executeCreateCommand(commands);
	String[] fieldCommands = {"create", "field", "c1", "public", "boolean", "f1"};
	executeCreateCommand(fieldCommands);
	String[] methodCommands = {"create", "method", "c1", "protected", "String", "m1"};
	executeCreateCommand(methodCommands);
	String[] parameterCommands = {"create", "parameter", "c1", "m1", "int", "p1"};
	executeCreateCommand(parameterCommands);
	
	String[] settypeFieldCommand = {"settype", "field", "c1", "f1", "int"};
	executeCommand(settypeFieldCommand);
	assertEquals("CLI did not make a class; Should be one class called c1.", "int", model.copyEntity("c1").copyField("f1").getType());
	
	String[] settypeMethodCommand = {"settype", "method", "c1", "m1", "boolean"};
	executeCommand(settypeMethodCommand);
	assertEquals("CLI did not make a class; Should be one class called c1.", "boolean", model.copyEntity("c1").copyMethod("m1").getType());
	
	String[] settypeParameterCommand = {"settype", "parameter", "c1", "m1", "p1", "String"};
	executeCommand(settypeParameterCommand);
	assertEquals("CLI did not make a class; Should be one class called c1.", "String", model.copyEntity("c1").copyMethod("m1").copyParameter("p1").getType());
    	
    }
    
    @Test
    public void settypeErrorTest() {
	// Test creating one class
	String[] commands = {"create", "class", "c1"};
	executeCreateCommand(commands);
	String[] fieldCommands = {"create", "field", "c1", "public", "boolean", "f1"};
	executeCreateCommand(fieldCommands);
	String[] methodCommands = {"create", "method", "c1", "protected", "String", "m1"};
	executeCreateCommand(methodCommands);
	String[] parameterCommands = {"create", "parameter", "c1", "m1", "int", "p1"};
	executeCreateCommand(parameterCommands);
	
	
	// Print out classes
	System.out.println("\n"
		+ "ERROR: settype field failed. Make sure the class and field both exist.\n");
	String expected = out.toString();
	resetStreams();
	String[] settypeFieldError = {"settype", "field", "c1", "f2", "int"};
	executeCommand(settypeFieldError);
	String actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
	
	resetStreams();
	System.out.println("\n"
		+ "ERROR: settype method failed. Make sure the class and method both exist.\n");
	expected = out.toString();
	resetStreams();
	String[] settypeMethodError = {"settype", "method", "c1", "m2", "boolean"};
	executeCommand(settypeMethodError);
	actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
	
	resetStreams();
	System.out.println("\n"
		+ "ERROR: settype parameter failed. Make sure the class, method, and parameter all exist.\n");
	expected = out.toString();
	resetStreams();
	String[] settypeParameterError = {"settype", "parameter", "c1", "m1", "p2", "boolean"};
	executeCommand(settypeParameterError);
	actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
    }
    
    @Test
    public void settypeNotFoundErrorTest() {
	// Test creating one class
	String[] commands = {"create", "class", "c1"};
	executeCreateCommand(commands);
	String[] fieldCommands = {"create", "field", "c1", "public", "boolean", "f1"};
	executeCreateCommand(fieldCommands);
	String[] methodCommands = {"create", "method", "c1", "protected", "String", "m1"};
	executeCreateCommand(methodCommands);
	String[] parameterCommands = {"create", "parameter", "c1", "m1", "int", "p1"};
	executeCreateCommand(parameterCommands);
	
	
	// Print out classes
	System.out.println("\n"
		+ "ERROR: Error in parsing command. Proper command usage is: \n"
		+ "  settype field <class name> <field name> <newtype>\n"
		+ "  settype method <class name> <method name> <newtype>\n");
	String expected = out.toString();
	resetStreams();
	String[] settypeFieldError = {"settype", "ERROR", "c1", "f1", "int"};
	executeCommand(settypeFieldError);
	String actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
	
	resetStreams();
	System.out.println("\n"
		+ "ERROR: Error in parsing command. Proper command usage is: \n"
		+ "  settype parameter <class name> <method name> <parameter name> <newtype>\n");
	expected = out.toString();
	resetStreams();
	String[] settypeParameterError = {"settype", "ERROR", "c1", "m1", "p1", "boolean"};
	executeCommand(settypeParameterError);
	actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
	
	resetStreams();
	System.out.println("\n"
		+ "ERROR: Error in parsing command. Proper command usage is: \n"
		+ "  settype field <class name> <field name> <newtype>\n"
		+ "  settype method <class name> <method name> <newtype>\n"
		+ "  settype parameter <class name> <method name> <parameter name> <newtype>\n");
	expected = out.toString();
	resetStreams();
	String[] settypeMethodError = {"settype", "ERROR"};
	executeCommand(settypeMethodError);
	actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
    }
    
}

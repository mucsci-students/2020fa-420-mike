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
import mike.datastructures.Entity.visibility;
import mike.view.CLIView;

public class SetvisCommandTest {
    
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
	SetvisCommand setvis = new SetvisCommand(model, view, commands, prompt);
	prompt = setvis.execute();
    }
    
    private void executeCreateCommand(String[] commands) {
	CreateCommand create = new CreateCommand(model, view, commands, prompt);
	prompt = create.execute();
    }
    
    @Test
    public void setvisTest() {
	// Test creating one class
	String[] commands = {"create", "class", "c1"};
	executeCreateCommand(commands);
	String[] fieldCommands = {"create", "field", "c1", "public", "boolean", "f1"};
	executeCreateCommand(fieldCommands);
	String[] methodCommands = {"create", "method", "c1", "protected", "String", "m1"};
	executeCreateCommand(methodCommands);
	
	String[] setvisFieldCommand = {"setvis", "field", "c1", "f1", "private"};
	executeCommand(setvisFieldCommand);
	assertEquals("CLI did not make a class; Should be one class called c1.", visibility.PRIVATE, model.copyEntity("c1").copyField("f1").getVisibility());
	
	String[] setvisMethodCommand = {"setvis", "method", "c1", "m1", "public"};
	executeCommand(setvisMethodCommand);
	assertEquals("CLI did not make a class; Should be one class called c1.", visibility.PUBLIC, model.copyEntity("c1").copyMethod("m1").getVisibility());
    }
    
    @Test
    public void setvisErrorTest() {
	// Test creating one class
	String[] commands = {"create", "class", "c1"};
	executeCreateCommand(commands);
	String[] fieldCommands = {"create", "field", "c1", "public", "boolean", "f1"};
	executeCreateCommand(fieldCommands);
	String[] methodCommands = {"create", "method", "c1", "protected", "String", "m1"};
	executeCreateCommand(methodCommands);
	
	// Print out classes
	System.out.println("\n"
		+ "ERROR: setvis field failed. Make sure the class and field both exist, and you passed a valid visibility.\n");
	String expected = out.toString();
	resetStreams();
	String[] setvisFieldError = {"setvis", "field", "c1", "f2", "protected"};
	executeCommand(setvisFieldError);
	String actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
	
	resetStreams();
	System.out.println("\n"
		+ "ERROR: setvis method failed. Make sure the class and method both exist, and you passed a valid visibility.\n");
	expected = out.toString();
	resetStreams();
	String[] setvisMethodError = {"setvis", "method", "c1", "m2", "public"};
	executeCommand(setvisMethodError);
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
	
	
	// Print out classes
	System.out.println("\n"
		+ "ERROR: Error in parsing command. Proper command usage is: \n"
		+ "  setvis field <class name> <field name> <visibility>\n"
		+ "  setvis method <class name> <method name> <visibility>\n");
	String expected = out.toString();
	resetStreams();
	String[] setvisFieldError = {"setvis", "ERROR", "c1", "f1", "private"};
	executeCommand(setvisFieldError);
	String actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
	
	
	resetStreams();
	System.out.println("\n"
		+ "ERROR: Error in parsing command. Proper command usage is: \n"
		+ "  setvis field <class name> <field name> <visibility>\n"
		+ "  setvis method <class name> <method name> <visibility>\n");
	expected = out.toString();
	resetStreams();
	String[] setvisLengthError = {"setvis", "ERROR"};
	executeCommand(setvisLengthError);
	actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
	
	resetStreams();
	System.out.println("\n"
		+ "ERROR: setvis failed due to invalid visibility type. Valid visibility types are: public, private, and protected.\n");
	expected = out.toString();
	resetStreams();
	String[] setvisError = {"setvis", "field", "c1", "f1", "ERROR"};
	executeCommand(setvisError);
	actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
    }
    
}

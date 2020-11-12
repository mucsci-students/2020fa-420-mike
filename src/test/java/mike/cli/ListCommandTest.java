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

public class ListCommandTest {
    
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
	String[] commands = {"sudo", "clear"};
	executeCommand(commands);
    }
    
    private void resetStreams() {
        out.reset();
        err.reset();
    }
    
    private void executeCommand(String[] commands) {
	ListCommand create = new ListCommand(model, view, commands, prompt);
	prompt = create.execute();
    }
    
    @Test
    public void listErrorTest() {
	System.out.println("\n"
		+ "ERROR: Error in parsing command. Proper command usage is: \n"
		+ "  list classes\n"
		+ "  list relationships\n"
		+ "  list all\n");
	String expected = out.toString();
	resetStreams();
	String[] commands = {"list"};
	executeCommand(commands);
	String actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
    }
    
    @Test
    public void listClassesTest() {
	// Print out classes
	System.out.println();
	System.out.println("Classes:");
        System.out.println();
	String expected = out.toString();
	resetStreams();
	String[] commands = {"list", "classes"};
	executeCommand(commands);
	String actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
	
	// Print out error
	resetStreams();
	System.out.println("\n"
		+ "ERROR: Error in parsing command. Proper command usage is: \n"
		+ "  list classes\n");
	expected = out.toString();
	resetStreams();
	String[] commandsErr = {"list", "classes", "error"};
	executeCommand(commandsErr);
	actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
	
    }
    
    @Test
    public void listRelationshipTest() {
	// Print out Relationships
	System.out.println();
	System.out.println("Relationships:");
        System.out.println();
	String expected = out.toString();
	resetStreams();
	String[] commands = {"list", "relationships"};
	executeCommand(commands);
	String actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
	
	// Print out error
	resetStreams();
	System.out.println("\n"
		+ "ERROR: Error in parsing command. Proper command usage is: \n"
		+ "  list relationships\n");
	expected = out.toString();
	resetStreams();
	String[] commandsErr = {"list", "relationships", "error"};
	executeCommand(commandsErr);
	actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
    }
    
    @Test
    public void listAllTest() {
	// Print out all
	System.out.println();
	System.out.println("Classes:");
	System.out.println();
	System.out.println("Relationships:");
        System.out.println();
	String expected = out.toString();
	resetStreams();
	String[] commands = {"list", "all"};
	executeCommand(commands);
	String actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
	
	// Print out error
	resetStreams();
	System.out.println("\n"
		+ "ERROR: Error in parsing command. Proper command usage is: \n"
		+ "  list all\n");
	expected = out.toString();
	resetStreams();
	String[] commandsErr = {"list", "all", "error"};
	executeCommand(commandsErr);
	actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
    }
}

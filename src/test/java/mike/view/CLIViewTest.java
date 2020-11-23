package mike.view;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CLIViewTest {
    
    CLIView view;
    
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();

    private final PrintStream origOut = System.out;
    private final PrintStream origErr = System.err;
    
    @Before
    public void createCLI() throws IOException {
	System.setOut(new PrintStream(out));
	System.setErr(new PrintStream(err));
	view = new CLIView();
    }
    
    @After
    public void cleanCLI() {
	System.setOut(origOut);
	System.setErr(origErr);
    }
    
    private void resetStreams() {
        out.reset();
        err.reset();
    }
	
    @Test
    public void printIntroTest() {
	System.out.println("Hello, and welcome to Team mike's UML editor.");
	System.out.println("To exit the program, type 'quit'.");
	System.out.println("To see all the commands available, type 'help'.\n");
	String expected = out.toString();
	resetStreams();
	view.printIntro();
	assertEquals("printIntro text is wrong.", expected, out.toString());
    }
    
    @Test
    public void printInvalidCommandTest() {
	System.out.println("\nInvalid command.\nType help to see a list of all commands.\n");
	String expected = out.toString();
	resetStreams();
	view.printInvalidCommand();
	assertEquals("printInvalidCommand text is wrong.", expected, out.toString());
    }
    
    @Test
    public void printErrorTest() {
	String e = "This is a test!";
	System.out.println("\nERROR: " + e);
	String expected = out.toString();
	resetStreams();
	view.printError(e);
	assertEquals("printInvalidCommand text is wrong.", expected, out.toString());
    }

}

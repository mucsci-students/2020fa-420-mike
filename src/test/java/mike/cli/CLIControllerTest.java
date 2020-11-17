package mike.cli;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mike.datastructures.Entity.visibility;
import mike.datastructures.Model;
import mike.controller.CLIController;

import mike.view.ViewTemplate;

public class CLIControllerTest {

    Model model;
    ViewTemplate view;
    CLIController control;

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();

    private final PrintStream origOut = System.out;
    private final PrintStream origErr = System.err;

    @Before
    public void createCLI() throws IOException {
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
        model = new Model();
        view = new ViewTemplate(ViewTemplate.InterfaceType.CLI);
        control = new CLIController(model, view);
    }

    @After
    public void cleanCLI() {
        System.setOut(origOut);
        System.setErr(origErr);
        String[] commands = {"sudo", "clear"};
        control.evaluateCommand(commands);
    }

    private void resetStreams() {
        out.reset();
        err.reset();
    }

    //**********************************************************************************************//
    //**                                CONSTRUCTOR TEST                                          **//
    //**                                (INITIALIZATION)                                          **//
    //**********************************************************************************************//

    @Test
    public void initTest() {
        assertTrue("CLI is null; Should not be null.", control != null);
        assertTrue("CLI's model is not empty; Should be empty.", control.getModel().empty());
    }

    @Test
    public void evaluateCommandTest() {
        // Test Help doesn't crash
        String[] helpCommands = {"help"};
        control.evaluateCommand(helpCommands);

        // Test Create
        String[] commands = {"create", "class", "c1"};
        control.evaluateCommand(commands);
        commands[2] = "c2";
        control.evaluateCommand(commands);
        assertTrue("Model does not contain a class", control.getModel().containsEntity("c1"));
        assertTrue("Model does not contain a class", control.getModel().containsEntity("c2"));

        // Test Rename
        String[] Renamecommands = {"rename", "class", "c1", "c3"};
        control.evaluateCommand(Renamecommands);
        assertFalse("Model does not contain a class", control.getModel().containsEntity("c1"));
        assertTrue("Model does not contain a class", control.getModel().containsEntity("c2"));
        assertTrue("Model does not contain a class", control.getModel().containsEntity("c3"));

        // Test delete
        String[] deleteCommands = {"delete", "class", "c3"};
        control.evaluateCommand(deleteCommands);
        assertTrue("Model does not contain a class", control.getModel().containsEntity("c2"));

        // Test settype
        String[] createFieldCommands = {"create", "field", "c2", "public", "String", "f1"};
        control.evaluateCommand(createFieldCommands);
        String[] settypeCommands = {"settype", "field", "c2", "f1", "int"};
        control.evaluateCommand(settypeCommands);
        assertEquals("Model does not contain a class", "int", control.getModel().copyEntity("c2").copyField("f1").getType());

        // Test setvis
        String[] setvisCommands = {"setvis", "field", "c2", "f1", "private"};
        control.evaluateCommand(setvisCommands);
        assertEquals("Model does not contain a class", visibility.PRIVATE, control.getModel().copyEntity("c2").copyField("f1").getVisibility());

        // Test invalid command
        resetStreams();
        System.out.println("\n"
                + "Invalid command.\n"
                + "Type help to see a list of all commands.\n");
        String expected = out.toString();
        resetStreams();
        String[] errorCommand = {"error"};
        control.evaluateCommand(errorCommand);
        assertEquals("Model does not contain a class", expected, out.toString());
    }


    //**********************************************************************************************//
    //**                                MISC. TESTS                                               **//
    //**                            (CLEAR, UNDO, REDO)                                           **//
    //**********************************************************************************************//

    @Test
    public void createErr() {
        System.out.println("\nERROR: "
                + "Error in parsing command. Proper command usage is: \n"
                + "  create class <name>\n"
                + "  create field <class name> <field visibility> <field type> <field name>\n"
                + "  create method <class name> <method visibility> <method type> <method name>\n"
                + "  create relationship <type> <class name1> <class name2>\n"
                + "  create parameter <class name> <method> <parameter type> <parameter name>\n");
        String expected = out.toString();
        resetStreams();
        String[] createErr2 = {"create"};
        control.evaluateCommand(createErr2);
        assertEquals("load error message did not appear correctly.", expected, out.toString());
        resetStreams();

        System.out.println("\nERROR: "
                + "Invalid visibility type. Valid types are public, private, or protected.\n");
        expected = out.toString();
        resetStreams();
        String[] createErr = {"create", "field", "c1", "wrong", "int", "f1"};
        control.evaluateCommand(createErr);
        assertEquals("load error message did not appear correctly.", expected, out.toString());
        resetStreams();

        System.out.println("\nERROR: "
                + "Invalid visibility type. Valid types are public, private, or protected.\n");
        expected = out.toString();
        resetStreams();
        createErr[1] = "method";
        createErr[5] = "m1";
        control.evaluateCommand(createErr);
        assertEquals("load error message did not appear correctly.", expected, out.toString());
        resetStreams();
    }
    
    @Test
    public void undoTest() {
	String[] commands = {"create", "class", "c1"};
        control.evaluateCommand(commands);
        commands[2] = "c2";
        control.evaluateCommand(commands);
        assertTrue("Model does not contain a class", control.getModel().containsEntity("c1"));
        assertTrue("Model does not contain a second class", control.getModel().containsEntity("c2"));
        
        String[] undoCommand = {"undo"};
        control.evaluateCommand(undoCommand);
        
        assertFalse("Model still has a second class after undo.", control.getModel().containsEntity("c2"));
        
        control.evaluateCommand(undoCommand);
        
        System.out.println("No actions to undo.");
        String expected = out.toString();
        resetStreams();
        control.evaluateCommand(undoCommand);
        assertEquals("Tried to undo with nothing to undo.", expected, out.toString());
        
        commands[2] = "c3";
        control.evaluateCommand(commands);
        
        assertTrue("Model does not contain a class.", control.getModel().containsEntity("c3"));
    }
    
    @Test
    public void redoTest() {
	String[] commands = {"create", "class", "c1"};
        control.evaluateCommand(commands);
        commands[2] = "c2";
        control.evaluateCommand(commands);
        assertTrue("Model does not contain a class", control.getModel().containsEntity("c1"));
        assertTrue("Model does not contain a second class", control.getModel().containsEntity("c2"));
        
        String[] undoCommand = {"undo"};
        control.evaluateCommand(undoCommand);
        
        String[] redoCommand = {"redo"};
        control.evaluateCommand(redoCommand);;
        
        assertTrue("Model does not contain the second class after the redo.", control.getModel().containsEntity("c2"));
        
        System.out.println("No actions to redo.");
        String expected = out.toString();
        resetStreams();
        control.evaluateCommand(redoCommand);
        assertEquals("Tried to redo with nothing to redo.", expected, out.toString());
    }
}

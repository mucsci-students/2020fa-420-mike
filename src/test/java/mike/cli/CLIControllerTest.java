package mike.cli;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mike.datastructures.Entity.visibility;
import mike.datastructures.Model;
import mike.HelperMethods;
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
    public void initTest(){
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
    //**                         	  (CLEAR)                                                 **//
    //**********************************************************************************************//

    @Test
    public void saveTest() throws IOException, ParseException {
	// Test relative Path error length 4
	System.out.println("\nERROR: "
		+ "Error in parsing command. Proper command usage is: \n"
		+ "  save <name>.json (optional <path>)\n");
	String expected = out.toString();
	resetStreams();
	String[] saveErr2 = {"save", "testDemoCLI.json", "\\src\\test\\java\\mike", "WRONG"};
	control.evaluateCommand(saveErr2);
	assertEquals("save error message did not appear correctly.", expected, out.toString());
	resetStreams();
	
	// Test relative Path
	String[] save = {"save", "\\src\\test\\java\\mike\\testDemoCLI.json"};
	control.evaluateCommand(save);
	Path path = Paths.get(System.getProperty("user.dir") + "\\src\\test\\java\\mike\\testDemoCLI.json");
	saveWorked(path);
	
	// Test absolute Path
	save[1] = System.getProperty("user.dir") + "\\src\\test\\java\\mike\\testDemoCLI.json";
	control.evaluateCommand(save);
	path = Paths.get(save[1]);
	saveWorked(path);
	
	// Test giving relative directory
	String[] saveDir = {"save", "testDemoCLI.json", "\\src\\test\\java\\mike" };
	control.evaluateCommand(saveDir);
	Path pathDir = Paths.get(System.getProperty("user.dir") + "\\src\\test\\java\\mike\\testDemoCLI.json");
	saveWorked(pathDir);
	
	// Test giving absolute directory
	saveDir[2] = System.getProperty("user.dir") + "\\src\\test\\java\\mike\\testDemoCLI.json";
	control.evaluateCommand(saveDir);
	pathDir = Paths.get(System.getProperty("user.dir") + "\\src\\test\\java\\mike\\testDemoCLI.json");
	saveWorked(pathDir);
	
    }
    
    @Test
    public void loadTest() throws IOException, ParseException, java.text.ParseException {
	// Test relative Path error length 3
	System.out.println("\nERROR: "
		+ "Error in parsing command. Proper command usage is: \n"
		+ "  load <path>\n");
	String expected = out.toString();
	resetStreams();
	String[] loadErr2 = {"load", "testDemo.json", "WRONG"};
	control.evaluateCommand(loadErr2);
	assertEquals("load error message did not appear correctly.", expected, out.toString());
	resetStreams();
	
	// Test relative Path error length 2
	System.out.println("\nERROR: Failed to parse directory. Exiting.");
	expected = out.toString();
	resetStreams();
	String[] loadErr3 = {"load", "\\src\\test\\java\\mike\\testDemo2.json"};
	control.evaluateCommand(loadErr3);
	assertEquals("load error message did not appear correctly.", expected, out.toString());
	
	// Test relative Path
	String[] load = {"load", "\\src\\test\\java\\mike\\testDemoCLI.json"};
	control.evaluateCommand(load);
	Path path = Paths.get(System.getProperty("user.dir") + "\\src\\test\\java\\mike\\testDemoCLI.json");
	loadWorked(path);
	
	// Test absolute Path
	load[1] = System.getProperty("user.dir") + "\\src\\test\\java\\mike\\testDemoCLI.json";
	control.evaluateCommand(load);
	path = Paths.get(load[1]);
	loadWorked(path);
	
    }
    
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
	String[] createErr = { "create", "field", "c1", "wrong", "int", "f1" };
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
	/*
	System.out.println("\n"
		+ "Error in parsing command. Proper command usage is: \n"
		+ "  create class <name>\n"
		+ "  create field <class name> <field visibility> <field type> <field name>\n"
		+ "  create method <class name> <method visibility> <method type> <method name>\n"
		+ "  create relationship <type> <class name1> <class name2>\n"
		+ "  create parameter <class name> <method> <parameter type> <parameter name>\n");
	expected = out.toString();
	resetStreams();
	String[] createErr3 = { "create", "Error" };
	cli.evaluateCommand(createErr3);
	assertEquals("Error message did not appear correctly.", expected, out.toString());
	resetStreams();
	*/
    }
    
    private void saveWorked(Path path) throws IOException, ParseException {
	HelperMethods.save(path, model);
	File directory = new File(path.toString());
	Object obj = new JSONParser().parse(new FileReader(directory));
	
	String emptyModelFile = "{\"Relationships\":[],\"Classes\":[]}";
	assertEquals("CLI is null; Should not be null.", emptyModelFile, obj.toString());
    }
    
    private void loadWorked(Path path) throws IOException, ParseException, java.text.ParseException {
	HelperMethods.save(path, model);
	Model loadModel = new Model();
	HelperMethods.load(path, loadModel, null, null);
	
	assertEquals("loadModel contains a class.", 0, loadModel.getEntities().size());
	assertEquals("loadModel contains a relationship.", 0, loadModel.getRelationships().size());
    }

}

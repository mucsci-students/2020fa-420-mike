package mike.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mike.datastructures.Model;
import mike.datastructures.Relationship.Type;
import mike.view.CLIView;
import mike.view.ViewTemplate;

public class MiscCommandTest {
    Model model;
    CLIView view;
    ViewTemplate viewTemp;
    CreateCommand command;
    boolean prompt;
    private Terminal terminal;
    private DefaultParser parser;
    private LineReader savePromptReader;

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

	terminal = TerminalBuilder.builder().system(true).build();
	StringsCompleter savePromptCompleter = new StringsCompleter("yes", "no");
	parser = new DefaultParser();
	parser.setEscapeChars(new char[] {});
	savePromptReader = LineReaderBuilder.builder().terminal(terminal).completer(savePromptCompleter)
		.variable(LineReader.MENU_COMPLETE, true).parser(parser).build();
    }

    @After
    public void cleanCLI() {
	System.setOut(origOut);
	System.setErr(origErr);
	String[] commands = { "sudo", "clear" };
	command = new CreateCommand(model, view, commands, prompt);
    }

    private void executeCommand(String[] commands) {
	MiscCommand create = new MiscCommand(model, view, commands, prompt, savePromptReader);
	prompt = create.execute();
    }

    private void executeCreateCommand(String[] commands) {
	CreateCommand create = new CreateCommand(model, view, commands, prompt);
	prompt = create.execute();
    }

    @Test
    public void helpTest() {
	String[] help = { "help" };
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	ByteArrayOutputStream err = new ByteArrayOutputStream();

	System.setOut(new PrintStream(out));
	System.setErr(new PrintStream(err));

	System.out.println("\n" + "Here is a list of available commands:\n"
		+ "  save <name>.json - Save file to specific path\n"
		+ "  load <path> - Loads a file at a specific path\n" + "\n"
		+ "  create class <name> - create a class with title <name>\n"
		+ "  create field <class name> <field visibility> <field type> <field name> - create a field in <class name> with visibility <type visibility>, type <field type> titled <field name>\n"
		+ "  create method <class name> <method visibility> <method type> <method name> - create a method in <class name> with visibility <method visibility>, type <method type> titled <method name>\n"
		+ "  create relationship <type> <class name1> <class name2> - create a relationship between <class name1> and <class name2> with type <type> (Aggregation, Realization, Composition, Inheritance)\n"
		+ "  create parameter <class name> <method> <parameter type> <parameter name> - create a parameter in <class name> for <method> with type <parameter type> titled <parameter name>\n"
		+ "\n" + "  delete class <name> - delete a class with title <name>\n"
		+ "  delete field <class name> <field name> - delete field <field name> in class titled <class name>\n"
		+ "  delete method <class name> <method name> - delete method <method name> in class titled <class name>\n"
		+ "  delete relationship <type> <class name1> <class name2> - delete a relationship with type <type> (Aggregation, Realization, Composition, Inheritance) between <class name1> and <class name2>\n"
		+ "  delete parameter <class name> <method name>, <parameter name> - delete a parameter in <class name> for <method name> with  <parameter name>\n"
		+ "\n" + "  rename class <name> <newname> - rename class <name> to <new name>\n"
		+ "  rename field <class name> <field name> <newname> - rename field <field name> to <newname> in class titled <class name>\n"
		+ "  rename method <class name> <method name> <newname> - rename method <method name> to <newname> in class titled <class name>\n"
		+ "  rename parameter <class name> <method name> <parameter name> <parameter newname> - rename parameter in <class name> for <method> titled <parameter name> to <parameter newname>\n"
		+ "\n"
		+ "  settype field <class name> <field name> <newtype> - set type of field <field name> in <class name> to <type>\n"
		+ "  settype method <class name> <method name> <newtype> - set type of method <method name> in <class name> to <type>\n"
		+ "  settype parameter <class name> <method name> <parameter name> <newtype> - set type of parameter <class name> in <method name> titled <parameter name> to <type>\n"
		+ "\n"
		+ "  setvis field <class name> <field name> <visibility> - set visibility of field <field name> in <class name> to <visibility>\n"
		+ "  setvis method <class name> <method name> <visibility> - set visibility of method <method name> in <class name> to <visibility>\n"
		+ "\n" + "  list classes - List all existing classes\n"
		+ "  list relationships - List all existing relationships\n"
		+ "  list all - List all existing classes and relationships\n" + "\n"
		+ "  clear - Clear all classes and relationships\n"
		+ "  undo - Reverts the most recent change to the UML Editor\n"
		+ "  redo - Restores the most recently undone action.\n" + "  quit - exits the program" + "\n");
	String expected = out.toString();
	out.reset();
	err.reset();
	executeCommand(help);
	assertEquals("Initial print all does not equal printout", expected, out.toString());
    }

    @Test
    public void clearTest() {
	// Make a whole bunch of stuff to be cleared.
	String[] classes = { "create", "class", "c1" };
	executeCreateCommand(classes);
	classes[2] = "c2";
	executeCreateCommand(classes);

	assertEquals("CLI did not make the classes; Should be 2.", 2, model.getEntities().size());

	String[] field = { "create", "field", "c1", "public", "int", "f1" };
	executeCreateCommand(field);
	field[5] = "f2";
	executeCreateCommand(field);
	field[2] = "c2";
	field[5] = "f1";
	executeCreateCommand(field);

	assertTrue("CLI did not make 2 fields in c1; C1 should have f1 and f2.",
		model.getEntities().get(0).containsField("f1"));
	assertTrue("CLI did not make 2 fields in c1; C1 should have f1 and f2.",
		model.getEntities().get(0).containsField("f2"));
	assertTrue("CLI did not make 1 field in c2; C2 should have f1.",
		model.getEntities().get(1).containsField("f1"));

	String[] method = { "create", "method", "c1", "public", "double", "m1" };
	executeCreateCommand(method);
	method[5] = "m2";
	executeCreateCommand(method);
	method[2] = "c2";
	method[5] = "m1";
	executeCreateCommand(method);

	assertTrue("CLI did not make 2 methods in c1; C1 should have m1 and m2.",
		model.getEntities().get(0).containsMethod("m1"));
	assertTrue("CLI did not make 2 methods in c1; C1 should have m1 and m2.",
		model.getEntities().get(0).containsMethod("m2"));
	assertTrue("CLI did not make 1 method in c2; C2 should have m1.",
		model.getEntities().get(1).containsMethod("m1"));

	String[] parameter = { "create", "parameter", "c1", "m1", "char", "p1" };
	executeCreateCommand(parameter);
	parameter[5] = "p2";
	executeCreateCommand(parameter);
	parameter[3] = "m2";
	parameter[5] = "p1";
	executeCreateCommand(parameter);
	parameter[2] = "c2";
	parameter[3] = "m1";
	executeCreateCommand(parameter);

	assertTrue("CLI did not make 2 parameters in c1 > m1; M1 should have p1 and p2.",
		model.getEntities().get(0).getMethods().get(0).containsParameter("p1"));
	assertTrue("CLI did not make 2 parameters in c1 > m1; M1 should have p1 and p2.",
		model.getEntities().get(0).getMethods().get(0).containsParameter("p2"));
	assertTrue("CLI did not make 1 parameter in c1 > m2; M2 should have p1.",
		model.getEntities().get(0).getMethods().get(1).containsParameter("p1"));
	assertTrue("CLI did not make 1 parameter in c2 > m1; M1 should have p1.",
		model.getEntities().get(1).getMethods().get(0).containsParameter("p1"));

	String[] relationship = { "create", "relationship", "realization", "c1", "c1" };
	executeCreateCommand(relationship);
	relationship[2] = "aggregation";
	relationship[4] = "c2";
	executeCreateCommand(relationship);

	assertTrue("CLI did not make a recursive relationship. There should be a realization from c1 to c1.",
		model.containsRelationship(Type.REALIZATION, "c1", "c1"));
	assertTrue("CLI did not make a normal relationship. There should be an aggregation from c1 to c2.",
		model.containsRelationship(Type.AGGREGATION, "c1", "c2"));

	String[] clear = { "sudo", "clear" };
	executeCommand(clear);

	assertTrue("CLI's model is not empty; CLI's model should be empty.", model.empty());
    
	String[] invalidClear = {"clear", "WRONG"};
	out.reset();
	err.reset();
	System.out.println("\nERROR: Error in parsing command. Proper command usage is: \n  clear\n");
	String expected = out.toString();
	out.reset();
	err.reset();
	executeCommand(invalidClear);
	assertEquals("Initial print all does not equal printout", expected, out.toString());
    }
    
    @Test
    public void quitTest() {
	String[] invalidQuit = {"quit", "WRONG"};
	out.reset();
	err.reset();
	System.out.println("\nERROR: Error in parsing command. Proper command usage is: \n quit\n");
	String expected = out.toString();
	out.reset();
	err.reset();
	executeCommand(invalidQuit);
	assertEquals("Initial print all does not equal printout", expected, out.toString());
    }
}

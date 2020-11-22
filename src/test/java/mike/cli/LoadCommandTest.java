package mike.cli;

import mike.HelperMethods;
import mike.controller.CLIController;
import mike.datastructures.Model;
import mike.view.ViewTemplate;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class LoadCommandTest {

    Model model;
    ViewTemplate view;
    CLIController control;

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();

    private final String sep = File.separator;
    
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
        String[] loadErr3 = {"load", sep + "src" + sep + "test" + sep + "java" + sep + "mike" + sep + "testDemo2.json"};
        control.evaluateCommand(loadErr3);
        assertEquals("load error message did not appear correctly.", expected, out.toString());

        // Test relative Path
        String[] load = {"load", sep + "src" + sep + "test" + sep + "java" + sep + "mike" + sep + "testDemoCLI.json"};
        control.evaluateCommand(load);
	File file = new File(System.getProperty("user.dir") + sep + "src" + sep + "test" + sep + "java" + sep + "mike" + sep + "testDemoCLI.json");
        loadWorked(file);

        // Test absolute Path
        load[1] = System.getProperty("user.dir") + sep + "src" + sep + "test" + sep + "java" + sep + "mike" + sep + "testDemoCLI.json";
        control.evaluateCommand(load);
        File file2 = new File(load[1]);
        loadWorked(file2);
    }

    private void loadWorked(File file) throws IOException, ParseException, java.text.ParseException {
        HelperMethods.save(file, model);
        Model loadModel = new Model();
        HelperMethods.load(file, loadModel, null, null);

        assertEquals("loadModel contains a class.", 0, loadModel.getEntities().size());
        assertEquals("loadModel contains a relationship.", 0, loadModel.getRelationships().size());
    }

}

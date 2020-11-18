package mike.cli;

import mike.HelperMethods;
import mike.controller.CLIController;
import mike.datastructures.Model;
import mike.view.ViewTemplate;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class SaveCommandTest {

    Model model;
    ViewTemplate view;
    CLIController control;
    
    private final String sep = File.separator;
    private final String partialPath = sep + "src" + sep + "test" + sep + "java" + sep + "mike";

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

    @Test
    public void saveTest() throws IOException, ParseException {
        // Test relative Path error length 4
        System.out.println("\nERROR: "
                + "Error in parsing command. Proper command usage is: \n"
                + "  save <name>.json\n");
        String expected = out.toString();
        resetStreams();
        String[] saveErr2 = {"save", "testDemoCLI.json", partialPath, "WRONG"};
        control.evaluateCommand(saveErr2);
        assertEquals("save error message did not appear correctly.", expected, out.toString());
        resetStreams();

        // Test relative Path
        String[] save = {"save", partialPath + sep + "testDemoCLI.json"};
        control.evaluateCommand(save);
	File file = new File(System.getProperty("user.dir") + partialPath + sep + "testDemoCLI.json");
        saveWorked(file);

        // Test absolute Path
        save[1] = System.getProperty("user.dir") + partialPath + sep + "testDemoCLI.json";
        control.evaluateCommand(save);
        saveWorked(file);

        // Test giving relative directory
        String[] saveDir = {"save", "testDemoCLI.json", partialPath};
        control.evaluateCommand(saveDir);
        saveWorked(file);

        // Test giving absolute directory
        saveDir[2] = System.getProperty("user.dir") + partialPath + sep + "testDemoCLI.json";
        control.evaluateCommand(saveDir);
        saveWorked(file);
    }

    private void saveWorked(File file) throws IOException, ParseException {
        HelperMethods.save(file, model);
        Object obj = new JSONParser().parse(new FileReader(file));

        String emptyModelFile = "{\"Relationships\":[],\"Classes\":[]}";
        assertEquals("CLI is null; Should not be null.", emptyModelFile, obj.toString());
    }

}

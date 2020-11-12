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
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class SaveCommandTest {

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

    private void saveWorked(Path path) throws IOException, ParseException {
        HelperMethods.save(path, model);
        File directory = new File(path.toString());
        Object obj = new JSONParser().parse(new FileReader(directory));

        String emptyModelFile = "{\"Relationships\":[],\"Classes\":[]}";
        assertEquals("CLI is null; Should not be null.", emptyModelFile, obj.toString());
    }

}

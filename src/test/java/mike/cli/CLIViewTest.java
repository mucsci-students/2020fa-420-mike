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

public class CLIViewTest {
    
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
	
   

}

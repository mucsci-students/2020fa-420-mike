package mike;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mike.datastructures.Model;
import mike.controller.CLIController;
import mike.datastructures.Entity;
import mike.datastructures.Method;
import mike.datastructures.Relationship.Type;

import mike.view.ViewTemplate;

public class HelperMethodsTest {
    
    File file;
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
	file = new File(System.getProperty("user.dir") + sep + "src" + sep + "test" + sep + "java" + sep + "mike" + sep + "testDemo.json");
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
    public void HelperMethodConstructor() {
	new HelperMethods();
    }
    
    @Test
    public void listClassesTest() {
	// Easy
	System.out.println("Classes:");
	String expected = out.toString();
	resetStreams();
        HelperMethods.listClasses(model);
	String actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
	
	model.createClass("c1");
	model.createField("c1", "f1", "int", "public");
	model.createMethod("c1", "m1", "boolean", "private");
	model.createParameter("c1",  "m1",  "p1",  "String");
	resetStreams();
	
	// Medium
	System.out.println("Classes:");
	System.out.println("\tc1:");
	System.out.println("\t\tfields:  [ (public) int f1 ]");
	System.out.println("\t\tmethods: [ (private) boolean m1 -- {(String) p1} ]");
	expected = out.toString();
	resetStreams();
        HelperMethods.listClasses(model);
	actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
	
	model.createClass("c2");
	model.createField("c1", "f2", "int", "public");
	model.createMethod("c1", "m2", "boolean", "private");
	model.createParameter("c1",  "m1",  "p2",  "String");
	resetStreams();
	
	// Hard
	System.out.println("Classes:");
	System.out.println("\tc1:");
	System.out.println("\t\tfields:  [ (public) int f1, (public) int f2 ]");
	// Isn't println because brackets screw up byteArrayOutputStream
	System.out.print("\t\tmethods: [ (private) boolean m1 -- {(String) p1, (String) p2},\n");
	System.out.println("\t\t\t   (private) boolean m2 -- {} ]");
	System.out.println("\tc2:");
	System.out.println("\t\tfields:  [  ]");
	System.out.println("\t\tmethods: [  ]");
	expected = out.toString();
	resetStreams();
	HelperMethods.listClasses(model);
	actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
    }
    
    @Test
    public void listRelationshipTest() {
	// Easy
	System.out.println("Relationships:");
	String expected = out.toString();
	resetStreams();
	HelperMethods.listRelationships(model);
	String actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
	
	model.createClass("c1");
	model.createClass("c2");
	model.createRelationship(Type.INHERITANCE, "c1", "c2");
	resetStreams();
	
	// Medium
	System.out.println("Relationships:");
	System.out.println("   -- INHERITANCE: c1--c2");
	expected = out.toString();
	resetStreams();
	HelperMethods.listRelationships(model);
	actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
	
	model.createClass("c3");
	model.createRelationship(Type.AGGREGATION, "c1", "c1");
	model.createRelationship(Type.COMPOSITION, "c3", "c1");
	model.createRelationship(Type.REALIZATION, "c2",  "c3");
	resetStreams();
	
	// Hard
	System.out.println("Relationships:");
	System.out.println("   -- INHERITANCE: c1--c2");
	System.out.println("   -- AGGREGATION: c1--c1");
	System.out.println("   -- COMPOSITION: c3--c1");
	System.out.println("   -- REALIZATION: c2--c3");
	expected = out.toString();
	resetStreams();
	HelperMethods.listRelationships(model);
	actual = out.toString();
	assertEquals("Initial print all does not equal printout", expected, actual);
    }
    
    @Test
    public void intialSaveTest() throws IOException, ParseException {	
	HelperMethods.save(file, model);
	Object obj = new JSONParser().parse(new FileReader(file));
	
	String emptyModelFile = "{\"Relationships\":[],\"Classes\":[]}";
	assertEquals("CLI is null; Should not be null.", emptyModelFile, obj.toString());
    }
    
    @Test
    public void classSaveTest() throws IOException, ParseException {
	model.createClass("c1");
	model.createClass("c2");
	model.createField("c1", "f1", "int", "PUBLIC");
	model.createField("c1", "f2", "String", "PRIVATE");
	model.createMethod("c1", "m1", "String", "PUBLIC");
	model.createMethod("c1",  "m2",  "int", "PROTECTED");
	model.createParameter("c1",  "m2",  "p1", "char");
	model.createParameter("c1" , "m2",  "p2",  "boolean");
	
	HelperMethods.save(file, model);
	Object obj = new JSONParser().parse(new FileReader(file));
	
	String emptyModelFile = 
		  "{\"Relationships\":[],"
		+ "\"Classes\":["
			+ "{\"xPosition\":0,"
			+ "\"methods\":["
				+ "{\"methodVis\":\"PUBLIC\","
				+ "\"methodType\":\"String\","
					+ "\"Parameters\":[],"
				+ "\"methodName\":\"m1\"},"
				+ "{\"methodVis\":\"PROTECTED\","
				+ "\"methodType\":\"int\","
					+ "\"Parameters\":["
						+ "{\"paramType\":\"char\","
						+ "\"paramName\":\"p1\"},"
						+ "{\"paramType\":\"boolean\","
						+ "\"paramName\":\"p2\"}],"
				+ "\"methodName\":\"m2\"}],"
			+ "\"yPosition\":0,"
			+ "\"className\":\"c1\","
			+ "\"fields\":["
				+ "{\"fieldName\":\"f1\","
				+ "\"fieldVis\":\"PUBLIC\","
				+ "\"fieldType\":\"int\"},"
				+ "{\"fieldName\":\"f2\","
				+ "\"fieldVis\":\"PRIVATE\","
				+ "\"fieldType\":\"String\"}]},"
			+ "{\"xPosition\":0,"
			+ "\"methods\":[],"
			+ "\"yPosition\":0,"
			+ "\"className\":\"c2\","
			+ "\"fields\":[]}]}";
	assertEquals("CLI is null; Should not be null.", emptyModelFile, obj.toString());
    }
      
    @Test
    public void relationshipSaveTest() throws IOException, ParseException {
	model.createClass("c1");
	model.createClass("c2");
	model.createClass("c3");
	model.createRelationship(Type.AGGREGATION, "c1", "c2");
	model.createRelationship(Type.INHERITANCE, "c3", "c1");
	model.createRelationship(Type.COMPOSITION, "c2", "c2");

	HelperMethods.save(file, model);
	Object obj = new JSONParser().parse(new FileReader(file));
	
	String emptyModelFile = 
		 	"{\"Relationships\":["
		 		+ "{\"ClassOne\":\"c1\","
		 		+ "\"relationName\":\"AGGREGATION\","
		 		+ "\"ClassTwo\":\"c2\"},"
		 		+ "{\"ClassOne\":\"c3\","
		 		+ "\"relationName\":\"INHERITANCE\","
		 		+ "\"ClassTwo\":\"c1\"},"
		 		+ "{\"ClassOne\":\"c2\","
		 		+ "\"relationName\":\"COMPOSITION\","
		 		+ "\"ClassTwo\":\"c2\"}],"
		 	+ "\"Classes\":["
		 		+ "{\"xPosition\":0,"
		 		+ "\"methods\":[],"
		 		+ "\"yPosition\":0,"
		 		+ "\"className\":\"c1\","
		 		+ "\"fields\":[]},"
		 		+ "{\"xPosition\":0,"
		 		+ "\"methods\":[],"
		 		+ "\"yPosition\":0,"
		 		+ "\"className\":\"c2\","
		 		+ "\"fields\":[]},"
		 		+ "{\"xPosition\":0,"
		 		+ "\"methods\":[],"
		 		+ "\"yPosition\":0,"
		 		+ "\"className\":\"c3\","
		 		+ "\"fields\":[]}]}";
	assertEquals("CLI is null; Should not be null.", emptyModelFile, obj.toString());
    }
    
    @Test
    public void intialLoadTest() throws IOException, java.text.ParseException, ParseException {
	HelperMethods.save(file, model);
	Model loadModel = new Model();
	HelperMethods.load(file, loadModel, null, null);
	
	assertEquals("loadModel contains a class.", 0, loadModel.getEntities().size());
	assertEquals("loadModel contains a relationship.", 0, loadModel.getRelationships().size());
    }
    
    @Test
    public void classLoadTest() throws IOException, java.text.ParseException, ParseException {
	model.createClass("c1");
	model.createClass("c2");
	model.createField("c1",  "f1",  "int", "PUBLIC");
	model.createField("c1", "f2", "String", "PRIVATE");
	model.createMethod("c1", "m1", "String", "PROTECTED");
	model.createMethod("c1",  "m2",  "int", "PROTECTED");
	model.createParameter("c1",  "m2",  "p1", "char");
	model.createParameter("c1" , "m2",  "p2",  "boolean");
	
	HelperMethods.save(file, model);
	Model loadModel = new Model();
	HelperMethods.load(file, loadModel, null, null);
	
	// Classes (and relationships)
	assertEquals("loadModel contains more or less than two classes.", 2, loadModel.getEntities().size());
	assertEquals("loadModel contains a relationship.", 0, loadModel.getRelationships().size());
	assertTrue("loadModel does not contain class c1", loadModel.containsEntity("c1"));
	assertTrue("loadModel does not contain class c2", loadModel.containsEntity("c2"));
	Entity c1 = loadModel.copyEntity("c1");
	
	// Fields
	assertEquals("c1 contains more or less than two fields.", 2, c1.getFields().size());
	assertTrue("c1 does not contain field f1", c1.containsField("f1"));
	assertTrue("c1 does not contain field f1", c1.containsField("f2"));
	
	// Methods
	assertEquals("c1 contains more or less than two methods.", 2, c1.getMethods().size());
	assertTrue("c1 does not contain method f1", c1.containsMethod("m1"));
	assertTrue("c1 does not contain method f1", c1.containsMethod("m2"));
	Method m2 = c1.copyMethod("m2");
	
	// Parameters
	assertEquals("m2 contains more or less than two parameters.", 2, m2.getParameters().size());
	assertTrue("m2 does not contain parameter p1", m2.containsParameter("p1"));
	assertTrue("m2 does not contain parameter p2", m2.containsParameter("p2"));
    }
    
    @Test
    public void relationshipLoadTest() throws java.text.ParseException, IOException, ParseException {
	model.createClass("c1");
	model.createClass("c2");
	model.createClass("c3");
	model.createRelationship(Type.AGGREGATION, "c1", "c2");
	model.createRelationship(Type.INHERITANCE, "c3", "c1");
	model.createRelationship(Type.COMPOSITION, "c2", "c2");
	model.createRelationship(Type.REALIZATION, "c1", "c1");	
	
	HelperMethods.save(file, model);
	Model loadModel = new Model();
	HelperMethods.load(file, loadModel, null, null);
	
	// Classes
	assertEquals("loadModel contains more or less than three classes.", 3, loadModel.getEntities().size());
	assertTrue("loadModel does not contain class c1", loadModel.containsEntity("c1"));
	assertTrue("loadModel does not contain class c2", loadModel.containsEntity("c2"));
	assertTrue("loadModel does not contain class c3", loadModel.containsEntity("c3"));

	// Relationships
	assertEquals("loadModel contains more or less than four relationships.", 4, loadModel.getRelationships().size());
	assertTrue("loadModel does not contain relationship c1--c2", loadModel.containsRelationship(Type.AGGREGATION, "c1", "c2"));
	assertTrue("loadModel does not contain relationship c3--c1", loadModel.containsRelationship(Type.INHERITANCE, "c3", "c1"));
	assertTrue("loadModel does not contain relationship c2--c2", loadModel.containsRelationship(Type.COMPOSITION, "c2", "c2"));
	assertTrue("loadModel does not contain relationship c1--c1", loadModel.containsRelationship(Type.REALIZATION, "c1", "c1"));
    }
    
    @Test
    public void checkEnumTest() {
	assertEquals("Type does not equal AGGREGATION", Type.AGGREGATION, HelperMethods.checkEnum("AGGREGATION"));
	assertEquals("Type does not equal INHERITANCE", Type.INHERITANCE, HelperMethods.checkEnum("INHERITANCE"));
	assertEquals("Type does not equal COMPOSITION", Type.COMPOSITION, HelperMethods.checkEnum("COMPOSITION"));
	assertEquals("Type does not equal REALIZATION", Type.REALIZATION, HelperMethods.checkEnum("REALIZATION"));
	assertEquals("Type does not equal null", null, HelperMethods.checkEnum("invalid"));
    }
    	
    
    
    
}

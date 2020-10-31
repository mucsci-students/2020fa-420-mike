package mike.cli;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mike.datastructures.Model;
import mike.view.CLIView;
import mike.view.ViewTemplate;
import mike.datastructures.Relationship.Type;

public class CLIViewTest {

    private Model model;
    private CLIView cli;

    @Before
    public void createCLI() throws IOException {
        model = new Model();
        cli = (CLIView) new ViewTemplate(ViewTemplate.InterfaceType.CLI, model).getViewinterface();
    }

    @After
    public void cleanCLI() {
        String[] commands = {"sudo", "clear"};
        cli.evaluateCommand(commands);
    }


    @Test
    public void initClassTest(){
        assertTrue("CLI is null; Should not be null.", cli != null);
        assertTrue("CLI's model is not empty; Should be empty.", cli.getCLIModel().empty());
    }

    //**********************************************************************************************//
    //**                                CLASS TESTS                                               **//
    //**                            (CREATE, RENAME, DELETE)                                      **//
    //**********************************************************************************************//

    @Test
    public void createClassTest() {
        // Test making a single class
        String[] commands = {"create", "class", "c1"};
        cli.evaluateCommand(commands);
        assertTrue("CLI did not make a class; Should be one class called c1.", model.containsEntity("c1"));
        assertEquals("CLI made more or less than one class", 1, model.getEntities().size());

        // Test making multiple classes
        commands[2] = "c2";
        cli.evaluateCommand(commands);
        commands[2] = "c3";
        cli.evaluateCommand(commands);
        commands[2] = "c4";
        cli.evaluateCommand(commands);
        commands[2] = "c5";
        cli.evaluateCommand(commands);

        assertTrue("CLI did not make a class; Should be class called c2.", model.containsEntity("c2"));
        assertTrue("CLI did not make a class; Should be class called c3.", model.containsEntity("c3"));
        assertTrue("CLI did not make a class; Should be class called c4.", model.containsEntity("c4"));
        assertTrue("CLI did not make a class; Should be class called c5.", model.containsEntity("c5"));
        assertEquals("CLI did not make classes; Should be 5 classes.", 5, model.getEntities().size());

        // Test NOT creating with invalid length
        String[] invalidLengthCommand = {"create", "class", "badLength", "Yes."};
        cli.evaluateCommand(invalidLengthCommand);

        assertFalse("CLI created a class with bad length; Class named 'badLength' should not exist.", model.containsEntity("badLength"));
        assertEquals("CLI made a class; Should be 5 classes.", 5, model.getEntities().size());

        // Test NOT making an EXISTING class
        commands[2] = "c1";
        cli.evaluateCommand(commands);

        assertFalse("CLI created a class that already existed; Length should be 5.", model.getEntities().size() == 6);
    }

    @Test
    public void renameClassTest() {
        // Test setup of class creation
        String[] commands = {"create", "class", "c1"};
        cli.evaluateCommand(commands);
        commands[2] = "c2";
        cli.evaluateCommand(commands);

        assertTrue("CLI did not make a class; Should be class called c1.", model.containsEntity("c1"));
        assertTrue("CLI did not make a class; Should be class called c2.", model.containsEntity("c2"));

        // Test rename a class
        String[] rename = {"rename", "class", "c1", "c3"};
        cli.evaluateCommand(rename);

        assertTrue("CLI did not rename a class; Should be class called c3.", model.containsEntity("c3"));
        assertFalse("CLI did not remove/rename old class; c1 should no longer exist.", model.containsEntity("c1"));

        // Test NOT renaming with invalid length
        String[] invalidLengthCommand = {"create", "class", "c3", "c1", "badLength"};
        cli.evaluateCommand(invalidLengthCommand);

        assertFalse("CLI created a class with bad length; Class named 'c1' should not exist.", model.containsEntity("c1"));
        assertEquals("CLI made a class; Should be 2 classes.", 2, model.getEntities().size());


        // Test rename a NONEXISTENT class
        rename[2] = "c4";
        rename[3] = "c5";
        cli.evaluateCommand(rename);

        assertTrue("CLI renamed a class. c2 should exist.", model.containsEntity("c2"));
        assertTrue("CLI renamed a class. c3 should exist.", model.containsEntity("c3"));
        assertFalse("CLI created a new class. c4 should not exist.", model.containsEntity("c4"));
        assertFalse("CLI created a new class. c5 should not exist.", model.containsEntity("c5"));

        // Test rename an EXISTING class to another EXISTING class
        rename[2] = "c3";
        rename[3] = "c2";
        cli.evaluateCommand(rename);

        assertTrue("CLI renamed a class. c2 should exist.", model.containsEntity("c2"));
        assertTrue("CLI renamed a class. c3 should exist.", model.containsEntity("c3"));
        assertEquals("CLI does not have 2 classes. Model should have two entities.", 2, model.getEntities().size());
    }

    @Test
    public void deleteClassTest() {
        // Test setup of class creation
        String[] commands = {"create", "class", "c1"};
        cli.evaluateCommand(commands);
        commands[2] = "c2";
        cli.evaluateCommand(commands);

        assertTrue("CLI did not make a class; Should be class called c1.", model.containsEntity("c1"));
        assertTrue("CLI did not make a class; Should be class called c2.", model.containsEntity("c2"));

        // Test deleting a class
        String[] delete = {"delete", "class", "c1"};
        cli.evaluateCommand(delete);

        assertFalse("CLI did not delete a class; c1 should not exist.", model.containsEntity("c1"));
        assertTrue("CLI did something to another class; c2 should be okay.", model.containsEntity("c2"));

        // Test NOT deleting with invalid length
        String[] invalidLengthCommand = {"create", "class", "c2", "badLength"};
        cli.evaluateCommand(invalidLengthCommand);

        assertTrue("CLI deleted a class with bad length; Class named 'c2' should exist.", model.containsEntity("c2"));

        // Test NOT deleting a NONEXISTENT class
        cli.evaluateCommand(delete);

        assertTrue("CLI did deleted a class it wasn't supposed to; Class 'c2' should exist.", model.containsEntity("c2"));

        //Test deleting classes that have relationships
        //Ensure relationships are also deleted

    }

    //**********************************************************************************************//
    //**                             RELATIONSHIP TESTS                                           **//
    //**                              (CREATE, DELETE)                                            **//
    //**********************************************************************************************//

    @Test
    public void createRelationshipTest() {
        //create classes to have relationships
        String[] classes = {"create", "class", "c1"};
        cli.evaluateCommand(classes);
        classes[2] = "c2";
        cli.evaluateCommand(classes);
        classes[2] = "c3";
        cli.evaluateCommand(classes);

        //attempting to create relationships to non-existent classes
        String[] wrongRels = {"create", "relationship", "aggregation", "fake1", "fake2"};
        cli.evaluateCommand(wrongRels);
        assertTrue("Relationship was created with fake classes", model.getRelationships().isEmpty());

        //creating relationship with invalid type
        wrongRels[2] = "fake";
        wrongRels[3] = "c1";
        wrongRels[4] = "c2";
        cli.evaluateCommand(wrongRels);
        assertTrue("Relationship was created with invalid type", model.getRelationships().isEmpty());

        //create relationships of each type
        String[] relationships = {"create", "relationship", "aggregation", "c1", "c2"};
        cli.evaluateCommand(relationships);
        assertTrue("Aggregation relationship between c1 and c2 not found", model.containsRelationship(Type.AGGREGATION, "c1", "c2"));

        relationships[2] = "composition";
        relationships[3] = "c2";
        relationships[4] = "c3";

        cli.evaluateCommand(relationships);
        assertTrue("Composition relationship between c2 and c3 not found", model.containsRelationship(Type.COMPOSITION, "c2", "c3"));

        relationships[2] = "inheritance";
        relationships[3] = "c1";

        cli.evaluateCommand(relationships);
        assertTrue("Inheritance relationship between c1 and c3 not found", model.containsRelationship(Type.INHERITANCE, "c1", "c3"));

        //recursive relationship
        relationships[2] = "realization";
        relationships[3] = "c2";
        relationships[4] = "c2";

        cli.evaluateCommand(relationships);
        assertTrue("Recursive realization relationship associated with c2 not found", model.containsRelationship(Type.REALIZATION, "c2", "c2"));

        //attempting to create a relationship that already exists
        cli.evaluateCommand(relationships);
        assertEquals("Recursive realization c2 relationship created twice", 4, model.getRelationships().size());

        relationships[2] = "composition";
        assertEquals("New c2 recursive relationship created, even though c2 already has a recursive relationship", 4, model.getRelationships().size());
    }

    @Test
    public void deleteRelationshipTest() {
        //create classes to have relationships
        String[] classes = {"create", "class", "c1"};
        cli.evaluateCommand(classes);
        classes[2] = "c2";
        cli.evaluateCommand(classes);
        classes[2] = "c3";
        cli.evaluateCommand(classes);

        //create relationships (c1--c2, c2--c3, c3--c3)
        String[] relationships = {"create", "relationship", "aggregation", "c1", "c2"};
        cli.evaluateCommand(relationships);

        relationships[2] = "realization";
        relationships[3] = "c2";
        relationships[4] = "c3";
        cli.evaluateCommand(relationships);

        relationships[2] = "inheritance";
        relationships[3] = "c3";
        cli.evaluateCommand(relationships);

        //deleting non-existent relationships
        relationships[0] = "delete";
        relationships[2] = "fakeType";
        cli.evaluateCommand(relationships);
        assertTrue("c3 recursive relationship deleted despite fake relationship type", model.containsRelationship(Type.INHERITANCE, "c3", "c3"));

        relationships[2] = "realization";
        cli.evaluateCommand(relationships);
        assertTrue("Relationship c3--c3 deleted despite wrong relationship type given", model.containsRelationship(Type.INHERITANCE, "c3", "c3"));

        relationships[3] = "fake1";
        relationships[4] = "fake2";
        cli.evaluateCommand(relationships);
        assertEquals("Relationship deleted despite fake class names", 3, model.getRelationships().size());

        //deleting relationships
        relationships[2] = "realization";
        relationships[3] = "c2";
        relationships[4] = "c3";
        cli.evaluateCommand(relationships);
        assertFalse("Relationship c2--c3 failed to delete", model.containsRelationship(Type.REALIZATION, "c2", "c3"));
        assertEquals("Size of relationship list is wrong", 2, model.getRelationships().size());

        relationships[2] = "inheritance";
        relationships[3] = "c3";
        cli.evaluateCommand(relationships);
        assertFalse("Relationship c3--c3 failed to delete", model.containsRelationship(Type.INHERITANCE, "c3", "c3"));
        assertEquals("Size of relationship list is wrong", 1, model.getRelationships().size());
    }

    @Test
    public void clearTest() {
        // Make a whole bunch of stuff to be cleared.
        String[] classes = {"create", "class", "c1"};
        cli.evaluateCommand(classes);
        classes[2] = "c2";
        cli.evaluateCommand(classes);

        assertEquals("CLI did not make the classes; Should be 2.", 2, model.getEntities().size());

        String[] field = {"create", "field", "c1", "int", "f1"};
        cli.evaluateCommand(field);
        field[4] = "f2";
        cli.evaluateCommand(field);
        field[2] = "c2";
        field[4] = "f1";
        cli.evaluateCommand(field);

        assertTrue("CLI did not make 2 fields in c1; C1 should have f1 and f2.", model.getEntities().get(0).containsField("f1"));
        assertTrue("CLI did not make 2 fields in c1; C1 should have f1 and f2.", model.getEntities().get(0).containsField("f2"));
        assertTrue("CLI did not make 1 field in c2; C2 should have f1.", model.getEntities().get(1).containsField("f1"));

        String[] method = {"create", "method", "c1", "double", "m1"};
        cli.evaluateCommand(method);
        method[4] = "m2";
        cli.evaluateCommand(method);
        method[2] = "c2";
        method[4] = "m1";
        cli.evaluateCommand(method);

        assertTrue("CLI did not make 2 methods in c1; C1 should have m1 and m2.", model.getEntities().get(0).containsMethod("m1"));
        assertTrue("CLI did not make 2 methods in c1; C1 should have m1 and m2.", model.getEntities().get(0).containsMethod("m2"));
        assertTrue("CLI did not make 1 method in c2; C2 should have m1.", model.getEntities().get(1).containsMethod("m1"));

        String[] parameter = {"create", "parameter", "c1", "m1", "char", "p1"};
        cli.evaluateCommand(parameter);
        parameter[5] = "p2";
        cli.evaluateCommand(parameter);
        parameter[3] = "m2";
        parameter[5] = "p1";
        cli.evaluateCommand(parameter);
        parameter[2] = "c2";
        parameter[3] = "m1";
        cli.evaluateCommand(parameter);

        assertTrue("CLI did not make 2 parameters in c1 > m1; M1 should have p1 and p2.", model.getEntities().get(0).getMethods().get(0).containsParameter("p1"));
        assertTrue("CLI did not make 2 parameters in c1 > m1; M1 should have p1 and p2.", model.getEntities().get(0).getMethods().get(0).containsParameter("p2"));
        assertTrue("CLI did not make 1 parameter in c1 > m2; M2 should have p1.", model.getEntities().get(0).getMethods().get(1).containsParameter("p1"));
        assertTrue("CLI did not make 1 parameter in c2 > m1; M1 should have p1.", model.getEntities().get(1).getMethods().get(0).containsParameter("p1"));

        String[] relationship = {"create", "relationship", "realization", "c1", "c1"};
        cli.evaluateCommand(relationship);
        relationship[2] = "aggregation";
        relationship[4] = "c2";
        cli.evaluateCommand(relationship);

        assertTrue("CLI did not make a recursive relationship. There should be a realization from c1 to c1.", model.containsRelationship(Type.REALIZATION, "c1", "c1"));
        assertTrue("CLI did not make a normal relationship. There should be an aggregation from c1 to c2.", model.containsRelationship(Type.AGGREGATION, "c1", "c2"));


        String[] clear = {"sudo", "clear"};
        cli.evaluateCommand(clear);

        assertTrue("CLI's model is not empty; CLI's model should be empty.", model.empty());
    }

}

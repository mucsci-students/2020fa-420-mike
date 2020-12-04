package mike.datastructures;

//import junit methods
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

//import local model to test
import mike.datastructures.Relationship.Type;

/** Run tests on Model Data Structure
 *
 * @author Stefan Gligorevic
 */
public class ModelTest {

    /** create an instance of the Model data structure
     *
     * assert that the relationships and entities lists are empty
     */
    @Test
    public void initClass()
    {
        Model initClass = new Model();
        assertTrue("Relationships list is empty", initClass.getRelationships().isEmpty());
        assertTrue("Entities list is empty", initClass.getEntities().isEmpty());
    }
    
    @Test
    public void initCopyClass()
    {
        Model initClass = new Model();
        Model copyClassOne = new Model(initClass);
        assertTrue("Relationships list is empty", copyClassOne.getRelationships().isEmpty());
        assertTrue("Entities list is empty", copyClassOne.getEntities().isEmpty());
        
        copyClassOne.createClass("c1");
        Entity c1 = copyClassOne.copyEntity("c1");
        copyClassOne.createClass("c2");
        copyClassOne.createRelationship(Type.AGGREGATION, "c1", "c2");
        Model copyClassTwo = new Model(copyClassOne);
        assertFalse("c1 list is empty", initClass.containsEntity("c1"));
        assertFalse("c1 list is empty", initClass.containsEntity("c2"));
        assertFalse("c1 has more or less than one field", initClass.containsRelationship(Type.AGGREGATION, "c1", "c2"));
        assertEquals("Model has more or less than one relationship", 1, copyClassTwo.getRelationships().size());
        assertTrue("Model does not contain relationship c1-c2", copyClassTwo.containsRelationship(Type.AGGREGATION, "c1", "c2"));
        assertEquals("Model has more or less than two entities", 2, copyClassTwo.getEntities().size());
        assertTrue("Entities list is empty", copyClassTwo.containsEntity("c1"));
        assertTrue("Entities list is empty", copyClassTwo.containsEntity("c2"));
        
        copyClassTwo.createField("c1", "f1", "String", "public");
        copyClassTwo.createMethod("c1", "m1", "boolean", "private");
        copyClassTwo.createParameter("c1", "m1", "p1", "int");
        Model copyClassThree = new Model(copyClassTwo);
        Entity c1Copy = copyClassThree.copyEntity("c1");
        assertFalse("c1 list is empty", c1.containsField("f1"));
        assertFalse("c1 list is empty", c1.containsMethod("m1"));
        assertEquals("c1 has more or less than one field", 1, c1Copy.getFields().size());
        assertTrue("c1 list is empty", c1Copy.containsField("f1"));
        assertEquals("c1 has more or less than one method", 1, c1Copy.getMethods().size());
        assertTrue("c1 list is empty", c1Copy.containsMethod("m1"));
        assertEquals("m1 has more or less than one field", 1, c1Copy.copyMethod("m1").getParameters().size());
        assertTrue("Method list is empty", c1Copy.copyMethod("m1").containsParameter("p1"));
    }

    /* ------------------------------------------------------------------------------ */
    /*                          TEST HELPER/MEMBER FUNCTIONS                          */
    /* ------------------------------------------------------------------------------ */

    /** test the clear() method
     *
     * create model and relationships then call clear
     * assert that relationships and entities lists are empty after calling clear
     */
    @Test
    public void testClear()
    {
        Model model = new Model();
        model.createClass("Entity1");
        model.createClass("Entity2");
        model.createRelationship(Type.INHERITANCE, "Entity1", "Entity2");

        model.clear();

        assertTrue("Relationships list is empty after clear is called", model.getRelationships().isEmpty());
        assertTrue("Entities list is empty after clear is called", model.getEntities().isEmpty());

        model.createClass("E1");
        model.createClass("E2");
        model.createRelationship(Type.INHERITANCE, "E1", "E2");

        model.createClass("E3");
        model.createClass("E4");
        model.createRelationship(Type.INHERITANCE, "E3", "E4");

        model.createClass("E5");
        model.createClass("E6");
        model.createRelationship(Type.INHERITANCE, "E5", "E6");

        model.createClass("E7");
        model.createClass("E8");

        model.clear();

        assertTrue("Relationships list is empty after clear is called", model.getRelationships().isEmpty());
        assertTrue("Entities list is empty after clear is called", model.getEntities().isEmpty());
    }

    /** test the empty() method
     *
     * ensure that empty() returns true when a model instance has no entities/relationships
     * ensure that empty() returns false when a model instance has >= 1 entities/relationships
     */
    @Test
    public void testEmpty()
    {
        Model model = new Model();
        assertTrue("New model instance is empty", model.empty());

        model.createClass("E1");
        assertFalse("Model instance with class is not empty", model.empty());
        
        model.createClass("E2");
        model.createRelationship(Type.REALIZATION, "E1", "E2");
        assertFalse("Model instance with classes and relationship is not empty.", model.empty());

        model.clear();
        assertTrue("empty() returns true after clear() is called", model.empty());
    }

    /** test copyEntity
     *
     */
    @Test
    public void testCopyEntity()
    {
        Model model = new Model();
        model.createClass("E1");
        //copy entity
        Entity e1copy = model.copyEntity("E1");
        assertTrue("Two entities with no fields/methods are equal after copyEntity call", model.getEntities().get(0).equals(e1copy));

        //add attributes to entity
        model.createField("E1", "att1", "int", "PUBLIC");
        model.createField("E1", "att2", "int", "PROTECTED");
        model.createMethod("E1", "m1", "int", "PRIVATE");
        model.createMethod("E1", "m2", "int", "PUBLIC");

        Entity new_e1copy = model.copyEntity("E1");
        assertTrue("Entity with fields and Methods was copied correctly", model.getEntities().get(0).equals(new_e1copy));
        assertTrue("Fields are the same", model.getEntities().get(0).getFields().equals(new_e1copy.getFields()));
        assertTrue("Methods are the same", model.getEntities().get(0).getMethods().equals(new_e1copy.getMethods()));
        
        assertEquals("Returns null if non-existant entity given.", null, model.copyEntity("E2"));
    }

    /** test containsEntity
     *
     */
    @Test
    public void testContainsEntity()
    {
        Model model = new Model();
        model.createClass("e");
        assertTrue("Found class 'e'", model.containsEntity("e"));
        assertFalse("False for non-existing class", model.containsEntity("fake"));
    }

    /** test containsRelationship
     *
     */

    @Test
    public void testContainsRelationship()
    {
        Model model = new Model();
        model.createClass("e");
        model.createClass("e2");
        model.createRelationship(Type.INHERITANCE, "e", "e2");

        assertTrue("Found relationship 'r, e, e2'", model.containsRelationship(Type.INHERITANCE, "e", "e2"));
        assertFalse("False when given incorrect relationship type", model.containsRelationship(Type.AGGREGATION, "e", "e2"));
        assertFalse("False for non-existent class1 name", model.containsRelationship(Type.INHERITANCE, "fake", "e2"));
        assertFalse("False for non-existent class2 name", model.containsRelationship(Type.INHERITANCE, "e", "fake"));
        assertFalse("False when pairs are in wrong order", model.containsRelationship(Type.INHERITANCE, "e2", "e"));
        
        model.renameClass("e", "e1");
        
        assertTrue("Found relationship after class1 name was updated", model.containsRelationship(Type.INHERITANCE, "e1", "e2"));
        model.renameClass("e2", "c2");
        assertTrue("Found relationship after class2 name was updated", model.containsRelationship(Type.INHERITANCE, "e1", "c2"));
    }


    /** test getRelationship
     *
     */
    @Test
    public void testGetRelationship()
    {
        Model model = new Model();
        model.createClass("e");
        model.createClass("e2");
        model.createRelationship(Type.COMPOSITION, "e", "e2");

        Relationship r = model.getRelationship(Type.COMPOSITION, "e", "e2");

        assertEquals("Relationship types are equal", Type.COMPOSITION, r.getName());
        assertEquals("Class1 names are equal", "e", r.getFirstClass());
        assertEquals("Class2 names are equal", "e2", r.getSecondClass());
        
        assertEquals("Null when given incorrect relationship type", null, model.getRelationship(Type.AGGREGATION, "e", "e2"));
        assertEquals("Null for non-existent class1 name", null, model.getRelationship(Type.COMPOSITION, "fake", "e2"));
        assertEquals("Null for non-existent class2 name", null, model.getRelationship(Type.COMPOSITION, "e", "fake"));
        assertEquals("Null when pairs are in wrong order", null, model.getRelationship(Type.COMPOSITION, "e2", "e"));
        
        assertEquals("Null when relationship is not found", null, model.getRelationship(Type.AGGREGATION, "fake", "stillFake"));
    }

    /* ------------------------------------------------------------------------------- */
    /*                       CLASS (ENTITY) FUNCTIONS                                  */
    /*                       (CREATE, RENAME, DELETE)                                  */
    /* ------------------------------------------------------------------------------- */

    /** test the createClass method
     *
     * ensure that model are created and the entities arrayLists are updated properly
     */
    @Test
    public void testCreateClass()
    {
        Model model = new Model();
        model.createClass("E1");
        assertEquals("Entities list size is 1", 1, model.getEntities().size());
        assertTrue("Entities list contains E1", model.containsEntity("E1"));

        assertFalse("createClass returned false after called with name of existing class", model.createClass("E1"));

        model.createClass("E2");
        model.createClass("E3");
        assertEquals("Entities list size is 3", 3, model.getEntities().size());
        assertTrue("Entities list contains E2", model.containsEntity("E2"));
        assertTrue("Entities list contains E3", model.containsEntity("E3"));
        
        assertFalse("Entity list created class with null name", model.createClass(null));
    }

    /** test the renameClass method
     *
     * ensure that model can be renamed properly
     */
    @Test
    public void testRenameClass()
    {
        Model model = new Model();
        model.createClass("E1");

        assertFalse("renameClass returned false after called with name of non-existing class", model.renameClass("Non-exist", "e"));

        model.renameClass("E1", "E2");
        assertTrue("Entities list contains E2", model.containsEntity("E2"));
        assertFalse("Entities list does not contain E1", model.containsEntity("E1"));

        model.createClass("E1");
        assertTrue("Entities list contains E1", model.containsEntity("E1"));

        assertFalse("renameClass returned false after renaming to already existing class", model.renameClass("E1", "E2"));

        model.createField("E1", "f", "int", "PUBLIC");
        model.createMethod("E1", "m", "int", "PUBLIC");

        Entity e1_copy = model.copyEntity("E1");
        model.renameClass("E1", "e");
        Entity e = model.copyEntity("e");

        assertTrue("Class renamed to e", model.containsEntity("e"));
        assertFalse("Class E1 no longer exists", model.containsEntity("E1"));
        assertTrue("Fields still in tact", e.getFields().equals(e1_copy.getFields()));
        assertTrue("Methods still in tact", e.getMethods().equals(e1_copy.getMethods()));

        /* Make sure class names were changed in existing relationships */

        assertTrue("Relationship r1, e, E2 created", model.createRelationship(Type.REALIZATION, "e", "E2"));
        assertTrue("Relationship r1, e, E2 exists", model.containsRelationship(Type.REALIZATION, "e", "E2"));
        assertTrue("Class E2 renamed to e2", model.renameClass("E2", "ent2"));

        assertFalse("Old relationship no longer exists", model.containsRelationship(Type.REALIZATION, "e", "E2"));
        assertTrue("Class name (2) in relationship was updated", model.containsRelationship(Type.REALIZATION, "e", "ent2"));

        model.renameClass("e", "e1");
        assertTrue("Class name (1) in relationship was updated", model.containsRelationship(Type.REALIZATION, "e1", "ent2"));
        assertFalse("Old relationship no longer exists", model.containsRelationship(Type.REALIZATION, "e", "ent2"));

    }

    /** test the deleteClass method
     *
     * ensure that model and their relationships are deleted properly
     */
    @Test
    public void testDeleteClass()
    {
        Model model = new Model();
        model.createClass("e");

        assertFalse("False when deleting non-existent class", model.deleteClass("fake"));

        model.deleteClass("e");
        assertFalse("Class e no longer exists", model.containsEntity("e"));
        assertEquals("Entities list size is 0", 0, model.getEntities().size());

        /*Deleting model that contain fields and methods*/
        model.createClass("cla");
        model.createField("cla", "f", "int", "PUBLIC");
        model.createMethod("cla", "m", "int", "PUBLIC");

        assertTrue("Class 'cla' has been deleted", model.deleteClass("cla"));
        assertFalse("Class 'cla' no longer exists", model.containsEntity("cla"));
        assertEquals("Entities list size is 0", 0, model.getEntities().size());

        /* Make sure relationships associated with deleted model are deleted */

        model.createClass("e");
        model.createClass("e2");
        model.createClass("e3");

        model.createRelationship(Type.COMPOSITION, "e", "e2");
        model.createRelationship(Type.COMPOSITION, "e3", "e");
        model.createRelationship(Type.COMPOSITION, "e2", "e3");

        model.deleteClass("e");
        assertFalse("Class e no longer exists", model.containsEntity("e"));
        assertEquals("Entities list size is 2", 2, model.getEntities().size());

        boolean deletedRels = model.containsRelationship(Type.COMPOSITION, "e", "e2");
        assertFalse("Relationships associated with class e were deleted", deletedRels);
        assertTrue("Relationships not associated with class e still exist", model.containsRelationship(Type.COMPOSITION, "e2", "e3"));

    }

    /* ------------------------------------------------------------------------------ */
    /*                            ATTRIBUTE FUNCTIONS                                 */
    /*                              (METHODS, FIELDS)                                 */
    /*                          (CREATE, RENAME, DELETE)                              */
    /* ------------------------------------------------------------------------------ */

    /** test createField
     *
     */
    @Test
    public void testCreateField()
    {
        Model model = new Model();
        model.createClass("e");

        assertFalse("False when creating field for non-existent class", model.createField("fake", "a", "int", "PUBLIC"));

        assertTrue("Added field 'a' for class 'e'", model.createField("e", "a", "int", "PUBLIC"));
        assertTrue("e's field list contains 'a'", model.getEntities().get(0).containsField("a"));
        assertEquals("e's field list size is 1", 1, model.getEntities().get(0).getFields().size());

        assertFalse("False when creating field that already exists", model.createField("e", "a", "int", "PUBLIC"));
        assertEquals("e's field list size is still 1", 1, model.getEntities().get(0).getFields().size());
    }

    /** test renameField
     *
     */
    @Test
    public void testRenameField()
    {
        Model model = new Model();
        model.createClass("e");
        model.createField("e", "a", "int", "PUBLIC");
        model.createField("e", "a2", "int", "PRIVATE");

        assertFalse("False when renaming Field from non-existent class", model.renameField("fake", "a", "aa"));
        assertFalse("False when renaming non-existent Field", model.renameField("e", "fake", "aa"));
        assertFalse("False when renaming Field to an existing Field name", model.renameField("e", "a", "a2"));

        assertTrue("Field 'a' renamed to 'aa'", model.renameField("e", "a", "aa"));
        assertTrue("Field list contains 'aa'", model.getEntities().get(0).containsField("aa"));
        assertFalse("Field list no longer contains 'a'", model.getEntities().get(0).containsField("a"));
        assertEquals("Field list size is still 2", 2, model.getEntities().get(0).getFields().size());
    }
    
    /** test changeFieldType
     * 
     */
    @Test
    public void testChangeFieldType()
    {
	Model model = new Model();
	model.createClass("c");
	model.createField("c", "f1", "int", "PUBLIC");
	model.changeFieldType("c", "f1", "String");
	
	assertEquals("Field f1 should have type of String", "String", model.copyEntity("c").copyField("f1").getType());
	assertFalse("False when changing type with a non-valid class", model.changeFieldType("c2", "f1", "int"));
	assertFalse("False when changing type with a non-valid field", model.changeFieldType("c", "f2", "int"));
	assertFalse("False when changing type with a space", model.changeFieldType("c", "f1", "in t"));
    }
    
    /** test changeFieldVis
     * 
     */
    @Test
    public void testChangeFieldVis()
    {
	Model model = new Model();
	model.createClass("c");
	model.createField("c", "f1", "int", "PUBLIC");
	model.changeFieldVis("c", "f1", "PROTECTED");
	
	assertEquals("Field f1 should have visibility of PROTECTED", "PROTECTED", model.copyEntity("c").copyField("f1").getVisibility().toString());
	assertFalse("False when changing visibility with a non-valid class", model.changeFieldVis("c2", "f1", "PRIVATE"));

    }

    /** test deleteField
     *
     */
    @Test
    public void testDeleteField()
    {
        Model model = new Model();
        model.createClass("e");
        model.createField("e", "a", "int", "PUBLIC");
        model.createField("e", "a2", "int", "PRIVATE");

        assertFalse("False when deleting Field from non-existent class", model.deleteField("fake", "a"));
        assertFalse("False when deleting non-existent Field", model.deleteField("e", "fake"));

        assertTrue("Deleted Field 'a'", model.deleteField("e", "a"));
        assertFalse("Field list no longer contains 'a'", model.getEntities().get(0).containsField("a"));
        assertEquals("Field list size is 1", 1, model.getEntities().get(0).getFields().size());
    }

    /** test createMethod
     *
     */
    @Test
    public void testCreateMethod()
    {
        Model model = new Model();
        model.createClass("e");

        assertFalse("False when creating method for non-existent class", model.createMethod("fake", "a", "int", "PUBLIC"));

        assertTrue("Added method 'a' for class 'e'", model.createMethod("e", "a", "int", "PUBLIC"));
        assertTrue("e's methods list contains 'a'", model.getEntities().get(0).containsMethod("a"));
        assertEquals("e's method list size is 1", 1, model.getEntities().get(0).getMethods().size());

        assertFalse("False when creating method that already exists", model.createMethod("e", "a", "int", "PUBLIC"));
        assertEquals("e's method list size is still 1", 1, model.getEntities().get(0).getMethods().size());
        
        assertFalse("False when changing type with a space", model.changeMethodType("e", "a", "in t"));
    }

    /** test renameMethod
     *
     */
    @Test
    public void testRenameMethod()
    {
        Model model = new Model();
        model.createClass("e");
        model.createMethod("e", "a", "int", "PUBLIC");
        model.createMethod("e", "a2", "int", "PRIVATE");

        assertFalse("False when renaming method from non-existent class", model.renameMethod("fake", "a", "aa"));
        assertFalse("False when renaming non-existent method", model.renameMethod("e", "fake", "aa"));
        assertFalse("False when renaming method to an existing method name", model.renameMethod("e", "a", "a2"));

        assertTrue("Method 'a' renamed to 'aa'", model.renameMethod("e", "a", "aa"));
        assertTrue("Methods list contains 'aa'", model.getEntities().get(0).containsMethod("aa"));
        assertFalse("Methods list no longer contains 'a'", model.getEntities().get(0).containsMethod("a"));
        assertEquals("Methods list size is still 2", 2, model.getEntities().get(0).getMethods().size());
    }
    
    /** test changeMethodType
     * 
     */
    @Test
    public void testChangeMethodType()
    {
	Model model = new Model();
	model.createClass("c");
	model.createMethod("c", "m1", "int", "PUBLIC");
	model.changeMethodType("c", "m1", "String");
	
	assertEquals("Method m1 should have type of String", "String", model.copyEntity("c").copyMethod("m1").getType());
	assertFalse("False when changing type with a non-valid class", model.changeMethodType("c2", "f1", "int"));
	assertFalse("False when changing type with a non-valid method", model.changeMethodType("c", "m2", "int"));
    }
    
    /** test changeMethodVis
     * 
     */
    @Test
    public void testChangeMethodVis()
    {
	Model model = new Model();
	model.createClass("c");
	model.createMethod("c", "m1", "int", "PUBLIC");
	model.changeMethodVis("c", "m1", "PROTECTED");
	
	assertEquals("Method m1 should have visibility of PROTECTED", "PROTECTED", model.copyEntity("c").copyMethod("m1").getVisibility().toString());
	assertFalse("False when changing visibility on non-existent class", model.changeMethodVis("c2", "m1", "PRIVATE"));
    }

    /** test deleteMethod
     *
     */
    @Test
    public void testDeleteMethod()
    {
        Model model = new Model();
        model.createClass("e");
        model.createMethod("e", "a", "int", "PUBLIC");
        model.createMethod("e", "a2", "int", "PRIVATE");

        assertFalse("False when deleting Method from non-existent method", model.deleteMethod("fake", "a"));
        assertFalse("False when deleting non-existent method", model.deleteMethod("e", "fake"));

        assertTrue("Deleted method 'a'", model.deleteMethod("e", "a"));
        assertFalse("Methods list no longer contains 'a'", model.getEntities().get(0).containsMethod("a"));
        assertEquals("Methods list size is 1", 1, model.getEntities().get(0).getMethods().size());
    }
    
    
    /** test changeParameterType
     * 
     */
    @Test
    public void testChangeParameterType()
    {
	Model model = new Model();
	model.createClass("c");
	model.createMethod("c",  "m1", "int", "PRIVATE");
	model.createParameter("c", "m1", "p1", "int");
	model.changeParameterType("c", "m1", "p1", "String");
	
	assertEquals("Parameter p1 should have type of String", "String", model.copyEntity("c").copyMethod("m1").copyParameter("p1").getType());
	assertFalse("False when changing type with a non-valid class", model.changeParameterType("c2", "m1", "p1", "int"));
	assertFalse("False when changing type with a non-valid method", model.changeParameterType("c", "m2", "p1", "int"));
	assertFalse("False when changing type with a non-valid parameter", model.changeParameterType("c", "m1", "p2", "int"));
	assertFalse("False when changing type with a space in new type name", model.changeParameterType("c", "m1", "p1", "in t"));
    }

    /* ------------------------------------------------------------------------------ */
    /*                          RELATIONSHIP FUNCTIONS                                */
    /*                           (CREATE, DELETE)                                     */
    /* ------------------------------------------------------------------------------ */

    //test createRelationships
    @Test
    public void testCreateRelationship()
    {
        Model model = new Model();
        model.createClass("e");
        model.createClass("e2");
        assertFalse("False when creating relationship between non-existent model (1)", model.createRelationship(Type.REALIZATION, "fake1", "e2"));
        assertFalse("False when creating relationship between non-existent model (2)", model.createRelationship(Type.REALIZATION, "e", "fake"));
      
        model.createRelationship(Type.INHERITANCE, "e", "e2");
        assertTrue("Relationship created", model.containsRelationship(Type.INHERITANCE,"e", "e2"));
        assertEquals("Relationship list size is 1", 1, model.getRelationships().size());

        assertFalse("False when creating relationship that already exists", model.createRelationship(Type.INHERITANCE, "e", "e2"));
        assertFalse("False when creating relationship with null type", model.createRelationship(null, "e2", "e"));

        model.createClass("e3");
        model.createRelationship(Type.INHERITANCE, "e", "e3");
        assertTrue("Relationship with existing type but different pair created", model.containsRelationship(Type.INHERITANCE,"e", "e3"));

        assertFalse("False when creating another relationship between pairs that already have a relationship", model.createRelationship(Type.REALIZATION, "e", "e2"));
        assertFalse("Relationship 'r-dup' was not created", model.containsRelationship(Type.REALIZATION, "e", "e2"));

        assertTrue("Created relationship with pair flipped", model.createRelationship(Type.INHERITANCE, "e2", "e"));
        assertTrue("Relationship type INHERITANCE between e2 and e was created", model.containsRelationship(Type.INHERITANCE, "e2", "e"));
    }

    //test deleteRelationship
    @Test
    public void testDeleteRelationship()
    {
        Model model = new Model();
        model.createClass("e");
        model.createClass("e2");
        model.createRelationship(Type.REALIZATION, "e", "e2");

        assertFalse("False when deleting non-existent relationship (type)", model.deleteRelationship(Type.AGGREGATION, "e", "e2"));
        assertFalse("False when deleting non-existent relationship (class1)", model.deleteRelationship(Type.REALIZATION, "fake", "e2"));
        assertFalse("False when deleting non-existent relationship (class2)", model.deleteRelationship(Type.REALIZATION, "e", "fake"));

        model.deleteRelationship(Type.REALIZATION, "e", "e2");
        assertFalse("Relationship 'r' was deleted", model.containsRelationship(Type.REALIZATION, "e", "e2"));

        assertTrue("Relationships list is empty", model.getRelationships().isEmpty());
        assertTrue("Model e and e2 still exist", model.containsEntity("e") && model.containsEntity("e2"));
    }

}
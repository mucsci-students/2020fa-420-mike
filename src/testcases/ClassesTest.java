package testcases;

//import junit methods
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.jupiter.api.Test;

//import local classes to test
import datastructures.Classes;
import datastructures.Entity;
import datastructures.Relationship;

/** Run tests on Classes Data Structure
 *
 * @author Stefan Gligorevic
 */
public class ClassesTest {

    /** create an instance of the Classes data structure
     *
     * assert that the relationships and entities lists are empty
     */
    @Test
    public void initClass()
    {
        Classes initClass = new Classes();
        assertTrue("Relationships list is empty", initClass.getRelationships().isEmpty());
        assertTrue("Entities list is empty", initClass.getEntities().isEmpty());
    }

    /* ------------------------------------------------------------------------------ */
    /*                          TEST HELPER/MEMBER FUNCTIONS                          */
    /* ------------------------------------------------------------------------------ */

    /** test the clear() method
     *
     * create classes and relationships then call clear
     * assert that relationships and entities lists are empty after calling clear
     */
    @Test
    public void testClear()
    {
        Classes classes = new Classes();
        classes.createClass("Entity1");
        classes.createClass("Entity2");
        classes.createRelationship("Linkage", "Entity1", "Entity2");

        classes.clear()

        assertTrue("Relationships list is empty after clear is called", classes.getRelationships().isEmpty());
        assertTrue("Entities list is empty after clear is called", classes.getEntities().isEmpty());

        classes.createClass("E1");
        classes.createClass("E2");
        classes.createRelationship("Rel1", "E1", "E2");

        classes.createClass("E3");
        classes.createClass("E4");
        classes.createRelationship("Rel2", "E3", "E4");

        classes.createClass("E5");
        classes.createClass("E6");
        classes.createRelationship("Rel1", "E5", "E6");

        classes.createClass("E7");
        classes.createClass("E8");

        classes.clear();

        assertTrue("Relationships list is empty after clear is called", classes.getRelationships().isEmpty());
        assertTrue("Entities list is empty after clear is called", classes.getEntities().isEmpty());
    }

    /** test the empty() method
     *
     * ensure that empty() returns true when a classes instance has no entities/relationships
     * ensure that empty() returns false when a classes instance has >= 1 entities/relationships
     */
    @Test
    public void testEmpty()
    {
        Classes classes = new Classes();
        assertTrue("New classes instance is empty", classes.empty());

        classes.createClass("E1");
        assertFalse("Classes instance is not empty", classes.empty());

        classes.clear();
        assertTrue("empty() returns true after clear() is called", classes.empty());
    }

    /** test copyEntity
     *
     */
    @Test
    public void testCopyEntity()
    {
        Classes classes = new Classes();
        classes.createClass("E1");
        //copy entity
        Entity e1copy = classes.copyEntity("E1");
        assertTrue("Two entities with no fields/methods are equal after copyEntity call", classes.getEntities().get(0).equals(e1copy));

        //add attributes to entity
        classes.createField("E1", "att1");
        classes.createField("E1", "att2");
        classes.createMethod("E1", "m1");
        classes.createMethod("E1", "m2");

        Entity new_e1copy = classes.copyEntity("E1");
        assertTrue("Entity with fields and Methods was copied correctly", classes.getEntities().get(0).equals(new_e1copy));
        assertTrue("Fields are the same", classes.getEntities().get(0).getFields().equals(new_e1copy.getFields()));
        assertTrue("Methods are the same", classes.getEntities().get(0).getMethods().equals(new_e1copy.getMethods()));
        assertFalse("Old e1_copy no longer equal to E1", classes.getEntities().get(0).equals(e1copy))
    }

    /** test searchEntity
     *
     */
    @Test
    public void testSearchEntity()
    {
        Classes classes = new Classes();
        classes.createClass("e");

        assertTrue("Found class 'e'", classes.searchEntity("e"));
        assertFalse("False for non-existing class", classes.searchEntity("fake"));
    }

    /** test searchRelationship
     *
     */
    @Test
    public void testSearchRelationship()
    {
        Classes classes = new Classes();
        classes.createClass("e");
        classes.createClass("e2");
        classes.createRelationship("r", "e", "e2");

        assertTrue("Found relationship 'r, e, e2'", classes.searchRelationship("r", "e", "e2"));
        assertFalse("False when given non-existent relationship name", classes.searchRelationship("fake", "e", "e2"));
        assertFalse("False for non-existent class1 name", classes.searchRelationship("r", "fake", "e2"));
        assertFalse("False for non-existent class2 name", classes.searchRelationship("r", "e", "fake"));
        assertFalse("False when pairs are in wrong order", classes.searchRelationship("r", "e2", "e"));
    }

    /** test getRelationship
     *
     */
    @Test
    public void testGetRelationship()
    {
        Classes classes = new Classes();
        classes.createClass("e");
        classes.createClass("e2");
        classes.createRelationship("r", "e", "e2");

        Relationship r = classes.getRelationship("r", "e", "e2");

        assertEquals("Relationship names are equal", "r", r.getName());
        assertEquals("Class1 names are equal", "e", r.getFirstClass());
        assertEquals("Class2 names are equal", "e2", r.getSecondClass());

        assertEquals("Null when relationship is not found", null, classes.getRelationship("fake", "fake", "stillFake"));
    }

    /* ------------------------------------------------------------------------------- */
    /*                       CLASS (ENTITY) FUNCTIONS                                  */
    /*                       (CREATE, RENAME, DELETE)                                  */
    /* ------------------------------------------------------------------------------- */

    /** test the createClass method
     *
     * ensure that classes are created and the entities arrayLists are updated properly
     */
    @Test
    public void testCreateClass()
    {
        Classes classes = new Classes();
        classes.createClass("E1");
        assertTrue("Entities list size is 1", classes.getEntities().size() == 1);
        assertTrue("Entities list contains E1", classes.getEntities().contains(new Entity("E1")));

        assertFalse("createClass returned false after called with name of existing class", classes.createClass("E1"));

        classes.createClass("E2");
        classes.createClass("E3");
        assertTrue("Entities list size is 3", classes.getEntities().size() == 3);
        assertTrue("Entities list contains E2", classes.getEntities().contains(new Entity("E2")));
        assertTrue("Entities list contains E3", classes.getEntities().contains(new Entity("E3")));
    }

    /** test the renameClass method
     *
     * ensure that classes can be renamed properly
     */
    @Test
    public void testRenameClass()
    {
        Classes classes = new Classes();
        classes.createClass("E1");

        assertFalse("renameClass returned false after called with name of non-existing class", classes.renameClass("Non-exist", "e"));

        classes.renameClass("E1", "E2");
        assertTrue("Entities list contains E2", classes.getEntities().contains(new Entity("E2")));
        assertFalse("Entities list does not contain E1", classes.getEntities().contains(new Entity("E1")));

        classes.createClass("E1");
        assertTrue("Entities list contains E1", classes.getEntities().contains(new Entity("E1")));

        assertFalse("renameClass returned false after renaming to already existing class", classes.renameClass("E1", "E2"));

        classes.createField("E1", "f");
        classes.createMethod("E1", "m");

        Entity e1_copy = classes.copyEntity("E1");
        classes.renameClass("E1", "e");
        Entity e = classes.copyEntity("e");

        assertTrue("Class renamed to e", classes.getEntities().contains(e));
        assertFalse("Class E1 no longer exists", classes.getEntities().contains(e1_copy));
        assertTrue("Fields still in tact", e.getFields().equals(e1_copy.getFields()));
        assertTrue("Methods still in tact", e.getMethods().equals(e1_copy.getMethods()));

        /* Make sure class names were changed in existing relationships */
        classes.createRelationship("r1", "e", "E2");
        classes.renameClass("E2", "e2");

        assertTrue("Class name (2) in relationship was updated", classes.searchRelationship("r1", "e", "e2"));
        assertFalse("Old relationship no longer exists", classes.searchRelationship("r1", "e", "E2"));

        classes.renameClass("e", "e1");
        assertTrue("Class name (1) in relationship was updated", classes.searchRelationship("r1", "e1", "e2"));
        assertFalse("Old relationship no longer exists", classes.searchRelationship("r1", "e", "e2"));
    }

    /** test the deleteClass method
     *
     * ensure that classes and their relationships are deleted properly
     */
    @Test
    public void testDeleteClass()
    {
        Classes classes = new Classes();
        classes.createClass("e");

        assertFalse("False when deleting non-existent class", classes.deleteClass("fake"));

        classes.deleteClass("e");
        assertTrue("Class e no longer exists", classes.getEntities().contains(new Entity("e")));
        assertEquals("Entities list size is 0", 0, classes.getEntities().size());

        /* Make sure relationships associated with deleted classes are deleted */
        classes.createClass("e");
        classes.createClass("e2");
        classes.createClass("e3");

        classes.createRelationship("r1", "e", "e2");
        classes.createRelationship("r2", "e3", "e");
        classes.createRelationship("r3", "e2", "e3");

        classes.deleteClass("e");
        assertTrue("Class e no longer exists", classes.getEntities().contains(new Entity("e")));
        assertEquals("Entities list size is 2", 2, classes.getEntities().size());

        boolean deletedRels = classes.searchRelationship("r1", "e", "e2") && classes.searchRelationship("r2", "e3", "e");
        assertFalse("Relationships associated with class e were deleted", deletedRels);
        assertTrue("Relationships not associated with class e still exist", classes.searchRelationship("r3", "e2", "e3"));
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
        Classes classes = new Classes();
        classes.createClass("e");

        assertFalse("False when creating field for non-existent class", classes.createField("fake", "a"));

        assertTrue("Added field 'a' for class 'e'", classes.createField("e", "a"));
        assertTrue("e's field list contains 'a'", classes.getEntities().get(0).getFields().contains("a"));
        assertEquals("e's field list size is 1", 1, classes.getEntities().get(0).getFields().size());

        assertFalse("False when creating field that already exists", classes.createField("e", "a"));
        assertEquals("e's field list size is still 1", 1, classes.getEntities().get(0).getFields().size());
    }

    /** test renameField
     *
     */
    @Test
    public void testRenameField()
    {
        Classes classes = new Classes();
        classes.createClass("e");
        classes.createField("e", "a");
        classes.createField("e", "a2");

        assertFalse("False when renaming Field from non-existent class", classes.renameField("fake", "a", "aa"));
        assertFalse("False when renaming non-existent Field", classes.renameField("e", "fake", "aa"));
        assertFalse("False when renaming Field to an existing Field name", classes.renameField("e", "a", "a2"));

        assertTrue("Field 'a' renamed to 'aa'", classes.renameField("e", "a", "aa"));
        assertTrue("Field list contains 'aa'", classes.getEntities().get(0).getFields().contains("aa"));
        assertFalse("Field list no longer contains 'a'", classes.getEntities().get(0).getFields().contains("a"));
        assertEquals("Field list size is still 2", 2, classes.getEntities().get(0).getFields().size());
    }

    /** test deleteField
     *
     */
    @Test
    public void testDeleteField()
    {
        Classes classes = new Classes();
        classes.createClass("e");
        classes.createField("e", "a");
        classes.createField("e", "a2");

        assertFalse("False when deleting Field from non-existent class", classes.deleteField("fake", "a"));
        assertFalse("False when deleting non-existent Field", classes.deleteField("e", "fake"));

        assertTrue("Deleted Field 'a'", classes.deleteField("e", "a"));
        assertFalse("Field list no longer contains 'a'", classes.getEntities().get(0).getFields().contains("a"));
        assertEquals("Field list size is 1", 1, classes.getEntities().get(0).getFields().size());
    }

    /** test createMethod
     *
     */
    @Test
    public void testCreateMethod()
    {
        Classes classes = new Classes();
        classes.createClass("e");

        assertFalse("False when creating method for non-existent class", classes.createMethod("fake", "a"));

        assertTrue("Added method 'a' for class 'e'", classes.createMethod("e", "a"));
        assertTrue("e's methods list contains 'a'", classes.getEntities().get(0).getMethods().contains("a"));
        assertEquals("e's method list size is 1", 1, classes.getEntities().get(0).getMethods().size());

        assertFalse("False when creating method that already exists", classes.createMethod("e", "a"));
        assertFalse("False when creating method that already exists", classes.createMethod("e", "a"));
        assertEquals("e's method list size is still 1", 1, classes.getEntities().get(0).getMethods().size());
    }

    /** test renameMethod
     *
     */
    @Test
    public void testRenameMethod()
    {
        Classes classes = new Classes();
        classes.createClass("e");
        classes.createMethod("e", "a");
        classes.createMethod("e", "a2");

        assertFalse("False when renaming method from non-existent class", classes.renameMethod("fake", "a", "aa"));
        assertFalse("False when renaming non-existent method", classes.renameMethod("e", "fake", "aa"));
        assertFalse("False when renaming method to an existing method name", classes.renameMethod("e", "a", "a2"));

        assertTrue("Method 'a' renamed to 'aa'", classes.renameMethod("e", "a", "aa"));
        assertTrue("Methods list contains 'aa'", classes.getEntities().get(0).getMethods().contains("aa"));
        assertFalse("Methods list no longer contains 'a'", classes.getEntities().get(0).getMethods().contains("a"));
        assertEquals("Methods list size is still 2", 2, classes.getEntities().get(0).getMethods().size());
    }

    /** test deleteMethod
     *
     */
    @Test
    public void testDeleteMethod()
    {
        Classes classes = new Classes();
        classes.createClass("e");
        classes.createMethod("e", "a");
        classes.createMethod("e", "a2");

        assertFalse("False when deleting Method from non-existent method", classes.deleteMethod("fake", "a"));
        assertFalse("False when deleting non-existent method", classes.deleteMethod("e", "fake"));

        assertTrue("Deleted method 'a'", classes.deleteMethod("e", "a"));
        assertFalse("Methods list no longer contains 'a'", classes.getEntities().get(0).getMethods().contains("a"));
        assertEquals("Methods list size is 1", 1, classes.getEntities().get(0).getMethods().size());
    }

    /* ------------------------------------------------------------------------------ */
    /*                          RELATIONSHIP FUNCTIONS                                */
    /*                           (CREATE, DELETE)                                     */
    /* ------------------------------------------------------------------------------ */

    /** test createRelationship
     *
     */
    @Test
    public void testCreateRelationship()
    {
        Classes classes = new Classes();
        classes.createClass("e");
        classes.createClass("e2");

        assertFalse("False when creating relationship between non-existent classes (1)", classes.createRelationship("r", "fake1", "e2"));
        assertFalse("False when creating relationship between non-existent classes (2)", classes.createRelationship("r", "e", "fake"));

        classes.createRelationship("r", "e", "e2");
        assertTrue("Relationship created", classes.searchRelationship("r","e", "e2"));
        assertEquals("Relationship list size is 1", 1, classes.getRelationships().size());

        assertFalse("False when creating relationship that already exists", classes.createRelationship("r", "e", "e2"));
        assertFalse("False when creating relationship with null name", classes.createRelationship(null, "e2", "e"));

        classes.createRelationship("r", "e2", "e");
        assertTrue("Relationship with existing name but different pair created", classes.searchRelationship("r","e2", "e"));

        assertFalse("False when creating another relationship between pairs that already have a relationship", classes.createRelationship("r-dup", "e", "e2"));
        assertFalse("Relationship 'r-dup' was not created", classes.searchRelationship("r-dup", "e", "e2"));
    }

    /** test deleteRelationship
     *
     */
    @Test
    public void testDeleteRelationship()
    {
        Classes classes = new Classes();
        classes.createClass("e");
        classes.createClass("e2");
        classes.createRelationship("r", "e", "e2");

        assertFalse("False when deleting non-existent relationship", classes.deleteRelationship("fake", "e", "e2"));
        assertFalse("False when deleting non-existent relationship (class1)", classes.deleteRelationship("r", "fake", "e2"));
        assertFalse("False when deleting non-existent relationship (class2)", classes.deleteRelationship("r", "e", "fake"));

        classes.deleteRelationship("r", "e", "e2");
        assertFalse("Relationship 'r' was deleted", classes.searchRelationship("r", "e", "e2"));
        assertTrue("Relationships list is empty", classes.getRelationships().isEmpty());
        assertTrue("Classes e and e2 still exist", classes.searchEntity("e") && classes.searchEntity("e2"));
    }

}

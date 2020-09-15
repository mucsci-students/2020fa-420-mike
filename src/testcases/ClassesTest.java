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
        assertTrue("Two entities with no attributes are equal after copyEntity call", classes.getEntities().get(0).equals(e1copy));

        //add attributes to entity
        classes.createAttribute("E1", "att1");
        classes.createAttribute("E2", "att2");
        classes.createAttribute("E3", "att3");

        Entity new_e1copy = classes.copyEntity("E1");
        assertTrue("Entity with attributes was copied correctly", classes.getEntities().get(0).equals(new_e1copy));
        assertTrue("Attributes are the same", classes.getEntities().get(0).getAttributes().equals(new_e1copy.getAttributes()));
    }

    /** test searchClass
     *
     */
    @Test
    public void testSearchClass()
    {
        Classes classes = new Classes();
        classes.createClass("e");

        assertTrue("Found class 'e'", classes.searchClass("e"));
        assertFalse("False for non-existing class", classes.searchClass("fake"));
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

        classes.createAttribute("E1", "a");
        classes.createAttribute("E1", "a2");

        Entity e1_copy = classes.copyEntity("E1");
        classes.renameClass("E1", "e");
        Entity e = classes.copyEntity("e");

        assertTrue("Class renamed to e", classes.getEntities().contains(e));
        assertFalse("Class E1 no longer exists", classes.getEntities().contains(e1_copy));
        assertTrue("Attributes still in tact", e.getAttributes().equals(e1_copy.getAttributes()));

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
    /*                          (CREATE, RENAME, DELETE)                              */
    /* ------------------------------------------------------------------------------ */

    /** test createAttribute
     *
     */
    @Test
    public void testCreateAttribute()
    {
        Classes classes = new Classes();
        classes.createClass("e");

        assertFalse("False when creating attribute for non-existent class", classes.createAttribute("fake", "a"));

        assertTrue("Added attribute 'a' for class 'e'", classes.createAttribute("e", "a"));
        assertTrue("e's attributes list contains 'a'", classes.getEntities().get(0).getAttributes().contains("a"));
        assertEquals("e's attribute list size is 1", 1, classes.getEntities().get(0).getAttributes().size());

        assertFalse("False when creating attribute that already exists", classes.createAttribute("e", "a"));
        assertEquals("e's attribute list size is still 1", 1, classes.getEntities().get(0).getAttributes().size());
    }

    /** test renameAttribute
     *
     */
    @Test
    public void testRenameAttribute()
    {
        Classes classes = new Classes();
        classes.createClass("e");
        classes.createAttribute("e", "a");
        classes.createAttribute("e", "a2");

        assertFalse("False when renaming attribute from non-existent class", classes.renameAttribute("fake", "a", "aa"));
        assertFalse("False when renaming non-existent attribute", classes.renameAttribute("e", "fake", "aa"));
        assertFalse("False when renaming attribute to an existing attribute name", classes.renameAttribute("e", "a", "a2"));

        assertTrue("Attribute 'a' renamed to 'aa'", classes.renameAttribute("e", "a", "aa"));
        assertTrue("Attribute list contains 'aa'", classes.getEntities().get(0).getAttributes().contains("aa"));
        assertFalse("Attribute list no longer contains 'a'", classes.getEntities().get(0).getAttributes().contains("a"));
        assertEquals("Attributes list size is still 2", 2, classes.getEntities().get(0).getAttributes().size());
    }

    /** test deleteAttribute
     *
     */
    @Test
    public void testDeleteAttribute()
    {
        Classes classes = new Classes();
        classes.createClass("e");
        classes.createAttribute("e", "a");
        classes.createAttribute("e", "a2");

        assertFalse("False when deleting attribute from non-existent class", classes.deleteAttribute("fake", "a"));
        assertFalse("False when deleting non-existent attribute", classes.deleteAttribute("e", "fake"));

        assertTrue("Deleted attribute 'a'", classes.deleteAttribute("e", "a"));
        assertFalse("Attribute list no longer contains 'a'", classes.getEntities().get(0).getAttributes().contains("a"));
        assertEquals("Attribute list size is 1", 1, classes.getEntities().get(0).getAttributes().size());
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
        assertFalse("False when creating relationship with null name", classes.createRelationship(null, "e", "e2"));

        classes.createRelationship("r", "e", "e2");
        assertTrue("Relationship with existing name but different pair created", classes.searchRelationship("r","e2", "e"));
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

        assertFalse("False when deleting non-existent relationship", classes.createRelationship("fake", "e", "e2"));
        assertFalse("False when deleting non-existent relationship (class1)", classes.createRelationship("r", "fake", "e2"));
        assertFalse("False when deleting non-existent relationship (class2)", classes.createRelationship("r", "e", "fake"));

        classes.deleteRelationship("r", "e", "e2");
        assertFalse("Relationship 'r' was deleted", classes.searchRelationship("r", "e", "e2"));
        assertTrue("Relationships list is empty", classes.getRelationships().isEmpty());
        assertTrue("Classes e and e2 still exist", classes.searchClass("e") && classes.searchClass("e2"));
    }

}

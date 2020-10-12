package mike.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

import mike.datastructures.Entity;
import mike.datastructures.Field;
import mike.datastructures.Method;

public class EntityTest {

    /* test constructor */
    @Test
    public void initEntity() {
        Entity e = new Entity("e");
        assertEquals("Name was set properly", "e", e.getName());
        assertTrue("Fields list was initialized", e.getFields().isEmpty());
        assertTrue("Methods list was initialized", e.getMethods().isEmpty());
    }

    /* test equals */
    @Test
    public void testEquals() {
        Entity e = new Entity("e");
        Entity e2 = new Entity("e2");
        Entity e_copy = new Entity("e");
        String s = "";

        // Null check
        assertFalse("Null should result in false", e.equals(null));

        // Type check
        assertFalse("Differing object types should result in false", e.equals(s));

        // Identity check
        assertTrue("Entity should be equal with itself.", e.equals(e));

        // Equality check
        assertTrue("Entities should be equal.", e.equals(e_copy));
        assertFalse("Differing names should result in false", e.equals(e2));
    }

    /* test createField */
    @Test
    public void testCreateField() {
        Entity e = new Entity("e");
        e.createField("a1", "int");
      
        assertTrue("Field a1 was created", e.containsField("a1"));
        assertEquals("List size is 1", 1, e.getFields().size());
        assertFalse("False when duplicating field", e.createField("a1", "int"));
    }

    /* test renameField */
    @Test
    public void testRenameField() {
        Entity e = new Entity("e");
        e.createField("a1", "int");
        e.createField("a2", "boolean");

        assertFalse("False when renaming to already existing field", e.renameField("a1", "a2"));
        assertFalse("False when renaming non-existent field", e.renameField("fake", "a3"));

        assertTrue("Renamed a1 to a3", e.renameField("a1", "a3"));
        assertTrue("a3 field exists", e.containsField("a3"));

        assertFalse("a1 field no longer exists", e.containsField("a1"));
        assertEquals("List size still 2", 2, e.getFields().size());
    }

    /* test deleteField */
    @Test
    public void testDeleteField() {
        Entity e = new Entity("e");
        e.createField("a1", "int");
        e.createField("a2", "double");

        assertFalse("False when deleting non-existent field", e.deleteField("fake"));

        e.deleteField("a1");
      
        assertFalse("a1 field was deleted", e.containsField("a1"));
        assertEquals("List size is 1", 1, e.getFields().size());
        assertTrue("a2 field still exists", e.containsField("a2"));
    }

    /* test createMethod */
    @Test
    public void testCreateMethod() {
        Entity e = new Entity("e");

        e.createMethod("a1", "int");
        assertTrue("Method a1 was created", e.containsMethod("a1"));
        assertEquals("List size is 1", 1, e.getMethods().size());
        assertFalse("False when duplicating method", e.createMethod("a1", "int"));
    }

    /* test renameMethod */
    @Test
    public void testRenameMethod() {
        Entity e = new Entity("e");
        e.createMethod("a1", "int");
        e.createMethod("a2", "String");

        assertFalse("False when renaming to already existing method", e.renameMethod("a1", "a2"));
        assertFalse("False when renaming non-existent method", e.renameMethod("fake", "a3"));

        assertTrue("Renamed a1 to a3", e.renameMethod("a1", "a3"));
        assertTrue("a3 method exists", e.containsMethod("a3"));

        assertFalse("a1 method no longer exists", e.containsMethod("a1"));
        assertEquals("List size still 2", 2, e.getMethods().size());
    }

    /* test deleteMethod */
    @Test
    public void testDeleteMethod() {
        Entity e = new Entity("e");
        e.createMethod("a1", "int");
        e.createMethod("a2", "String");

        assertFalse("False when deleting non-existent method", e.deleteMethod("fake"));

        e.deleteMethod("a1");

        assertFalse("a1 method was deleted", e.containsMethod("a1"));
        assertEquals("List size is 1", 1, e.getMethods().size());
        assertTrue("a2 method still exists", e.containsMethod("a2"));
    }

    /* HELPER FUNCTIONS */

    /* containsField */
    @Test
    public void testContainsField()
    {
        Entity e = new Entity("e");
        e.createField("a1", "int");

        assertFalse("False when searching non-existent field", e.containsField("fake"));
        assertTrue("a1 field found", e.containsField("a1"));
    }

    /* containsMethod */
    @Test
    public void testContainsMethod()
    {
        Entity e = new Entity("e");
        e.createMethod("a1", "int");

        assertFalse("False when searching non-existent method", e.containsMethod("fake"));
        assertTrue("a1 method found", e.containsMethod("a1"));
    }

    /* copyField */
    @Test
    public void testCopyField()
    {
        Entity e = new Entity("e");
        e.createField("a1", "int");

        assertEquals("Null when copying non-existent field", null, e.copyField("fake"));

        Field f = e.copyField("a1");
        assertTrue("Field names are equal", f.getName().equals("a1"));
        assertTrue("Types are equal", f.getType().equals("int"));
    }

    /* copyMethod */
    @Test
    public void testCopyMethod()
    {
        Entity e = new Entity("e");
        e.createMethod("a1", "int");

        assertEquals("Null when copying non-existent method", null, e.copyMethod("fake"));

        Method m = e.copyMethod("a1");
        assertTrue("Method names are equal", m.getName().equals("a1"));
        assertTrue("Types are equal", m.getType().equals("int"));
    }

}


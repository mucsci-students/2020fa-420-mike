package testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

import datastructures.Entity;

public class EntityTest {

    /* test constructor */
    @Test
    public void initEntity()
    {
        Entity e = new Entity("e");
        assertEquals("Name was set properly", "e", e.getName());
        assertTrue("Fields list was initialized", e.getFields().isEmpty());
        assertTrue("Methods list was initialized", e.getMethods().isEmpty());
    }

    /* test equals */
    @Test
    public void testEquals()
    {
        Entity e = new Entity("e");
        Entity e2 = new Entity("e2");
        Entity e_copy = new Entity("e");
        Entity e2_copy = new Entity("e2");
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

        e.createField("a");
        e.createField("a2");

        assertFalse("Differing fields results in false", e.equals(e_copy));

        e_copy.createField("a");
        e_copy.createField("a2");

        assertTrue("Same fields and name (no methods) result in true", e.equals(e_copy));

        e.createMethod("a");
        e.createMethod("a2");

        assertFalse("Differing methods results in false", e.equals(e_copy));

        e_copy.createMethod("a");
        e_copy.createMethod("a2");

        assertTrue("Same fields, methods and name result in true", e.equals(e_copy));
    }

    /* test createField */
    @Test
    public void testCreateField()
    {
        Entity e = new Entity("e");

        e.createField("a1");
        assertTrue("Field a1 was created", e.getFields().contains("a1"));
        assertEquals("List size is 1", 1, e.getFields().size());
        assertFalse("False when duplicating field", e.createField("a1"));
    }

    /* test renameField */
    @Test
    public void testRenameField()
    {
        Entity e = new Entity("e");
        e.createField("a1");
        e.createField("a2");

        assertFalse("False when renaming to already existing field", e.renameField("a1", "a2"));
        assertFalse("False when renaming non-existent field", e.renameField("fake", "a3"));
        assertTrue("Renamed a1 to a3", e.renameField("a1", "a3"));
        assertTrue("a3 field exists", e.getFields().contains("a3"));
        assertFalse("a1 field no longer exists", e.getFields().contains("a1"));
        assertEquals("List size still 2", 2, e.getFields().size());
    }

    /* test deleteField */
    @Test
    public void testDeleteField()
    {
        Entity e = new Entity("e");
        e.createField("a1");
        e.createField("a2");

        assertFalse("False when deleting non-existent field", e.deleteField("fake"));

        e.deleteField("a1");
        assertFalse("a1 field was deleted", e.getFields().contains("a1"));
        assertEquals("List size is 1", 1, e.getFields().size());
        assertTrue("a2 field still exists", e.getFields().contains("a2"));
    }

    /* test createMethod */
    @Test
    public void testCreateMethod()
    {
        Entity e = new Entity("e");

        e.createMethod("a1");
        assertTrue("Method a1 was created", e.getMethods().contains("a1"));
        assertEquals("List size is 1", 1, e.getMethods().size());
        assertFalse("False when duplicating method", e.createMethod("a1"));
    }

    /* test renameMethod */
    @Test
    public void testRenameMethod()
    {
        Entity e = new Entity("e");
        e.createMethod("a1");
        e.createMethod("a2");

        assertFalse("False when renaming to already existing method", e.renameMethod("a1", "a2"));
        assertFalse("False when renaming non-existent method", e.renameMethod("fake", "a3"));
        assertTrue("Renamed a1 to a3", e.renameMethod("a1", "a3"));
        assertTrue("a3 method exists", e.getMethods().contains("a3"));
        assertFalse("a1 method no longer exists", e.getMethods().contains("a1"));
        assertEquals("List size still 2", 2, e.getMethods().size());
    }

    /* test deleteMethod */
    @Test
    public void testDeleteMethod()
    {
        Entity e = new Entity("e");
        e.createMethod("a1");
        e.createMethod("a2");

        assertFalse("False when deleting non-existent method", e.deleteMethod("fake"));

        e.deleteMethod("a1");
        assertFalse("a1 method was deleted", e.getMethods().contains("a1"));
        assertEquals("List size is 1", 1, e.getMethods().size());
        assertTrue("a2 method still exists", e.getMethods().contains("a2"));
    }
  
}
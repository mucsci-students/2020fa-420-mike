package testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.jupiter.api.Test;

import datastructures.Entity;

public class EntityTest {

    /* test constructor */
    @Test
    public void initEntity()
    {
        Entity e = new Entity("e");
        assertEquals("Name was set properly", "e", e.getName());
        assertTrue("Attributes list was initialized", e.getAttributes().isEmpty());
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

        e.createAttribute("a");
        e.createAttribute("a2");

        assertFalse("Differing attributes results in false", e.equals(e_copy));

        e_copy.createAttribute("a");
        e_copy.createAttribute("a2");

        assertTrue("Same attributes and name result in true", e.equals(e_copy));
    }

    /* test createAttribute */
    @Test
    public void testCreateAttribute()
    {
        Entity e = new Entity("e");

        e.createAttribute("a1");
        assertTrue("Attribute a1 was created", e.getAttributes().contains("a1"));
        assertEquals("List size is 1", 1, e.getAttributes().size());
        assertFalse("False when duplicating attribute", e.createAttribute("a1"));
    }

    /* test renameAttribute */
    @Test
    public void testRenameAttribute()
    {
        Entity e = new Entity("e");
        e.createAttribute("a1");
        e.createAttribute("a2");

        assertFalse("False when renaming to already existing attribute", e.renameAttribute("a1", "a2"));
        assertFalse("False when renaming non-existent attribute", e.renameAttribute("fake", "a3"));
        assertTrue("Renamed a1 to a3", e.renameAttribute("a1", "a3"));
        assertTrue("a3 attribute exists", e.getAttributes().contains("a3"));
        assertFalse("a1 attribute no longer exists", e.getAttributes().contains("a1"));
        assertEquals("List size still 1", 1, e.getAttributes().size());
    }

    /* test deleteAttribute */
    @Test
    public void testDeleteAttribute()
    {
        Entity e = new Entity("e");
        e.createAttribute("a1");
        e.createAttribute("a2");

        assertFalse("False when deleting non-existent attribute", e.deleteAttribute("fake"));

        e.deleteAttribute("a1");
        assertFalse("a1 attribute was deleted", e.getAttributes.contains("a1"));
        assertEquals("List size is 1", 1, e.getAttributes().size());
        assertTrue("a2 attribute still exists", e.getAttributes.contains("a2"));
    }
}

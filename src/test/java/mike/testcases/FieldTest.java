package mike.testcases;

import mike.datastructures.Field;

//import junit methods
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

/** Run tests on Field Data Structure
 *
 * @author Stefan Gligorevic
 */
public class FieldTest {

    /* Test constructor */
    @Test
    public void testInit()
    {
        Field f = new Field("f", "int");
        assertEquals("Name set correctly", "f", f.getName());
        assertEquals("Type set correctly", "int", f.getType());
    }

    /* Test equals */
    @Test
    public void testEquals()
    {
        Field f = new Field("f", "int");
        Field f2 = new Field("f", "int");
        Field f3 = new Field("f3", "int");

        assertFalse("False when comparing null", f.equals(null));
        assertFalse("False when comparing wrong object types", f.equals("Hello"));

        assertTrue("True when name and type match", f.equals(f2));
        assertFalse("False when names do not match", f.equals(f3));
    }
}

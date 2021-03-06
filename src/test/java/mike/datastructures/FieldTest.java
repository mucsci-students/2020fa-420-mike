package mike.datastructures;

//import junit methods
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

import mike.datastructures.Entity.visibility;

/** Run tests on Field Data Structure
 *
 * @author Stefan Gligorevic
 */
public class FieldTest {

    /* Test constructor */
    @Test
    public void testInit()
    {
        Field f = new Field("f", "int", visibility.PUBLIC);
        assertEquals("Name set correctly", "f", f.getName());
        assertEquals("Type set correctly", "int", f.getType());
    }

    /* Test equals */
    @Test
    public void testEquals()
    {
        Field f = new Field("f", "int", visibility.PUBLIC);
        Field f2 = new Field("f", "int", visibility.PRIVATE);
        Field f3 = new Field("f3", "int", visibility.PROTECTED);

        assertFalse("False when comparing null", f.equals(null));
        assertFalse("False when comparing wrong object types", f.getName().equals("Hello"));

        assertTrue("True when name and type match", f.equals(f2));
        assertFalse("False when names do not match", f.equals(f3));
    }
}

package mike.datastructures;

//import junit methods
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

/** Run tests on Formal Data Structure
 *
 * @author Stefan Gligorevic
 */
public class FormalTest {

    /* Test constructor */
    @Test
    public void testInit()
    {
        Formal f = new Formal("f", "int");
        assertEquals("Name set correctly", "f", f.getName());
        assertEquals("Type set correctly", "int", f.getType());
    }

    /* Test getters and setters */
    @Test
    public void testGettersAndSetters()
    {
	Formal f = new Formal("f", "int");
	
	f.setName("g");
	f.setType("char");
	
	assertEquals("Name changed correctly", "g", f.getName());
	assertEquals("Type changed correctly", "char", f.getType());
    }
    
    /* Test equals */
    @Test
    public void testEquals()
    {
	Formal f = new Formal("f", "int");
        Formal f2 = new Formal("f", "int");
        Formal f3 = new Formal("f3", "int");

        assertFalse("False when comparing null", f.equals(null));
        assertFalse("False when comparing wrong object types", f.getName().equals("Hello"));

        assertTrue("True when name and type match", f.equals(f2));
        assertFalse("False when names do not match", f.equals(f3));
    }
}

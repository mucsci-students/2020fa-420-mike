package mike.datastructures;

//import junit methods
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

import mike.datastructures.Entity.visibility;

/** Run tests on Method Data Structure
 *
 * @author Stefan Gligorevic
 */
public class MethodTest {

    /* test constructor */
    @Test
    public void initMethod()
    {
        Method m = new Method("m", "int", visibility.PUBLIC);
        assertEquals("name set correctly", "m", m.getName());
        assertEquals("return type set correctly", "int", m.getType());
        assertTrue("parameters list initialized and empty", m.getParameters().isEmpty());
    }
    
    @Test
    public void initCopyClass()
    {
	Method initMethod = new Method("m1", "int", visibility.PROTECTED);
	Method copyMethod = new Method(initMethod);
        assertTrue("Parameter list is empty", copyMethod.getParameters().isEmpty());
        assertEquals("copyMethod has same name as initMethod", initMethod.getName(), copyMethod.getName());
        
        copyMethod.createParameter("p1", "String");
        Method copyMethodTwo = new Method(copyMethod);
        assertTrue("Parameter list is empty", initMethod.getParameters().isEmpty());
        assertEquals("Parameter list is more or less than one", 1, copyMethodTwo.getParameters().size());
        assertTrue("copyMethod does not contain p1", copyMethodTwo.containsParameter("p1"));
    }

    /* test equals */
    @Test
    public void testEquals()
    {
        Method m = new Method("m", "int", visibility.PUBLIC);
        Method m2 = new Method("m", "int", visibility.PRIVATE);
        Method m3 = new Method("m3", "int", visibility.PROTECTED);

        assertFalse("False when comparing null", m.equals(null));
        assertFalse("False when comparing wrong object types", m.getName().equals("Hello"));

        assertTrue("True when name and type match", m.equals(m2));
        assertFalse("False when names do not match", m.equals(m3));
    }

    /* TEST PARAMETER FUNCTIONS */
    /* CREATE, RENAME, DELETE */

    /* test createParameter */
    @Test
    public void testCreateParameter()
    {
        Method m = new Method("m", "int", visibility.PUBLIC);
        m.createParameter("p", "int");

        assertEquals("Parameters list size is 1", 1, m.getParameters().size());
        assertTrue("Parameters list contains p", m.containsParameter("p"));

        assertFalse("False when creating duplicate", m.createParameter("p", "String"));
        
        assertFalse("False when creating parameter with space in the name", m.createParameter("p 1", "String"));
        assertFalse("False when creating parameter with space in the type", m.createParameter("p", "Str ing"));
    }

    /* test renameParameter */
    @Test
    public void testRenameParameter()
    {
        Method m = new Method("m", "int", visibility.PUBLIC);
        m.createParameter("p", "int");
        m.createParameter("p2", "int");

        assertFalse("False when renaming non-existent parameter", m.renameParameter("fake", "param"));
        assertFalse("False when renaming to existing parameter", m.renameParameter("p", "p2"));

        assertTrue("p renamed to p1", m.renameParameter("p", "p1"));
        assertTrue("Parameters list contains p1", m.containsParameter("p1"));
        assertFalse("Parameters list no longer contains p", m.containsParameter("p"));
        
        assertFalse("False when renaming parameter to a name with a space", m.renameParameter("p", "p 1"));
    }

    /* test deleteParameter */
    @Test
    public void testDeleteParameter()
    {
        Method m = new Method("m", "int", visibility.PUBLIC);
        m.createParameter("p", "int");

        assertFalse("False when deleting non-existent parameter", m.deleteParameter("fake"));
        assertTrue("p deleted", m.deleteParameter("p"));
        assertFalse("parameters list no longer contains p", m.containsParameter("p"));
        assertEquals("Parameters list size is 0", 0, m.getParameters().size());
    }

    /* HELPER METHODS */

    /* test containsParameter */
    @Test
    public void testContainsParameter()
    {
        Method m = new Method("m", "int", visibility.PUBLIC);
        m.createParameter("p", "int");

        assertTrue("p found", m.containsParameter("p"));
        assertFalse("non-existing parameter not found", m.containsParameter("fake"));
    }

    /* test copyParameter */
    @Test
    public void testCopyParameter()
    {
        Method m = new Method("m", "int", visibility.PUBLIC);
        m.createParameter("p", "int");

        assertEquals("Null when copying non-existent parameter", null, m.copyParameter("fake"));

        Parameter copy = m.copyParameter("p");
        assertEquals("names match", "p", copy.getName());
        assertEquals("types match", "int", copy.getType());
    }

}

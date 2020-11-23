package mike.datastructures;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class MementoTest {

    @Test
    public void mementoConstructorTest() {
	Memento meme = new Memento();
	assertFalse("Memento is somehow null.", meme.equals(null));
    }

}

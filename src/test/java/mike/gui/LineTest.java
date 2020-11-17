package mike.gui;

//import junit methods
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import mike.datastructures.Relationship;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

public class LineTest {

    private JFrame frame;

    @Before
    public void createFrame() {
        //JFrame to act as parent of JLabel (needed for Line constructor)
        frame = new JFrame("JFrame");
    }

    @Test
    public void testConstructor() {
        JLabel jl1 = new JLabel("jlabel");
        jl1.setPreferredSize(new Dimension(10, 10));
        frame.add(jl1);

        Line line = new Line(jl1, jl1, Relationship.Type.AGGREGATION);
        assertFalse("Line is somehow null", line.equals(null));

        assertEquals("isSelf boolean is false, when should be true", true, line.isSelf);
    }

    @Test
    public void testGetters() {
        JLabel jl1 = new JLabel("JLabel 1");
        JLabel jl2 = new JLabel("JLabel 2");

        jl1.setPreferredSize(new Dimension(10, 10));
        jl2.setPreferredSize(new Dimension(10, 10));

        frame.add(jl1);
        frame.add(jl2);

        Line line = new Line(jl1, jl2, Relationship.Type.AGGREGATION);

        assertEquals("Returned JLabel is not equal to jl1", jl1, line.getClassOne());
        assertEquals("Returned JLabel is not equal to jl2", jl2, line.getClassTwo());

        assertEquals("isSelf boolean is true, when should be false", false, line.isSelf);
    }

    @Test
    public void testSetters() {
        JLabel jl1 = new JLabel("JLabel 1");
        JLabel jl2 = new JLabel("JLabel 2");

        jl1.setPreferredSize(new Dimension(10, 10));
        jl2.setPreferredSize(new Dimension(10, 10));

        frame.add(jl1);
        frame.add(jl2);

        Line line = new Line(jl1, jl2, Relationship.Type.AGGREGATION);

        JLabel newJL1 = new JLabel("new JLabel 1");
        JLabel newJL2 = new JLabel("new JLabel 2");

        frame.add(newJL1);
        frame.add(newJL2);

        newJL1.setPreferredSize(new Dimension(10, 10));
        newJL2.setPreferredSize(new Dimension(10, 10));

        //call set methods
        line.setClassOne(newJL1);
        line.setClassTwo(newJL2);

        assertEquals("Returned JLabel is not equal to newJL1", newJL1, line.getClassOne());
        assertEquals("Returned JLabel is not equal to newJL2", newJL2, line.getClassTwo());
    }

}

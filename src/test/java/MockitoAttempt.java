import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import mike.gui.Line;
import mike.view.GUIView;

public class MockitoAttempt {

    
    @Mock
    private JLayeredPane pane;
    private HashMap<String, JLabel> entitylabels;
    private ArrayList<Line> relations;
    private JFrame frame;
    private JMenuBar menuBar;
    
    @InjectMocks
    private GUIView guiViewMock;

    @Before
    public void setUp() throws Exception {
	MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testGetters() throws Exception {    
	guiViewMock = new GUIView();
    }

}

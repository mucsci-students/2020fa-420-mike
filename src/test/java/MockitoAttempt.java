import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
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

    @Spy
    private HashMap<String, JLabel> entityLabels =  new HashMap<String, JLabel>();
    @Spy
    private ArrayList<Line> relations = new ArrayList<Line>();
    @Mock
    private JLayeredPane pane = new JLayeredPane();
    @Mock
    private JFrame frame = new JFrame();
    @Mock
    private JMenuBar menuBar = new JMenuBar();
    
    @Mock
    private GUIView guiViewMock;


    @Test
    public void initTest() throws Exception {    
	guiViewMock = new GUIView(pane, entityLabels, relations, frame, menuBar);
    }
    
    @Test
    public void getterTest() throws Exception{
	guiViewMock = new GUIView(pane, entityLabels, relations, frame, menuBar);
	
	assertFalse("entityLabels object is null.", guiViewMock.getEntityLabels().equals(null));
	assertFalse("relations object is null.", guiViewMock.getRelations().equals(null));
	assertFalse("pane object is null.", guiViewMock.getPane().equals(null));
	assertFalse("frame object is null.", guiViewMock.getFrame().equals(null));
	assertFalse("menuBar object is null.", guiViewMock.getMenuBar().equals(null));
    }

}

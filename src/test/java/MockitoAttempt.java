import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import mike.controller.Controller;
import mike.datastructures.Model;
import mike.gui.Line;
import mike.view.GUIView;

public class MockitoAttempt {

    @Spy
    ArrayList<Line> relations;
    @Mock
    private GUIView guiViewMock;

    @Before
    public void setUp() throws Exception {
	MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void test() throws Exception { 
	guiViewMock = new GUIView(relations);
	assertTrue("", guiViewMock.getRelations().isEmpty());
    }

}

package mike.view;

import static org.junit.Assert.*;

import org.junit.Test;

import mike.view.ViewTemplate.InterfaceType;

public class ViewTemplateTest {

    @Test
    public void DefaultConstructorTest() {
	ViewTemplate vt = new ViewTemplate();
	
	assertTrue("View template is null.", vt != null);
    }
    
    @Test
    public void GUIConstructorTest() {
	ViewTemplate guiVT = new ViewTemplate(InterfaceType.GUI);
	
	assertTrue("GUIViewTemplate is null.", guiVT != null);
    }
    
    @Test
    public void IsGUITest() {
	new ViewTemplate(InterfaceType.GUI);
	assertTrue("GUIViewTemplate not reported as GUIViewTemplate.", ViewTemplate.isGUI());
    }
    
    @Test
    public void CLIConstructorTest() {
	ViewTemplate cliVT = new ViewTemplate(InterfaceType.CLI);
	
	assertTrue("CLIViewTemplate is null.", cliVT != null);
    }
}

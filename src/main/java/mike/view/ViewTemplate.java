package mike.view;

public class ViewTemplate {

    public enum InterfaceType {
	GUI, CLI
    }

    private static InterfaceType viewtype;
    private static ViewInterface viewinterface;

    public ViewTemplate() {
    }

    public ViewTemplate(InterfaceType newtype) throws Exception {
	viewtype = newtype;

	if (viewtype.equals(InterfaceType.GUI)) {
	    setViewinterface(new GUIView());
	} else {
	    setViewinterface(new CLIView());
	}
    }

    public ViewInterface getViewinterface() {
	return viewinterface;
    }

    public static void setViewinterface(ViewInterface viewinterface) {
	ViewTemplate.viewinterface = viewinterface;
    }
    
    public static boolean isGUI() {
	if (viewtype.equals(InterfaceType.GUI))
	{
	    return true;
	}
	return false;
    }

}

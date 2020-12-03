package mike.view;

public class ViewTemplate {

    public enum InterfaceType {
	GUI, CLI
    }

    private static InterfaceType viewtype;
    private static ViewInterface viewinterface;

    public ViewTemplate() {
    }

    public ViewTemplate(InterfaceType newtype) {
	viewtype = newtype;

	if (viewtype.equals(InterfaceType.GUI)) {
	    try {
		setViewinterface(new GUIView());
	    } catch (Exception e) {
		e.printStackTrace();
	    }
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
	if (viewtype.equals(InterfaceType.GUI)) {
	    return true;
	}
	return false;
    }

}

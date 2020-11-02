package mike.view;

import java.io.IOException;

import mike.datastructures.Model;

public class ViewTemplate {

    public enum InterfaceType {
	GUI, CLI
    }

    private static InterfaceType viewtype;
    private static ViewInterface viewinterface;

    public ViewTemplate() {
    }

    public ViewTemplate(InterfaceType newtype, Model model) throws IOException {
	viewtype = newtype;

	if (viewtype.equals(InterfaceType.GUI)) {
	    setViewinterface(new GUIView(model));
	} else {
	    setViewinterface(new CLIView(model));
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

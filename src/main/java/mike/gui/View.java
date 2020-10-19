package mike.gui;

import mike.cli.*;

public class View {

	public enum InterfaceType {
		GUI, CLI
	}

	private static InterfaceType viewtype;
	private static ViewInterface viewinterface;

	public View(InterfaceType newtype) {
		viewtype = newtype;

		if (viewtype.equals(InterfaceType.GUI)) {
			setViewinterface(new GUI());
		} else {
			setViewinterface(new CommandLine());
		}
	}

	public static ViewInterface getViewinterface() {
		return viewinterface;
	}

	public static void setViewinterface(ViewInterface viewinterface) {
		View.viewinterface = viewinterface;
	}
}

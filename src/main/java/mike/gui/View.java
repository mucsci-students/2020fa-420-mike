package mike.gui;

import java.io.IOException;

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
			try {
				setViewinterface(new CommandLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static ViewInterface getViewinterface() {
		return viewinterface;
	}

	public static void setViewinterface(ViewInterface viewinterface) {
		View.viewinterface = viewinterface;
	}
}

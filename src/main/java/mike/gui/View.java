package mike.gui;

import mike.cli.HelperMethods;
import mike.datastructures.*;
import mike.cli.*;
import mike.gui.ViewInterface;

public class View {

	public enum InterfaceType {
		GUI, CLI
	}

	private static InterfaceType viewtype;
	private static ViewInterface viewinterface;

	public View(InterfaceType newtype) {
		viewtype = newtype;

		if (viewtype.equals(InterfaceType.GUI)) {
			viewinterface = new GUI();
		} else {
			viewinterface = new CommandLine();
		}
	}
  
}

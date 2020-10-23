package mike;

import mike.gui.Controller;
import mike.gui.View;

public class MikeApp {
	static Controller controller;
	public static void main(String[] args) {
		if (args.length == 1 && args[0].equals("cli")) {
			controller = new Controller(View.InterfaceType.CLI);
		} else if (args.length == 0) {
			controller = new Controller(View.InterfaceType.GUI);
		} else {
			System.out.println(
					"Invalid input. Enter 'cli' for the command line interface, or enter nothing for the gui.");
		}
	}
}

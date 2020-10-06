package mike;

import mike.cli.CommandLine;
import mike.gui.View;

public class main {

	public static void main(String[] args) {
		if (args.length == 1 && args[0].equals("cli")) {
			CommandLine.commandInterface();
		} else if (args.length == 0) {
			View.guiInterface();
		} else {
			System.out.println(
					"Invalid input. Enter 'cli' for the command line interface, or enter nothing for the gui.");
		}
	}
}

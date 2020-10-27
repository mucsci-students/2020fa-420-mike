package mike;

import java.io.IOException;

import mike.datastructures.Model;
import mike.view.CLIView;
import mike.view.GUIView;

public class MikeApp {
	public static void main(String[] args) throws IOException {
		if (args.length == 1 && args[0].equals("cli")) {
			new CLIView(new Model());
		} else if (args.length == 0) {
			new GUIView();
		} else {
			System.out.println(
					"Invalid input. Enter 'cli' for the command line interface, or enter nothing for the gui.");
		}
	}
}

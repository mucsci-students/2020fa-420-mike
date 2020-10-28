package mike;

import java.io.IOException;

import mike.controller.Controller;
import mike.datastructures.Model;
import mike.view.GUIView;
import mike.view.ViewTemplate;

public class MikeApp {
	public static void main(String[] args) throws IOException {
	    Model model = new Model();
	    if (args.length == 1 && args[0].equals("cli")) {
			new ViewTemplate(ViewTemplate.InterfaceType.CLI, model);
			
		} else if (args.length == 0) {
			GUIView view = (GUIView) new ViewTemplate(ViewTemplate.InterfaceType.GUI, model).getViewinterface();
			new Controller(model, view);
			
		} else {
			System.out.println("Invalid input. Enter 'cli' for the command line interface, or enter nothing for the gui.");
		}
	}
}

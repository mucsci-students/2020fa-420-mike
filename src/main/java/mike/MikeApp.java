package mike;

import mike.controller.Controller;
import mike.datastructures.Model;
import mike.view.ViewTemplate;

public class MikeApp {
    public static void main(String[] args) throws Exception {
	Model model = new Model();
	ViewTemplate view;
	Controller control;

	if (args.length == 1 && args[0].equals("cli")) {
	    view = new ViewTemplate(ViewTemplate.InterfaceType.CLI);
	    control = new Controller(model, view);
	    control.init();
	} else if (args.length == 0) {
	    view = new ViewTemplate(ViewTemplate.InterfaceType.GUI);
	    control = new Controller(model, view);
	    control.init();
	} else {
	    System.out.println(
		    "Invalid input. Enter 'cli' for the command line interface, or enter nothing for the gui.");
	}
    }
}

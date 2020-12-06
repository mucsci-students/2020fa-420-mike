
package mike.controller;

import java.io.IOException;

import mike.datastructures.Model;
import mike.view.GUIView;
import mike.view.ViewTemplate;

public class Controller {
    private ControllerType control;

    public Controller(Model model, ViewTemplate view) throws IOException {

	if (ViewTemplate.isGUI()) {
	    control = new GUIController(model, (GUIView) view);
	} else {
	    control = new CLIController(model, view);
	}
    }

    public void init() {
	control.init();
    }
}

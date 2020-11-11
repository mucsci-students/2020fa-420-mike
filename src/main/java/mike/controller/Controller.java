
package mike.controller;

import mike.datastructures.Model;
import mike.view.ViewTemplate;

public class Controller {
    private ControllerType control;

    public Controller(Model model, ViewTemplate view) {

	if (view.isGUI()) {
	    control = new GUIController(model, view);
	} else {
	    control = new CLIController(model, view);
	}
    }
    
    public void init()
    {
	control.init();
    }
}

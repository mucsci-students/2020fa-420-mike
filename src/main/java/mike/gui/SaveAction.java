package mike.gui;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import mike.HelperMethods;
import mike.controller.GUIController;

public class SaveAction extends AbstractAction {

    private static final long serialVersionUID = 2041789355508350097L;
    private GUIController control;

    public SaveAction(GUIController control) {
	this.control = control;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	try {
	    if (control.getFile() == null) {
		saveWithInput(control);
	    } else {
		HelperMethods.save(control.getFile(), control.getModel());
		control.setChanged(false);
	    }
	} catch (IOException e1) {
	    e1.printStackTrace();
	}
    }

    private static void saveWithInput(GUIController control) {
	JFileChooser chooser = new JFileChooser();
	FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON files", "json");
	chooser.setFileFilter(filter);
	int returnVal = chooser.showSaveDialog(control.getView().getFrame().getParent());
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    control.setFile(chooser.getSelectedFile());
	    try {
		HelperMethods.save(control.getFile(), control.getModel());
		control.setChanged(false);
	    } catch (IOException e) {
		System.out.println("Something went wrong when calling save from saveWithInput.");
		e.printStackTrace();
	    }
	}
    }
}

package mike.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import mike.HelperMethods;
public class SaveController {

    // Listen to any function calls
    protected static void saveListener(JButton save, GUIController control) {
	save.addActionListener(new ActionListener() {
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
	});
    }

    protected static void saveAsListener(JButton saveAs, GUIController control) {
	saveAs.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		saveWithInput(control);
	    }
	});
    }

    private static void saveWithInput(GUIController control) {
	JFileChooser chooser = new JFileChooser();
	FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON files", "json");
	chooser.setFileFilter(filter);
	int returnVal = chooser.showOpenDialog(control.getView().getFrame().getParent());
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

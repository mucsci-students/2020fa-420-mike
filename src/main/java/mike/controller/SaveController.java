package mike.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mike.HelperMethods;
import mike.view.GUIView;

public class SaveController {

    // Listen to any function calls
    protected static void saveListener(JButton save, Controller control) {
	save.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		try {
		    if (control.getPath() == null) {
			saveWithInput(control);
		    } else {
			HelperMethods.save(control.getPath(), control.getModel());
			control.setChanged(false);
		    }
		} catch (IOException e1) {
		    e1.printStackTrace();
		}
	    }
	});
    }

    protected static void saveAsListener(JButton saveAs, Controller control) {
	saveAs.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		saveWithInput(control);
	    }
	});
    }

    private static void saveWithInput(Controller control) {
	try {
	    JTextField fileName = new JTextField(20);
	    JTextField directory = new JTextField(40);

	    // Create a panel containing a drop-down box and text field
	    JPanel inputFields = new JPanel();
	    inputFields.add(new JLabel("Enter a File name: "));
	    inputFields.add(fileName);
	    inputFields.add(new JLabel("Enter a Directory (optional): "));
	    inputFields.add(directory);

	    // Ask for input with inputFields
	    int result = JOptionPane.showConfirmDialog(null, inputFields, "Save As", JOptionPane.OK_CANCEL_OPTION);
	    if (result == 0) {
		File file = new File(directory.getText() + "\\" + fileName.getText());
		if (file.isDirectory() && file.isAbsolute()) {
		    control.setPath(Paths.get(file.toString()));
		} else {
		    control.setPath(Paths
			    .get(System.getProperty("user.dir") + directory.getText() + "\\" + fileName.getText()));
		    file = new File(control.getPath().getParent().toString());
		    if (!file.isDirectory()) {
			JOptionPane.showMessageDialog( ((GUIView) control.getView()).getFrame(),
				"Directory does not exist.  File saved to uml directory.");
		    }
		}

		HelperMethods.save(control.getPath(), control.getModel());
		control.setChanged(false);
	    }

	} catch (IOException e1) {
	    e1.printStackTrace();
	}
    }

}

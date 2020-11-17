package mike.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import mike.HelperMethods;
import mike.datastructures.Entity;
import mike.view.GUIView;

public class LoadController {

    // Listen to any function calls
    protected static void loadListener(JButton load, GUIController control) {
	load.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		try {
		    if (control.getChanged()) {
			int n = JOptionPane.showConfirmDialog(((GUIView) control.getView()).getFrame(),
				"You have unsaved changes.  Do you still want to load a new file?", "Exit",
				JOptionPane.YES_NO_OPTION);
			// No, go back
			if (n == 1) {
			    return;
			}
		    }
		    JFileChooser chooser = new JFileChooser();
		    FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON files", "json");
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(control.getView().getFrame().getParent());
		    if (returnVal == JFileChooser.APPROVE_OPTION) {
			control.setPath(Paths.get(chooser.getSelectedFile().getAbsolutePath()));
			control.getView().getPane().removeAll();
			HelperMethods.load(control.getPath(), control.getModel(), control,
				((GUIView) control.getView()));
			control.setChanged(false);
			control.getView().validateRepaint();
			for (Entity curEntity : control.getModel().getEntities()) {
			    JLabel curLabel = ((GUIView) control.getView()).getEntityLabels().get(curEntity.getName());
			    curLabel.setLocation(curEntity.getXLocation(), curEntity.getYLocation());
			}
		    }
		} catch (Exception e1) {
		    e1.printStackTrace();
		}
	    }
	});
    }

}

package mike.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import mike.controller.GUIController;

public class RedoAction extends AbstractAction {

    private static final long serialVersionUID = 5936682208339408960L;
    private GUIController control;

    public RedoAction(GUIController control) {
	this.control = control;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (control.getCurrMeme() < control.getMementos().size() - 1) {
	    control.setCurrMeme(control.getCurrMeme() + 1);
	    control.setModel(control.getMementos().get(control.getCurrMeme()).getModel());
	    control.getView().repaintEverything(control.getModel(), control);
	} else {
	    JOptionPane.showMessageDialog(control.getView().getFrame(), "There are no actions to redo");
	}
    }
}

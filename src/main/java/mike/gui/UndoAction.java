package mike.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import mike.controller.GUIController;
import mike.view.GUIView;

public class UndoAction extends AbstractAction {

    private static final long serialVersionUID = 53159213647269108L;
    private GUIController control;

    public UndoAction(GUIController control) {
	this.control = control;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (control.getCurrMeme() > 0) {
	    control.setCurrMeme(control.getCurrMeme() - 1);
	    control.setModel(control.getMementos().get(control.getCurrMeme()).getModel());
	    ((GUIView) control.getView()).repaintEverything(control.getModel(), control);
	} else {
	    JOptionPane.showMessageDialog(control.getView().getFrame(), "There are no actions to undo");
	}
    }
}
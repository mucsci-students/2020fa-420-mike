package mike.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import mike.datastructures.Memento;
import mike.view.GUIView;

public class UndoRedoController {
    protected static void undoListener(JButton undo, ArrayList<Memento> mementos, GUIController control) {
	undo.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (control.getCurrMeme() > 0) {
		    control.setCurrMeme(control.getCurrMeme() - 1);
		    control.setModel(mementos.get(control.getCurrMeme()).getModel());
		    ((GUIView) control.getView()).repaintEverything(control.getModel(), control);
		} else {
		    JOptionPane.showMessageDialog(control.getView().getFrame(), "There are no actions to undo");
		}
	    }
	});
    }

    protected static void redoListener(JButton redo, ArrayList<Memento> mementos, GUIController control) {
	redo.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (control.getCurrMeme() < mementos.size() - 1) {
		    control.setCurrMeme(control.getCurrMeme() + 1);
		    control.setModel(mementos.get(control.getCurrMeme()).getModel());
		    ((GUIView) control.getView()).repaintEverything(control.getModel(), control);
		} else {
		    JOptionPane.showMessageDialog(control.getView().getFrame(), "There are no actions to redo");
		}
	    }
	});
    }
}

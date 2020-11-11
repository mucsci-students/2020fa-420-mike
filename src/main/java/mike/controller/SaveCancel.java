package mike.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mike.datastructures.Entity;
import mike.datastructures.Method;
import mike.datastructures.Model;
import mike.datastructures.Relationship;
import mike.gui.Line;
import mike.gui.editBox;
import mike.view.GUIView;

public class SaveCancel {

    protected static void saveClass(JButton saveButton, GUIController control) {
	saveButton.addActionListener(new ActionListener() {
	    @SuppressWarnings("unchecked")
	    public void actionPerformed(ActionEvent e) {
		GUIView view = entering(control);

		Entity entity = editBox.getEntity();
		String eName = entity.getName();
		int fieldSize = entity.getFields().size(), methodNum = 0, paramSize;
		JLabel newBox = editBox.getBox();
		JTextField text = (JTextField) ((JPanel) newBox.getComponent(1)).getComponent(1);
		Model model = control.getModel();
		model.renameClass(eName, text.getText());

		for (int x = 0; x < fieldSize; ++x) {
		    JPanel panelField = (JPanel) newBox.getComponent(x + 3);
		    String textField = ((JTextField) panelField.getComponent(3)).getText();
		    String typeField = ((JTextField) panelField.getComponent(2)).getText();
		    String visType = ((JComboBox<String>) panelField.getComponent(1)).getSelectedItem().toString();
		    String fieldName = entity.getFields().get(x).getName();

		    model.renameField(eName, fieldName, textField);
		    model.changeFieldType(eName, fieldName, typeField);
		    model.changeFieldVis(eName, fieldName, visType);
		}

		for (int x = fieldSize + 5; x < newBox.getComponentCount() - 1; x += paramSize + 2, ++methodNum) {
		    JPanel panelMethod = (JPanel) newBox.getComponent(x);
		    String textMethod = ((JTextField) panelMethod.getComponent(3)).getText();
		    String typeMethod = ((JTextField) panelMethod.getComponent(2)).getText();
		    String visType = ((JComboBox<String>) panelMethod.getComponent(1)).getSelectedItem().toString();

		    Method m = entity.getMethods().get(methodNum);
		    paramSize = m.getParameters().size();

		    model.renameMethod(eName, m.getName(), textMethod);
		    model.changeMethodType(eName, m.getName(), typeMethod);
		    model.changeMethodVis(eName, m.getName(), visType);

		    for (int y = 0; y < paramSize; ++y) {
			JPanel panelParam = (JPanel) newBox.getComponent(x + y + 1);
			String typeParam = ((JTextField) panelParam.getComponent(2)).getText();
			String textParam = ((JTextField) panelParam.getComponent(3)).getText();

			m.renameParameter(m.getParameters().get(y).getName(), textParam);
			m.changeParameterType(textParam, typeParam);
		    }
		}

		control.getinClass().setName(text.getText());
		view.exitEditingClass(control.getinClass(), control, model);
		exiting(view.getMenuBar(), control, view.getFrame());
	    }
	});
    }

    protected static void cancelClass(JButton cancelButton, GUIController control, Model backup) {
	cancelButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		GUIView view = entering (control);
		control.setModel(backup);

		for (Line relation : view.getRelations()) {
		    view.getPane().remove(relation);
		}
		view.getRelations().clear();
		for (Relationship relation : backup.getRelationships()) {
		    view.createRelationship(relation.getName(), relation.getFirstClass(), relation.getSecondClass(),
			    control.getModel());
		}

		view.exitEditingClass(control.getinClass(), control, control.getModel());
		exiting (view.getMenuBar(), control, view.getFrame());
	    }
	});
    }

    protected static void deleteEntity(JButton deletion, GUIController control) {
	deletion.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		int n = JOptionPane.showConfirmDialog(((GUIView) control.getView()).getFrame(),
			"Are you sure you want to delete this class?", "Delete Class", JOptionPane.YES_NO_OPTION);
		if (n == 0) {
		    GUIView view = entering (control);
		    view.deleteLines(editBox.getEntity().getName());
		    control.getModel().deleteClass(editBox.getEntity().getName());
		    view.getPane().remove(control.getinClass());
		    exiting (view.getMenuBar(), control, view.getFrame());
		}
	    }
	});
    }

    private static GUIView entering(GUIController control) {
	GUIView view = (GUIView) control.getView();
	JMenuBar menuBar = view.getMenuBar();
	menuBar.getComponent(4).setBackground(Color.RED);
	menuBar.getComponent(4).setEnabled(true);
	menuBar.getComponent(3).setEnabled(true);
	return view;
    }
    
    private static void exiting(JMenuBar menuBar, GUIController control, JFrame frame) {
	control.setinClass(null);
	editBox.setBox(null);
	menuBar.remove(6);
	menuBar.remove(5);
	frame.validate();
	frame.repaint();
    }

}

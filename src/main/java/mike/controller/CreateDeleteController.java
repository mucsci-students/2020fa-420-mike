package mike.controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mike.datastructures.Entity;
import mike.datastructures.Method;
import mike.gui.editBox;
import mike.view.GUIView;

public class CreateDeleteController {

    @SuppressWarnings("unchecked")
    protected static void createFunction(JPanel panel, GUIController control, String attribute, String methodName) {
	JButton creation = (JButton) panel.getComponent(6);
	creation.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		String visType = null;
		if (attribute != "parameter") {
		    visType = ((JComboBox<String>) panel.getComponent(0)).getSelectedItem().toString();
		}
		String newType = ((JTextField) panel.getComponent(2)).getText();
		String newName = ((JTextField) panel.getComponent(4)).getText();

		if (newType.isEmpty() || newName.isEmpty()) {
		    return;
		}
		Entity entity = editBox.getEntity();
		JLabel newview = editBox.getBox();

		// Create attribute in model and view (makes controllers at the same time)
		if (attribute == "field") {
		    editBox.getEditModel().createField(entity.getName(), newName, newType, visType);
		    int spot = editBox.getEntity().getFields().size() + 2;
		    control.deleteField(editBox.editSection(newType, newName, false, spot));
		    ((JComboBox<String>) panel.getComponent(0)).setSelectedItem("public");
		} else if (attribute == "method") {
		    editBox.getEditModel().createMethod(entity.getName(), newName, newType, visType);
		    int spot = newview.getComponentCount() - 1;
		    control.deleteMethod(editBox.editSection(newType, newName, false, spot));
		    control.createParam(editBox.newSection(true, spot + 1), newName);
		    ((JComboBox<String>) panel.getComponent(0)).setSelectedItem("public");
		} else if (attribute == "parameter") {
		    editBox.getEditModel().createParameter(entity.getName(), methodName, newName, newType);
		    int spot = entity.getFields().size() + 4;
		    for (Method m : entity.getMethods()) {
			spot += m.getParameters().size() + 2;
			if (m.getName().equals(methodName)) {
			    --spot;
			    break;
			}
		    }
		    control.deleteParam(editBox.editSection(newType, newName, true, spot), methodName);
		}
		((JTextField) panel.getComponent(2)).setText("");
		((JTextField) panel.getComponent(4)).setText("");
		ending(control, newview, entity);

		editBox.newEditMeme();
	    }
	});
    }

    protected static void deleteFunction(JPanel panel, GUIController control, String attribute, String methodName) {
	JButton deletion;
	if (attribute.equals("parameter")) {
	    deletion = (JButton) panel.getComponent(2);
	} else {
	    deletion = (JButton) panel.getComponent(0);
	}
	deletion.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		JLabel newview = editBox.getBox();
		Entity entity = editBox.getEntity();
		String deleteAtt = ((JTextField) panel.getComponent(6)).getText();

		// Delete attribute from model
		if (attribute.equals("field")) {
		    editBox.getEditModel().deleteField(entity.getName(), deleteAtt);
		} else if (attribute.equals("method")) {
		    // Delete parameters in view as well
		    int methodSpot = newview.getComponentZOrder(panel);
		    int numParams = entity.copyMethod(deleteAtt).getParameters().size();
		    for (int x = 0; x < numParams + 1; ++x) {
			newview.remove(methodSpot + 1);
		    }

		    editBox.getEditModel().deleteMethod(entity.getName(), deleteAtt);
		} else if (attribute.equals("parameter")) {
		    editBox.getEditModel().deleteParameter(entity.getName(), methodName, deleteAtt);
		}

		// Delete attribute from view
		newview.remove(deletion.getParent());
		ending(control, newview, entity);

		editBox.newEditMeme();
	    }
	});
    }

    private static void ending(GUIController control, JLabel newview, Entity entity) {
	Dimension dim = newview.getLayout().preferredLayoutSize(newview);
	newview.setBounds(entity.getXLocation(), entity.getYLocation(), dim.width, dim.height);
	((GUIView) control.getView()).validateRepaint();
    }

}

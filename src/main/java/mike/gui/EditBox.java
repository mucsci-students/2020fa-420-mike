package mike.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import mike.controller.GUIController;
import mike.datastructures.Entity;
import mike.datastructures.Field;
import mike.datastructures.Memento;
import mike.datastructures.Method;
import mike.datastructures.Model;
import mike.datastructures.Parameter;
import mike.view.GUIView;

public class EditBox {

    private static JLabel newBox;
    private static Entity e;
    private static Model editModel;
    private static ArrayList<Memento> editMementos;

    public EditBox(JLabel label, GUIController control, Model model, GUIView view) {
	editModel = new Model(model);
	e = editModel.copyEntity(label.getName());
	editMementos = new ArrayList<Memento>();

	// Create entire editBox
	newBox = new JLabel();
	newBox.setLayout(new BoxLayout(newBox, BoxLayout.Y_AXIS));

	// Add all model parts into newBox
	createClassSection(label.getName(), control, view);
	createSection("Fields:", control);
	createSection("Methods:", control);

	// Design entire label
	newBox.setBackground(new Color(30, 30, 30));
	newBox.setOpaque(true);
	newBox.setBorder(
		new CompoundBorder(BorderFactory.createLineBorder(Color.CYAN.darker(), 4), (new EmptyBorder(6, 6, 6, 6))));

	// Position/show label
	newBox.setName(e.getName());
	newBox.setBounds(e.getXLocation(), e.getYLocation(), newBox.getLayout().preferredLayoutSize(newBox).width,
		newBox.getLayout().preferredLayoutSize(newBox).height);

	newBox.setLocation(label.getX(), label.getY());
    }

    public static JLabel getBox() {
	return newBox;
    }

    public static Entity getEntity() {
	return e;
    }

    public static Model getEditModel() {
	return editModel;
    }

    public static void setEditModel(Model newModel) {
	editModel = newModel;
    }

    public static ArrayList<Memento> getEditMementos() {
	return editMementos;
    }

    public static void setBox(JLabel b) {
	newBox = b;
    }

    public static void newEditMeme() {
	editMementos.add(new Memento(new Model(editModel)));
    }

    private void createClassSection(String labelName, GUIController control, GUIView view) {
	JFrame frame = view.getFrame();
	JMenuBar menuBar = view.getMenuBar();
	JButton addRelation = new JButton("Add Relationship");
	JButton deleteRelation = new JButton("Delete Relationship");
	menuBar.add(addRelation);
	menuBar.add(deleteRelation);
	addRelation.setBackground(new Color(30, 30, 30));
	deleteRelation.setBackground(new Color(30, 30, 30));
	frame.getContentPane().add(BorderLayout.NORTH, menuBar);
	frame.setLocationRelativeTo(null);
	frame.setVisible(true);
	frame.validate();
	frame.repaint();

	JPanel saveCancel = setUpJPanel();
	JButton saveButton = new JButton("Save");
	JButton cancelButton = new JButton("Cancel");
	saveButton.setBackground(new Color(30, 30, 30));
	cancelButton.setBackground(new Color(30, 30, 30));
	saveCancel.add(saveButton);
	saveCancel.add(cancelButton);
	newBox.add(saveCancel);

	JPanel newEntity = setUpJPanel();
	JButton xButton = new JButton("X");
	xButton.setBackground(Color.RED);
	JTextField className = new JTextField(labelName);
	newEntity.add(xButton);
	newEntity.add(Box.createHorizontalStrut(5));
	newEntity.add(className);
	control.saveCancel(saveButton, cancelButton, xButton, addRelation, deleteRelation);
	newBox.add(newEntity);
    }

    private void createSection(String section, GUIController control) {
	JLabel Label = new JLabel(section);
	Label.setFont(new Font("", Font.BOLD, 18));
	Label.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
	Label.setForeground(Color.WHITE);
	newBox.add(Label);
	
	JLabel start;
	if(section == "Fields:") {
	    if (control.getModel().copyEntity(e.getName()).getFields().size() == 0) {
		start = new JLabel(" Visibility        Type              Name");
	    } else {
		start = new JLabel("            Visibility        Type              Name");
	    }
	} else {
	    if (control.getModel().copyEntity(e.getName()).getMethods().size() == 0) {
		start = new JLabel(" Visibility        Type              Name");
	    } else {
		start = new JLabel("            Visibility        Type              Name");
	    }
	}
	start.setFont(new Font("", Font.BOLD, 14));
	start.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 0));
	start.setForeground(Color.WHITE);
	newBox.add(start);

	// Add all existing fields/methods(and Paramters) to editBox
	if (section == "Fields:") {
	    for (Field f : e.getFields()) {
		control.deleteField(editSection(f.getType(), f.getName(), false, newBox.getComponentCount()));
	    }
	    control.createField(newSection(false, newBox.getComponentCount()));
	} else {
	    for (Method m : e.getMethods()) {
		control.deleteMethod(editSection(m.getType(), m.getName(), false, newBox.getComponentCount()));
		for (Parameter p : m.getParameters()) {
		    control.deleteParam(editSection(p.getType(), p.getName(), true, newBox.getComponentCount()),
			    m.getName());
		}
		control.createParam(newSection(true, newBox.getComponentCount()), m.getName());
	    }
	    control.createMethod(newSection(false, newBox.getComponentCount()));
	}

    }

    public static JPanel editSection(String sectionType, String sectionName, Boolean parameter, int spot) {
	JPanel editSection = setUpJPanel();

	String[] visTypesArray = { "public", "private", "protected" };
	JComboBox<String> visTypes = new JComboBox<>(visTypesArray);

	if (e.containsField(sectionName)) {
	    visTypes.setSelectedItem(e.copyField(sectionName).getVisibility().toString().toLowerCase());
	} else if (e.containsMethod(sectionName)) {
	    visTypes.setSelectedItem(e.copyMethod(sectionName).getVisibility().toString().toLowerCase());
	}
	JButton xButton = new JButton("X");
	JTextField Type = new JTextField(sectionType, 10);
	JTextField Name = new JTextField(sectionName, 10);
	xButton.setBackground(Color.RED);

	if (parameter) {
	    JLabel Tab = new JLabel("------");
	    editSection.add(Tab);
	    editSection.add(Box.createHorizontalStrut(5));
	}
	editSection.add(xButton);
	editSection.add(Box.createHorizontalStrut(5));
	if (!parameter) {
	    editSection.add(visTypes);
	    editSection.add(Box.createHorizontalStrut(5));
	}
	editSection.add(Type);
	editSection.add(Box.createHorizontalStrut(5));
	editSection.add(Name);

	newBox.add(editSection, spot);

	return editSection;
    }

    public static JPanel newSection(Boolean parameter, int spot) {
	JPanel newSection = setUpJPanel();

	JButton plusButton = new JButton("+");
	plusButton.setBackground(Color.green.darker());
	if (parameter) {
	    JLabel Tab = new JLabel("------");
	    newSection.add(Tab);
	    newSection.add(Box.createHorizontalStrut(5));
	} else {
	    String[] visTypesArray = { "public", "private", "protected" };
	    JComboBox<String> visTypes = new JComboBox<>(visTypesArray);
	    visTypes.setSelectedItem("public");
	    newSection.add(visTypes);
	    newSection.add(Box.createHorizontalStrut(5));
	}

	newSection.add(new JTextField(10)); // Type
	newSection.add(Box.createHorizontalStrut(5));
	newSection.add(new JTextField(10)); // Name
	newSection.add(Box.createHorizontalStrut(5));
	newSection.add(plusButton);
	newSection.add(Box.createHorizontalStrut(5));

	newBox.add(newSection, spot);

	return newSection;
    }

    private static JPanel setUpJPanel() {
	JPanel panel = new JPanel();
	panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
	panel.setAlignmentX(Component.LEFT_ALIGNMENT);
	panel.setBackground(new Color(30, 30, 30));
	panel.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
	return panel;
    }

}

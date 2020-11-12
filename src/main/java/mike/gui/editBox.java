package mike.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
//import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
//import javax.swing.ImageIcon;
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
import mike.datastructures.Method;
import mike.datastructures.Model;
import mike.datastructures.Parameter;
import mike.view.GUIView;

public class editBox {

    private static JLabel newBox;
    // private static ImageIcon xIcon = new ImageIcon(new
    // ImageIcon(GUIView.class.getResource("..\\gui\\resources\\xmark.jpg")).getImage().getScaledInstance(16,
    // 16, Image.SCALE_DEFAULT));
    // private static ImageIcon addIcon = new ImageIcon(new
    // ImageIcon(GUIView.class.getResource("..\\gui\\resources\\addSymbol.png")).getImage().getScaledInstance(16,
    // 16, Image.SCALE_DEFAULT));
    private static Entity e;
    private Model backupMod;

    public editBox(JLabel label, GUIController control, Model model, GUIView view) {
	backupMod = new Model(model);
	e = model.copyEntity(label.getName());

	// Create entire editBox
	newBox = new JLabel();
	newBox.setLayout(new BoxLayout(newBox, BoxLayout.Y_AXIS));

	// Add all model parts into newBox
	createClassSection(label.getName(), control, view);
	createSection("Fields:", control, model);
	createSection("Methods:", control, model);

	// Design entire label
	newBox.setBackground(Color.LIGHT_GRAY);
	newBox.setOpaque(true);
	newBox.setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 4), (new EmptyBorder(6, 6, 6, 6))));

	// Position/show label
	newBox.setName(e.getName());
	newBox.setBounds(e.getXLocation(), e.getYLocation(), newBox.getLayout().preferredLayoutSize(newBox).width,newBox.getLayout().preferredLayoutSize(newBox).height);
    }

    public static JLabel getBox() {
	return newBox;
    }

    public static Entity getEntity() {
	return e;
    }

    public Model getBackup() {
	return backupMod;
    }

    public static void setBox(JLabel b) {
	newBox = b;
    }

    private void createClassSection(String labelName, GUIController control, GUIView view) {
	JFrame frame = view.getFrame();
	JMenuBar menuBar = view.getMenuBar();
	JButton addRelation = new JButton("Add Relationship");
	JButton deleteRelation = new JButton("Delete Relationship");
	menuBar.add(addRelation);
	menuBar.add(deleteRelation);
	frame.getContentPane().add(BorderLayout.NORTH, menuBar);
	frame.setLocationRelativeTo(null);
	frame.setVisible(true);
	frame.validate();
	frame.repaint();

	JPanel saveCancel = setUpJPanel();
	JButton saveButton = new JButton("Save");
	JButton cancelButton = new JButton("Cancel");
	saveCancel.add(saveButton);
	saveCancel.add(cancelButton);
	newBox.add(saveCancel);

	JPanel newEntity = setUpJPanel();
	JButton xButton = new JButton("X");
	JTextField className = new JTextField(labelName);
	newEntity.add(xButton);
	newEntity.add(className);
	control.saveCancel(saveButton, cancelButton, xButton, addRelation, deleteRelation, backupMod);
	newBox.add(newEntity);
    }

    private void createSection(String section, GUIController control, Model model) {
	JLabel Label = new JLabel(section);
	Label.setFont(new Font("", Font.BOLD, 18));
	Label.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
	newBox.add(Label);

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
	JTextField Type = new JTextField(sectionType);
	JTextField Name = new JTextField(sectionName);

	if (parameter) {
	    JLabel Tab = new JLabel("------");
	    editSection.add(Tab);
	}
	editSection.add(xButton);
	if (!parameter) {
	    editSection.add(visTypes);
	}
	editSection.add(Type);
	editSection.add(Name);

	newBox.add(editSection, spot);

	return editSection;
    }

    public static JPanel newSection(Boolean parameter, int spot) {
	JPanel newSection = setUpJPanel();

	JTextField Type = new JTextField(7);
	JTextField Name = new JTextField(7);

	JButton plusButton = new JButton("+");
	if (parameter) {
	    JLabel Tab = new JLabel("------");
	    newSection.add(Tab);
	} else {
	    String[] visTypesArray = { "public", "private", "protected" };
	    JComboBox<String> visTypes = new JComboBox<>(visTypesArray);
	    newSection.add(visTypes);
	}
	newSection.add(Type);
	newSection.add(Name);
	newSection.add(plusButton);

	newBox.add(newSection, spot);

	return newSection;
    }

    private static JPanel setUpJPanel() {
	JPanel panel = new JPanel();
	panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
	panel.setAlignmentX(Component.LEFT_ALIGNMENT);
	panel.setBackground(Color.LIGHT_GRAY);
	panel.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
	return panel;
    }

}

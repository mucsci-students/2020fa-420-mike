package mike.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import mike.datastructures.Entity;
import mike.datastructures.Model;
import mike.datastructures.Relationship;
import mike.datastructures.Relationship.Type;
import mike.gui.editBox;
import mike.view.GUIView;
import mike.view.ViewTemplate;

public class ClassController {

    private static boolean editMode = false;
    private static int x_pressed = 0;
    private static int y_pressed = 0;

    protected static void addRelationListener(JButton addRelation, Controller control) {
	addRelation.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mousePressed(MouseEvent e) {
		GUIView view = (GUIView) control.getView();
		ArrayList<Entity> entities = control.getModel().getEntities();
		String[] entityStrings = new String[entities.size()];
		for (int x = 0; x < entities.size(); ++x) {
		    entityStrings[x] = entities.get(x).getName();
		}
		createRelation(control.getModel(), entityStrings, view.getFrame(), view);
	    }
	});
    }

    protected static void deleteRelationListener(JButton deleteRelation, Controller control) {
	deleteRelation.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mousePressed(MouseEvent e) {
		GUIView view = (GUIView) control.getView();
		deleteRelation(control.getModel(), view.getFrame(), view);
	    }
	});
    }

    protected static void clickClass(JLabel newview, Controller control) {
	newview.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mousePressed(MouseEvent e) {
		if (editMode) {
		    if (control.getinClass() == null) {
			GUIView view = (GUIView) control.getView();
			JButton addClassButton = (JButton) view.getMenuBar().getComponent(3);
			JButton editModeButton = (JButton) view.getMenuBar().getComponent(4);
			editModeButton.setBackground(Color.LIGHT_GRAY);
			editModeButton.setEnabled(false);
			addClassButton.setEnabled(false);
			control.setinClass(view.htmlBoxToEditBox(newview, control, control.getModel()));
		    }
		} // catching the current values for x,y coordinates on screen
		else if (e.getSource() == newview) {
		    // if in edit mode, show text boxes and such for fields/methods/parameter
		    x_pressed = e.getX();
		    y_pressed = e.getY();
		}
	    }
	});
    }

    protected static void moveClass(JLabel newview, Entity entity, ViewTemplate view) {
	newview.addMouseMotionListener(new MouseMotionAdapter() {
	    @Override
	    public void mouseDragged(MouseEvent e) {
		// allow dragging classes if not in edit mode
		if (!editMode) {
		    if (e.getSource() == newview) {
			JComponent jc = (JComponent) e.getSource();
			jc.setLocation(jc.getX() + e.getX() - x_pressed, jc.getY() + e.getY() - y_pressed);
			entity.setXLocation(jc.getX() + e.getX() - x_pressed);
			entity.setYLocation(jc.getY() + e.getY() - y_pressed);
		    }
		    ((GUIView) view).repaintLine(entity.getName());
		}
	    }
	});
    }

    protected static void editModeListener(JButton editButton, Controller control) {
	editButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		GUIView view = (GUIView) control.getView();
		// If leaving edit mode
		if (editMode) {
		    editMode = false;
		    for (int x = 0; x < 4; ++x) {
			JButton button = (JButton) view.getMenuBar().getComponent(x);
			button.setEnabled(true);
		    }
		    // Change button to signify we are out of edit mode
		    editButton.setText("Enable Edit Mode");
		    editButton.setBackground(null);
		    if (control.getinClass() != null) {
			view.exitEditingClass(control.getinClass(), control, control.getModel());
			view.getMenuBar().remove(6);
			view.getMenuBar().remove(5);
			view.getFrame().validate();
			view.getFrame().repaint();
		    }
		    control.setinClass(null);
		}
		// If entering edit mode
		else {
		    editMode = true;
		    for (int x = 0; x < 3; ++x) {
			JButton button = (JButton) view.getMenuBar().getComponent(x);
			button.setEnabled(false);
		    }
		    // change button to signify we are in edit mode
		    editButton.setText("Disable Edit Mode");
		    editButton.setBackground(Color.RED);
		}
	    }
	});
    }

    private static void createRelation(Model classes, String[] entityStrings, JFrame frame, GUIView view) {
	if (classes.getEntities().size() < 2) {
	    JOptionPane.showMessageDialog(frame, "There are not enough classes to create a relationship.");
	    return;
	}

	// Create the radio buttons for selecting a relationship
	JRadioButton composition = new JRadioButton("Composition");
	JRadioButton aggregation = new JRadioButton("Aggregation");
	JRadioButton realization = new JRadioButton("Realization");
	JRadioButton inheritance = new JRadioButton("Inheritance");
	composition.setSelected(true);

	// Group the radio buttons (Only one of these may be selected at a time)
	ButtonGroup group = new ButtonGroup();
	group.add(composition);
	group.add(aggregation);
	group.add(realization);
	group.add(inheritance);

	// Create a panel to display type selection and drop-down of all classes
	// Construct visual components of the first menu of creating a relationship.
	// Contains radio buttons of relationship type, and the list of all classes
	JPanel inputFields = new JPanel();
	inputFields.add(new JLabel("Choose the Relationship Type: "));
	inputFields.add(composition);
	inputFields.add(aggregation);
	inputFields.add(realization);
	inputFields.add(inheritance);

	JComboBox<String> listTwo = new JComboBox<>(entityStrings);
	ArrayList<Relationship> allRelationships = classes.getRelationships();
	String selectedClass = editBox.getBox().getName();

	// Remove classes from listTwo based on existing relationships
	// Find all relationships that have use the selected class as class 1...
	for (int x = 0; x < allRelationships.size(); ++x) {
	    if (selectedClass.equals(allRelationships.get(x).getFirstClass())) {
		Relationship currRelationship = allRelationships.get(x);
		// ... and remove the second class of of those relationships from the drop-down
		// box of listTwo
		listTwo.removeItem(currRelationship.getSecondClass());
	    } else if (selectedClass.equals(allRelationships.get(x).getSecondClass())) {
		Relationship currRelationship = allRelationships.get(x);
		// ... and remove the second class of of those relationships from the drop-down
		// box of listTwo
		listTwo.removeItem(currRelationship.getFirstClass());
	    }
	}

	// Check if creating a relationship is valid
	if (listTwo.getItemCount() == 0) {
	    JOptionPane.showMessageDialog(frame, "There are no possible relationships to create with this class.");
	    return;
	}

	// Add display selection for class 2
	inputFields.add(new JLabel("Choose a Class 2: "));
	inputFields.add(listTwo);

	// Actually display the stuff
	int result = JOptionPane.showConfirmDialog(null, inputFields, "Create Relationship",
		JOptionPane.OK_CANCEL_OPTION);

	Type type;
	if (composition.isSelected()) {
	    type = Type.COMPOSITION;
	} else if (aggregation.isSelected()) {
	    type = Type.AGGREGATION;
	} else if (realization.isSelected()) {
	    type = Type.REALIZATION;
	} else {
	    type = Type.INHERITANCE;
	}

	if (result == 0) {
	    String name1 = editBox.getBox().getName();
	    String name2 = listTwo.getSelectedItem().toString();
	    classes.createRelationship(type, name1, name2);
	    view.createRelationship(type, name1, name2, classes);
	}
    }

    private static void deleteRelation(Model classes, JFrame frame, GUIView view) {
	if (classes.getRelationships().size() == 0) {
	    JOptionPane.showMessageDialog(frame, "There are no relationships to delete.");
	    return;
	}
	// Create a panel containing a drop-down box to select the first class of the
	// relationship
	JPanel inputFields = new JPanel();
	// Used to select second class of relationship
	ArrayList<Relationship> allRelationships = classes.getRelationships();

	inputFields.add(new JLabel("Choose a Relationship: "));

	// Create String array holding all existing relationships
	// Use a common string between class one and class two for easier parsing
	String editName = editBox.getBox().getName();
	int y = 0;
	for (int x = 0; x < allRelationships.size(); ++x) {
	    if (editName.equals(allRelationships.get(x).getFirstClass())) {
		++y;
	    }
	}
	String[] relationStrings = new String[y];
	y = 0;
	for (int x = 0; x < allRelationships.size(); ++x) {
	    Relationship r = allRelationships.get(x);
	    if (editName.equals(r.getFirstClass())) {
		relationStrings[y] = r.getFirstClass() + "--" + r.getSecondClass();
		++y;
	    }
	}

	// Add String array to combo box for user selection
	JComboBox<String> listTwo = new JComboBox<>(relationStrings);
	inputFields.add(listTwo);
	inputFields.add(Box.createHorizontalStrut(15)); // a spacer

	int result = JOptionPane.showConfirmDialog(null, inputFields, "Delete Relationship",
		JOptionPane.OK_CANCEL_OPTION);

	if (result == 0) {
	    // Parse user selected relationship into its two classes
	    String[] deleteRelation = listTwo.getSelectedItem().toString().split("--");
	    String class1 = deleteRelation[0];
	    String class2 = deleteRelation[1];
	    Type targetType = null;

	    // Find type of relationship between those classes
	    for (Relationship currRelation : classes.getRelationships()) {
		if (class1.equals(currRelation.getFirstClass()) && class2.equals(currRelation.getSecondClass())) {
		    targetType = currRelation.getName();
		}
	    }
	    classes.deleteRelationship(targetType, class1, class2);
	    view.deleteLine(class1, class2);
	}
    }
}

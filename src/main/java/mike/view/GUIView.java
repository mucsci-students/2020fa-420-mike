package mike.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import mike.controller.GUIController;
import mike.datastructures.*;
import mike.gui.Line;
import mike.gui.EditBox;
import mike.gui.HtmlBox;

public class GUIView extends ViewTemplate implements ViewInterface {
    // Global Variables
    private JLayeredPane pane;
    private HashMap<String, JLabel> entityLabels;
    private ArrayList<Line> relations;
    private JFrame frame;
    private JMenuBar menuBar;
    private JScrollPane scrollPane;
    
    public GUIView(HashMap<String, JLabel> entityLabels, JLayeredPane pane, JScrollPane scrollPane, ArrayList<Line> relations,
	    JMenuBar menuBar) {
	super();

	// initialize globals
	this.entityLabels = entityLabels;
	this.scrollPane = scrollPane;
	this.pane = pane;
	this.relations = relations;
	this.menuBar = menuBar;

	GUIInit();
    }

    public GUIView() {
	super();

	// initialize globals
	entityLabels = new HashMap<String, JLabel>();
	pane = new JLayeredPane();
	pane.setPreferredSize(new Dimension(5000, 5000));
	scrollPane = new JScrollPane(pane);
	relations = new ArrayList<Line>();
	menuBar = new JMenuBar();
	frame = new JFrame("Team mike UML Editor");
	
	GUIInit();
	
	// Creating the menu bar and its options
	JButton[] buttons = { new JButton("Save"), new JButton("Save As"), new JButton("Load"), new JButton("Undo"),
		new JButton("Redo"), new JButton("Add Class"), new JButton("Enable Edit Mode") };
	for (int x = 0; x < 7; ++x) {
	    menuBar.add(buttons[x]);
	}
	
	// Adding all panels onto frame
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(1000, 800);
	frame.getContentPane().add(BorderLayout.CENTER, scrollPane);
	frame.getContentPane().add(BorderLayout.NORTH, menuBar);
	frame.setLocationRelativeTo(null);
	frame.setVisible(true);
    }

    public void GUIInit() {
	// set look and feel to match user's OS
	try {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
		| UnsupportedLookAndFeelException e) {
	    System.out.println("UIManager had a big oopsy-woopsy");
	    e.printStackTrace();
	}
	
	// Creating the middle panel
	pane.setBackground(Color.WHITE);
	pane.setOpaque(true);
	validateRepaint();
    }

    public JMenuBar getMenuBar() {
	return menuBar;
    }

    public HashMap<String, JLabel> getEntityLabels() {
	return entityLabels;
    }

    public JLayeredPane getPane() {
	return pane;
    }

    public ArrayList<Line> getRelations() {
	return relations;
    }

    public JFrame getFrame() {
	return frame;
    }

    public void validateRepaint() {
	scrollPane.validate();
	scrollPane.repaint();
    }

    public void repaintEverything(Model model, GUIController control) {
	pane.removeAll();
	relations.clear();
	entityLabels.clear();
	for (Entity e : model.getEntities()) {
	    showClass(e, control);
	    JLabel curLabel = this.getEntityLabels().get(e.getName());
	    curLabel.setLocation(e.getXLocation(), e.getYLocation());
	}
	for (Relationship r : model.getRelationships()) {
	    createRelationship(r.getName(), r.getFirstClass(), r.getSecondClass(), model);
	}
	
	validateRepaint();
    }
    
    public HtmlBox showClass(Entity entity, GUIController control) {
	HtmlBox newview = new HtmlBox(entity, control);
	pane.add(newview.getBox(), JLayeredPane.PALETTE_LAYER);
	entityLabels.put(entity.getName(), newview.getBox());
	validateRepaint();

	return newview;
    }

    public void deleteLines(String name) {
	ArrayList<Line> deletingLines = new ArrayList<Line>();
	for (Line l : relations) {
	    if (l.getClassOne().getName().equals(name) || l.getClassTwo().getName().equals(name)) {
		pane.remove(l);
		deletingLines.add(l);
	    }
	}
	for (Line l : deletingLines) {
	    relations.remove(l);
	}
	validateRepaint();
    }

    public void deleteLine(String class1, String class2) {
	Line temp = null;
	for (Line l : relations) {
	    if (l.getClassOne().getName().equals(class1) && l.getClassTwo().getName().equals(class2)) {
		pane.remove(l);
		temp = l;
	    }
	}
	relations.remove(temp);
	validateRepaint();
    }

    public void repaintLine(String name) {
	for (Line l : relations) {
	    if (l.getClassOne().getName().equals(name) || l.getClassTwo().getName().equals(name)) {
		l.update();
		l.repaint();
	    }
	}
    }

    public void deleteClass(String name) {
	pane.remove(entityLabels.get(name));
	entityLabels.remove(name);
	validateRepaint();
    }

    public void createRelationship(Relationship.Type type, String name1, String name2, Model model) {
	JLabel L1;
	JLabel L2;
	JLabel box = EditBox.getBox();
	Boolean notNull = (box != null);

	if (notNull && name1.equals(box.getName())) {
	    L1 = box;
	} else {
	    L1 = entityLabels.get(name1);
	}

	if (notNull && name2.equals(box.getName())) {
	    L2 = box;
	} else {
	    L2 = entityLabels.get(name2);
	}
	Line line = new Line(L1, L2, type);
	line.setBounds(0, 0, pane.getWidth(), pane.getHeight());
	model.createRelationship(type, name1, name2);
	relations.add(line);
	pane.add(line, JLayeredPane.DEFAULT_LAYER);
	pane.validate();
    }

    public JLabel htmlBoxToEditBox(JLabel label, GUIController control, Model model) {
	pane.remove(label);

	new EditBox(label, control, model, this);
	pane.add(EditBox.getBox(), JLayeredPane.MODAL_LAYER);

	resetLinesFromConversion(label, EditBox.getBox());

	return EditBox.getBox();
    }

    public void exitEditingClass(JLabel inClass, GUIController control, Model model) {
	pane.remove(inClass);
	Entity e = model.copyEntity(inClass.getName());
	HtmlBox box = showClass(e, control);
	
	// Create settings so the box can appear at right spot
	Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        Border margin = new EmptyBorder(6, 6, 6, 6);
        box.getBox().setBorder(new CompoundBorder(border, margin));
        box.getBox().setBounds(0, 0, box.getBox().getPreferredSize().width, box.getBox().getPreferredSize().height);
        box.getBox().setLocation(e.getXLocation(), e.getYLocation());
        
	resetLinesFromConversion(inClass, box.getBox());
    }

    private void resetLinesFromConversion(JLabel label, JLabel box) {
	String name = label.getName();
	int width = label.getWidth();
	int height = label.getHeight();
	for (Line l : relations) {
	    Boolean equalsClassOne = (name.equals(l.getClassOne().getName()));
	    Boolean equalsClassTwo = (name.equals(l.getClassTwo().getName()));
	    if (equalsClassOne || equalsClassTwo) {
		if (equalsClassOne && equalsClassTwo) {
		    l.setClassOne(box);
		    l.setClassTwo(box);
		    l.setPreferredSize(new Dimension(width, height));
		} else if (equalsClassOne) {
		    l.setClassOne(box);
		    l.setPreferredSize(new Dimension(width, l.getClassTwo().getParent().getHeight()));
		} else {
		    l.setClassTwo(box);
		    l.setPreferredSize(new Dimension(l.getClassTwo().getParent().getWidth(), height));
		}
		l.update();
	    }
	}
	validateRepaint();
    }

}

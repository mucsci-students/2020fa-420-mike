package mike.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;

import mike.controller.Controller;
import mike.datastructures.*;
import mike.gui.Line;
import mike.gui.editBox;
import mike.gui.htmlBox;

public class GUIView {
	// Global Variables
	private static JLayeredPane pane = new JLayeredPane();
	private static HashMap<String, JLabel> entitylabels = new HashMap<String, JLabel>();
	private static ArrayList<Line> relations = new ArrayList<Line>();
	private static JFrame frame = new JFrame("Team mike UML Editor");
	private static Controller control = new Controller();
	private static JMenuBar menuBar = new JMenuBar();
	
	public GUIView() {
		
		// Creating the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 800);

		// Creating the menu bar and its options
		JButton save = new JButton("Save");
		JButton saveAs = new JButton("Save As");
		JButton load = new JButton("Load");
		JButton addClass = new JButton("Add Class");
		JButton editMode = new JButton("Enable Edit Mode");
		menuBar.add(save);
		menuBar.add(saveAs);
		menuBar.add(load);
		menuBar.add(addClass);
		menuBar.add(editMode);
		

		// Creating the middle panel
		pane.setBackground(Color.WHITE);
		
		// Adding all panels onto frame
		frame.getContentPane().add(BorderLayout.CENTER, pane);
		frame.getContentPane().add(BorderLayout.NORTH, menuBar);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		validateRepaint();

		control.frameControls(save, saveAs, load, addClass, editMode);
	
	}
	
	public static JMenuBar getMenuBar() {
		return menuBar;
	}
	public static HashMap<String, JLabel> getEntityLabels() {
		return entitylabels;
	}

	public static JLayeredPane getPane() {
		return pane;
	}
	
	public static ArrayList<Line> getRelations() {
		return relations;
	}
	
	public static JFrame getFrame() {
		return frame;
	}
	
	public static Controller getController() {
		return control;
	}
	
	public static void validateRepaint() {
		pane.validate();
		pane.repaint();
	}

	public static htmlBox showClass(Entity entity) {
		htmlBox newview = new htmlBox(entity);
		pane.add(newview.getBox(), 2);
		entitylabels.put(entity.getName(), newview.getBox());
		validateRepaint();
		
		return newview;
	}

	public static void deleteLines(String name) {
		ArrayList<Line> deletingLines = new ArrayList<Line>();
		for(Line l : relations) {
			if(l.getClassOne().getName().equals(name) || l.getClassTwo().getName().equals(name)) {
				pane.remove(l);
				deletingLines.add(l);
			}
		}
		for(Line l : deletingLines) {
			relations.remove(l);
		}
		validateRepaint();
	}
	
	public static void deleteLine(String class1, String class2) {		
		Line temp = null;
		for(Line l : relations) {
			if(l.getClassOne().getName().equals(class1) && l.getClassTwo().getName().equals(class2)) {
				pane.remove(l);
				temp = l;	
			}
		}
		relations.remove(temp);
		validateRepaint();
	}
	
	public static void repaintLine(String name) {
		for(Line l : relations) {
			if(l.getClassOne().getName().equals(name) || l.getClassTwo().getName().equals(name)) {
				l.update();
				l.repaint();
			}
		}
	}

	public static void deleteClass(String name) {
		pane.remove(entitylabels.get(name));
		entitylabels.remove(name);
		validateRepaint();
	}

	public static void createRelationship(Relationship.Type type, String name1, String name2)
	{
		//JLabel L1 = entitylabels.get(name1);
		JLabel L2 = entitylabels.get(name2);
		Line line;
		if(name1.equals(name2)){
			line = new Line(editBox.getBox(), editBox.getBox(), type);
		} else {
			line = new Line(editBox.getBox(), L2, type);
		}
		
		line.setBounds(0, 0, pane.getWidth(), pane.getHeight());
		Controller.getModel().createRelationship(type, name1, name2);
		relations.add(line);
		pane.add(line);
		pane.validate();
	}
	
	public static JLabel htmlBoxToEditBox(JLabel label) {
		pane.remove(label);

		editBox newBox = new editBox(label);
		pane.add(editBox.getBox(), 2);
		ArrayList<Line> movingLines = new ArrayList<Line>();
		for(Line l : relations) {
			if(l.getClassOne().getName().equals(label.getName()) || l.getClassTwo().getName().equals(label.getName())) {
				movingLines.add(l);
			}
		}
		for(Line l : movingLines) {
			
			if(label.getName().equals(l.getClassOne().getName()) && label.getName().equals(l.getClassTwo().getName())){
				l.setClassOne(editBox.getBox());
				l.setClassTwo(editBox.getBox());
				l.setPreferredSize(new Dimension(label.getWidth(), label.getHeight()));
			} else if (label.getName().equals(l.getClassOne().getName())){
				l.setClassOne(editBox.getBox());
				l.setPreferredSize(new Dimension(label.getWidth(), l.getClassTwo().getParent().getHeight()));
			} else {
				l.setClassTwo(editBox.getBox());
				l.setPreferredSize(new Dimension(l.getClassTwo().getParent().getWidth(), label.getHeight()));
			}
			l.update();
		}
		validateRepaint();
		
		return editBox.getBox();
	}
	
	public static void exitEditingClass(JLabel inClass) {
		pane.remove(inClass);
		Entity e = Controller.getModel().copyEntity(inClass.getName());

		ArrayList<Line> movingLines = new ArrayList<Line>();
		for(Line l : relations) {
			if(l.getClassOne().getName().equals(inClass.getName()) || l.getClassTwo().getName().equals(inClass.getName())) {
				movingLines.add(l);
			}
		}

		JLabel box = showClass(e).getBox();
		for(Line l : movingLines) {
			if(inClass.getName().equals(l.getClassTwo().getName()) && inClass.getName().equals(l.getClassOne().getName())){
				l.setClassOne(box);
				l.setClassTwo(box);
				l.setPreferredSize(new Dimension(inClass.getWidth(), inClass.getHeight()));
			} else if (inClass.getName().equals(l.getClassOne().getName())){
				l.setClassOne(box);
				l.setPreferredSize(new Dimension(inClass.getWidth(), l.getClassTwo().getParent().getHeight()));
			} else {
				l.setClassTwo(box);
				l.setPreferredSize(new Dimension(l.getClassTwo().getParent().getWidth(), inClass.getHeight()));
			}

			l.update();
		}

		validateRepaint();
	}
	
}

package mike.gui;

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
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import mike.datastructures.*;
import mike.gui.Line;

public class GUI implements ViewInterface {
	// Global Variables
	private static JLayeredPane pane = new JLayeredPane();
	private static HashMap<String, JLabel> entitylabels = new HashMap<String, JLabel>();
	private static ArrayList<Line> relations = new ArrayList<Line>();
	private static JFrame frame = new JFrame("Team mike UML Editor");
	
	public GUI() {
		// Creating the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 800);

		// Creating the menu bar and its options
		JMenuBar menuBar = new JMenuBar();
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

		// Creating the side-bar
		JTree tree = createTree();
		tree.setRootVisible(false);
		JScrollPane treeView = new JScrollPane(tree);
		Dimension d = new Dimension(200, treeView.getHeight());
		treeView.setPreferredSize(d);

		// Creating the middle panel
		pane.setBackground(Color.WHITE);
		
		// Adding all panels onto frame
		frame.getContentPane().add(BorderLayout.CENTER, pane);
		frame.getContentPane().add(BorderLayout.WEST, treeView);
		frame.getContentPane().add(BorderLayout.NORTH, menuBar);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		validateRepaint();
		
		Controller.addClassListener(addClass);
		Controller.editModeListener(editMode);
		Controller.saveListener(save);
		Controller.saveAsListener(saveAs);
		Controller.loadListener(load);
		Controller.treeListener(tree);
		Controller.exitListener();
		Controller.resizeListener();
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
	
	private static void validateRepaint() {
		pane.validate();
		pane.repaint();
	}
	
	private static JTree createTree() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("UML");
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("Class");
		top.add(node);
		node.add(new DefaultMutableTreeNode("Create Class"));
		node.add(new DefaultMutableTreeNode("Rename Class"));
		node.add(new DefaultMutableTreeNode("Delete Class"));
		node = new DefaultMutableTreeNode("Relationship");
		top.add(node);
		node.add(new DefaultMutableTreeNode("Create Relationship"));
		node.add(new DefaultMutableTreeNode("Delete Relationship"));
		node = new DefaultMutableTreeNode("Field");
		top.add(node);
		node.add(new DefaultMutableTreeNode("Create Field"));
		node.add(new DefaultMutableTreeNode("Rename Field"));
		node.add(new DefaultMutableTreeNode("Delete Field"));
		node = new DefaultMutableTreeNode("Method");
		top.add(node);
		node.add(new DefaultMutableTreeNode("Create Method"));
		node.add(new DefaultMutableTreeNode("Rename Method"));
		node.add(new DefaultMutableTreeNode("Delete Method"));
		node = new DefaultMutableTreeNode("Parameter");
		top.add(node);
		node.add(new DefaultMutableTreeNode("Create Parameter"));
		node.add(new DefaultMutableTreeNode("Rename Parameter"));
		node.add(new DefaultMutableTreeNode("Delete Parameter"));
		return new JTree(top);
	}

	public static void showClass(Entity entity) {
		htmlBox newview = new htmlBox(entity);
		
		pane.add(newview.getBox(), 2);
		entitylabels.put(entity.getName(), newview.getBox());
		validateRepaint();
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
				JLabel L1 = entitylabels.get(l.getClassOne().getName());
				JLabel L2 = entitylabels.get(l.getClassTwo().getName());
				
				l.update(L1, L2);
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
		JLabel L1 = entitylabels.get(name1);
		JLabel L2 = entitylabels.get(name2);
		Line line = new Line(L1, L2, type);
		line.setBounds(0, 0, GUI.pane.getWidth(), GUI.pane.getHeight());

		GUI.relations.add(line);
		GUI.pane.add(line);
		GUI.pane.validate();
	}
	
	public static JLabel htmlBoxToEditBox(JLabel label) {
		pane.remove(label);
		
		editBox newview = new editBox(label);
		pane.add(newview.getBox(), 2);
		validateRepaint();
		return newview.getBox();
	}
	
	public static void exitEditingClass(JLabel inClass) {
		pane.remove(inClass);
		Entity e = Controller.getClasses().copyEntity(inClass.getName());
		showClass(e);
	}
	
}

package mike.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import mike.datastructures.*;
import mike.gui.Line;

public class GUI implements ViewInterface {
	// Global Variables
	static JLayeredPane pane = new JLayeredPane();
	static int x_pressed = 0;
	static int y_pressed = 0;
	public static HashMap<String, JLabel> entitylabels = new HashMap<String, JLabel>();
	static ArrayList<Line> relations = new ArrayList<Line>();
	static JFrame frame = new JFrame("Team mike UML Editor");
	public GUI() {
		// Creating the frame
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 800);

		// Creating the menu bar and its options
		JMenuBar menuBar = new JMenuBar();
		JButton save = new JButton("Save");
		JButton saveAs = new JButton("Save As");
		JButton load = new JButton("Load");
		menuBar.add(save);
		menuBar.add(saveAs);
		menuBar.add(load);

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

		pane.validate();
		pane.repaint();
		
		Controller.resizeListener(frame, relations);
		Controller.saveListener(save, frame);
		Controller.saveAsListener(saveAs, frame);
		Controller.loadListener(load, entitylabels, pane, frame);
		Controller.treeListener(tree, frame, entitylabels);
		Controller.exitListener(frame);
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

	public static JLabel showClass(Entity entity) {
		JLabel newview = new JLabel(entityToHTML(entity));
		newview.setBackground(Color.LIGHT_GRAY);
		newview.setOpaque(true);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 2);

		Border margin = new EmptyBorder(6, 6, 6, 6);
		newview.setBorder(new CompoundBorder(border, margin));

		pane.add(newview, 2);
		entitylabels.put(entity.getName(), newview);

		newview.setName(entity.getName());
		Controller.clickClass(newview);
		Controller.moveClass(newview, entity);
		
		newview.setBounds(0, 0, newview.getPreferredSize().width, newview.getPreferredSize().height);
		pane.validate();

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
		pane.validate();
		pane.repaint();
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
		pane.validate();
		pane.repaint();
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
	
	public static void updateClass(String oldname, Entity e) {
		JLabel classObj = entitylabels.remove(oldname);
		pane.remove(classObj);
		classObj.setText(entityToHTML(e));
		entitylabels.put(e.getName(), classObj);
		classObj.setBounds(classObj.getX(), classObj.getY(), classObj.getPreferredSize().width, classObj.getPreferredSize().height);
		classObj.setName(e.getName());
		pane.add(classObj, new Integer(2));
		pane.validate();
	}

	public static void deleteClass(String name) {
		pane.remove(entitylabels.get(name));
		entitylabels.remove(name);
		pane.repaint();
	}

	public static void createRelationship(Relationship.Type type, String name1, String name2)
	{
		JLabel L1 = entitylabels.get(name1);
		JLabel L2 = entitylabels.get(name2);
		GUIRelationship.drawRelationship(type, L1, L2);
	}
	
	public static String entityToHTML(Entity e) {
		String html = "<html>" + e.getName() + "<br/>";

		ArrayList<Field> fields = e.getFields();
		if (fields.size() > 0) {
			html += "<hr>Fields:<br/>";
			for (Field f : fields) {
				html += "&emsp " + f.getType() + " " + f.getName() + "<br/>";
			}
		}

		ArrayList<Method> methods = e.getMethods();
		if (methods.size() > 0) {
			html += "<hr>Methods:<br/>";

			for (Method m : methods) {
				ArrayList<Parameter> parameters = m.getParameters();
				html += "&emsp " + m.getType() + " " + m.getName() + "(";
				if (parameters.size() == 1) {
					html += parameters.get(0).getType() + " " + parameters.get(0).getName();
				}
				if (parameters.size() > 1) {
					html += parameters.get(0).getType() + " " + parameters.get(0).getName();
					for (int i = 1; i < parameters.size(); ++i) {
						html += ", " + parameters.get(i).getType() + " " + parameters.get(i).getName();
					}
				}
				html += ")<br/>";
			}
		}

		html += "</html>";

		return html;
	}
	
	public static HashMap<String, JLabel> getEntityLabels() {
		return entitylabels;
	}

	public static JLayeredPane getPane() {
		return pane;
	}
}

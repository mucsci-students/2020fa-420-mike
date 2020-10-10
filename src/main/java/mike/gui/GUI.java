package mike.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import mike.datastructures.Entity;
import mike.datastructures.Field;
import mike.datastructures.Method;
import mike.datastructures.Parameter;

public class GUI implements ViewInterface {
	// Global Variables
	public static JPanel centerPanel = new JPanel();
	static int x_pressed = 0;
	static int y_pressed = 0;
	static HashMap<String, JLabel> entityLabels = new HashMap<String, JLabel>();

	public GUI() {
		// Creating the frame
		JFrame frame = new JFrame("UML GUI Example");
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
		centerPanel.setBackground(Color.WHITE);

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		centerPanel.setLayout(gridbag);

		// Adding all panels onto frame
		frame.getContentPane().add(BorderLayout.CENTER, centerPanel);
		frame.getContentPane().add(BorderLayout.WEST, treeView);
		frame.getContentPane().add(BorderLayout.NORTH, menuBar);
		frame.setVisible(true);

		Controller.saveListener(save, frame);
		Controller.saveAsListener(saveAs, frame);
		Controller.loadListener(load, entityLabels, centerPanel, frame);
		Controller.treeListener(tree, frame, entityLabels, centerPanel);
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
		node = new DefaultMutableTreeNode("list");
		top.add(node);
		node.add(new DefaultMutableTreeNode("list all"));
		return new JTree(top);
	}

	public static JLabel showClass(Entity entity) {
		JLabel newview = new JLabel(entityToHTML(entity));
		newview.setBackground(Color.LIGHT_GRAY);
		newview.setOpaque(true);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 4);
		Border margin = new EmptyBorder(6, 6, 6, 6);
		newview.setBorder(new CompoundBorder(border, margin));

		centerPanel.add(newview);
		entityLabels.put(entity.getName(), newview);

		newview.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// catching the current values for x,y coordinates on screen
				if (e.getSource() == newview) {
					x_pressed = e.getX();
					y_pressed = e.getY();
				}
			}
		});

		newview.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (e.getSource() == newview) {
					JComponent jc = (JComponent) e.getSource();
					jc.setLocation(jc.getX() + e.getX() - x_pressed, jc.getY() + e.getY() - y_pressed);
					entity.setXLocation(jc.getX() + e.getX() - x_pressed);
					entity.setYLocation(jc.getY() + e.getY() - y_pressed);
				}
			}
		});

		centerPanel.validate();

		return newview;
	}

	public static void updateClass(String oldname, Entity e) {
		JLabel classObj = entityLabels.remove(oldname);
		centerPanel.remove(classObj);
		classObj.setText(entityToHTML(e));
		entityLabels.put(e.getName(), classObj);
		centerPanel.add(classObj);
		centerPanel.validate();
	}

	public static void deleteClass(String name) {
		centerPanel.remove(entityLabels.get(name));
		entityLabels.remove(name);
		centerPanel.repaint();
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
}

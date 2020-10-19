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
import javax.swing.JSeparator;
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
	static JFrame frame = new JFrame("UML GUI Example");
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

		pane.validate();
		pane.repaint();
		
		Controller.addClassListener(addClass ,frame);
		Controller.editModeListener(editMode, frame, pane);
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
		/*URL p = GUI.class.getResource("xmark.jpg");
		ImageIcon icon = new ImageIcon(new ImageIcon(p).getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT));
		
		//Entire label		
		JLabel newview = new JLabel();
		//BoxLayout lay = new BoxLayout(newview, BoxLayout.Y_AXIS);
		//newview.setLayout(lay);
		
		// Line 1
		JPanel line = new JPanel();
		line.setLayout(new BoxLayout(line, BoxLayout.X_AXIS));
		line.setBackground(Color.LIGHT_GRAY);
		JButton test = new JButton(icon);
		JLabel className = new JLabel("Class: ");
		JTextField test3 = new JTextField("this is text field");
		test.setAlignmentX(Component.LEFT_ALIGNMENT);
		className.setAlignmentX(Component.LEFT_ALIGNMENT);
		test3.setAlignmentX(Component.LEFT_ALIGNMENT);
		line.setAlignmentX(Component.LEFT_ALIGNMENT);
		line.add(test);
		line.add(className);
		line.add(test3);
		
		line.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		
		// Line 2
		JPanel line2 = new JPanel();
		line2.setLayout(new BoxLayout(line2, BoxLayout.X_AXIS));
		line2.setBackground(Color.LIGHT_GRAY);
		JButton test2 = new JButton("hi");
		line2.add(test2);
		line2.setAlignmentX(Component.LEFT_ALIGNMENT);
		line2.setBorder(BorderFactory.createEmptyBorder(5, 0, 3, 0));
		
		// Line 3
		JPanel line3 = new JPanel();
		line3.setLayout(new BoxLayout(line3, BoxLayout.X_AXIS));
		line3.setBackground(Color.LIGHT_GRAY);
		JButton test4 = new JButton("hi once again");
		JTextField test5 = new JTextField("this is another text field");
		line3.add(test4);
		line3.add(test5);
		line3.setAlignmentX(Component.LEFT_ALIGNMENT);
		line3.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
		
		// Design entire label
		newview.setBackground(Color.LIGHT_GRAY);
		newview.setOpaque(true);
		newview.setName("new name");
		Border border = BorderFactory.createLineBorder(Color.BLACK, 4);
		Border margin = new EmptyBorder(6, 6, 6, 6);
		newview.setBorder(new CompoundBorder(border, margin));

		// Add all lines into label
		newview.add(line);
		newview.add(new JSeparator(JSeparator.HORIZONTAL));
		newview.add(line2);
		newview.add(line3);
		
		newview.setBounds(0, 0, lay.preferredLayoutSize(newview).width, lay.preferredLayoutSize(newview).height);
        
		pane.add(newview, new Integer(2));
		entitylabels.put(entity.getName(), newview);
		
		newview.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				// catching the current values for x,y coordinates on screen
				if (e.getSource() == newview) {
					System.out.println("testing");
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
				repaintLine(entity.getName());
			}
		});
		
		
		//JLabel y = entityToEditingHTML(entity);
		pane.add(newview, new Integer(2));
		entitylabels.put(entity.getName(), y);
		pane.validate();
		
		return y;*/
		JLabel newview = new JLabel(entityToHTML(entity));
		newview.setBackground(Color.LIGHT_GRAY);
		newview.setOpaque(true);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 4);
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
				// (a,b) = L1 center
				double a = L1.getLocation().x + L1.getSize().width / 2;
				double b = L1.getLocation().y + L1.getSize().height / 2;
				
				// (c,d) = L2 center
				double c = L2.getLocation().x + L2.getSize().width / 2;
				double d = L2.getLocation().y + L2.getSize().height / 2;

				double[] centers1 = {a, b, c, d};
				Point p1 = GUIRelationship.getEdgeIntersectionPoint(L1.getSize(), L1.getLocation(), centers1);
				double[] centers2 = {c, d, a, b};
				Point p2 = GUIRelationship.getEdgeIntersectionPoint(L2.getSize(), L2.getLocation(), centers2);
				
				l.setNewPoints(p1.getX(), p1.getY(), p2.getX(), p2.getY());
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
	
	public static JLabel entityToEditingHTML(JLabel label) {
		pane.remove(label);
		Entity e = Controller.getClasses().copyEntity(label.getName());
		
		URL p = GUI.class.getResource("xmark.jpg");
		ImageIcon icon = new ImageIcon(new ImageIcon(p).getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT));
		
        JLabel newview = new JLabel();
        BoxLayout lay = new BoxLayout(newview, BoxLayout.Y_AXIS);
        newview.setLayout(lay);

        JPanel newEntity = new JPanel();
        newEntity.setLayout(new BoxLayout(newEntity, BoxLayout.X_AXIS));
        newEntity.setBackground(Color.LIGHT_GRAY);

        //create X button
        JButton xButton = new JButton(icon);
        //class name next to X button
        JTextField className = new JTextField(label.getName());

        //set alignment along left of JLabel
        xButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        className.setAlignmentX(Component.LEFT_ALIGNMENT);
        //add button and class name then left align
        newEntity.add(xButton);
        newEntity.add(className);
        newEntity.setAlignmentX(Component.LEFT_ALIGNMENT);

        newview.add(newEntity);
		
		//Entire label		
		/*JLabel newview = new JLabel();
		BoxLayout lay = new BoxLayout(newview, BoxLayout.Y_AXIS);
		newview.setLayout(lay);
		
		// Line 1
		JPanel line = new JPanel();
		line.setLayout(new BoxLayout(line, BoxLayout.X_AXIS));
		line.setBackground(Color.LIGHT_GRAY);
		JButton test = new JButton(icon);
		JLabel className = new JLabel("Class: ");
		JTextField test3 = new JTextField("this is text field");
		test.setAlignmentX(Component.LEFT_ALIGNMENT);
		className.setAlignmentX(Component.LEFT_ALIGNMENT);
		test3.setAlignmentX(Component.LEFT_ALIGNMENT);
		line.add(test);
		line.add(className);
		line.add(test3);
		line.setAlignmentX(Component.LEFT_ALIGNMENT);
		line.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		
		// Line 2
		JPanel line2 = new JPanel();
		line2.setLayout(new BoxLayout(line2, BoxLayout.X_AXIS));
		line2.setBackground(Color.LIGHT_GRAY);
		JButton test2 = new JButton("hi");
		line2.add(test2);
		line2.setAlignmentX(Component.LEFT_ALIGNMENT);
		line2.setBorder(BorderFactory.createEmptyBorder(5, 0, 3, 0));
		
		// Line 3
		JPanel line3 = new JPanel();
		line3.setLayout(new BoxLayout(line3, BoxLayout.X_AXIS));
		line3.setBackground(Color.LIGHT_GRAY);
		JButton test4 = new JButton("hi once again");
		JTextField test5 = new JTextField("this is another text field");
		line3.add(test4);
		line3.add(test5);
		line3.setAlignmentX(Component.LEFT_ALIGNMENT);
		line3.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
		*/
		// Design entire label
		newview.setBackground(Color.LIGHT_GRAY);
		newview.setOpaque(true);
		newview.setName("new name");
		Border border = BorderFactory.createLineBorder(Color.BLACK, 4);
		Border margin = new EmptyBorder(6, 6, 6, 6);
		newview.setBorder(new CompoundBorder(border, margin));

		// Add all lines into label
		newview.add(newEntity);
		//newview.add(new JSeparator(JSeparator.HORIZONTAL));
		//newview.add(line2);
		//newview.add(line3);
		
		newview.setBounds(0, 0, lay.preferredLayoutSize(newview).width, lay.preferredLayoutSize(newview).height);
		newview.setLocation(e.getXLocation(), e.getYLocation());
		newview.setName(e.getName());

		pane.add(newview, 2);
		pane.validate();
		pane.repaint();
		return newview;
		
	}
	
	public static HashMap<String, JLabel> getEntityLabels() {
		return entitylabels;
	}

	public static JLayeredPane getPane() {
		return pane;
	}
	
	public static void exitEditingClass(JLabel inClass) {
		pane.remove(inClass);
		Entity e = Controller.getClasses().copyEntity(inClass.getName());
		
		JLabel newview = new JLabel(entityToHTML(e));
		newview.setBackground(Color.LIGHT_GRAY);
		newview.setOpaque(true);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 4);
		Border margin = new EmptyBorder(6, 6, 6, 6);
		newview.setBorder(new CompoundBorder(border, margin));

		Controller.clickClass(newview);
		Controller.moveClass(newview, e);
		
		newview.setBounds(0, 0, newview.getPreferredSize().width, newview.getPreferredSize().height);
		newview.setLocation(e.getXLocation(), e.getYLocation());
		newview.setName(e.getName());
		
		pane.add(newview);
		pane.revalidate();
		pane.repaint();
		
	}
	
}
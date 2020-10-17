package mike.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
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
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
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
	static HashMap<String, JLabel> entitylabels = new HashMap<String, JLabel>();
	static ArrayList<Line> relations = new ArrayList<Line>();

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
		
		pane.validate();
		pane.repaint();

		//set listeners
		Controller.addClassListener(addClass ,frame);
		Controller.editModeListener(editMode, frame, pane);
		Controller.saveListener(save, frame);
		Controller.saveAsListener(saveAs, frame);
		Controller.loadListener(load, entitylabels, pane, frame);
		Controller.exitListener(frame);
	}

	public static JLayeredPane showClass(Entity entity) {
		URL p = GUI.class.getResource("xmark.jpg");
		
		//Entire label		
		JLabel newview = new JLabel();
		BoxLayout lay = new BoxLayout(newview, BoxLayout.Y_AXIS);
		newview.setLayout(lay);
		
		// Line 1
		JPanel line = new JPanel();
		line.setLayout(new BoxLayout(line, BoxLayout.X_AXIS));
		line.setBackground(Color.LIGHT_GRAY);
		JButton test = new JButton("hi");
		JTextField test3 = new JTextField("this is text field");
		test.setAlignmentX(Component.LEFT_ALIGNMENT);
		test3.setAlignmentX(Component.LEFT_ALIGNMENT);
		line.add(test);
		line.add(test3);
		line.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// Line 2
		JPanel line2 = new JPanel();
		line2.setLayout(new BoxLayout(line2, BoxLayout.X_AXIS));
		JButton test2 = new JButton("hi");
		line2.add(test2);
		line2.setAlignmentX(Component.LEFT_ALIGNMENT);
		// Line 3
		JPanel line3 = new JPanel();
		line3.setLayout(new BoxLayout(line3, BoxLayout.X_AXIS));
		line3.setBackground(Color.LIGHT_GRAY);
		JButton test4 = new JButton("hi once again");
		JTextField test5 = new JTextField("this is another text field");
		line3.add(test4);
		line3.add(test5);
		line3.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// Design entire label
		newview.setBackground(Color.LIGHT_GRAY);
		newview.setOpaque(true);
		newview.setName("new name");
		Border border = BorderFactory.createLineBorder(Color.BLACK, 4);
		Border margin = new EmptyBorder(6, 6, 6, 6);
		newview.setBorder(new CompoundBorder(border, margin));

		// Add all lines into label
		newview.add(line);
		
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
		
		pane.validate();
		
		return pane;
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
	}
	
	public static void repaintLine(String name) {
		for(Line l : relations) {
			if(l.getClassOne().getName().equals(name) || l.getClassTwo().getName().equals(name)) {
				JLabel L1 = entitylabels.get(l.getClassOne().getName());
				JLabel L2 = entitylabels.get(l.getClassTwo().getName());
				Point p1 = GUIRelationship.getEdgeIntersectionPoint(L1, L2);
				Point p2 = GUIRelationship.getEdgeIntersectionPoint(L2, L1);
				l.setx1(p1.getX());
				l.sety1(p1.getY());
				l.setx2(p2.getX());
				l.sety2(p2.getY());
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
		pane.add(classObj, 2);
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
		Point p1 = GUIRelationship.getEdgeIntersectionPoint(L1, L2);
		Point p2 = GUIRelationship.getEdgeIntersectionPoint(L2, L1);
		
		Line line = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY(), L1, L2);

		line.setBounds(0, 0, pane.getWidth(), pane.getHeight());

		relations.add(line);
		pane.add(line, 1);
		pane.validate();
		pane.repaint();

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

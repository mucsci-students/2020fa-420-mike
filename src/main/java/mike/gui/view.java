package GUI;

//Usually you will require both swing and awt packages
//even if you are working with just swings.
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cli.HelperMethods;
import datastructures.*;

public class view extends HelperMethods {
	public static JPanel centerPanel = new JPanel();
	static int x_pressed = 0;
	static int y_pressed = 0;
	static HashMap<String, JLabel> entityLabels = new HashMap<String, JLabel>();
	
	
	public static void guiInterface() {
		Classes classes = new Classes();

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

		Controller controller = new Controller(classes);
		controller.saveListener(save);
		controller.saveAsListener(saveAs);
		controller.loadListener(load);
		controller.treeListener(tree, frame, entityLabels, centerPanel);
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

	public static JLabel showClass(Entity e) { 		
 		String html = "<html>";
 		html += e.getName() + "<br/>";
 			
 		ArrayList<Field> fields = e.getFields();
	 	if (fields.size() > 0)
	 		{
	 		html += "<hr>Fields:<br/>";
	 		for (Field f : fields)
	 		{
	 			html += "&emsp " + f.getType() + " " + f.getName() + "<br/>"; 
	 		}
 		}
	 	
	 	ArrayList<Method> methods = e.getMethods(); 
	 	if (methods.size() > 0) {
	 		html += "<hr>Methods:<br/>";
	 		
	 		for (Method m : methods)
	 		{
	 			html += "&emsp " + m.getType() + " " + m.getName() + "()<br/>";
	 		}
	 	}
 		
 		html += "</html>";
 		
		JLabel newview = new JLabel(html);
        newview.setBackground(Color.LIGHT_GRAY);
        newview.setOpaque(true);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 4);
        Border margin = new EmptyBorder(6, 6, 6, 6);
        newview.setBorder(new CompoundBorder(border, margin));
        
        centerPanel.add(newview);
        entityLabels.put(e.getName(), newview);
        
		newview.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e) {
                //catching the current values for x,y coordinates on screen
                if (e.getSource() == newview) {
                    x_pressed = e.getX();
                    y_pressed = e.getY();
                }
            }
        });
        
        newview.addMouseMotionListener(new MouseMotionAdapter(){
            @Override
            public void mouseDragged(MouseEvent e){
                if (e.getSource() == newview) 
                {
                    JComponent jc = (JComponent)e.getSource();
                    jc.setLocation(jc.getX()+e.getX()-x_pressed, jc.getY()+e.getY()-y_pressed);
                }
            }
        });

        centerPanel.validate();
        centerPanel.repaint();
        
        return newview;
 	}
}

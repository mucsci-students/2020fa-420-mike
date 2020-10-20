package mike.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import mike.datastructures.Entity;

public class editBox {
	
	private JLabel newBox;
	
	editBox (JLabel label) {
		Entity e = Controller.getClasses().copyEntity(label.getName());
		
		// Get deletion image
		URL p = GUI.class.getResource("xmark.jpg");
		ImageIcon icon = new ImageIcon(new ImageIcon(p).getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT));
		
		// Create entire editBox
        this.newBox = new JLabel();
        BoxLayout lay = new BoxLayout(this.newBox, BoxLayout.Y_AXIS);
        this.newBox.setLayout(lay);
        
        //Create Class Panel
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

        this.newBox.add(newEntity);
		
		// Design entire label
        this.newBox.setBackground(Color.LIGHT_GRAY);
        this.newBox.setOpaque(true);
        this.newBox.setName("new name");
		Border border = BorderFactory.createLineBorder(Color.BLACK, 4);
		Border margin = new EmptyBorder(6, 6, 6, 6);
		this.newBox.setBorder(new CompoundBorder(border, margin));

		// Add all lines into label
		this.newBox.add(newEntity);
		
		this.newBox.setBounds(0, 0, lay.preferredLayoutSize(this.newBox).width, lay.preferredLayoutSize(this.newBox).height);
		this.newBox.setLocation(e.getXLocation(), e.getYLocation());
		this.newBox.setName(e.getName());

	}
	
	public JLabel getBox() {
		return newBox;
	}
}
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

//newview.add(new JSeparator(JSeparator.HORIZONTAL));
*/

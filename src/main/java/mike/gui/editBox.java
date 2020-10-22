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

import mike.controller.Controller;
import mike.datastructures.Entity;
import mike.view.GUIView;

public class editBox {
	
	private JLabel newBox;
	
	public editBox (JLabel label) {
		Entity e = Controller.getModel().copyEntity(label.getName());
		
		// Get deletion image
		URL p = GUIView.class.getResource("..\\gui\\resources\\xmark.jpg");
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

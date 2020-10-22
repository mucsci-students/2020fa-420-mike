package mike.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

import mike.datastructures.Entity;
import mike.view.GUIView;

public class ClassController {

	private static Boolean editMode = false;
	private static JLabel inClass = null;
	private static int x_pressed = 0;
	private static int y_pressed = 0;

	protected static void clickClass(JLabel newview) {
		newview.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(editMode) {
					if(inClass != null){
						GUIView.exitEditingClass(inClass);
					}
					inClass = GUIView.htmlBoxToEditBox(newview);
				}// catching the current values for x,y coordinates on screen
				else if (e.getSource() == newview) {
					//if in edit mode, show text boxes and such for fields/methods/parameter
					x_pressed = e.getX();
					y_pressed = e.getY();
				}
			}
		});
	}
		
	protected static void moveClass(JLabel newview, Entity entity) {
		newview.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				//allow dragging classes if not in edit mode
				if(!editMode) {
					if (e.getSource() == newview) {
						JComponent jc = (JComponent) e.getSource();
						jc.setLocation(jc.getX() + e.getX() - x_pressed, jc.getY() + e.getY() - y_pressed);
						entity.setXLocation(jc.getX() + e.getX() - x_pressed);
						entity.setYLocation(jc.getY() + e.getY() - y_pressed);
					}
					GUIView.repaintLine(entity.getName());
				}
				//if in edit mode, drag from one class to another to create relationship
				else {
				}
			}
		});
	}

	protected static void editModeListener(JButton editButton) {
		editButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//if in edit mode
				if(editMode) {
					editMode = false;
					//Change button to signify we are out of edit mode
					editButton.setText("Enable Edit Mode");
					editButton.setBackground(null);
					if(inClass != null) {
						GUIView.exitEditingClass(inClass);
					}
					inClass = null;
				}
				//if not in edit mode
				else {
					editMode = true;
					//change button to signify we are in edit mode
					editButton.setText("Disable Edit Mode");
					editButton.setBackground(Color.RED);
				}
			}
		});
	}
	
}

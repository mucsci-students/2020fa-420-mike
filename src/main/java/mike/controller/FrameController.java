package mike.controller;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mike.datastructures.Entity;
import mike.datastructures.Relationship;
import mike.gui.Line;
import mike.view.GUIView;
import mike.view.ViewTemplate;

public class FrameController {
	
	protected static void exitListener(ViewTemplate view, Controller control) throws HeadlessException {	
	    ((GUIView) view).getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    ((GUIView) view).getFrame().addWindowListener(new WindowAdapter()
	    {
	        //@Override
	        public void windowClosing(WindowEvent e)
	        {	
	        	if(control.getChanged()){
	        		int n = JOptionPane.showConfirmDialog(
	        			((GUIView) view).getFrame(),
					    "You have unsaved changes.  Do you still want to exit?",
					    "Exit",
					    JOptionPane.YES_NO_OPTION);
					// Yes, exit
					if(n == 0) {
						e.getWindow().dispose();
					}	 
	        	} else {
	        		e.getWindow().dispose();
	        	}
				           
	        }
	    });
	}
	
	protected static void addClassListener(JButton addClass, Controller control) {
		addClass.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JTextField name = new JTextField(20);

				// Create a panel containing a drop-down box and text field
				JPanel inputFields = new JPanel();
				inputFields.add(new JLabel("Enter a Class name: "));
				inputFields.add(name);
				
				// Ask for input with inputFields
				int result = JOptionPane.showConfirmDialog(null, inputFields, "Create Class", JOptionPane.OK_CANCEL_OPTION);
				
				if(name.getText().contains(" ")){
					JOptionPane.showMessageDialog( ((GUIView) control.getView()).getFrame(), "Spaces are not allowed");
					return;
				}
				
				if (result == 0) {
					if(control.getModel().createClass(name.getText())){
					    ((GUIView) control.getView()).showClass(control.getModel().copyEntity(name.getText()), control);
					} else {
						JOptionPane.showMessageDialog( ((GUIView) control.getView()).getFrame(), "An entity with that name already exists.");
					}
				}
				
				ArrayList<Entity> entities = control.getModel().getEntities();

				//prevent classes from gathering in middle after class is added
				for(Entity curEntity : entities) {
					JLabel curLabel =  ((GUIView) control.getView()).getEntityLabels().get(curEntity.getName());
					curLabel.setLocation(curEntity.getXLocation(), curEntity.getYLocation());
				}
				for(Relationship r : control.getModel().getRelationships())
				{
				    ((GUIView) control.getView()).createRelationship(r.getName(), r.getFirstClass(), r.getSecondClass(), control.getModel());
				}
				control.setChanged(true);
			}
		});
	}
	
	protected static void resizeListener(ViewTemplate view)
	{
	    ((GUIView) view).getFrame().addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent componentEvent) {
		    	for (Line l : ((GUIView) view).getRelations())
		    	{
		    		l.setBounds(0, 0, ((GUIView) view).getPane().getWidth(), ((GUIView) view).getPane().getHeight());
		    	}
		    }
		});
	}
}

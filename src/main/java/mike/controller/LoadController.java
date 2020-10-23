package mike.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mike.HelperMethods;
import mike.datastructures.Entity;
import mike.view.GUIView;

public class LoadController {

	// Listen to any function calls
	protected static void loadListener(JButton load) {
		load.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  try {
				  if(Controller.getChanged() == true) {
					  int n = JOptionPane.showConfirmDialog(
							  GUIView.getFrame(),
							    "You have unsaved changes.  Do you still want to load a new file?",
							    "Exit",
							    JOptionPane.YES_NO_OPTION);
						// No, go back
						if(n == 1) {
							return;
						}	
				  }
				  
				  JTextField directory = new JTextField(40);
				  
				  // Create a panel containing a drop-down box and text field
				  JPanel inputFields = new JPanel();
				  inputFields.add(new JLabel("Enter a Directory: "));
				  inputFields.add(directory);
				  
				  // Ask for input with inputFields
				  int result = JOptionPane.showConfirmDialog(null, inputFields, "Load", JOptionPane.OK_CANCEL_OPTION);

				  if (result == 0) {
					  JLayeredPane pane = GUIView.getPane();
					  Controller.getModel().empty();
					  pane.removeAll();
					  pane.repaint();
					  
					  File file = new File(directory.getText());
					  if(file.isAbsolute()){
						  Controller.setPath(Paths.get(directory.getText()));
					  }
					  else {
						  Controller.setPath(Paths.get(System.getProperty("user.dir") + "\\" + directory.getText()));  
					  }
					  
					  HelperMethods.load(Controller.getPath(), Controller.getModel());
					  Controller.setChanged(false);
					  pane.validate();
				  }
				 
				  
			  }  catch (Exception e1) {
				  e1.printStackTrace();
			  }
			  for(Entity curEntity : Controller.getModel().getEntities()) {
					JLabel curLabel = GUIView.getEntityLabels().get(curEntity.getName());
					curLabel.setLocation(curEntity.getXLocation(), curEntity.getYLocation());				
			  }
		  }
		});
	}

}

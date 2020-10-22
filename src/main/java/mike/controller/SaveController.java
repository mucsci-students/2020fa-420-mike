package mike.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mike.HelperMethods;
import mike.view.GUIView;

public class SaveController {

	// Listen to any function calls
	protected static void saveListener(JButton save) {
		save.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  try {
				  if(Controller.getPath() == null){
					  saveWithInput();
				  }
				  else{
					  HelperMethods.save(Controller.getPath(), Controller.getModel());
					  Controller.setChanged(false);
				  }
			  }  catch (IOException e1) {
				  e1.printStackTrace();
			  }
		  }
		});
	}
	
	protected static void saveAsListener(JButton saveAs) {
		saveAs.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  saveWithInput();
		  }
		});
	}
	
	private static void saveWithInput() {
		try {	
		  JTextField fileName = new JTextField(20);
		  JTextField directory = new JTextField(40);
		  
		  // Create a panel containing a drop-down box and text field
		  JPanel inputFields = new JPanel();
	 	  inputFields.add(new JLabel("Enter a File name: "));
		  inputFields.add(fileName);
		  inputFields.add(new JLabel("Enter a Directory (optional): "));
		  inputFields.add(directory);
		  
		  // Ask for input with inputFields
		  int result = JOptionPane.showConfirmDialog(null, inputFields, "Save As", JOptionPane.OK_CANCEL_OPTION);
		  if (result == 0) {
			  File file = new File(directory.getText() + "\\" + fileName.getText());
			  if(file.isDirectory() && file.isAbsolute()){
				  Controller.setPath(Paths.get(file.toString()));
			  } else {
				  Controller.setPath(Paths.get(System.getProperty("user.dir") + directory.getText() + "\\" + fileName.getText()));
				  file = new File(Controller.getPath().getParent().toString());
				  if(!file.isDirectory()){  
					  JOptionPane.showMessageDialog(GUIView.getFrame(), "Directory does not exist.  File saved to uml directory.");
				  }
			  }
			      
			  HelperMethods.save(Controller.getPath(), Controller.getModel());
			  Controller.setChanged(false);
		  }
		 
	  }  catch (IOException e1) {
		  e1.printStackTrace();
	  }
  }
  
}

package mike.controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mike.datastructures.Entity;
import mike.datastructures.Method;
import mike.gui.editBox;
import mike.view.GUIView;

public class CreateDeleteController {
	
	protected static void createField(JPanel panel, Controller control) {
		JButton creation = (JButton) panel.getComponent(2);
		creation.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  JTextField type = (JTextField) panel.getComponent(0);
			  JTextField name = (JTextField) panel.getComponent(1);
			  if(type.getText().isEmpty() || name.getText().isEmpty()) {
				  return;
			  }
			  Entity entity = editBox.getEntity();
			  //control.getModel().createField(entity.getName(),  name.getText(),  type.getText());
			  JLabel newview = editBox.getBox();
			  
			  int spot = entity.getFields().size() + 2;
			  control.deleteField(editBox.editSection(type.getText(), name.getText(), false, spot));
			  Dimension dim = newview.getLayout().preferredLayoutSize(newview);
			  newview.setBounds(entity.getXLocation(), entity.getYLocation(), dim.width, dim.height);
			  type.setText("");
			  name.setText("");
			  ((GUIView) control.getView()).validateRepaint();
		  }
		});
	}
	
	protected static void createMethod(JPanel panel, Controller control) {
		JButton creation = (JButton) panel.getComponent(2);
		creation.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  JTextField type = (JTextField) panel.getComponent(0);
			  JTextField name = (JTextField) panel.getComponent(1);
			  if(type.getText().isEmpty() || name.getText().isEmpty()) {
				  return;
			  }
			  Entity entity = editBox.getEntity();
			  //control.getModel().createMethod(entity.getName(),  name.getText(),  type.getText());
			  JLabel newview = editBox.getBox();
			  
			  int spot = newview.getComponentCount() - 1;
			  control.deleteMethod(editBox.editSection(type.getText(), name.getText(), false, spot));
			  control.createParam(editBox.newSection(true, spot + 1), name.getText());
			  Dimension dim = newview.getLayout().preferredLayoutSize(newview);
			  newview.setBounds(entity.getXLocation(), entity.getYLocation(), dim.width, dim.height);
			  type.setText("");
			  name.setText("");
			  ((GUIView) control.getView()).validateRepaint();
		  }
		});
	}
	
	protected static void createParam(JPanel panel, String methodName, Controller control) {
		JButton creation = (JButton) panel.getComponent(3);
		creation.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  JTextField type = (JTextField) panel.getComponent(1);
			  JTextField name = (JTextField) panel.getComponent(2);
			  if(type.getText().isEmpty() || name.getText().isEmpty()) {
				  return;
			  }
			  Entity entity = editBox.getEntity();
			  control.getModel().createParameter(entity.getName(), methodName, name.getText(),  type.getText());
			  JLabel newview = editBox.getBox();
			  
			  int spot = entity.getFields().size() + 4;
			  for(Method m : entity.getMethods()) {
				  spot += m.getParameters().size() + 2;
				  if(m.getName() == methodName) {
					  break;
				  }
			  }
			  --spot;
			 
			  control.deleteParam(editBox.editSection(type.getText(), name.getText(), true, spot), methodName);
			 
			  Dimension dim = newview.getLayout().preferredLayoutSize(newview);
			  newview.setBounds(entity.getXLocation(), entity.getYLocation(), dim.width, dim.height);
			  type.setText("");
			  name.setText("");
			  ((GUIView) control.getView()).validateRepaint();
		  }
		});
	}
	
	protected static void deleteField(JPanel panel, Controller control) {
		JButton deletion = (JButton) panel.getComponent(0);
		deletion.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  Entity entity = editBox.getEntity();
			  control.getModel().deleteField(entity.getName(), ((JTextField) panel.getComponent(2)).getText());
			  JLabel newview = editBox.getBox();
			  newview.remove(deletion.getParent());
			  Dimension dim = newview.getLayout().preferredLayoutSize(newview);
			  newview.setBounds(entity.getXLocation(), entity.getYLocation(), dim.width, dim.height);
			  ((GUIView) control.getView()).validateRepaint();
		  }
		});
	}
	
	protected static void deleteMethod(JPanel panel, Controller control) {
		JButton deletion = (JButton) panel.getComponent(0);
		deletion.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  Entity entity = editBox.getEntity();
			  for(Method m : entity.getMethods()){
				  String methodName = ((JTextField) panel.getComponent(2)).getText();
				  if(m.getName().equals(methodName)) {
					  JLabel newview = editBox.getBox();
					  int methodSpot = newview.getComponentZOrder(panel);
					  int numParams = m.getParameters().size();
					  for(int x = 0; x < numParams + 2; ++x){
						  newview.remove(methodSpot);
					  }
					  control.getModel().deleteMethod(entity.getName(), methodName);
					  Dimension dim = newview.getLayout().preferredLayoutSize(newview);
					  newview.setBounds(entity.getXLocation(), entity.getYLocation(), dim.width, dim.height);
					  ((GUIView) control.getView()).validateRepaint();
					  return;
				  }
			  }
		  }
		});
	}
	
	protected static void deleteParam(JPanel panel, String methodName, Controller control) {
		JButton deletion = (JButton) panel.getComponent(1);
		deletion.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  Entity entity = editBox.getEntity();
			  control.getModel().deleteParameter(entity.getName(), methodName, ((JTextField) panel.getComponent(3)).getText());
			  JLabel newview = editBox.getBox();
			  newview.remove(deletion.getParent());
			  Dimension dim = newview.getLayout().preferredLayoutSize(newview);
			  newview.setBounds(entity.getXLocation(), entity.getYLocation(), dim.width, dim.height);
			  ((GUIView) control.getView()).validateRepaint();
		  }
		});
	}
	
}

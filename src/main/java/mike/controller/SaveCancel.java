package mike.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mike.datastructures.Entity;
import mike.datastructures.Field;
import mike.datastructures.Method;
import mike.datastructures.Parameter;
import mike.datastructures.Relationship;
import mike.gui.Line;
import mike.gui.editBox;
import mike.view.GUIView;

public class SaveCancel {
	
	protected static void saveClass(JButton saveButton, Controller control) {
		saveButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  JButton editModeButton = (JButton)  ((GUIView) control.getView()).getMenuBar().getComponent(4);
			  JButton addClassButton = (JButton)  ((GUIView) control.getView()).getMenuBar().getComponent(3);
			  editModeButton.setBackground(Color.RED);
			  editModeButton.setEnabled(true);
			  addClassButton.setEnabled(true);
			  Entity entity = editBox.getEntity();
			  JLabel newBox = editBox.getBox();
			  JPanel panel = (JPanel) newBox.getComponent(1);
			  JTextField text = (JTextField) panel.getComponent(1);
			  control.getModel().renameClass(entity.getName(), text.getText());
			  
			  for (int x = 3; x < entity.getFields().size()+3; ++x){
				  JPanel panelField = (JPanel) newBox.getComponent(x);
				  JTextField textField = (JTextField) panelField.getComponent(2);
				  
				  control.getModel().renameField(entity.getName(), entity.getFields().get(x-3).getName(), textField.getText());
			  }
			  
			  int methodNum = -1;
			  Method m;
			  for (int x = entity.getFields().size()+5; x < newBox.getComponentCount()-1; x+=m.getParameters().size()+2){
				  JPanel panelMethod = (JPanel) newBox.getComponent(x);
				  JTextField textMethod = (JTextField) panelMethod.getComponent(2);
				  ++methodNum;
				  m = entity.getMethods().get(methodNum);
				  control.getModel().renameMethod(entity.getName(), m.getName(), textMethod.getText());
				  
				  for (int y = x+1; y < m.getParameters().size()+x+1; ++y){
					  JPanel panelParam = (JPanel) newBox.getComponent(y);
					  JTextField textParam = (JTextField) panelParam.getComponent(3);
					  
					  control.getModel().renameParameter(entity.getName(), m.getName(), m.getParameters().get(y-x-1).getName(), textParam.getText()); 
				  } 
			  }
			  
			  control.getinClass().setName(text.getText());
			  ((GUIView) control.getView()).exitEditingClass(control.getinClass(), control, control.getModel());
			  control.setinClass(null);
			  editBox.setBox(null);
			  
			  ((GUIView) control.getView()).getMenuBar().remove(6);
			  ((GUIView) control.getView()).getMenuBar().remove(5);
			  ((GUIView) control.getView()).getFrame().validate();
			  ((GUIView) control.getView()).getFrame().repaint();
		  }
		});
	}
	
	protected static void cancelClass(JButton cancelButton, Controller control) {
		cancelButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  JButton editModeButton = (JButton)  ((GUIView) control.getView()).getMenuBar().getComponent(4);
			  JButton addClassButton = (JButton)  ((GUIView) control.getView()).getMenuBar().getComponent(3);
			  editModeButton.setBackground(Color.RED);
			  editModeButton.setEnabled(true);
			  addClassButton.setEnabled(true);
			  
			  
			  
			  control.getModel().clear();
			  int x = 0;
			  for(Entity entity : editBox.getBackup()){
			      control.getModel().createClass(entity.getName());
			      control.getModel().getEntities().get(x).setXLocation(entity.getXLocation());
			      control.getModel().getEntities().get(x).setYLocation(entity.getYLocation());
				  for(Field field : entity.getFields()){
				      control.getModel().createField(entity.getName(), field.getName(), field.getType());
				  }
				  for(Method method : entity.getMethods()){
				      control.getModel().createMethod(entity.getName(), method.getName(), method.getType());
					  for(Parameter param : method.getParameters()){
					      control.getModel().createParameter(entity.getName(), method.getName(), param.getName(), param.getType());
					  }
				  }
				  ++x;
			  }
			  
			  for(Line relation :  ((GUIView) control.getView()).getRelations()){
			      ((GUIView) control.getView()).getPane().remove(relation);
			  }
			  
			  ((GUIView) control.getView()).getRelations().clear();

			  for(Relationship relation : editBox.getBackupRel()){
			      ((GUIView) control.getView()).createRelationship(relation.getName(), relation.getFirstClass(), relation.getSecondClass(), control.getModel());
			  }
			  
			  ((GUIView) control.getView()).exitEditingClass(control.getinClass(), control, control.getModel());
			  control.setinClass(null);
			  editBox.setBox(null);
			  ((GUIView) control.getView()).getMenuBar().remove(6);
			  ((GUIView) control.getView()).getMenuBar().remove(5);
			  ((GUIView) control.getView()).getMenuBar().validate();
			  ((GUIView) control.getView()).getMenuBar().repaint();
		  }
		});
	}
	
	protected static void deleteEntity(JButton deletion, Controller control) {
		deletion.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  int n = JOptionPane.showConfirmDialog(
				  ((GUIView) control.getView()).getFrame(),
				    "Are you sure you want to delete this class?",
				    "Delete Class",
				    JOptionPane.YES_NO_OPTION);
			  if(n == 0) {
				  
			      ((GUIView) control.getView()).deleteLines(editBox.getEntity().getName());
				  control.getModel().deleteClass(editBox.getEntity().getName());
				  
				  ((GUIView) control.getView()).getPane().remove(control.getinClass());
				  JButton editModeButton = (JButton)  ((GUIView) control.getView()).getMenuBar().getComponent(4);
				  JButton addClassButton = (JButton)  ((GUIView) control.getView()).getMenuBar().getComponent(3);
				  editModeButton.setBackground(Color.RED);
				  editModeButton.setEnabled(true);
				  addClassButton.setEnabled(true);
				  
				  ((GUIView) control.getView()).getMenuBar().remove(6);
				  ((GUIView) control.getView()).getMenuBar().remove(5);
				  ((GUIView) control.getView()).getMenuBar().validate();
				  ((GUIView) control.getView()).getMenuBar().repaint();
				  control.setinClass(null);
				  editBox.setBox(null);
				  ((GUIView) control.getView()).validateRepaint();
				}	 
		  }
		});
	}
	
}

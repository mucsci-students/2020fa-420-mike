package mike.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
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
	
	protected static void saveClass(JButton saveButton) {
		saveButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  Entity entity = editBox.getEntity();
			  JLabel newBox = editBox.getBox();
			  JPanel panel = (JPanel) newBox.getComponent(1);
			  JTextField text = (JTextField) panel.getComponent(1);
			  
			  Controller.getModel().renameClass(entity.getName(), text.getText());
			  
			  for (int x = 3; x < entity.getFields().size()+3; ++x){
				  JPanel panelField = (JPanel) newBox.getComponent(x);
				  JTextField textField = (JTextField) panelField.getComponent(2);
				  
				  Controller.getModel().renameField(entity.getName(), entity.getFields().get(x-3).getName(), textField.getText());
			  }
			  
			  int methodNum = -1;
			  Method m;
			  for (int x = entity.getFields().size()+5; x < newBox.getComponentCount()-1; x+=m.getParameters().size()+2){
				  JPanel panelMethod = (JPanel) newBox.getComponent(x);
				  JTextField textMethod = (JTextField) panelMethod.getComponent(2);
				  ++methodNum;
				  m = entity.getMethods().get(methodNum);
				  Controller.getModel().renameMethod(entity.getName(), m.getName(), textMethod.getText());
				  
				  for (int y = x+1; y < m.getParameters().size()+x+1; ++y){
					  JPanel panelParam = (JPanel) newBox.getComponent(y);
					  JTextField textParam = (JTextField) panelParam.getComponent(3);
					  
					  Controller.getModel().renameParameter(entity.getName(), m.getName(), m.getParameters().get(y-x-1).getName(), textParam.getText()); 
				  } 
			  }
			  
			  Controller.getinClass().setName(text.getText());
			  GUIView.exitEditingClass(Controller.getinClass());
			  Controller.setinClass(null);
			  GUIView.getMenuBar().remove(6);
			  GUIView.getMenuBar().remove(5);
			  GUIView.getFrame().validate();
			  GUIView.getFrame().repaint();
		  }
		});
	}
	
	protected static void cancelClass(JButton cancelButton) {
		cancelButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  Controller.getModel().clear();
			  int x = 0;
			  for(Entity entity : editBox.getBackup()){
				  Controller.getModel().createClass(entity.getName());
				  Controller.getModel().getEntities().get(x).setXLocation(entity.getXLocation());
				  Controller.getModel().getEntities().get(x).setYLocation(entity.getYLocation());
				  for(Field field : entity.getFields()){
					  Controller.getModel().createField(entity.getName(), field.getName(), field.getType());
				  }
				  for(Method method : entity.getMethods()){
					  Controller.getModel().createMethod(entity.getName(), method.getName(), method.getType());
					  for(Parameter param : method.getParameters()){
						  Controller.getModel().createParameter(entity.getName(), method.getName(), param.getName(), param.getType());
					  }
				  }
				  ++x;
			  }
			  
			  for(Line relation : GUIView.getRelations()){
				 if((relation.getClassOne().getName()).equals(editBox.getBox().getName())){
					 GUIView.getPane().remove(relation);
				 }
			  }
			  GUIView.getRelations().clear();
			  for(Relationship relation : editBox.getBackupRel()){
				  GUIView.createRelationship(relation.getName(), relation.getFirstClass(), relation.getSecondClass());
				  Controller.getModel().createRelationship(relation.getName(), relation.getFirstClass(), relation.getSecondClass());
			  }
			  GUIView.exitEditingClass(Controller.getinClass());
			  Controller.setinClass(null);

			  GUIView.getMenuBar().remove(6);
			  GUIView.getMenuBar().remove(5);
			  GUIView.getFrame().validate();
			  GUIView.getFrame().repaint();
			  GUIView.validateRepaint();
		  }
		});
	}
	
	protected static void deleteEntity(JButton deletion) {
		deletion.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  Controller.getModel().deleteClass(editBox.getEntity().getName());
			  GUIView.getPane().remove(Controller.getinClass());
			  Controller.setinClass(null);
			  GUIView.validateRepaint();
		  }
		});
	}
	
}

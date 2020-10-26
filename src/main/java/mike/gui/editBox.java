package mike.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
//import java.awt.Image;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
//import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import mike.controller.Controller;
import mike.datastructures.Entity;
import mike.datastructures.Field;
import mike.datastructures.Method;
import mike.datastructures.Parameter;
import mike.view.GUIView;

public class editBox {
	
	private static JLabel newBox;
	//private static ImageIcon xIcon = new ImageIcon(new ImageIcon(GUIView.class.getResource("..\\gui\\resources\\xmark.jpg")).getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT));
	//private static ImageIcon addIcon = new ImageIcon(new ImageIcon(GUIView.class.getResource("..\\gui\\resources\\addSymbol.png")).getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT));
	private static Entity e;
	private static ArrayList<Entity> backup = new ArrayList<Entity>();
	
	public editBox (JLabel label) {
		backup.clear();
		for(Entity entity : Controller.getModel().getEntities()){
			Entity adding = new Entity(entity.getName());
			adding.setXLocation(entity.getXLocation());
			adding.setYLocation(entity.getYLocation());
			  for(Field field : entity.getFields()){
				  adding.createField(field.getName(), field.getType());
			  }
			  for(Method method : entity.getMethods()){
				  adding.createMethod(method.getName(), method.getType());
				  for(Parameter param : method.getParameters()){
					  adding.createParameter(method.getName(), param.getName(), param.getType());
				  }
			  }
			  backup.add(adding);
		  }
		
		e = Controller.getModel().copyEntity(label.getName());
		
		// Create entire editBox
		newBox = new JLabel();
        newBox.setLayout(new BoxLayout(newBox, BoxLayout.Y_AXIS));
        
        // Add all model parts into newBox
        createClassSection(label.getName());
        createSection("Fields:");
        createSection("Methods:");
        
		// Design entire label
        newBox.setBackground(Color.LIGHT_GRAY);
        newBox.setOpaque(true);
		newBox.setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 4), (new EmptyBorder(6, 6, 6, 6))));
		
		// Position/show label
		newBox.setName(e.getName());
		newBox.setBounds(e.getXLocation(), e.getYLocation(), newBox.getLayout().preferredLayoutSize(newBox).width, newBox.getLayout().preferredLayoutSize(newBox).height);

	}
	
	public static JLabel getBox() {
		return newBox;
	}
	
	public static Entity getEntity() {
		return e;
	}
	
	public static ArrayList<Entity> getBackup() {
		return backup;
	}
	
	private void createClassSection(String labelName) {
        JPanel saveCancel = setUpJPanel();     
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        saveCancel.add(saveButton);
        saveCancel.add(cancelButton); 
        newBox.add(saveCancel);
        
        JPanel newEntity = setUpJPanel();
        JButton xButton = new JButton("X");
        JTextField className = new JTextField(labelName);
        newEntity.add(xButton);
        newEntity.add(className);
        GUIView.getController().saveCancel(saveButton, cancelButton, xButton);
        newBox.add(newEntity);
	}
	
	private void createSection(String section) {
		JLabel Label = new JLabel(section);
		Label.setFont(new Font("", Font.BOLD, 18));
		Label.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
		newBox.add(Label);
		
		// Add all existing fields/methods(and Paramters) to editBox
		Controller control = GUIView.getController();
		if(section == "Fields:"){
			for(Field f : e.getFields()){
				control.deleteField(editSection(f.getType(), f.getName(), false, newBox.getComponentCount()));
			}
			control.createField(newSection(false, newBox.getComponentCount()));
		} else {
			for(Method m : e.getMethods()){
				control.deleteMethod(editSection(m.getType(), m.getName(), false, newBox.getComponentCount())); 
				for (Parameter p : m.getParameters()) {
					control.deleteParam(editSection(p.getType(), p.getName(), true, newBox.getComponentCount()), m.getName());
				}
				control.createParam(newSection(true, newBox.getComponentCount()), m.getName());
			}
			control.createMethod(newSection(false, newBox.getComponentCount()));
		}
        
	}
	
	public static JPanel editSection(String sectionType, String sectionName, Boolean parameter, int spot) {
		JPanel editSection = setUpJPanel();
		
		JButton xButton = new JButton("X");
		JLabel Type = new JLabel(sectionType);
		JTextField Name = new JTextField(sectionName);
		if(parameter) {
			JLabel Tab = new JLabel("------");
			editSection.add(Tab);
		}
		editSection.add(xButton);
		editSection.add(Type);
		editSection.add(Name);
		
		newBox.add(editSection, spot);
		
		return editSection;
	}
	
	
	public static JPanel newSection(Boolean parameter, int spot) {
		JPanel newSection = setUpJPanel();
		
		JTextField Type = new JTextField();
        JTextField Name = new JTextField();
        JButton xButton = new JButton("+");
        if(parameter){
        	JLabel Tab = new JLabel("------");
			newSection.add(Tab);
        }
        newSection.add(Type);
        newSection.add(Name);
        newSection.add(xButton);
        
        newBox.add(newSection, spot);
       
        return newSection;
	}
	
	private static JPanel setUpJPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
		return panel;
	}
	
}

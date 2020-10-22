package mike.gui;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import mike.controller.Controller;
import mike.datastructures.Entity;
import mike.datastructures.Field;
import mike.datastructures.Method;
import mike.datastructures.Parameter;

public class htmlBox {
	
	private JLabel newBox;
	
	public htmlBox (Entity entity) {
		// Create JLabel and basic settings
		this.newBox = new JLabel(entityToHTML(entity));
		newBox.setName(entity.getName());
		this.newBox.setBackground(Color.LIGHT_GRAY);
		this.newBox.setOpaque(true);
		
		// Create border and margin
		Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
		Border margin = new EmptyBorder(6, 6, 6, 6);
		this.newBox.setBorder(new CompoundBorder(border, margin));
		
		// Create settings so the box can appear at right spot
		this.newBox.setBounds(0, 0, this.newBox.getPreferredSize().width, this.newBox.getPreferredSize().height);
		this.newBox.setLocation(entity.getXLocation(), entity.getYLocation());
		
		Controller.classControls(this.newBox, entity);
	}
	
	public JLabel getBox() {
		return newBox;
	}
	
	private static String entityToHTML(Entity e) {
		String html = "<html>" + e.getName() + "<br/>";

		ArrayList<Field> fields = e.getFields();
		if (fields.size() > 0) {
			html += "<hr>Fields:<br/>";
			for (Field f : fields) {
				html += "&emsp " + f.getType() + " " + f.getName() + "<br/>";
			}
		}

		ArrayList<Method> methods = e.getMethods();
		if (methods.size() > 0) {
			html += "<hr>Methods:<br/>";

			for (Method m : methods) {
				ArrayList<Parameter> parameters = m.getParameters();
				html += "&emsp " + m.getType() + " " + m.getName() + "(";
				if (parameters.size() == 1) {
					html += parameters.get(0).getType() + " " + parameters.get(0).getName();
				}
				if (parameters.size() > 1) {
					html += parameters.get(0).getType() + " " + parameters.get(0).getName();
					for (int i = 1; i < parameters.size(); ++i) {
						html += ", " + parameters.get(i).getType() + " " + parameters.get(i).getName();
					}
				}
				html += ")<br/>";
			}
		}

		html += "</html>";

		return html;
	}
	
}

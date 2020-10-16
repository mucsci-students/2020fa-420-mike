package mike.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class Line extends JComponent {
	double x1, y1, x2, y2;
	JLabel classOne, classTwo;
	Line(double x1, double y1, double x2, double y2, JLabel classOne, JLabel classTwo) {
		super();
		
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.classOne = classOne;
		this.classTwo = classTwo;
		
		this.setVisible(true);
		this.setPreferredSize(new Dimension (782, 725));
	}
	
	public void setx1(double newx1) {
		x1 = newx1;
	}
	
	public void sety1(double newy1) {
		y1 = newy1;
	}
	
	public void setx2(double newx2) {
		x2 = newx2;
	}
	
	public void sety2(double newy2) {
		y2 = newy2;
	}
	
	public JLabel getClassOne(){
		return classOne;
	}
	
	public JLabel getClassTwo(){
		return classTwo;
	}
	
	public void paint(Graphics g) {	
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(4f));

		g2d.draw(new Line2D.Double(x1, y1, x2, y2));
	}
}

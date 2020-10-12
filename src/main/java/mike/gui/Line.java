package mike.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import javax.swing.JComponent;
import javax.swing.JPanel;

import mike.datastructures.Relationship;

public class Line extends JComponent {
	double x1, y1, x2, y2;

	Line(double x1, double y1, double x2, double y2) {
		super();
		System.out.println("Making Line");
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.setPreferredSize(new Dimension((int)Math.abs(x1 - x2),(int) Math.abs(y1 - y2)));
	}
	
	//@Override
	public void paint(Graphics g) {
		System.out.println("Painting Line");
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(4f));
		g2d.draw(new Line2D.Double(x1, y1, x2, y2));
	}
}

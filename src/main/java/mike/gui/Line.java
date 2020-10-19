package mike.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;

import javax.swing.JComponent;
import javax.swing.JLabel;

import mike.datastructures.Relationship.Type;

public class Line extends JComponent {
	double x1, y1, x2, y2;
	JLabel classOne, classTwo;
	boolean toSelf;
	Type type;

	Line(double x1, double y1, double x2, double y2, JLabel classOne, JLabel classTwo, Type type) {
		super();
		this.type = type;
		this.toSelf = toSelf;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.classOne = classOne;
		this.classTwo = classTwo;

		this.setVisible(true);
		this.setPreferredSize(new Dimension(classOne.getParent().getWidth(), classOne.getParent().getHeight()));
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

	public JLabel getClassOne() {
		return classOne;
	}

	public JLabel getClassTwo() {
		return classTwo;
	}

	public void paint(Graphics g) {
		// Setting Rendering Hints
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHints(rh);

		// Get line length
		double ydiff = y2 - y1;
		double xdiff = x2 - x1;
		double length = Math.sqrt(xdiff * xdiff + ydiff * ydiff);
		
		super.paint(g);
		
		// Draw line
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(4f));
		g2d.rotate(-Math.atan2(xdiff, ydiff), x1, y1);
		g2d.draw(new Line2D.Double(x1, y1, x1, y1 + length));

		// Stylizing line.

		Path2D.Double path = new Path2D.Double();

		double y = y1 + length;

		switch (type) {
		case COMPOSITION:
			// Diamond
			g2d.setStroke(new BasicStroke(8f));
			path.moveTo(x1, y);
			path.lineTo(x1 + 6, y - 10);
			path.lineTo(x1, y - 20);
			path.lineTo(x1 - 6, y - 10);
			path.lineTo(x1, y);
			break;
		case AGGREGATION:
			// Empty Diamond
			g2d.setStroke(new BasicStroke(4f));
			path.moveTo(x1, y);
			path.lineTo(x1 + 8, y - 12);
			path.lineTo(x1, y - 24);
			path.lineTo(x1 - 8, y - 12);
			path.lineTo(x1, y);
			break;
		case REALIZATION:
			// Filled Arrow
			g2d.setStroke(new BasicStroke(8f));
			path.moveTo(x1, y);
			path.lineTo(x1 + 6, y - 10);
			path.lineTo(x1 - 6, y - 10);
			path.lineTo(x1, y);
			break;
		default:
			// Empty Arrow
			g2d.setStroke(new BasicStroke(4f));
			path.moveTo(x1, y);
			path.lineTo(x1 + 12, y - 16);
			path.lineTo(x1 - 12, y - 16);
			path.lineTo(x1, y);
			break;
		}

		g2d.draw(path);
	}
}

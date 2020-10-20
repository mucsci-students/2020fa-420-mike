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

	public void setNewPoints(double newx1, double newy1, double newx2, double newy2) {
		x1 = newx1;
		y1 = newy1;
		x2 = newx2;
		y2 = newy2;
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

	public void paintComponent(Graphics g) {
		// Setting Rendering Hints
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		// Get line length
		double ydiff = y2 - y1;
		double xdiff = x2 - x1;
		double length = Math.sqrt(xdiff * xdiff + ydiff * ydiff);

		super.paintComponent(g);

		// Draw line
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(1f));
		g2d.rotate(-Math.atan2(xdiff, ydiff), x1, y1);
		
		if (type == Type.REALIZATION) {
			g2d.draw(new Line2D.Double(x1, y1 + 4, x1, y1 + length - 4));
			return;
		}
		g2d.draw(new Line2D.Double(x1, y1 + 4, x1, y1 + length - 8));
		// Stylizing line

		int y = (int) (y1 + length - 4);
		int x = (int) x1;

		if (type == Type.COMPOSITION) {
			// Filled Diamond
			int xpoints[] = { x, x + 6, x, x - 6, x };
			int ypoints[] = { y, y - 10, y - 20, y - 10, y };
			g2d.fillPolygon(xpoints, ypoints, 5);
		} else if (type == Type.AGGREGATION) {
			// Empty Diamond
			g2d.setColor(Color.WHITE);
			int xpoints[] = { x, x + 6, x, x - 6, x };
			int ypoints[] = { y, y - 10, y - 20, y - 10, y };
			g2d.fillPolygon(xpoints, ypoints, 5);
			g2d.setColor(Color.BLACK);
			g2d.drawPolygon(xpoints, ypoints, 5);
		} else {
			// Filled Arrow
			g2d.setColor(Color.WHITE);
			int xpoints[] = {x, x + 7, x - 7, x};
			int ypoints[] = {y, y - 14, y - 14, y };
			g2d.fillPolygon(xpoints, ypoints, 4);
			g2d.setColor(Color.BLACK);
			g2d.drawPolygon(xpoints, ypoints, 4);
		}
	}
}

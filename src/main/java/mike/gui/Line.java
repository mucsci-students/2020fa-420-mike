package mike.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;

import javax.swing.JComponent;
import javax.swing.JLabel;

import mike.datastructures.Relationship.Type;

public class Line extends JComponent {
	private static final long serialVersionUID = -5573668863596984829L;
	private double x1, y1, x2, y2;
	private JLabel classOne, classTwo;
	private boolean toSelf;
	private Type type;

	Line(JLabel L1, JLabel L2, Type type) {
		super();
		this.type = type;
		update(L1, L2);

		this.setVisible(true);
		this.setPreferredSize(new Dimension(classOne.getParent().getWidth(), classOne.getParent().getHeight()));
	}

	public void update(JLabel L1, JLabel L2) {
		// (a,b) = L1 center
		double a = L1.getLocation().x + L1.getSize().width / 2;
		double b = L1.getLocation().y + L1.getSize().height / 2;

		// (c,d) = L2 center
		double c = L2.getLocation().x + L2.getSize().width / 2;
		double d = L2.getLocation().y + L2.getSize().height / 2;

		double[] centers1 = { a, b, c, d };
		Point p1 = getEdgeIntersectionPoint(L1.getSize(), L1.getLocation(), centers1);
		double[] centers2 = { c, d, a, b };
		Point p2 = getEdgeIntersectionPoint(L2.getSize(), L2.getLocation(), centers2);

		x1 = p1.getX();
		y1 = p1.getY();
		x2 = p2.getX();
		y2 = p2.getY();
		classOne = L1;
		classTwo = L2;
	}

	// Returns the point on the edge of L1 on the line between the centers of L1 and
	// L2
	public static Point getEdgeIntersectionPoint(Dimension d1, Point p1, double[] centers) {
		if (centers[1] <= centers[3]) {
			p1.y += d1.getHeight();
		}
		if (centers[0] <= centers[2]) {
			p1.x += d1.getWidth();
		}

		// Slope of the line going from (a,b) -> (c,d)
		double m = (centers[3] - centers[1]) / (centers[2] - centers[0]);
		double j1 = m * (p1.x - centers[0]);
		double i2 = (p1.y - centers[1]) / m;

		// Point of intersection; Pick closest intersection point
		if (Math.pow((centers[0] - p1.x), 2) + j1 * j1 > i2 * i2 + Math.pow((centers[1] - p1.y), 2)) {
			p1.x = (int) (i2 + centers[0]);
		} else {
			p1.y = (int) (j1 + centers[1]);
		}
		return p1;
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

		super.paintComponent(g);

		// Get line length
		double ydiff = y2 - y1;
		double xdiff = x2 - x1;
		double length = Math.sqrt(xdiff * xdiff + ydiff * ydiff);

		// Draw line
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(1f));
		g2d.rotate(-Math.atan2(xdiff, ydiff), x1, y1);

		int y = (int) (y1 + length - 4);
		int x = (int) x1;

		if (type == Type.REALIZATION) {
			// Dashed line
			float[] dashedpattern = { 4f, 4f };
			Stroke dashedstroke = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f,
					dashedpattern, 1.0f);

			g2d.setStroke(dashedstroke);
			g2d.draw(new Line2D.Double(x1, y1 + 4, x1, y1 + length - 8));

			g2d.setStroke(new BasicStroke(1f));
			drawEmptyArrow(g2d, x, y);
			return;
		}

		g2d.draw(new Line2D.Double(x1, y1 + 4, x1, y1 + length - 8));
		// Stylizing line
		if (type == Type.COMPOSITION) {
			drawFilledDiamond(g2d, x, y);
		} else if (type == Type.AGGREGATION) {
			drawEmptyDiamond(g2d, x, y);
		} else { // Inheritance
			drawEmptyArrow(g2d, x, y);
		}
	}

	public void drawFilledDiamond(Graphics2D g2d, int x, int y) {
		int xpoints[] = { x, x + 6, x, x - 6, x };
		int ypoints[] = { y, y - 10, y - 20, y - 10, y };
		g2d.fillPolygon(xpoints, ypoints, 5);
	}

	public void drawEmptyDiamond(Graphics2D g2d, int x, int y) {
		g2d.setColor(Color.WHITE);
		int xpoints[] = { x, x + 6, x, x - 6, x };
		int ypoints[] = { y, y - 10, y - 20, y - 10, y };
		g2d.fillPolygon(xpoints, ypoints, 5);
		g2d.setColor(Color.BLACK);
		g2d.drawPolygon(xpoints, ypoints, 5);
	}

	public void drawEmptyArrow(Graphics2D g2d, int x, int y) {
		g2d.setColor(Color.WHITE);
		int xpoints[] = { x, x + 7, x - 7, x };
		int ypoints[] = { y, y - 14, y - 14, y };
		g2d.fillPolygon(xpoints, ypoints, 4);
		g2d.setColor(Color.BLACK);
		g2d.drawPolygon(xpoints, ypoints, 4);
	}
}

package mike.gui;

import javax.swing.JLabel;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.Graphics2D;

import mike.gui.Line;
import mike.gui.GUI;
import mike.datastructures.Relationship;

public class GUIRelationship {
	public static void drawRelationship(Relationship.Type type, JLabel L1, JLabel L2) {
		Point p1 = getEdgeIntersectionPoint(L1, L2);
		Point p2 = getEdgeIntersectionPoint(L2, L1);
		
		Line line = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
		
		GUI.relations.add(line);
		GUI.centerPanel.add(line);
		GUI.centerPanel.validate();
	}

	// Returns the point on the edge of L1 on the line between the centers of L1 and L2
	public static Point getEdgeIntersectionPoint(JLabel L1, JLabel L2) {
		double x1, y1, x2, y2, h1, w1, h2, w2;
		x1 = L1.getX();
		y1 = L1.getY();
		h1 = L1.getHeight();
		w1 = L1.getWidth();
		x2 = L2.getX();
		y2 = L2.getY();
		h2 = L2.getHeight();
		w2 = L2.getWidth();

		// (a,b) = L1 center
		double a = x1 + w1 / 2;
		double b = y1 + h1 / 2;

		// (c,d) = L2 center
		double c = x2 + w2 / 2;
		double d = y2 + h2 / 2;

		// Slope of the line going from (a,b) -> (c,d)
		double m = (d - b) / (c - a);

		// Point of intersection
		Point p = new Point();
		// Computationally light conditions.
		// Top, Down, Left, Right.
		if (x1 <= c && c <= x1 + w1 && y1 + h1 <= d) {
			// y = y1+h, so y1+h = m(x-a)+b
			p.x = (int) ((y1 + h1 - b) / m + a);
			p.y = (int) (y1 + h1);
			return p;
		} else if (x1 <= c && c <= x1 + w1 && d <= y1) {
			// y = y1, so y1 = m(x-a)+b
			p.x = (int) ((y1 - b) / m + a);
			p.y = (int) y1;
			return p;
		} else if (c <= x1 && y1 <= d && d <= y1 + h1) {
			// x = x1 so y = m(x1-a)+b
			p.x = (int) x1;
			p.y = (int) (m * (x1 - a) + b);
			return p;
		} else if (x1 + w1 < c && y1 <= d && d <= y1 + h1) {
			// x = x1 + w1 so y = m(x1-a)+b
			p.x = (int) (x1 + w1);
			p.y = (int) (m * (x1 + w1 - a) + b);
			return p;
		}

		double i1, i2, j1, j2;
		// Computationally heavier conditions.
		// Top-Right, Top-Left, Bottom-Right, Bottom-Left
		if (x1 + w1 < c && y1 + h1 < d) {
			// Find the intersection from y=m(x-a)+b on lines x=x1+w1 and y=y1+h1
			// x=x1+w1
			i1 = x1 + w1;
			j1 = m * (x1 + w1 - a) + b;
			// y=y1+h1
			i2 = (y1 + h1 - b) / m + a;
			j2 = y1 + h1;
		} else if (c < x1 && y1 + h1 < d) {
			// Find the intersection from y=m(x-a)+b on lines x=x1 and y=y1+h1
			// x=x1
			i1 = x1;
			j1 = m * (x1 - a) + b;
			// y=y1+h1
			i2 = (y1 + h1 - b) / m + a;
			j2 = y1 + h1;
		} else if (x1 + w1 < c && d < y1) {
			// Find the intersection from y=m(x-a)+b on lines x=x1+w1 and y=y1
			// x=x1+w1
			i1 = x1 + w1;
			j1 = m * (x1 + w1 - a) + b;
			// y=y1
			i2 = (y1 - b) / m + a;
			j2 = y1;
		} else {
			// Find the intersection from y=m(x-a)+b on lines x=x1 and y=y1
			// x=x1
			i1 = x1;
			j1 = m * (x1 - a) + b;
			// y=y1
			i2 = (y1 - b) / m + a;
			j2 = y1;
		}

		// Pick closest intersection point
		if (i1 * i1 + j1 * j1 > i2 * i2 + j2 * j2) {
			p.x = (int) i2;
			p.y = (int) j2;
		} else {
			p.x = (int) i1;
			p.y = (int) j1;
		}

		return p;
	}
}

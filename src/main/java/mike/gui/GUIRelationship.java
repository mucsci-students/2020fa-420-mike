package mike.gui;

import javax.swing.JLabel;
import java.awt.Point;

import mike.gui.Line;
import mike.gui.GUI;
import mike.datastructures.Relationship;

public class GUIRelationship {
	public static void drawRelationship(Relationship.Type type, JLabel L1, JLabel L2) {
		Point p1 = getEdgeIntersectionPoint(L1, L2);
		Point p2 = getEdgeIntersectionPoint(L2, L1);
		
		Line line = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY(), L1, L2);
		
		GUI.relations.add(line);
		GUI.pane.add(line);
		GUI.pane.validate();
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
		
		double i1, i2, j1, j2;
		// Computationally heavier conditions.
		// Top-Right, Top-Left, Bottom-Right, Bottom-Left
		  if (a <= c && b <= d) {
	            // Find the intersection from y=m(x-a)+b on lines x=x1+w1 and y=y1+h1
	            // x=x1+w1
	            i1 = x1 + w1;
	            j1 = m * (x1 + w1 - a) + b;
	            // y=y1+h1
	            i2 = (y1 + h1 - b) / m + a;
	            j2 = y1 + h1;
	        } else if (c <= a && b <= d) {
	            // Find the intersection from y=m(x-a)+b on lines x=x1 and y=y1+h1
	            // x=x1
	            i1 = x1;
	            j1 = m * (x1 - a) + b;
	            // y=y1+h1
	            i2 = (y1 + h1 - b) / m + a;
	            j2 = y1 + h1;
	        } else if (a <= c && d <= b) {
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
		double diff_i1 = a - i1;
        double diff_j1 = b - j1;
        double diff_i2 = a - i2;
        double diff_j2 = b - j2;
        
        if (diff_i1 * diff_i1 + diff_j1 * diff_j1 > diff_i2 * diff_i2 + diff_j2 * diff_j2) {
            p.x = (int) i2;
            p.y = (int) j2;
        } else {
            p.x = (int) i1;
            p.y = (int) j1;
        }
		return p;
	}
}

package mike.gui;

import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.Point;

import mike.datastructures.Relationship;

public class GUIRelationship {
	public static void drawRelationship(Relationship.Type type, JLabel L1, JLabel L2) {
		// (a,b) = L1 center
		double a = L1.getLocation().x + L1.getSize().width / 2;
		double b = L1.getLocation().y + L1.getSize().height / 2;
		
		// (c,d) = L2 center
		double c = L2.getLocation().x + L2.getSize().width / 2;
		double d = L2.getLocation().y + L2.getSize().height / 2;
		
		double[] centers1 = {a, b, c, d};
		Point p1 = GUIRelationship.getEdgeIntersectionPoint(L1.getSize(), L1.getLocation(), centers1);
		double[] centers2 = {c, d, a, b};
		Point p2 = GUIRelationship.getEdgeIntersectionPoint(L2.getSize(), L2.getLocation(), centers2);
		
		Line line = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY(), L1, L2);
		line.setBounds(0, 0, GUI.pane.getWidth(), GUI.pane.getHeight());
		
		GUI.relations.add(line);
		GUI.pane.add(line);
		GUI.pane.validate();
	}

	// Returns the point on the edge of L1 on the line between the centers of L1 and L2
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
}

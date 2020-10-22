package mike.gui;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import mike.gui.Line;

public class Panel extends JPanel {
	private static final long serialVersionUID = 4006186725318929847L;
	public ArrayList<Line> lines;

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		for (Line l : lines)
		{
			l.paint(g);
		}
	}
}
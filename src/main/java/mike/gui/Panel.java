package mike.gui;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import mike.gui.Line;

public class Panel extends JPanel {
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

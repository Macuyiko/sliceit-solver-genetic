package com.macuyiko.sliceitgenetic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.JPanel;

public class VisualizationPanel extends JPanel {
	private static final long serialVersionUID = -2154545455151517339L;
	private MainFrame parent;
	private List<SplitLine> entity;
	
	public VisualizationPanel(MainFrame frame, List<SplitLine> entity) {
		this.parent = frame;
		this.entity = entity;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Rectangle r = parent.getLevel().getOriginalPolygon().getBounds();
		g.setColor(Color.blue);
		g.drawRect(r.x-10, r.y-10, r.width+20, r.height+20);
		for (int i = 0; i < parent.getLevel().getPolygons().size(); i++) {
			Polygon p = parent.getLevel().getPolygons().get(i);
			g.setColor(Color.lightGray);
			g.fillPolygon(p);
			g.setColor(Color.black);
			g.drawPolygon(p);
		}
		for (SplitLine sl : entity) {
			if (parent.getLevel().validLineSplit(sl.x1, sl.y1, sl.x2, sl.y2))
				g.setColor(Color.green);
			else
				g.setColor(Color.red);
			g.drawLine(sl.x1, sl.y1, sl.x2, sl.y2);
		}
		Level eval = parent.getLevel().clone();
		String s = "";
		for (SplitLine sl : entity)
			eval.applyLineSplit(sl.x1, sl.y1, sl.x2, sl.y2);
		double totalarea = PolygonUtils.getArea(eval.getPolygons());
		if (eval.isGameWon())
			g.setColor(Color.blue);
		else
			g.setColor(Color.red);
		for (int i = 0; i < eval.getPolygons().size(); i++) {
			Polygon p = eval.getPolygons().get(i);
			double area = PolygonUtils.getArea(p);
			double percentage = area / totalarea * 100;
			s += "#" + i + ": " + String.format("%.2g%n", percentage) + "%  ";
		}
		g.drawString(s, 10, 10);
	}
}

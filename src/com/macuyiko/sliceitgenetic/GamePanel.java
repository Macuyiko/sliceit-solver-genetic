package com.macuyiko.sliceitgenetic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = -6134188393261867339L;
	private MainFrame parent;
	
	private int x1, x2, y1, y2;
	private boolean isDrawing;
	private boolean isBlocked;
	
	public GamePanel(MainFrame frame) {
		this.parent = frame;
		isDrawing = false;
		isBlocked = false;
		setupListeners();
	}
	
	public void blockUI() {
		isBlocked = true;
	}

	public void unblockUI() {
		isBlocked = false;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < parent.getLevel().getPolygons().size(); i++) {
			Polygon p = parent.getLevel().getPolygons().get(i);
			g.setColor(Color.lightGray);
			g.fillPolygon(p);
			g.setColor(Color.black);
			g.drawPolygon(p);
		}
	}
	
	public void refresh() {
		invalidate();
		repaint();
	}

	private void startLineDrag(int x, int y) {
		x1 = x;
		y1 = y;
		isDrawing = true;
	}
	
	private void endLineDrag(int x, int y) {
		x2 = x;
		y2 = y;
		isDrawing = false;
		applyLineSplit(x1, y1, x2, y2);
	}
	
	private void applyLineSplit(int x1, int y1, int x2, int y2) {
		parent.getLevel().applyLineSplit(x1, y1, x2, y2);
		refresh();
		showAreas();
		checkGame();
	}
	
	private void showAreas() {
		parent.getSidePanel().clear();
		double totalarea = PolygonUtils.getArea(parent.getLevel().getPolygons());
		for (int i = 0; i < parent.getLevel().getPolygons().size(); i++) {
			Polygon p = parent.getLevel().getPolygons().get(i);
			double area = PolygonUtils.getArea(p);
			double percentage = area / totalarea * 100;
			parent.getSidePanel().addLine("#" + i + ": " + String.format("%.2g%n", percentage) + "%");
		}
		parent.getFrame().setTitle(
				parent.getLevel().getNrStrokes() + " / " + parent.getLevel().getNrTargetStrokes() 
				+ "   " + parent.getLevel().getNrAreas() + " / " + parent.getLevel().getNrTargetAreas()
		);
	}
	
	private void checkGame() {
		if (parent.getLevel().isGameOver()) {
			parent.getSidePanel().addLine(" ---- GAME OVER ---- ");
		}
		if (parent.getLevel().isGameWon()) {
			parent.getSidePanel().addLine(" ---- GAME WON! ---- ");
		}
	}
	
	private void refreshLine(int x, int y) {
		if (!isDrawing)
			return;
		refresh();
		Graphics g = getGraphics();
		if (parent.getLevel().validLineSplit(x1, y1, x, y))
			g.setColor(Color.green);
		else
			g.setColor(Color.red);
		g.drawLine(x1, y1, x, y);
		Point[] intersections = 
				PolygonUtils.getIntersections(parent.getLevel().getPolygons(), x1, y1, x, y);
		for (Point pt : intersections) {
			g.drawRect(pt.x-3, pt.y-3, 6, 6);
			g.fillRect(pt.x-3, pt.y-3, 6, 6);
		}
	}
	
	private void setupListeners() {
		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
				if (!parent.getLevel().isGameOver() && !isBlocked)
					startLineDrag(e.getX(), e.getY());
			}
			public void mouseReleased(MouseEvent e) {
				if (!parent.getLevel().isGameOver() && !isBlocked)
					endLineDrag(e.getX(), e.getY());
			}
	    });
	    
	    addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				refreshLine(e.getX(), e.getY());
			}
			public void mouseMoved(MouseEvent e) {
			}
	    });
	}
}

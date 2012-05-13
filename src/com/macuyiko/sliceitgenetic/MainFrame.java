package com.macuyiko.sliceitgenetic;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Polygon;

import javax.swing.JFrame;

public class MainFrame {
	private final JFrame frame;
	private final GamePanel gamePanel;
	private final SidePanel sidePanel;
	private final Level level;
	
	public MainFrame() {
		frame = new JFrame();
		frame.setTitle("SliceIt! Genetic");
		frame.setSize(350, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container contentPane = frame.getContentPane();

		Polygon p = PolygonUtils.createPolygon(
						new int[] { 100, 240, 240, 170, 100, 100 }, 
						new int[] { 230, 230, 130, 30, 130, 230 });
		level = new Level(p, 10, 5);
		
		gamePanel = new GamePanel(this);
		contentPane.add(gamePanel, BorderLayout.CENTER);

		sidePanel = new SidePanel(this);
		sidePanel.setPreferredSize(new Dimension(80, 200));
		contentPane.add(sidePanel, BorderLayout.LINE_END);

		frame.setVisible(true);
	}
	
	public void reset() {
		level.reset();
		gamePanel.refresh();
		frame.setTitle("SliceIt! Genetic");
		sidePanel.clear();
	}
	
	public void blockUI() {
		gamePanel.blockUI();
		sidePanel.blockUI();
	}

	public void unblockUI() {
		gamePanel.unblockUI();
		sidePanel.unblockUI();
	}

	public JFrame getFrame() {
		return frame;
	}
	
	public GamePanel getGamePanel() {
		return gamePanel;
	}

	public SidePanel getSidePanel() {
		return sidePanel;
	}
	
	public Level getLevel() {
		return level;
	}
}

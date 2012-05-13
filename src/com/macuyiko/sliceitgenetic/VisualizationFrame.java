package com.macuyiko.sliceitgenetic;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;

import org.uncommons.watchmaker.swing.evolutionmonitor.EvolutionMonitor;

public class VisualizationFrame {
	public VisualizationFrame(EvolutionMonitor<List<SplitLine>> monitor) {
		JFrame frame = new JFrame();
		frame.setTitle("running...");
		frame.setSize(350, 350);
		frame.getContentPane().add(monitor.getGUIComponent(), BorderLayout.CENTER);
		frame.setVisible(true);
	}
}

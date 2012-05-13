package com.macuyiko.sliceitgenetic;

import java.util.List;
import javax.swing.JComponent;
import org.uncommons.watchmaker.framework.interactive.Renderer;

public class LevelSwingRenderer implements
		Renderer<List<SplitLine>, JComponent> {
	private MainFrame parent;
	
	public LevelSwingRenderer(MainFrame parent) {
		this.parent = parent;
	}

	public JComponent render(List<SplitLine> entity) {
		VisualizationPanel panel = new VisualizationPanel(parent, entity);
		return panel;
	}
}

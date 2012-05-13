package com.macuyiko.sliceitgenetic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.uncommons.watchmaker.swing.AbortControl;

public class SidePanel extends JPanel {
	private static final long serialVersionUID = -8481984878489898919L;
	private MainFrame parent;
	private JLabel textfield;
	private String text;
	private JButton restart, solve;
	private AbortControl abort;

	public SidePanel(MainFrame frame) {
		this.parent = frame;

		this.setBackground(Color.gray);

		this.restart = new JButton("Restart");
		this.restart.setPreferredSize(new Dimension(80, 20));
		this.restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				parent.reset();
			}
		});
		this.add(restart, BorderLayout.NORTH);

		this.solve = new JButton("Solve");
		this.solve.setPreferredSize(new Dimension(80, 20));
		this.solve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				abort = new AbortControl();
				add(abort.getControl());
				parent.reset();
				parent.blockUI();
		        new EvolutionTask(parent, 100,
						3, abort.getTerminationCondition()).execute();
			}
		});
		this.add(solve, BorderLayout.NORTH);
		
		this.textfield = new JLabel();
		this.textfield.setPreferredSize(new Dimension(80, 200));
		this.add(this.textfield, BorderLayout.SOUTH);

		this.text = "";
	}
	
	public void blockUI() {
		abort.getControl().setEnabled(true);
		solve.setEnabled(false);
		restart.setEnabled(false);
		invalidate();
		repaint();
	}

	public void unblockUI() {
		remove(abort.getControl());
		solve.setEnabled(true);
		restart.setEnabled(true);
	}

	private void updateText() {
		textfield.setText("<html>" + text + "</html>");
	}

	public void addLine(String line) {
		text = line + "<br>" + text;
		updateText();
	}

	public void clear() {
		text = "";
		updateText();
	}
}

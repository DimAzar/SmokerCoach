package com.d.apps.scoach.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.d.apps.scoach.CounterApp;
import com.d.apps.scoach.util.Utilities;

public class AboutIFrame extends JInternalFrame {
	private static final long serialVersionUID = -892682552079556150L;
	String version = null;
	
	public AboutIFrame() {
		super();
		version = CounterApp.appProperties.getProperty("app.version");
		
		initGrcs();
	}
	
	
	private void initGrcs() {
		JPanel parent = (JPanel) getContentPane();
		
		GridBagLayout layout = new GridBagLayout();
		parent.setLayout(layout);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 1 };
		gridBagLayout.rowHeights   = new int[] { 20, 20, 20 };
		gridBagLayout.columnWeights = new double[] { 1.0 };
		gridBagLayout.rowWeights    = new double[] { 0.0, 0.0, 1.0 };
		parent.setLayout(gridBagLayout);

		parent.add(new JLabel("Version"), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 2, 0, 2), 0, 0));
		parent.add(new JLabel(version), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 2, 0, 2), 0, 0));

		setName(Utilities.NAME_ABOUTDIALOG);
		setClosable(true);
		setTitle("About Counter");
		setSize(100, 100);
	}
}
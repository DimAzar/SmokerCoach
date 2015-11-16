package com.d.apps.scoach.ui.iframes;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;

public class SmokerCoachIFrame extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private final String SMOKECOUNT_LBL = "Smoked today : ";
	
	private JLabel smokeCountLabel = new JLabel(SMOKECOUNT_LBL);

	public SmokerCoachIFrame() {
		super();
		
		initGrcs();
		
		setClosable(true);
		setTitle("Smoker Coach");
		setSize(400, 200);
		setLocation(10,10);
		setResizable(true);
		setName("Smoker Coach IFrame");
	}
	
	private void initGrcs() {
		Container parent = getContentPane();

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]	{7, 7, 7, 7};
		gridBagLayout.rowHeights = new int[]	{7, 7, 7, 7};
		gridBagLayout.columnWeights = new double[]	{Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE, 1};
		gridBagLayout.rowWeights = new double[]		{Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE, 1};
		parent.setLayout(gridBagLayout);
		
		getContentPane().add(smokeCountLabel, new GridBagConstraints(3,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0,0));
	}
	
	public void setSmokeCount(int count) {
		smokeCountLabel.setText(SMOKECOUNT_LBL+count);
	}
}

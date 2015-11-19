package com.d.apps.scoach.ui.iframes;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class ManageCountersIFrame extends AbstractManageEntityIFRame {
	private static final long serialVersionUID = -892682552079556150L;
	
	private JPopupMenu rmenu = new JPopupMenu();
	
	public ManageCountersIFrame() {
		super();
		
		initGrcs();
		
		setTitle("Manage System Counters");
		setName("Manage Counters IFrame");
		setSize(400, 200);
		setLocation(10,10);
		setClosable(true);
		setResizable(true);
	}
	
	protected void initGrcs() {
		JPanel parent = (JPanel) getContentPane();
	}
}
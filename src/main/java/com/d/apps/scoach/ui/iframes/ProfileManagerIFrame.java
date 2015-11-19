package com.d.apps.scoach.ui.iframes;

import javax.swing.JInternalFrame;

public class ProfileManagerIFrame extends JInternalFrame {
	private static final long serialVersionUID = 1L;

	public ProfileManagerIFrame() {
		super();
		
		initGrcs();
		
		setTitle("Profile Manager");
		setName("Profile Manager");
		setSize(400, 200);
		setLocation(10,10);
		setClosable(true);
		setResizable(true);
	}
	
	private void initGrcs() {
		
	}
}

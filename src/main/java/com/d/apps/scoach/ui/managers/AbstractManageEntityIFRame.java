package com.d.apps.scoach.ui.managers;

import javax.swing.JInternalFrame;
import javax.swing.JPopupMenu;


public class AbstractManageEntityIFRame extends JInternalFrame {
	private static final long serialVersionUID = 1L;

	protected JPopupMenu rmenu = new JPopupMenu();
	
	public AbstractManageEntityIFRame() {
		super();
		setSize(400, 200);
		setClosable(true);
		setResizable(true);
	}
}

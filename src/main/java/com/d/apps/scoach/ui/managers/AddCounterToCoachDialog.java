package com.d.apps.scoach.ui.managers;

import javax.swing.JDialog;

import com.d.apps.scoach.Utilities;

public class AddCounterToCoachDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	public AddCounterToCoachDialog() {
		super();
		
		initGrcs();
	}

	private void initGrcs() {
		setModal(true);
		setTitle("Add Counter to Coach");
		
		setName(Utilities.NAME_ADDCOUNTER);
		setSize(300,300);
	}
}

package com.d.apps.scoach.ui.managers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JInternalFrame;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.d.apps.scoach.db.model.Profile;
import com.d.apps.scoach.ui.MainFrame;


public abstract class AbstractManageEntityIFRame extends JInternalFrame {
	private static final long serialVersionUID = 1L;

	protected Profile profile = null;
	protected JPopupMenu rmenu = new JPopupMenu();
	protected JTable entityTable = null;


	protected AbstractManageEntityIFRame(Profile profile) {
		super();
		this.profile = profile;

		setSize(400, 200);
		setClosable(true);
		setResizable(true);
	}
	
	public void profileChanged(Profile profile) {
		if (profile == null) {
			dispose();
		}

		this.profile = profile;
		((AbstractTableModel)entityTable.getModel()).fireTableDataChanged();
	}
	
	protected void notifyActiveProfileChanged() {
		((MainFrame)getTopLevelAncestor()).notifyActiveProfileChanged();
	}
	
	protected void notifyActiveProfileChanged(Profile profile) {
		((MainFrame)getTopLevelAncestor()).setActiveProfile(profile);
	}

	protected void setupListeners() {
		entityTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					if (rmenu.isShowing()) {
						return;
					} 
					
					if (entityTable.getSelectedRow() < 0) {
						return;
					}
					rmenu.setLocation(e.getLocationOnScreen());
					rmenu.setVisible(true);
					return;
				} 
				if (rmenu.isShowing()) {
					rmenu.setVisible(false);
				}
			}
		});
	}
}
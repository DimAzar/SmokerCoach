package com.d.apps.scoach.ui.managers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;


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

package com.d.apps.scoach.ui.iframes;

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

	protected JTable entityTable = null;
	protected JPopupMenu rmenu = new JPopupMenu();
	
	protected class CustomRenderer implements TableCellRenderer {
		public final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			JComponent renderer = null;
			if (column == 0 || column == 1) {
				if (column == 0) {
					renderer = new JLabel(""+(row+1));
				} else {
					renderer = new JLabel(value.toString());
				}
			} else {
				renderer = new JCheckBox("", Boolean.parseBoolean(value.toString()));
			}
			
			if ((row % 2) > 0) {
				renderer.setBackground(Color.white);
			} else {
				renderer.setBackground(new Color(240,240,240));
			}

			if (isSelected) {
				renderer.setBackground(Color.yellow);
			} else {
				renderer.setBackground(Color.white);
				renderer.setForeground(Color.black);
			}
			
			renderer.setOpaque(true);
			return renderer;
		}
	}
}

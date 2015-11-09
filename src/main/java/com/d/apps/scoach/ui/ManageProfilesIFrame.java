package com.d.apps.scoach.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.d.apps.scoach.db.DBManager;
import com.d.apps.scoach.db.model.Profile;

public class ManageProfilesIFrame extends JInternalFrame {
	private static final long serialVersionUID = -892682552079556150L;
	
	private String version = null;
	private JTable profilesTable = new JTable(new CustomTableModel());
	
	public ManageProfilesIFrame(Properties properties) {
		super();
		version = properties.getProperty("app.version");
		
		initGrcs();
		initData();
		setTitle("Manage System Profiles "+version);
		setSize(400, 200);
		setLocation(10,10);
		setResizable(true);
	}
	
	private void initData() {
		List<Profile> users = DBManager.getProfiles();
		DefaultTableModel dtm = (DefaultTableModel) profilesTable.getModel();
		
		dtm.getDataVector().removeAllElements();
		int cnt = 1;
		for (Profile user : users) {
			String[] rowData = new String[3];
			rowData[0] = ""+cnt;
			rowData[1] = user.getFirstName();
			rowData[2] = ""+user.isActive();
			
			dtm.addRow(rowData);
			cnt++;
		}
		dtm.fireTableDataChanged();
		profilesTable.setModel(dtm);
	}
	
	private void initGrcs() {
		JPanel parent = (JPanel) getContentPane();
		
		parent.setLayout(new BorderLayout());
		

		parent.add(new JScrollPane(profilesTable), BorderLayout.CENTER);
		
		TableCellRenderer renderer = new CustomRenderer();
		profilesTable.setDefaultRenderer(Object.class, renderer);
		
		profilesTable.setRowSelectionAllowed(true);
		profilesTable.getColumnModel().getColumn(0).setMaxWidth(25);
		profilesTable.getColumnModel().getColumn(2).setMaxWidth(50);
	}
	
}

class CustomRenderer implements TableCellRenderer {
	public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		JComponent renderer = null;
		if (column == 0 || column == 1) {
			renderer = new JLabel(value.toString());
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
		
		if (hasFocus) {
			renderer.setBorder(BorderFactory.createEtchedBorder(Color.white, Color.black));
		} else {
			renderer.setBorder(BorderFactory.createEmptyBorder());
		}
		
		renderer.setOpaque(true);
		return renderer;
	}
}

class CustomTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;

	@Override
	public int getColumnCount() {
		return 3;
	}
	
	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "Id";
		case 1:
			return "Name";
		case 2:
			return "Active";
		default:
			return "N/A";
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == 2) {
			return true;
		}
		return false;
	}
}
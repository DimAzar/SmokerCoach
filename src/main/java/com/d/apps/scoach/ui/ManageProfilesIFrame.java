package com.d.apps.scoach.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.d.apps.scoach.SmokerCoach;
import com.d.apps.scoach.db.model.Profile;

public class ManageProfilesIFrame extends JInternalFrame {
	private static final long serialVersionUID = -892682552079556150L;
	
	private String version = null;
	private JTable profilesTable = null;
	private JPopupMenu rmenu = new JPopupMenu();
	
	public ManageProfilesIFrame(Properties properties) {
		super();
		version = properties.getProperty("app.version");
		
		initGrcs();
		setupListeners();
		
		setTitle("Manage System Profiles "+version);
		setSize(400, 200);
		setLocation(10,10);
		setResizable(true);
	}
	
	private void setupListeners() {
		profilesTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					if (rmenu.isShowing()) {
						return;
					} 
					
					if (profilesTable.getSelectedRow() < 0) {
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
	
	private void initGrcs() {
		JPanel parent = (JPanel) getContentPane();
		parent.setLayout(new BorderLayout());
		
		AbstractTableModel dtm = new CustomTableModel();
		profilesTable = new JTable(dtm);
		parent.add(new JScrollPane(profilesTable), BorderLayout.CENTER);
		parent.add(new CreateProfilePanel(), BorderLayout.SOUTH);
		profilesTable.setRowSelectionAllowed(true);
		profilesTable.setDefaultRenderer(Object.class, new CustomRenderer());
		profilesTable.getColumnModel().getColumn(0).setMaxWidth(25);
		profilesTable.getColumnModel().getColumn(2).setMaxWidth(50);
		profilesTable.setRowSelectionAllowed(true);
		profilesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dtm.fireTableDataChanged();

		initRMenu();
	}
	
	private void initRMenu() {
		JMenuItem delete = new JMenuItem("Delete profile");
		rmenu.add(delete);
		 
		JMenuItem setActive = new JMenuItem("Set Active");
		rmenu.add(setActive);
		
		setActive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = profilesTable.getSelectedRow();
				if (profilesTable.getValueAt(row, 2).equals("true")) {
					return;
				}
				int pid = Integer.parseInt(profilesTable.getValueAt(row, 0).toString());
				SmokerCoach.DBServices.setActiveProfile(pid);
				updateUIProfileChanged();
			}
		});
		
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rmenu.setVisible(false);
				if (JOptionPane.showConfirmDialog(null, "Delete Profile ?") == JOptionPane.OK_OPTION) {
					int row = profilesTable.getSelectedRow();
					int pid = Integer.parseInt(profilesTable.getValueAt(row, 0).toString());
					
					boolean isActive = Boolean.parseBoolean(profilesTable.getValueAt(row, 2).toString());
					if (isActive) {
						if (JOptionPane.showConfirmDialog(null, "Profile is the active profile\nDelete Profile ?") == JOptionPane.OK_OPTION) {
							SmokerCoach.DBServices.deleteProfile(pid);
							updateUIProfileChanged();
						}
					} else {
						SmokerCoach.DBServices.deleteProfile(pid);
						updateUIProfileChanged();
					}
				}
			}
		});
	}
	
	private void updateUIProfileChanged() {
		((MainFrame)getTopLevelAncestor()).activeProfileChanged();
		((CustomTableModel)profilesTable.getModel()).refresh();
		rmenu.setVisible(false);
	}

	class CreateProfilePanel extends JPanel {
		private static final long serialVersionUID = 5244913423336516955L;
		private JTextField name = new JTextField(20);
		private JCheckBox active = new JCheckBox("", true);
		private JButton create = new JButton("Create");
		
		public CreateProfilePanel() {
			super();

			add(name);
			add(active);
			add(create);
			
			create.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String nameval = name.getText();
					boolean activeval = active.isSelected();
					
					if (nameval.length() <= 0) {
						JOptionPane.showMessageDialog(null, "Name cannot be empty! ");
						return;
					}
					if (!isProfileNameUnique(nameval)) {
						JOptionPane.showMessageDialog(null, "Name must be unique! ");
						return;
					}
					if (activeval) {
						SmokerCoach.DBServices.deactivateAllProfiles();
					}
					
					if (SmokerCoach.DBServices.getProfilesCount() <= 0) {
						JOptionPane.showMessageDialog(null, "No other profiles, setting as active profile!");
						activeval = true;
					}
					SmokerCoach.DBServices.createProfile(nameval , activeval );
					updateUIProfileChanged();
				}
			});
		}
		
		private boolean isProfileNameUnique(String name) {
			for (int i = 0; i < profilesTable.getRowCount(); i++) {
				if (profilesTable.getValueAt(i, 1).toString().equals(name)) {
					return false;
				}
			}
			
			return true;
		}
	}
}

class CustomRenderer implements TableCellRenderer {
	public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
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

class CustomTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private List<Profile> profiles = new ArrayList<Profile>();
	
	public CustomTableModel() {
		super();
		refresh();
	}
	
	public void refresh() {
		this.profiles = SmokerCoach.DBServices.getProfiles();
		fireTableDataChanged();
	}
	@Override
	public int getColumnCount() {
		return 3;
	}
	
	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "#";
		case 1:
			return "Name";
		case 2:
			return "Active";
		default:
			return "N/A";
		}
	}

	@Override
	public Object getValueAt(int row, int column) {
		Profile p = profiles.get(row);
		
		switch (column) {
			case 0:
				return p.getId();
			case 1:
				return p.getFirstName();
			case 2:
				return p.isActive();
		}
		throw new RuntimeException("Cannot get value :"+row+","+column);
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == 2) {
			return true;
		}
		return false;
	}

	@Override
	public int getRowCount() {
		return profiles.size();
	}
}
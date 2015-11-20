package com.d.apps.scoach.ui.managers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.d.apps.scoach.CounterApp;
import com.d.apps.scoach.Utilities;
import com.d.apps.scoach.db.model.CoachInstance;
import com.d.apps.scoach.db.model.CoachTemplate;
import com.d.apps.scoach.db.model.Profile;
import com.d.apps.scoach.ui.MainFrame;

public class ManageProfilesIFrame extends AbstractManageEntityIFRame {
	private static final long serialVersionUID = -892682552079556150L;
	
	private JTable entityTable = null;
	
	public ManageProfilesIFrame() {
		super();
		setName(Utilities.NAME_PROFILESMANAGER);
		
		initGrcs();
		setupListeners();
	}
	
	private void setupListeners() {
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
	
	private void initGrcs() {
		JPanel parent = (JPanel) getContentPane();
		parent.setLayout(new BorderLayout());
		
		AbstractTableModel dtm = new CustomProfilesTableModel();
		entityTable = new JTable(dtm);
		parent.add(new JScrollPane(entityTable), BorderLayout.CENTER);
		parent.add(new CreateProfilePanel(), BorderLayout.SOUTH);
		entityTable.setRowSelectionAllowed(true);
		entityTable.setDefaultRenderer(Object.class, new ProfilesCustomRenderer());
		entityTable.getColumnModel().getColumn(0).setMaxWidth(25);
		entityTable.getColumnModel().getColumn(2).setMaxWidth(50);
		entityTable.setRowSelectionAllowed(true);
		entityTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dtm.fireTableDataChanged();

		initRMenu();
		setTitle("Manage System Profiles ");
	}
	
	private void initRMenu() {
		JMenuItem delete = new JMenuItem("Delete profile");
		rmenu.add(delete);
		 
		JMenuItem setActive = new JMenuItem("Set Active");
		rmenu.add(setActive);
		
		JMenuItem coaches = new JMenuItem("Coaches");
		rmenu.add(coaches);

		JMenuItem manage = new JMenuItem("Manage");
		rmenu.add(manage);

		setActive.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = entityTable.getSelectedRow();
				if (entityTable.getValueAt(row, 2).equals("true")) {
					return;
				}
				int pid = Integer.parseInt(entityTable.getValueAt(row, 0).toString());
				updateUIActiveProfileChanged(CounterApp.DBServices.setActiveProfile(pid));
			}
		});
		
		coaches.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = entityTable.getSelectedRow();
				int pid = Integer.parseInt(entityTable.getValueAt(row, 0).toString());

				Profile profile = CounterApp.DBServices.findProfile(pid);

				ProfileCoachesDialog d = new ProfileCoachesDialog((MainFrame)getTopLevelAncestor(), profile);
				rmenu.setVisible(false);
				Point p = getLocationOnScreen();
				p.translate(getSize().width/2-d.getSize().width/2, getSize().height/2-d.getSize().height/2);
				d.setLocation(p);
				d.setVisible(true);
			}
		});

		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rmenu.setVisible(false);
				if (JOptionPane.showConfirmDialog(null, "Delete Profile ?") == JOptionPane.OK_OPTION) {
					int row = entityTable.getSelectedRow();
					int pid = Integer.parseInt(entityTable.getValueAt(row, 0).toString());
					
					boolean isActive = Boolean.parseBoolean(entityTable.getValueAt(row, 2).toString());
					if (isActive) {
						if (JOptionPane.showConfirmDialog(null, "Profile is the active profile\nDelete Profile ?") == JOptionPane.OK_OPTION) {
							CounterApp.DBServices.deleteProfile(pid);
							updateUIActiveProfileChanged(null);
						}
					} else {
						CounterApp.DBServices.deleteProfile(pid);
						profileActionRefreshUI();
					}
				}
			}
		});
		
		manage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = entityTable.getSelectedRow();
				int pid = Integer.parseInt(entityTable.getValueAt(row, 0).toString());

				Profile p = CounterApp.DBServices.findProfile(pid);
				((MainFrame)getTopLevelAncestor()).showIFrame(new ManagerProfileIFrame(p));
				profileActionRefreshUI();
			}
		});
	}

	private void updateUIActiveProfileChanged(Profile p) {
		((MainFrame)getTopLevelAncestor()).activeProfileChanged(p);
		profileActionRefreshUI();
	}

	private void profileActionRefreshUI() {
		((CustomProfilesTableModel)entityTable.getModel()).refresh();
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
						CounterApp.DBServices.deactivateAllProfiles();
					}
					
					if (CounterApp.DBServices.getProfilesCount() <= 0) {
						JOptionPane.showMessageDialog(null, "No other profiles, setting as active profile!");
						activeval = true;
					}
					Profile newProfile = CounterApp.DBServices.createProfile(nameval , activeval);
					if (activeval) {
						updateUIActiveProfileChanged(newProfile);
					}
					name.setText("");
				}
			});
		}
		
		private boolean isProfileNameUnique(String name) {
			for (int i = 0; i < entityTable.getRowCount(); i++) {
				if (entityTable.getValueAt(i, 1).toString().equals(name)) {
					return false;
				}
			}
			
			return true;
		}
	}
}

class CustomProfilesTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private List<Profile> profiles = new ArrayList<Profile>();
	
	public CustomProfilesTableModel() {
		super();
		refresh();
	}
	
	public void refresh() {
		this.profiles = CounterApp.DBServices.getProfiles();
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

class ProfileCoachesDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private JButton saveButt = new JButton("Save");
	private List<JCheckBox> options = new ArrayList<JCheckBox>();
	private Profile profile;
	private MainFrame controller;
	
	public ProfileCoachesDialog(MainFrame controller, Profile profile) {
		super();
		this.profile = profile;
		this.controller = controller;
		
		initGrcs();
		createListeners();
		setModal(true);
	}
	
	private void initGrcs() {
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.add(createCoachesPanel(), BorderLayout.CENTER);
		c.add(saveButt, BorderLayout.SOUTH);
		setName(Utilities.NAME_PROFILECOACHES);
		setSize(350, 300);
	}
	
	private void createListeners() {
		saveButt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (JCheckBox box : options) {
					if (box.isSelected() && !isCoachEnabledInProfile(box.getName(), profile)) {
						Profile p = CounterApp.DBServices.enableCoach(box.getName(), profile);
						if (p.isActive()) {
							controller.updateProfileRelatedUI(p.getCoaches());
						}
					} else
					if (!box.isSelected() && isCoachEnabledInProfile(box.getName(), profile)) {
						Profile p = CounterApp.DBServices.disableCoach(box.getName(), profile);
						if (p.isActive()) {
							controller.updateProfileRelatedUI(p.getCoaches());
						}
					}
				}
			}
		});
	}
	
	private JPanel createCoachesPanel() {
		JPanel ans = new JPanel();
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]	{7, 7, };
		gridBagLayout.columnWeights = new double[]	{.5 , .5};

		List<CoachTemplate> allCoaches = CounterApp.DBServices.getCoachTemplates();
		double[] da = new double[allCoaches.size()+1];
		int[] ia = new int[allCoaches.size()+1];
		for (int i = 0; i < allCoaches.size()+1; i++) {
			da[i] = Double.MIN_VALUE;
			ia[i] = 7;
		}
		da[allCoaches.size()] = 1;
		gridBagLayout.rowWeights = da;
		gridBagLayout.rowHeights = ia;

		ans.setLayout(gridBagLayout);

		int rowcnt = 0;

		for (CoachTemplate coachEntry : allCoaches) {
			JLabel l = new JLabel(coachEntry.getName());
			JCheckBox chkb = new JCheckBox("enable", isCoachEnabledInProfile(coachEntry.getId(), profile));
			chkb.setName(coachEntry.getName());
			
			ans.add(l, new GridBagConstraints(0,rowcnt,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0,0));
			ans.add(chkb, new GridBagConstraints(1,rowcnt,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 10), 0,0));
			rowcnt++;
			
			options.add(chkb);
		}

		ans.setBorder(BorderFactory.createEtchedBorder());
		return ans;
	}
	
	private boolean isCoachEnabledInProfile(int id, Profile p) {
		for (CoachInstance profileCoach : p.getCoaches()) {
			if (profileCoach.getTemplate().getId() == id) {
				return true;
			}
		}
		return false;
	}

	private boolean isCoachEnabledInProfile(String name, Profile p) {
		for (CoachInstance profileCoach : p.getCoaches()) {
			if (profileCoach.getTemplate().getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
}

final class ProfilesCustomRenderer implements TableCellRenderer {
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
		} 
		
		renderer.setOpaque(true);
		return renderer;
	}
}
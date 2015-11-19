package com.d.apps.scoach.ui.iframes.profilemanager;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d.apps.scoach.db.model.CoachInstance;
import com.d.apps.scoach.db.model.Profile;

public class ProfileManagerIFrame extends JInternalFrame {
	private static final Logger LOG = LoggerFactory.getLogger(ProfileManagerIFrame.class);
	private static final long serialVersionUID = 1L;

	protected JTable entityTable = null;
	private Profile profile = null;
	
	public ProfileManagerIFrame(Profile profile) {
		super();
		
		this.profile = profile;
		
		initGrcs();
		
		setTitle("Managing :"+profile.getFirstName());
		setName("Profile Manager");
		setSize(500, 500);
		setLocation(10,10);
		setClosable(true);
		setResizable(true);
	}
	
	private void initGrcs() {
		Container parent = getContentPane();
		
		GridBagLayout layout = new GridBagLayout();
		
		layout.columnWidths = new int[] { 7 };
		layout.rowHeights   = new int[] { 7, 7, 7 };
		
		layout.columnWeights = new double[] { 1 };
		layout.rowWeights    = new double[] { .0, .5, .5 };

		parent.setLayout(layout);
		parent.add(getProfileEditorPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 5, 5), 0, 0));
		parent.add(getCoachesTablePanel() , new GridBagConstraints(0, 1, 1, 1, 1.0, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 5, 2), 0, 0));
		parent.add(getCoachesEditorPanel(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 5, 2), 0, 0));
		
	}
	
	private JPanel getProfileEditorPanel() {
		JPanel ans = new JPanel();
		
		add(new JLabel(profile.getFirstName()));
		ans.setBorder(BorderFactory.createEtchedBorder());
		return ans;
	}
	
	private JPanel getCoachesTablePanel() {
		JPanel ans = new JPanel();
		AbstractTableModel dtm = new CustomProfilesTableModel(profile.getCoaches());
		entityTable = new JTable(dtm);

		entityTable.setRowSelectionAllowed(true);
		entityTable.getColumnModel().getColumn(0).setMaxWidth(25);
		entityTable.setRowSelectionAllowed(true);
		entityTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		ans.setLayout(new BorderLayout());
		ans.add(new JScrollPane(entityTable), BorderLayout.CENTER);
		ans.setBorder(BorderFactory.createEtchedBorder());
		return ans;
	}
	private JPanel getCoachesEditorPanel() {
		JPanel ans = new JPanel();
		ans.setBorder(BorderFactory.createEtchedBorder());
		return ans;
	}	
}

class CustomProfilesTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private List<CoachInstance> coaches = new ArrayList<CoachInstance>();
	
	public CustomProfilesTableModel(List<CoachInstance> coaches) {
		super();
		this.coaches = coaches;
		refresh();
	}
	
	public void refresh() {
		fireTableDataChanged();
	}
	@Override
	public int getColumnCount() {
		return 2;
	}
	
	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "#";
		case 1:
			return "Name";
		default:
			return "N/A";
		}
	}

	@Override
	public Object getValueAt(int row, int column) {
		CoachInstance ci = coaches.get(row);
		
		switch (column) {
			case 0:
				return ci.getId();
			case 1:
				return ci.getName();
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
		return coaches.size();
	}
}
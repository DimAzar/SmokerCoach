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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d.apps.scoach.CounterApp;
import com.d.apps.scoach.Utilities;
import com.d.apps.scoach.db.model.CoachInstance;
import com.d.apps.scoach.db.model.Counter;
import com.d.apps.scoach.db.model.Profile;

public class ManagerProfileIFrame extends AbstractManageEntityIFRame {
	private static final Logger LOG = LoggerFactory.getLogger(ManagerProfileIFrame.class);
	private static final long serialVersionUID = 1L;

	//SWING 
	private JTable coachesTable, countersTable;
	private JButton addCounterButt = new JButton("Add Counter");

	//MODEL
	private Profile profile = null;
	private CoachInstance selectedCoach;
	
	public ManagerProfileIFrame(Profile profile) {
		super();
		this.profile = profile;
		
		initGrcs();
		addListeners();
		LOG.debug("Profile manager at " +new Date().toString());
	}
	
	private void initGrcs() {
		Container parent = getContentPane();
		
		GridBagLayout layout = new GridBagLayout();
		
		layout.columnWidths = new int[] { 7 };
		layout.rowHeights   = new int[] { 7, 7, 7,  7 };
		
		layout.columnWeights = new double[] { 1.0 };
		layout.rowWeights    = new double[] { 0.0, .33, .0, .33 };
 
		parent.setLayout(layout);

		parent.add(getProfileEditorPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 5, 5), 0, 0));
		parent.add(getCoachesTablePanel() , new GridBagConstraints(0, 1, 1, 1, 1.0, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 5, 2), 0, 0));
		parent.add(addCounterButt, new GridBagConstraints(0, 2, 1, 1, 1.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 2), 0, 0));
		parent.add(getCountersTablePanel(), new GridBagConstraints(0, 3, 1, 1, 1.0, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 5, 2), 0, 0));
	
		setName(Utilities.NAME_PROFILEMANAGER);
		setTitle("Managing :"+profile.getFirstName());
		setSize(500, 500);
		setLocation(10,10);
		setClosable(true);
		setResizable(true);
	}
	
	private void addListeners() {
		addCounterButt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddCounterToCoachDialog d = new AddCounterToCoachDialog();
				Point p = getLocationOnScreen();
				p.translate(getSize().width/2-d.getSize().width/2, getSize().height/2-d.getSize().height/2);
				d.setLocation(p);
				d.setVisible(true);
				updateCountersData();
			}
		});
	}

	private JPanel getProfileEditorPanel() {
		JPanel ans = new JPanel();
		ans.setLayout(new BorderLayout());
		ans.add(new JLabel(profile.getFirstName()), BorderLayout.CENTER);
		ans.setBorder(BorderFactory.createEtchedBorder());
		return ans;
	}
	
	private JPanel getCoachesTablePanel() {
		JPanel ans = new JPanel();
		AbstractTableModel dtm = new CustomCoachesTableModel(profile.getCoaches());
		coachesTable = new JTable(dtm);

		coachesTable.setRowSelectionAllowed(true);
		coachesTable.setDefaultRenderer(Object.class, new CoachesCustomRenderer());
		coachesTable.getColumnModel().getColumn(0).setMaxWidth(25);
		coachesTable.setRowSelectionAllowed(true);
		coachesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		coachesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
	        public void valueChanged(ListSelectionEvent e) {
	        	if (!e.getValueIsAdjusting()) {
	        		int selectionIndex = ((DefaultListSelectionModel)e.getSource()).getLeadSelectionIndex();
	        		int cid = Integer.parseInt(coachesTable.getValueAt(selectionIndex, 0).toString());
	        		selectedCoach = CounterApp.DBServices.findCoachInstance(cid);	        		
	        		coachSelectionChanged();
	        	}
	        }
	    });
		ans.setLayout(new BorderLayout());
		ans.add(new JScrollPane(coachesTable), BorderLayout.CENTER);
		return ans;
	}
	
	private void coachSelectionChanged() {
		updateCountersData();
	}
	
	private JPanel getCountersTablePanel() {
		JPanel ans = new JPanel();
		AbstractTableModel dtm = new CustomCountersTableModel();
		countersTable = new JTable(dtm);

		countersTable.setRowSelectionAllowed(true);
		countersTable.setDefaultRenderer(Object.class, new CountersCustomRenderer());
		countersTable.getColumnModel().getColumn(0).setMaxWidth(25);
		countersTable.setRowSelectionAllowed(true);
		countersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		ans.setLayout(new BorderLayout());
		ans.add(new JScrollPane(countersTable), BorderLayout.CENTER);
		return ans;
	}	
	
	public void updateCountersData() {
		DefaultTableModel dtm = (DefaultTableModel) countersTable.getModel();
		List<Counter> data = selectedCoach.getCounters();
		
		dtm.getDataVector().removeAllElements();
		
		for (int i = 0; i < data.size(); i++) {
			Counter row = data.get(i); 
			dtm.addRow(new Object[]{row.getId(), row.getName()});	
		}
		dtm.fireTableDataChanged();
		countersTable.setModel(dtm);
	}

}

class CustomCoachesTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private List<CoachInstance> coaches = new ArrayList<CoachInstance>();
	
	public CustomCoachesTableModel(List<CoachInstance> coaches) {
		super();
		this.coaches = coaches;
		refresh();
	}
	
	public void refresh() {
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
			return "Template";
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
			case 2:
				return ci.getTemplate().getName();
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

class CustomCountersTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;
	private List<Counter> coaches = new ArrayList<Counter>();
	
	public CustomCountersTableModel() {
		super();
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
		Counter ci = coaches.get(row);
		
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
}

final class CountersCustomRenderer implements TableCellRenderer {
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
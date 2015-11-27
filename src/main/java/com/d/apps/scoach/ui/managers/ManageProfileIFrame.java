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
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d.apps.scoach.CounterApp;
import com.d.apps.scoach.Utilities;
import com.d.apps.scoach.db.model.CoachGraph;
import com.d.apps.scoach.db.model.Coach;
import com.d.apps.scoach.db.model.Counter;
import com.d.apps.scoach.db.model.Profile;
import com.d.apps.scoach.ui.GraphFrame;
import com.d.apps.scoach.ui.MainFrame;

public class ManageProfileIFrame extends AbstractManageEntityIFRame {
	private static final Logger LOG = LoggerFactory.getLogger(ManageProfileIFrame.class);
	private static final long serialVersionUID = 1L;

	//SWING 
	private JTable coachesTable, countersTable, graphsTable;
	private JButton addCounterButt = new JButton("Add Counter");
	private JButton addGraphButt = new JButton("Add Graph");
	protected JPopupMenu rmenuCoaches = new JPopupMenu(),
						rmenuGraphs= new JPopupMenu();;
	

	//MODEL
	private Profile profile = null;
	private Coach selectedCoach;
	private CoachGraph    selectedGraph;
	
	public ManageProfileIFrame(Profile profile) {
		super();
		this.profile = profile;
		
		initGrcs();
		addListeners();
		createRMenus();
		LOG.debug("Profile manager at " +new Date().toString());
	}
	
	private void createRMenus() {
		JMenuItem plot = new JMenuItem("Plot");
		rmenuGraphs.add(plot);
		
		plot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rmenuGraphs.setVisible(false);
				new GraphFrame(selectedGraph);
			}
		});
		
		JMenuItem manage= new JMenuItem("Manage");
		rmenuGraphs.add(manage);
		
		manage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rmenuGraphs.setVisible(false);
				manageGraphs(selectedGraph);
			}
		});

		JMenuItem delete = new JMenuItem("Delete");
		rmenuGraphs.add(delete);
		
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rmenuGraphs.setVisible(false);
				selectedGraph.removeCounters();
				CounterApp.DBServices.updateGraph(selectedGraph);
				
				selectedCoach.removeGraph(selectedGraph);
				selectedCoach = CounterApp.DBServices.updateCoach(selectedCoach);
				
				selectedGraph = null;
				updateGraphsData();
			}
		});
	}
	
	private void initGrcs() {
		Container parent = getContentPane();
		
		GridBagLayout layout = new GridBagLayout();
		
		layout.columnWidths = new int[] { 7 };
		layout.rowHeights   = new int[] { 7, 7, 7,  7, 7, 7 };
		
		layout.columnWeights = new double[] { 1.0 };
		layout.rowWeights    = new double[] { 0.0, .33, .0, .0, .0,  .33 };
 
		parent.setLayout(layout);

		parent.add(getProfileEditorPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 5, 5), 0, 0));
		parent.add(getCoachesTablePanel() , new GridBagConstraints(0, 1, 1, 1, 1.0, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 5, 2), 0, 0));
		parent.add(addGraphButt, new GridBagConstraints(0, 2, 1, 1, 1.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 2), 0, 0));
		parent.add(getGraphsTablePanel() , new GridBagConstraints(0, 3, 1, 1, 1.0, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 5, 2), 0, 0));
		parent.add(addCounterButt, new GridBagConstraints(0, 4, 1, 1, 1.0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 2), 0, 0));
		parent.add(getCountersTablePanel(), new GridBagConstraints(0, 5, 1, 1, 1.0, 0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 5, 2), 0, 0));
	
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
				if (selectedCoach == null) {
					JOptionPane.showMessageDialog(null, "Please select a coach first!");
					return;
				}
				AddCounterToCoachDialog d = new AddCounterToCoachDialog(selectedCoach.getId());
				Point p = getLocationOnScreen();
				p.translate(getSize().width/2-d.getSize().width/2, getSize().height/2-d.getSize().height/2);
				d.setLocation(p);
				d.setVisible(true);
				updateCountersData();
			}
		});
		
		addGraphButt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedCoach == null) {
					JOptionPane.showMessageDialog(null, "Please select a coach first!");
					return;
				}
				//create graphs
				manageGraphs(null);
			}
		});
		
		coachesTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				rmenuAction(e, rmenuCoaches, coachesTable);
			}
		});		
		graphsTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				rmenuAction(e, rmenuGraphs, graphsTable);
			}
		});		
	}

	private void manageGraphs(CoachGraph editingGraph) {
		AddGraphToCoachDialog d = new AddGraphToCoachDialog(selectedCoach, editingGraph);
		Point p = getLocationOnScreen();
		p.translate(getSize().width/2-d.getSize().width/2, getSize().height/2-d.getSize().height/2);
		d.setLocation(p);
		d.setVisible(true);
		updateGraphsData();
	}
	
	private void rmenuAction(MouseEvent e, JPopupMenu rmenu, JTable table) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			if (rmenu.isShowing()) {
				return;
			} 
			
			if (table.getSelectedRow() < 0) {
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
		updateGraphsData();
		
	}
	
	private JPanel getCountersTablePanel() {
		JPanel ans = new JPanel();
		
		List<Counter> counters = (selectedCoach == null) ? new ArrayList<Counter>() : selectedCoach.getCounters();
		AbstractTableModel dtm = new CustomCountersTableModel(counters);
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
	
	private JPanel getGraphsTablePanel() {
		JPanel ans = new JPanel();
		
		List<CoachGraph> graphs = (selectedCoach == null) ? new ArrayList<CoachGraph>() : selectedCoach.getGraphs();
		AbstractTableModel dtm = new CustomGraphsTableModel(graphs);
		graphsTable = new JTable(dtm);

		graphsTable.setRowSelectionAllowed(true);
		graphsTable.setDefaultRenderer(Object.class, new GraphsCustomRenderer());
		graphsTable.getColumnModel().getColumn(0).setMaxWidth(25);
		graphsTable.setRowSelectionAllowed(true);
		graphsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		graphsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
	        public void valueChanged(ListSelectionEvent e) {
	        	if (!e.getValueIsAdjusting()) {
	        		int selectionIndex = ((DefaultListSelectionModel)e.getSource()).getLeadSelectionIndex();
	        		if (selectionIndex < 0) {
	        			return;
	        		}
	        		int gid = Integer.parseInt(graphsTable.getValueAt(selectionIndex, 0).toString());
	        		selectedGraph = selectedCoach.getGraphById(gid);	        		
//	        		updateGraphsData();
	        	}
	        }
	    });

		ans.setLayout(new BorderLayout());
		ans.add(new JScrollPane(graphsTable), BorderLayout.CENTER);
		return ans;
	}	
	
	public void updateGraphsData() {
		CustomGraphsTableModel dtm = (CustomGraphsTableModel) graphsTable.getModel();
		dtm.setGraphs(selectedCoach.getGraphs());
		dtm.fireTableDataChanged();
		graphsTable.setModel(dtm);
		
	}

	public void updateCountersData() {
		CustomCountersTableModel dtm = (CustomCountersTableModel) countersTable.getModel();
		dtm.setCounters(selectedCoach.getCounters());
		dtm.fireTableDataChanged();
		countersTable.setModel(dtm);
		
		if (profile.updateCoach(selectedCoach)) {
			((MainFrame)getTopLevelAncestor()).updateProfileRelatedUI(profile.getCoaches());
		} else {
			LOG.error(String.format("Could not update profile coaches pid:%s cid:%s", profile.getId(), selectedCoach.getId()));
		}

	}

}

class CustomCoachesTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private List<Coach> coaches = new ArrayList<Coach>();
	
	public CustomCoachesTableModel(List<Coach> coaches) {
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
		Coach ci = coaches.get(row);
		
		switch (column) {
			case 0:
				return ci.getId();
			case 1:
				return ci.getName();
			case 2:
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

class CustomGraphsTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private List<CoachGraph> graphs = new ArrayList<CoachGraph>();
	
	public CustomGraphsTableModel(List<CoachGraph> graphs) {
		super();
		this.graphs = graphs;
		fireTableDataChanged();
	}
	
	public void setGraphs(List<CoachGraph> graphs) {
		this.graphs = graphs;
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
		CoachGraph cg = graphs.get(row);
		
		switch (column) {
			case 0:
				return cg.getId();
			case 1:
				return cg.getName();
		
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
		return graphs.size();
	}
}

class CustomCountersTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private List<Counter> counters = new ArrayList<Counter>();
	
	public CustomCountersTableModel(List<Counter> coaches) {
		super();
		this.counters = coaches;
		
		fireTableDataChanged();
	}

	public void setCounters(List<Counter> counters) {
		this.counters = counters;
	}
	
	@Override
	public int getColumnCount() {
		return 4;
	}
	
	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "#";
		case 1:
			return "Name";
		case 2:
			return "Type";
		case 3:
			return "Step value";
		default:
			return "N/A";
		}
	}

	@Override
	public Object getValueAt(int row, int column) {
		Counter ci = counters.get(row);
		
		switch (column) {
			case 0:
				return ci.getId();
			case 1:
				return ci.getName();
			case 2:
				return ci.getType().getDescription();
			case 3:
				return ci.getStepValue();
				
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
		return counters.size();
	}
}

final class CountersCustomRenderer implements TableCellRenderer {
	public final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		JComponent renderer = null;
		if (column == 0) {
			renderer = new JLabel(""+(row+1));
		} else 
		if (column == 3 && value.toString().equals("0.0")) {
			renderer = new JLabel("N/a");
		} else {
			renderer = new JLabel(value.toString());
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

final class GraphsCustomRenderer implements TableCellRenderer {
	public final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		JComponent renderer = null;
		if (column == 0) {
			renderer = new JLabel(""+(row+1));
		} else 
		if (column == 3 && value.toString().equals("0.0")) {
			renderer = new JLabel("N/a");
		} else {
			renderer = new JLabel(value.toString());
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
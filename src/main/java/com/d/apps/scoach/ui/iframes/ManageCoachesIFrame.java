package com.d.apps.scoach.ui.iframes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

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
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.d.apps.scoach.CounterApp;
import com.d.apps.scoach.db.model.CoachTemplate;

public class ManageCoachesIFrame extends JInternalFrame {
	private static final long serialVersionUID = -892682552079556150L;
	
	private JTable coachesTable = null;
	private JPopupMenu rmenu = new JPopupMenu();
	
	public ManageCoachesIFrame() {
		super();
		
		initGrcs();
		setupListeners();
		
		setTitle("Manage System Coaches ");
		setName("Manage Coaches IFrame");
		setSize(400, 200);
		setLocation(10,10);
		setClosable(true);
		setResizable(true);
	}
	
	private void setupListeners() {
		coachesTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					if (rmenu.isShowing()) {
						return;
					} 
					
					if (coachesTable.getSelectedRow() < 0) {
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
		coachesTable = new JTable(dtm);
		
		parent.add(new JScrollPane(coachesTable), BorderLayout.CENTER);
		parent.add(new CreateProfilePanel(), BorderLayout.SOUTH);
		
		coachesTable.setRowSelectionAllowed(true);
		coachesTable.setDefaultRenderer(Object.class, new CustomRenderer());
		coachesTable.getColumnModel().getColumn(0).setMaxWidth(25);
		coachesTable.setRowSelectionAllowed(true);
		coachesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dtm.fireTableDataChanged();

		initRMenu();
	}
	
	private void initRMenu() {
		JMenuItem delete = new JMenuItem("Delete Coach");
		rmenu.add(delete);
		 
		JMenuItem counters = new JMenuItem("Counters");
		rmenu.add(counters);

		counters.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});

		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rmenu.setVisible(false);
				if (JOptionPane.showConfirmDialog(null, "Delete Coach?") == JOptionPane.OK_OPTION) {
					int row = coachesTable.getSelectedRow();
					int cid = Integer.parseInt(coachesTable.getValueAt(row, 0).toString());
					
					CounterApp.DBServices.deleteCoach(cid);
					updateUIProfileChanged();
				}
			}
		});
	}
	
	private void updateUIProfileChanged() {
		((CustomTableModel)coachesTable.getModel()).refresh();
		rmenu.setVisible(false);
	}

	class CreateProfilePanel extends JPanel {
		private static final long serialVersionUID = 5244913423336516955L;
		
		public CreateProfilePanel() {
		}
	}

	class CustomRenderer implements TableCellRenderer {
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
	
	class CustomTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private List<CoachTemplate> coaches = new ArrayList<CoachTemplate>();
		
		public CustomTableModel() {
			super();
			refresh();
		}
		
		public void refresh() {
			this.coaches = CounterApp.DBServices.getCoachTemplates();
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
			CoachTemplate ct = coaches.get(row);
			
			switch (column) {
				case 0:
					return ct.getId();
				case 1:
					return ct.getName();
			}
			throw new RuntimeException("Cannot get value :"+row+","+column);
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	
		@Override
		public int getRowCount() {
			return coaches.size();
		}
	}
}
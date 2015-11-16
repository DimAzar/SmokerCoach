package com.d.apps.scoach.ui.iframes;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import com.d.apps.scoach.CounterApp;
import com.d.apps.scoach.db.model.CounterTemplate;

public class ManageCountersIFrame extends AbstractManageEntityIFRame {
	private static final long serialVersionUID = -892682552079556150L;
	
	private JPopupMenu rmenu = new JPopupMenu();
	
	public ManageCountersIFrame() {
		super();
		
		initGrcs();
		initRMenu();

		setupListeners();
		
		setTitle("Manage System Counters");
		setName("Manage Counters IFrame");
		setSize(400, 200);
		setLocation(10,10);
		setClosable(true);
		setResizable(true);
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
	
	protected void initGrcs() {
		JPanel parent = (JPanel) getContentPane();
		parent.setLayout(new BorderLayout());
		
		AbstractTableModel dtm = new CustomCounteresTableModel();
		entityTable = new JTable(dtm);
		
		parent.add(new JScrollPane(entityTable), BorderLayout.CENTER);
		parent.add(new CreateCounteresPanel(), BorderLayout.SOUTH);
		
		entityTable.setRowSelectionAllowed(true);
		entityTable.setDefaultRenderer(Object.class, new CustomRenderer());
		entityTable.getColumnModel().getColumn(0).setMaxWidth(25);
		entityTable.setRowSelectionAllowed(true);
		entityTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dtm.fireTableDataChanged();

	}
	
	private void initRMenu() {
		JMenuItem delete = new JMenuItem("Delete Counter");
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
				if (JOptionPane.showConfirmDialog(null, "Delete Counter?") == JOptionPane.OK_OPTION) {
					int row = entityTable.getSelectedRow();
					int cid = Integer.parseInt(entityTable.getValueAt(row, 0).toString());
					
					CounterApp.DBServices.deleteCounterTemplate(cid);
					updateUIProfileChanged();
				}
			}
		});
	}
	
	private void updateUIProfileChanged() {
		((CustomCounteresTableModel)entityTable.getModel()).refresh();
		rmenu.setVisible(false);
	}

	class CreateCounteresPanel extends JPanel {
		private static final long serialVersionUID = 5244913423336516955L;
		private JTextField name = new JTextField(20);
		private JButton create = new JButton("Create");
		
		public CreateCounteresPanel() {
			add(name);
			add(create);

			create.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String nameval = name.getText();
					if (nameval.length() <= 0) {
						JOptionPane.showMessageDialog(null, "Name cannot be empty! ");
						return;
					}
					if (!isCounterNameUnique(nameval)) {
						JOptionPane.showMessageDialog(null, "Name must be unique! ");
						return;
					}

					CounterApp.DBServices.createCounterTemplate(name.getText());
					updateUIProfileChanged();
					name.setText("");
				}
			});
		}
		
		private boolean isCounterNameUnique(String name) {
			for (int i = 0; i < entityTable.getRowCount(); i++) {
				if (entityTable.getValueAt(i, 1).toString().equals(name)) {
					return false;
				}
			}
			
			return true;
		}
	}

	class CustomCounteresTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private List<CounterTemplate> coaches = new ArrayList<CounterTemplate>();
		
		public CustomCounteresTableModel() {
			super();
			refresh();
		}
		
		public void refresh() {
			this.coaches = CounterApp.DBServices.getCounterTemplates();
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
			CounterTemplate ct = coaches.get(row);
			
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
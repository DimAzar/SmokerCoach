package com.d.apps.scoach.ui.managers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
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

import com.d.apps.scoach.CounterApp;
import com.d.apps.scoach.Utilities;
import com.d.apps.scoach.db.model.CoachTemplate;

public class ManageCoachesIFrame extends AbstractManageEntityIFRame {
	private static final long serialVersionUID = -892682552079556150L;
	
	private JPopupMenu rmenu = new JPopupMenu();
	private JTable entityTable = null;

	public ManageCoachesIFrame() {
		super();
		setName(Utilities.NAME_COACHMANAGER);
		
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
		
		AbstractTableModel dtm = new CustomCoachesTableModel();
		entityTable = new JTable(dtm);
		
		parent.add(new JScrollPane(entityTable), BorderLayout.CENTER);
		parent.add(new CreateCoachesPanel(), BorderLayout.SOUTH);
		
		entityTable.setRowSelectionAllowed(true);
		entityTable.setDefaultRenderer(Object.class, new CoachesCustomRenderer());
		entityTable.getColumnModel().getColumn(0).setMaxWidth(25);
		entityTable.setRowSelectionAllowed(true);
		entityTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dtm.fireTableDataChanged();

		initRMenu();
		setTitle("Manage System Coaches ");
	}
	
	private void initRMenu() {
		JMenuItem delete = new JMenuItem("Delete Coach");
		rmenu.add(delete);
		 
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rmenu.setVisible(false);
				if (JOptionPane.showConfirmDialog(null, "Delete Coach?") == JOptionPane.OK_OPTION) {
					int row = entityTable.getSelectedRow();
					int cid = Integer.parseInt(entityTable.getValueAt(row, 0).toString());
					
					CounterApp.DBServices.deleteCoachTemplate(cid);
					updateUIProfileChanged();
				}
			}
		});
	}
	
	private void updateUIProfileChanged() {
		((CustomCoachesTableModel)entityTable.getModel()).refresh();
		rmenu.setVisible(false);
	}

	class CreateCoachesPanel extends JPanel {
		private static final long serialVersionUID = 5244913423336516955L;
		private JTextField name = new JTextField(20);
		private JButton create = new JButton("Create");
		
		public CreateCoachesPanel() {
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
					if (!isCoachNameUnique(nameval)) {
						JOptionPane.showMessageDialog(null, "Name must be unique! ");
						return;
					}

					CounterApp.DBServices.createCoachTemplate(name.getText());
					updateUIProfileChanged();
					name.setText("");
				}
			});
		}
		
		private boolean isCoachNameUnique(String name) {
			for (int i = 0; i < entityTable.getRowCount(); i++) {
				if (entityTable.getValueAt(i, 1).toString().equals(name)) {
					return false;
				}
			}
			
			return true;
		}
	}

	class CustomCoachesTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private List<CoachTemplate> coaches = new ArrayList<CoachTemplate>();
		
		public CustomCoachesTableModel() {
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

final class CoachesCustomRenderer implements TableCellRenderer {
	public final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		JComponent renderer = null;
		if (column == 0) {
			renderer = new JLabel(""+(row+1));
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
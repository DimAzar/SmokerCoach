package com.d.apps.scoach.ui.managers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
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
import com.d.apps.scoach.Utilities.CounterDimension;
import com.d.apps.scoach.Utilities.CounterFunctionType;
import com.d.apps.scoach.db.model.Counter;
import com.d.apps.scoach.db.model.Profile;
import com.d.apps.scoach.ui.managers.iface.ProfileSubManager;

public class ManageCountersIFrame extends AbstractManageEntityIFRame implements ProfileSubManager {
	private static final long serialVersionUID = -892682552079556150L;
	
	public ManageCountersIFrame(Profile profile) {
		super(profile);
		setName(Utilities.NAME_COUNTERSMANAGER);
		
		initGrcs();
		setupListeners();
		
		setSize(300, 500);
	}
	
	private void initGrcs() {
		JPanel p = (JPanel) new JPanel();
		p.setLayout(new BorderLayout());
		
		AbstractTableModel dtm = new CustomCounterTableModel();
		entityTable = new JTable(dtm);
		
		p.add(new JScrollPane(entityTable), BorderLayout.CENTER);
		p.add(new CreateCountersPanel(), BorderLayout.SOUTH);
		
		entityTable.setRowSelectionAllowed(true);
		entityTable.getColumnModel().getColumn(0).setMaxWidth(25);
		entityTable.setDefaultRenderer(Object.class, new CoachesCustomRenderer());
		entityTable.setRowSelectionAllowed(true);
		entityTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setContentPane(p);
		dtm.fireTableDataChanged();

		initRMenu();
		setTitle("Manage System Counters ");
	}
	
	private void initRMenu() {
		JMenuItem delete = new JMenuItem("Delete Counter");
		rmenu.add(delete);
		 
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rmenu.setVisible(false);
				if (JOptionPane.showConfirmDialog(null, "Delete Counter?") == JOptionPane.OK_OPTION) {
					int row = entityTable.getSelectedRow();
					int cid = Integer.parseInt(entityTable.getValueAt(row, 0).toString());

					profile.removeCounter(cid);
					profile = CounterApp.DBServices.updateProfile(profile);
					notifyActiveProfileChanged();
				}
			}
		});
	}

	class CreateCountersPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		//SWING
		private final String inputTypeDescr = CounterFunctionType.INPUT.getDescription();

		private JComboBox<String> typeCombo = new JComboBox<String>();
		private JComboBox<String> dimensionsCombo = new JComboBox<String>();
		private JTextField nameField = new JTextField();
		private JTextField stepValue1d = new JTextField("0"),
							stepValue2d = new JTextField("0"),
							stepValue3d = new JTextField("0");
		private JButton addButt = new JButton("Add counter");
		
		public CreateCountersPanel() {
			super();
		
			initGrcs();
			initModel();
		}

		
		private void initGrcs() {
			GridBagLayout layout = new GridBagLayout();
			
			layout.columnWidths = new int[] { 7, 7, 7, 7, 7 };
			layout.rowHeights   = new int[] { 7, 7, 7, 7, 7 };
			
			layout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.5 };
			layout.rowWeights    = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0 };
			setLayout(layout);
			
			add(new JLabel("Counter name"), new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 2, 10), 0, 0));
			add(nameField, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 2), 0, 0));
			add(dimensionsCombo, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 10), 0, 0));
			add(new JLabel("Counter type"), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 2, 10), 0, 0));
			add(new JLabel("Step"), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 2, 10), 0, 0));
			add(typeCombo, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 2), 0, 0));
			add(stepValue1d, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 2), 0, 0));
			add(stepValue2d, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 2), 0, 0));
			add(stepValue3d, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 10), 0, 0));
			add(addButt, new GridBagConstraints(0, 4, 4, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(10, 10, 0, 10), 0, 0));
			
			toggleCounterDimensions(false);
		}
		
		private void initModel() {

			DefaultComboBoxModel<String> dModel = new DefaultComboBoxModel<String>();
			for (CounterDimension  type : CounterDimension.values()) {
				dModel.addElement(type.getDescription());
			}
			dimensionsCombo.setModel(dModel);
			
			DefaultComboBoxModel<String> aModel = new DefaultComboBoxModel<String>();
			for (CounterFunctionType  type : CounterFunctionType.values()) {
				aModel.addElement(type.getDescription());
			}
			
			typeCombo.setModel(aModel);
			typeCombo.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String val = typeCombo.getSelectedItem().toString();
					if (val.equals(inputTypeDescr)) {
						toggleCounterDimensions(false);
					} else {
						toggleCounterDimensions(true);
					}
				}
			});
			typeCombo.setSelectedItem(inputTypeDescr);
			
			addButt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Counter instance = new Counter();
					instance.setName(nameField.getText());
					instance.setStepValue(Double.parseDouble(stepValue1d.getText()));
					instance.setType(CounterFunctionType.getId(typeCombo.getSelectedItem().toString()));
					
					profile.addCounter(instance);
			    	CounterApp.DBServices.updateProfile(profile);
			    	notifyActiveProfileChanged();
				}
			});
		}
		
		private void toggleCounterDimensions(Boolean value ) {
			stepValue1d.setEnabled(value);
			stepValue2d.setEnabled(value);
			stepValue3d.setEnabled(value);
		}
	}

	class CustomCounterTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		public CustomCounterTableModel() {
			super();
			fireTableDataChanged();
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
				return "Step";
				
			default:
				return "N/A";
			}
		}
	
		@Override
		public Object getValueAt(int row, int column) {
			Counter ct = profile.getCounters().get(row);
			
			switch (column) {
				case 0:
					return ct.getId();
				case 1:
					return ct.getName();
				case 2:
					return ct.getType();
				case 3:
					return ct.getStepValue();
			}
			throw new RuntimeException("Cannot get value :"+row+","+column);
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	
		@Override
		public int getRowCount() {
			return profile.getCounters().size();
		}
	}
}
final class CounterCustomRenderer implements TableCellRenderer {
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
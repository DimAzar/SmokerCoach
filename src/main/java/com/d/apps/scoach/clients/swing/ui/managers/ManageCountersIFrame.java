package com.d.apps.scoach.clients.swing.ui.managers;

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

import com.d.apps.scoach.clients.swing.CounterAppClient;
import com.d.apps.scoach.clients.swing.Utilities;
import com.d.apps.scoach.clients.swing.Utilities.CounterDimension;
import com.d.apps.scoach.clients.swing.Utilities.CounterFunctionType;
import com.d.apps.scoach.clients.swing.Utilities.CounterSize;
import com.d.apps.scoach.clients.swing.ui.managers.iface.ProfileSubManager;
import com.d.apps.scoach.server.db.model.Counter;
import com.d.apps.scoach.server.db.model.Profile;

public class ManageCountersIFrame extends AbstractManageEntityIFrame implements ProfileSubManager {
	private static final long serialVersionUID = -892682552079556150L;
	
	public ManageCountersIFrame(Profile profile) {
		super(profile);
		setName(Utilities.NAME_COUNTERSMANAGER);
		
		initGrcs();
		setupListeners();
		
		setSize(450, 450);
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
					profile = CounterAppClient.DBServices.updateProfile(profile);
					notifyActiveProfileChanged();
				}
			}
		});
	}

	class CreateCountersPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		//SWING
		private JComboBox<String> typeCombo = new JComboBox<String>();
		private JComboBox<String> dimensionsCombo = new JComboBox<String>();
		private JTextField nameField = new JTextField();
		private JTextField stepValue1d = new JTextField("0",6),
							stepValue2d = new JTextField("0", 6),
							stepValue3d = new JTextField("0", 6);
		private JButton addButt = new JButton("Add counter");
		
		public CreateCountersPanel() {
			super();
		
			initGrcs();
			initModel();
		}

		
		private void initGrcs() {
			GridBagLayout layout = new GridBagLayout();
			
			layout.columnWidths = new int[] { 7, 7, 50, 50, 50 };
			layout.rowHeights   = new int[] { 7, 7, 7, 7, 7 };
			
			layout.columnWeights = new double[] { 0.5, 0.5, 0.0, 0.0, 0.0 };
			layout.rowWeights    = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0 };
			setLayout(layout);
			
			add(new JLabel("Counter name"), new GridBagConstraints(0, 0, 5, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 2, 10), 0, 0));
			add(new JLabel("Counter Value"),new GridBagConstraints(3, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 2, 10), 0, 0));
			add(nameField,					new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 2), 0, 0));
			add(dimensionsCombo, 			new GridBagConstraints(2, 1, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 10), 0, 0));
			add(new JLabel("Counter type"), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 2, 10), 0, 0));
			add(new JLabel("Step"), 		new GridBagConstraints(2, 2, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 2, 10), 0, 0));
			
			add(typeCombo  , new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 2), 0, 0));
			add(stepValue1d, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 2), 0, 0));
			add(stepValue2d, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 2), 0, 0));
			add(stepValue3d, new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 10), 0, 0));
			
			add(addButt, new GridBagConstraints(0, 4, 5, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
			
			toggleCounterDimensions(false);
		}
		
		private void initModel() {

			DefaultComboBoxModel<String> dModel = new DefaultComboBoxModel<String>();
			for (CounterSize type : CounterSize.values()) {
				dModel.addElement(type.getDescription());
			}
			dimensionsCombo.setModel(dModel);
			dimensionsCombo.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					toggleOnDimension();
				}
			});
			dimensionsCombo.setSelectedItem(CounterDimension.X);
			toggleOnDimension();
			DefaultComboBoxModel<String> aModel = new DefaultComboBoxModel<String>();
			for (CounterFunctionType  type : CounterFunctionType.values()) {
				aModel.addElement(type.getDescription());
			}
			
			typeCombo.setModel(aModel);
			typeCombo.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (typeCombo.getSelectedItem() == CounterFunctionType.INPUT) {
						toggleOnDimension();
					}
				}
			});
			typeCombo.setSelectedItem(CounterFunctionType.INPUT);
			
			addButt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int len = nameField.getText().length();
					if (len == 0) {
						JOptionPane.showMessageDialog(null, "Name cannot be empty");
						return;
					}
					
					Counter instance = new Counter();
					instance.setName(nameField.getText());
					
					instance.setType(CounterFunctionType.getId(typeCombo.getSelectedItem().toString()));
					instance.setDimension(CounterSize.getId(dimensionsCombo.getSelectedItem().toString()));

					instance.setStepValueX(Double.parseDouble(stepValue1d.getText()));
					instance.setStepValueY(Double.parseDouble(stepValue2d.getText()));
					instance.setStepValueZ(Double.parseDouble(stepValue3d.getText()));

					
					profile.addCounter(instance);
			    	CounterAppClient.DBServices.updateProfile(profile);
			    	notifyActiveProfileChanged();
				}
			});
		}
		
		private void toggleCounterDimensions(Boolean value ) {
			stepValue1d.setEnabled(value);
			stepValue2d.setEnabled(value);
			stepValue3d.setEnabled(value);
		}
		
		private void toggleOnDimension() {
			if (dimensionsCombo.getSelectedItem() == CounterSize.D1.getDescription()) {
				stepValue1d.setEnabled(true);
				stepValue2d.setEnabled(false);
				stepValue3d.setEnabled(false);
			} else 					
			if (dimensionsCombo.getSelectedItem() == CounterSize.D2.getDescription()) {
				stepValue1d.setEnabled(true);
				stepValue2d.setEnabled(true);
				stepValue3d.setEnabled(false);
			} else {
				toggleCounterDimensions(true);
			}
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
			return 7;
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
				return "Dimension";
			case 4:
				return "Step X";
			case 5:
				return "Step Y";
			case 6:
				return "Step Z";
				
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
					return ct.getDimension();					
				case 4:
					return ct.getStepValueX();
				case 5:
					return ct.getStepValueY();
				case 6:
					return ct.getStepValueZ();					
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
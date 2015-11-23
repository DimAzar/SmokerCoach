package com.d.apps.scoach.ui.managers;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.d.apps.scoach.CounterApp;
import com.d.apps.scoach.Utilities;
import com.d.apps.scoach.Utilities.CounterFunctionType;

public class AddCounterToCoachDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	//SWING
	private final String inputTypeDescr = CounterFunctionType.INPUT.getDescription();

	private JComboBox<String> typeCombo = new JComboBox<String>();
	private JTextField nameField = new JTextField();
	private JTextField stepValue = new JTextField("0");
	private JButton addButt = new JButton("Add counter");
	
	private int coachId; 
	
	public AddCounterToCoachDialog(int coachId) {
		super();
	
		this.coachId = coachId;
		
		initGrcs();
		initModel();
	}

	private void initGrcs() {
		Container parent = getContentPane();
		
		GridBagLayout layout = new GridBagLayout();
		
		layout.columnWidths = new int[] { 7, 7 };
		layout.rowHeights   = new int[] { 7, 7, 7, 7, 7 };
		
		layout.columnWeights = new double[] { 0.5, 0.5 };
		layout.rowWeights    = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0 };
		parent.setLayout(layout);
		
		parent.add(new JLabel("Counter name"), new GridBagConstraints(0, 0, 2, 1, .0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 2, 10), 0, 0));
		parent.add(nameField, new GridBagConstraints(0, 1, 2, 1, .0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));

		parent.add(new JLabel("Counter type"), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 2, 10), 0, 0));
		parent.add(new JLabel("Step"), new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 2, 10), 0, 0));
		parent.add(typeCombo, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 2), 0, 0));
		parent.add(stepValue, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 10), 0, 0));
		parent.add(addButt, new GridBagConstraints(0, 4, 2, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 10), 0, 0));
		
		stepValue.setEnabled(false);
		
		setModal(true);
		setTitle("Add Counter to Coach");
		setName(Utilities.NAME_ADDCOUNTER);
		setSize(300,200);
	}
	
	private void initModel() {

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
					stepValue.setEnabled(false);
				} else {
					stepValue.setEnabled(true);
				}
			}
		});
		typeCombo.setSelectedItem(inputTypeDescr);
		
		addButt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CounterApp.DBServices.createCounter(
						coachId, 
						nameField.getText(), 
						CounterFunctionType.getId(typeCombo.getSelectedItem().toString()), 
						Double.parseDouble(stepValue.getText()));
			}
		});
	}
}
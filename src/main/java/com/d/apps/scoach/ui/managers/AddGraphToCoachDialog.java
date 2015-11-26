package com.d.apps.scoach.ui.managers;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.text.Position;

import com.d.apps.scoach.CounterApp;
import com.d.apps.scoach.Utilities;
import com.d.apps.scoach.db.model.CoachInstance;
import com.d.apps.scoach.db.model.Counter;

public class AddGraphToCoachDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private final JTextField nameField = new JTextField();
	private final JButton addButt = new JButton("Create graph");
	
	private final JButton addCounterButt = new JButton(">");
	private final JButton removeCounterButt = new JButton("<");
	private final JButton addAllCounterButt = new JButton(">>");
	private final JButton removeAllCounterButt = new JButton("<<");
	
	private final JList<String> availableCountersList = new JList<String>();
	private final JList<String> addedCountersList = new JList<String>();
	
	private CoachInstance coach; 
	
	public AddGraphToCoachDialog(CoachInstance coach) {
		super();
	
		this.coach = coach;
		
		initGrcs();
		initModel();
	}

	private void initGrcs() {
		Container parent = getContentPane();
		
		GridBagLayout layout = new GridBagLayout();
		
		layout.columnWidths = new int[] { 7, 7 , 7};
		layout.rowHeights   = new int[] { 7, 7, 7, 7, 7 };
		
		layout.columnWeights = new double[] { .5,0.0,.5 };
		layout.rowWeights    = new double[] { 0.0, 0.0, 0.0, .5, 0.0, 0.0, 0.5, 0.0 };
		parent.setLayout(layout);
		
		parent.add(new JLabel("Graph name"), new GridBagConstraints(0, 0, 3, 1, .0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 10), 0, 0));
		parent.add(nameField, new GridBagConstraints(0, 1, 3, 1, .0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 10), 0, 0));

		parent.add(new JLabel("Graph Counters"), new GridBagConstraints(0, 2, 3, 1, .0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 10), 0, 0));
		parent.add(new JScrollPane(availableCountersList), new GridBagConstraints(0, 3, 1, 4, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 10, 5, 10), 0, 0));
		parent.add(new JScrollPane(addedCountersList), new GridBagConstraints(2, 3, 1, 4, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 10, 5, 10), 0, 0));

		parent.add(addCounterButt, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		parent.add(removeCounterButt, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		parent.add(addAllCounterButt, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		parent.add(removeAllCounterButt, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		
		parent.add(addButt, new GridBagConstraints(0, 7, 3, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		
		addCounterButt.setPreferredSize(new Dimension(50, 20));
		removeCounterButt.setPreferredSize(new Dimension(50, 20));
		addAllCounterButt.setPreferredSize(new Dimension(50, 20));
		removeAllCounterButt.setPreferredSize(new Dimension(50, 20));
		
		setModal(true);
		setTitle("Add Graph to Coach");
		setName(Utilities.NAME_ADDCOUNTER);
		setSize(500,350);
	}
	
	private void initModel() {
		DefaultListModel<String> dlm = new DefaultListModel<String>();
		for (Counter  c : coach.getCounters()) {
			dlm.addElement(c.getName());
		}
		
		availableCountersList.setModel(dlm);
		availableCountersList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		addedCountersList.setModel(new DefaultListModel<String>());
		addedCountersList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		addCounterButt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String toBeAdded = availableCountersList.getSelectedValue();
				
				if (addedCountersList.getModel().getSize() > 0) {
					if (addedCountersList.getNextMatch(toBeAdded,0,Position.Bias.Forward) == -1) {
						((DefaultListModel<String>)addedCountersList.getModel()).addElement(toBeAdded);
					};
				} else {
					((DefaultListModel<String>)addedCountersList.getModel()).addElement(toBeAdded);
				}
			}
		});
		
		removeCounterButt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				((DefaultListModel<String>)addedCountersList.getModel()).removeElement(addedCountersList.getSelectedValue());
			}
		});		
		
		addButt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Integer> counterIds = new ArrayList<Integer>();

				Enumeration<String> vals = ((DefaultListModel<String>)addedCountersList.getModel()).elements();
				while (vals.hasMoreElements()) {
					String value = (String) vals.nextElement();
					for (Counter c : coach.getCounters()) {
						if (c.getName().equals(value)) {
							counterIds.add(c.getId());
						}
					}
				}

				CounterApp.DBServices.addGraph (
						coach.getId(), 
						nameField.getText(), 
						counterIds);
			}
		});
	}
}
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
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.text.Position;

import com.d.apps.scoach.CounterApp;
import com.d.apps.scoach.Utilities;
import com.d.apps.scoach.Utilities.ChartPlotType;
import com.d.apps.scoach.Utilities.CounterDimension;
import com.d.apps.scoach.Utilities.GraphAxisHigherFunctions;
import com.d.apps.scoach.Utilities.GraphDimensions;
import com.d.apps.scoach.Utilities.HigherFunctionTypes;
import com.d.apps.scoach.db.model.Coach;
import com.d.apps.scoach.db.model.CoachGraph;
import com.d.apps.scoach.db.model.Counter;

public class AddGraphToCoachDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static final String ACMND_CREATEGRAPH = "CREATE";
	private static final String ACMND_UPDATEGRAPH = "UPDATE";
	private static final String ACMND_XDIM = "X";
	private static final String ACMND_YDIM = "Y";
	private static final String ACMND_ZDIM = "Z";
	
	private final JTextField nameField = new JTextField();
	private final JButton addButt = new JButton();
	
	private final JButton addCounterButt = new JButton(">");
	private final JButton removeCounterButt = new JButton("<");
	private final JButton addAllCounterButt = new JButton(">>");
	private final JButton removeAllCounterButt = new JButton("<<");
	
	private final JCheckBox higherFuncBox = new JCheckBox("Higher Time Functions", false);
	private final JList<String> availableCountersList = new JList<String>();
	private final JList<String> addedCountersList = new JList<String>();
	
	private final JComboBox<String> plotTypeCombo = new JComboBox<String>(),
									counterXDimensionCombo = new JComboBox<String>(),
									counterYDimensionCombo = new JComboBox<String>(),
									counterZDimensionCombo = new JComboBox<String>(),
									counterXHFuncCombo = new JComboBox<String>(),
									counterYHFuncCombo = new JComboBox<String>(),
									counterZHFuncCombo = new JComboBox<String>(),
									graphDimensionCombo = new JComboBox<String>();
	
	private Coach coach; 
	private CoachGraph editingGraph;
	private List<Counter> profileCounters;
	
	
	public AddGraphToCoachDialog(Coach coach, CoachGraph editingGraph, List<Counter> profilecounters) {
		super();
	
		this.coach = coach;
		this.editingGraph = editingGraph;
		this.profileCounters = profilecounters;
		initGrcs();
		initModel();
		createListeners();
		
		if (editingGraph != null) {
			counterXDimensionCombo.setSelectedItem(editingGraph.getXAxisDataFetch().getDescription());
			counterYDimensionCombo.setSelectedItem(editingGraph.getYAxisDataFetch().getDescription());
			if (editingGraph.getGraphDimension() == GraphDimensions.D3GRAPH) {
				counterZDimensionCombo.setSelectedItem(editingGraph.getZAxisDataFetch().getDescription());
			}
			plotTypeCombo.setSelectedItem(editingGraph.getPlotType().getDescription());
			graphDimensionCombo.setSelectedItem(editingGraph.getGraphDimension().getDescription());
			
			counterXHFuncCombo.setSelectedItem(editingGraph.getGraphXHFunc().name());
			counterYHFuncCombo.setSelectedItem(editingGraph.getGraphYHFunc().name());
			counterZHFuncCombo.setSelectedItem(editingGraph.getGraphZHFunc().name());
			
			DefaultListModel<String> dlm5 = new DefaultListModel<String>();
			for (Counter  c : editingGraph.getCounters()) {
				dlm5.addElement(c.getName());
			}
			addedCountersList.setModel(dlm5);
			nameField.setText(editingGraph.getName());
		} else {
			addedCountersList.setModel(new DefaultListModel<String>());
			counterXDimensionCombo.setSelectedItem(CounterDimension.X.getDescription());
			counterYDimensionCombo.setSelectedItem(CounterDimension.Y.getDescription());
			counterZDimensionCombo.setSelectedItem(CounterDimension.Z.getDescription());
			counterXHFuncCombo.setSelectedItem(GraphAxisHigherFunctions.NONE.name());
			counterYHFuncCombo.setSelectedItem(GraphAxisHigherFunctions.NONE.name());
			counterZHFuncCombo.setSelectedItem(GraphAxisHigherFunctions.NONE.name());

			plotTypeCombo.setSelectedItem(ChartPlotType.LINE.getDescription());
			graphDimensionCombo.setSelectedItem(GraphDimensions.D2GRAPH.getDescription());
		}
		toggleHFuncBoxes(false);
		canHaveHigherFunctions();
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		JComboBox<String> source = (JComboBox<String>)e.getSource();
		CounterDimension dim = CounterDimension.getId(source.getSelectedItem().toString());
		DefaultComboBoxModel<String> dlm = new DefaultComboBoxModel<String>();
		for (GraphAxisHigherFunctions c : GraphAxisHigherFunctions.values()) {
			if ((c.getType() == HigherFunctionTypes.DATE || c.getType() == HigherFunctionTypes.COMMON) && 	dim == CounterDimension.T) {
				dlm.addElement(c.name());
			}
			if ((c.getType() == HigherFunctionTypes.NUMERIC || c.getType() == HigherFunctionTypes.COMMON) && 	dim != CounterDimension.T) {
				dlm.addElement(c.name());
			}
		}
		
		if (source.getActionCommand().equals(ACMND_XDIM)) {
			counterXHFuncCombo.setModel(dlm);	
		} else
		if (source.getActionCommand().equals(ACMND_YDIM)) {
			counterYHFuncCombo.setModel(dlm);	
		} else { 
			counterZHFuncCombo.setModel(dlm);	
		}
		
		canHaveHigherFunctions();
	}

	private void canHaveHigherFunctions() {
		boolean isXTime = CounterDimension.getId(counterXDimensionCombo.getSelectedItem().toString()) == CounterDimension.T;
		boolean isYTime = CounterDimension.getId(counterYDimensionCombo.getSelectedItem().toString()) == CounterDimension.T;
		boolean isZTime = CounterDimension.getId(counterZDimensionCombo.getSelectedItem().toString()) == CounterDimension.T;
		
		higherFuncBox.setEnabled(isZTime || isXTime || isYTime); 
	}
	
	private void initGrcs() {
		Container parent = getContentPane();
		
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 7 };
		layout.rowHeights   = new int[] { 7, 7, 7, 7};
		layout.columnWeights = new double[] { 1.0 };
		layout.rowWeights    = new double[] { 1.0, 0.0, 0.0, 1.0 };
		parent.setLayout(layout);

		parent.add(createTopPanel(),new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 0, 10), 0, 0));
		parent.add(createCounterPanel(),new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 10, 0, 10), 0, 0));
		parent.add(createBotPanel(),new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 10, 10, 10), 0, 0));
		
		addCounterButt.setPreferredSize(new Dimension(50, 20));
		removeCounterButt.setPreferredSize(new Dimension(50, 20));
		addAllCounterButt.setPreferredSize(new Dimension(50, 20));
		removeAllCounterButt.setPreferredSize(new Dimension(50, 20));
		
		if (editingGraph != null) {
			addButt.setActionCommand(ACMND_UPDATEGRAPH);
			addButt.setText("Update Graph");
		} else {
			addButt.setActionCommand(ACMND_CREATEGRAPH);
			addButt.setText("Create graph");
		}
		setModal(true);
		setTitle("Add Graph to Coach");
		setName(Utilities.NAME_ADDCOUNTER);
		setSize(700,561);
	}

	private JPanel createTopPanel() {
		JPanel parent = new JPanel();
		JLabel label = new JLabel("Z Axis");
		JLabel label_2 = new JLabel("X Axis");
		JLabel label_1 = new JLabel("Y Axis");

		JLabel label2 = new JLabel("Z Axis");
		JLabel label2_2 = new JLabel("X Axis");
		JLabel label2_1 = new JLabel("Y Axis");

		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 7, 7, 7, 7, 7, 7, 0 };
		layout.rowHeights   = new int[] { 7, 7, 7, 7, 0, 7};
		layout.columnWeights = new double[] { .0, 0.0, 0.0, 0.0 , 0, .0, 0.0 };
		layout.rowWeights    = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
		parent.setLayout(layout);

		parent.add(new JLabel("Graph name"), new GridBagConstraints(0, 0, 1, 1, .0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 5), 0, 0));
		parent.add(nameField, new GridBagConstraints(0, 1, 7, 1, .0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));

		parent.add(new JLabel("Graph Plot Type"), new GridBagConstraints(0, 2, 1, 1, .0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 5), 0, 0));
		parent.add(plotTypeCombo, new GridBagConstraints(1, 2, 6, 1, .0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));
		parent.add(new JLabel("Graph Dimensions"), new GridBagConstraints(0, 3, 1, 1, .0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 5), 0, 0));
		parent.add(graphDimensionCombo, new GridBagConstraints(1, 3, 6, 1, .0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));
		parent.add(label_2, new GridBagConstraints(1, 4, 1, 1, .0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 5), 0, 0));
		parent.add(counterXDimensionCombo, new GridBagConstraints(2, 4, 1, 1, .0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 5), 0, 0));
		parent.add(label_1, new GridBagConstraints(3, 4, 1, 1, .0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 5), 0, 0));
		parent.add(counterYDimensionCombo, new GridBagConstraints(4, 4, 1, 1, .0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 5), 0, 0));
		parent.add(label, new GridBagConstraints(5, 4, 1, 1, .0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 5), 0, 0));
		parent.add(counterZDimensionCombo, new GridBagConstraints(6, 4, 1, 1, .0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));
		
		parent.add(higherFuncBox, new GridBagConstraints(0, 5, 1, 1, .0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 5), 0, 0));
		parent.add(label2_2, new GridBagConstraints(1, 5, 1, 1, .0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 5), 0, 0));
		parent.add(counterXHFuncCombo, new GridBagConstraints(2, 5, 1, 1, .0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 5), 0, 0));
		parent.add(label2_1, new GridBagConstraints(3, 5, 1, 1, .0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 5), 0, 0));
		parent.add(counterYHFuncCombo, new GridBagConstraints(4, 5, 1, 1, .0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 5), 0, 0));
		parent.add(label2, new GridBagConstraints(5, 5, 1, 1, .0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 5), 0, 0));
		parent.add(counterZHFuncCombo, new GridBagConstraints(6, 5, 1, 1, .0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 0, 0));
		return parent;
		
	}
	
	private JPanel createBotPanel() {
		JPanel parent = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 7 };
		layout.rowHeights   = new int[] { 7, 7, 7, 7};
		layout.columnWeights = new double[] { 1.0 };
		layout.rowWeights    = new double[] { 0.0, 0.0, 0.0, 1.0 };
		parent.setLayout(layout);

		return parent;
		
	}
	private JPanel createCounterPanel() {
		JPanel parent = new JPanel();
		GridBagLayout layout = new GridBagLayout();

		layout.columnWidths = new int[] { 7, 7, 7, 7, 7 , 7};
		layout.rowHeights   = new int[] { 7, 7, 7, 7, 7 };
		
		layout.columnWeights = new double[] { .5,0.0, 0.0, 0.0, 0.0,.5 };
		layout.rowWeights    = new double[] { 0.0, 0.0, 0.0, .5, 0.0, 0.0, 0.5, 0.0 };
		parent.setLayout(layout);

		parent.add(new JLabel("Graph Counters"), new GridBagConstraints(0, 2, 6, 1, .0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 10), 0, 0));
		parent.add(new JScrollPane(availableCountersList), new GridBagConstraints(0, 3, 1, 4, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 10, 5, 10), 0, 0));
		parent.add(new JScrollPane(addedCountersList), new GridBagConstraints(2, 3, 4, 4, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 10, 5, 10), 0, 0));

		parent.add(addCounterButt, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(2, 2, 5, 5), 0, 0));
		parent.add(removeCounterButt, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 5, 5), 0, 0));
		parent.add(addAllCounterButt, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 5, 5), 0, 0));
		parent.add(removeAllCounterButt, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(2, 2, 5, 5), 0, 0));
		
		parent.add(addButt, new GridBagConstraints(0, 7, 6, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));
		return parent;
	}
	
	private void initModel() {
		DefaultComboBoxModel<String> dlm = new DefaultComboBoxModel<String>();
		for (CounterDimension c : CounterDimension.values()) {
			dlm.addElement(c.getDescription());
		}
		counterXDimensionCombo.setModel(dlm);
		
		DefaultComboBoxModel<String> dlm1 = new DefaultComboBoxModel<String>();
		for (CounterDimension c : CounterDimension.values()) {
			dlm1.addElement(c.getDescription());
		}
		counterYDimensionCombo.setModel(dlm1);
		
		DefaultComboBoxModel<String> dlm2 = new DefaultComboBoxModel<String>();
		for (CounterDimension c : CounterDimension.values()) {
			dlm2.addElement(c.getDescription());
		}
		counterZDimensionCombo.setModel(dlm2);
		
		DefaultComboBoxModel<String> dlm0 = new DefaultComboBoxModel<String>();
		for (ChartPlotType  c : ChartPlotType.values()) {
			dlm0.addElement(c.getDescription());
		}
		plotTypeCombo.setModel(dlm0);

		DefaultListModel<String> dlm4 = new DefaultListModel<String>();
		for (Counter  c : profileCounters) {
			dlm4.addElement(c.getName());
		}
		availableCountersList.setModel(dlm4);
		
		DefaultComboBoxModel<String> dlm3 = new DefaultComboBoxModel<String>();
		for (GraphDimensions  c : GraphDimensions.values()) {
			dlm3.addElement(c.getDescription());
		}
		graphDimensionCombo.setModel(dlm3);
		
		graphDimensionCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (GraphDimensions.getId(graphDimensionCombo.getSelectedItem().toString()) == GraphDimensions.D2GRAPH) {
					counterXDimensionCombo.setEnabled(true);
					counterYDimensionCombo.setEnabled(true);
					counterZDimensionCombo.setEnabled(false);
					if (higherFuncBox.isSelected()) {
						counterXHFuncCombo.setEnabled(true);
						counterYHFuncCombo.setEnabled(true);
						counterZHFuncCombo.setEnabled(false);
					}
				} else {
					counterXDimensionCombo.setEnabled(true);
					counterYDimensionCombo.setEnabled(true);
					counterZDimensionCombo.setEnabled(true);
					if (higherFuncBox.isSelected()) {
						counterXHFuncCombo.setEnabled(true);
						counterYHFuncCombo.setEnabled(true);
						counterZHFuncCombo.setEnabled(true);
					}
				}
			}
		});
		
		availableCountersList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		addedCountersList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
	}
	
	private void toggleHFuncBoxes(boolean b) {
		counterXHFuncCombo.setEnabled(b);
		counterYHFuncCombo.setEnabled(b);
		counterZHFuncCombo.setEnabled(b);
	}
	
	private void createListeners() {
		counterXDimensionCombo.addActionListener(this);
		counterXDimensionCombo.setActionCommand(ACMND_XDIM);
		counterYDimensionCombo.addActionListener(this);
		counterYDimensionCombo.setActionCommand(ACMND_YDIM);
		counterZDimensionCombo.addActionListener(this);
		counterZDimensionCombo.setActionCommand(ACMND_ZDIM);
		
		higherFuncBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleHFuncBoxes(higherFuncBox.isSelected());
			}
		} );
		
		addCounterButt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (addedCountersList.getModel().getSize() > 0) {
					JOptionPane.showMessageDialog(null, "Multi-counter graphs not functional yet");
					return;
				}
				
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
				String accommand = ((JButton)e.getSource()).getActionCommand();
				
				if (accommand.equals(ACMND_CREATEGRAPH)) {
					createNewGraph();
					JOptionPane.showMessageDialog(null, "Graph created");
					dispose();
				} else {
					editingGraph.setName(nameField.getText());
					editingGraph.setGraphDimension(GraphDimensions.getId(graphDimensionCombo.getSelectedItem().toString()));
					editingGraph.setPlotType(ChartPlotType.getId(plotTypeCombo.getSelectedItem().toString()));
					editingGraph.setXAxisDataFetch(CounterDimension.getId(counterXDimensionCombo.getSelectedItem().toString()));
					editingGraph.setYAxisDataFetch(CounterDimension.getId(counterYDimensionCombo.getSelectedItem().toString()));
					editingGraph.setZAxisDataFetch(CounterDimension.getId(counterZDimensionCombo.getSelectedItem().toString()));

					editingGraph.setGraphXHFunc(GraphAxisHigherFunctions.valueOf(counterXHFuncCombo.getSelectedItem().toString()));
					editingGraph.setGraphYHFunc(GraphAxisHigherFunctions.valueOf(counterYHFuncCombo.getSelectedItem().toString()));
					editingGraph.setGraphZHFunc(GraphAxisHigherFunctions.valueOf(counterZHFuncCombo.getSelectedItem().toString()));
					
					updateGraphCounters();
					CounterApp.DBServices.updateGraph (editingGraph);
					JOptionPane.showMessageDialog(null, "Graph updated");
					dispose();
				}
			}
		});		
	}

	private void updateGraphCounters() {
		Enumeration<String> vals = ((DefaultListModel<String>)addedCountersList.getModel()).elements();
		while (vals.hasMoreElements()) {
			boolean found = false;
			String value = (String) vals.nextElement();
			for (Counter c : editingGraph.getCounters()) {
				if (c.getName().equals(value)) {
					found = true;
					break;
				}
			}
			
			if (!found) {
				//TODO ..
				//editingGraph.addGraphCounter(Utilities.findCoachCounterFromName(coach, value));
			}
		}
		
		vals = ((DefaultListModel<String>)addedCountersList.getModel()).elements();
		Vector<Counter> toBeRemoved = new Vector<Counter>();
		
		for (Counter  counter : editingGraph.getCounters()) {
			boolean found = false;
			while (vals.hasMoreElements()) {
				String value = (String) vals.nextElement();
				if (counter.getName().equals(value)) {
					found = true;
					break;
				}
			}
			
			if (!found) {
				toBeRemoved.add(counter);
			}
		}
		for (Counter counter : toBeRemoved) {
			editingGraph.removeGraphCounter(counter);
		}
	}
	
	private void createNewGraph() {
		ArrayList<Integer> counterIds = new ArrayList<Integer>();

		Enumeration<String> vals = ((DefaultListModel<String>)addedCountersList.getModel()).elements();
		while (vals.hasMoreElements()) {
			String value = (String) vals.nextElement();
			for (Counter c : profileCounters) {
				if (c.getName().equals(value)) {
					counterIds.add(c.getId());
				}
			}
		}
		
		CounterDimension[] arr = new CounterDimension[3];
		GraphAxisHigherFunctions[] arr2 = new GraphAxisHigherFunctions[3];
		
		if (graphDimensionCombo.getSelectedItem().toString().equals(GraphDimensions.D2GRAPH.getDescription())) {
			arr[0] = CounterDimension.getId(counterXDimensionCombo.getSelectedItem().toString());
			arr[1] = CounterDimension.getId(counterYDimensionCombo.getSelectedItem().toString());
			arr[2] = CounterDimension.NONE;
		} else {
			arr[0] = CounterDimension.getId(counterXDimensionCombo.getSelectedItem().toString());
			arr[1] = CounterDimension.getId(counterYDimensionCombo.getSelectedItem().toString());
			arr[2] = CounterDimension.getId(counterZDimensionCombo.getSelectedItem().toString());
		}
		
		if (higherFuncBox.isSelected()) {
			arr2[0] = GraphAxisHigherFunctions.valueOf(counterXHFuncCombo.getSelectedItem().toString());
			arr2[1] = GraphAxisHigherFunctions.valueOf(counterYHFuncCombo.getSelectedItem().toString());
			arr2[2] = GraphAxisHigherFunctions.valueOf(counterZHFuncCombo.getSelectedItem().toString());
		} else {
			arr2[0] = GraphAxisHigherFunctions.NONE;
			arr2[1] = GraphAxisHigherFunctions.NONE;
			arr2[2] = GraphAxisHigherFunctions.NONE;
		}
		CounterApp.DBServices.addGraph (
								coach.getId(), 
								nameField.getText(), 
								counterIds,
								GraphDimensions.getId(graphDimensionCombo.getSelectedItem().toString()),
								ChartPlotType.getId(plotTypeCombo.getSelectedItem().toString()),
								arr,
								arr2
								);
	}
}
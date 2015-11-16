package com.d.apps.scoach.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.d.apps.scoach.CounterApp;
import com.d.apps.scoach.db.model.CoachCounter;
import com.d.apps.scoach.db.model.CoachTemplate;

public class CoachCountersDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private JButton saveButt = new JButton("Save");
	private List<CoachCounter> currentCoachCounters = null;
	private MainFrame controller = null;
	private List<JCheckBox> options = new ArrayList<JCheckBox>();
	
	public CoachCountersDialog(MainFrame controller, List<CoachCounter> currentCoachCounters) {
		this.controller = controller;
		this.currentCoachCounters = currentCoachCounters;
		
		initGrcs();
		
		setSize(350, 300);
		setModal(true);
	}
	
	private void initGrcs() {
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.add(createCoachesPanel(), BorderLayout.CENTER);
		c.add(saveButt, BorderLayout.SOUTH);
		saveButt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (JCheckBox box : options) {
					if (box.isSelected() && !isCounterSetInCoach(box.getName())) {
					} else
					if (!box.isSelected() && isCounterSetInCoach(box.getName())) {
					}
				}
			}
		});
	}
	
	private JPanel createCoachesPanel() {
		JPanel ans = new JPanel();
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]	{7, 7, };
		gridBagLayout.columnWeights = new double[]	{.5 , .5};

		List<CoachTemplate> allCoaches = CounterApp.DBServices.getCoachTemplates();
		double[] da = new double[allCoaches.size()+1];
		int[] ia = new int[allCoaches.size()+1];
		for (int i = 0; i < allCoaches.size()+1; i++) {
			da[i] = Double.MIN_VALUE;
			ia[i] = 7;
		}
		da[allCoaches.size()] = 1;
		gridBagLayout.rowWeights = da;
		gridBagLayout.rowHeights = ia;

		ans.setLayout(gridBagLayout);

		int rowcnt = 0;

		for (CoachTemplate coachEntry : allCoaches) {
			JLabel l = new JLabel(coachEntry.getName());
			JCheckBox chkb = new JCheckBox("enable", isCounterSetInCoach(coachEntry.getId()));
			chkb.setName(coachEntry.getName());
			
			ans.add(l, new GridBagConstraints(0,rowcnt,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0,0));
			ans.add(chkb, new GridBagConstraints(1,rowcnt,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 10), 0,0));
			rowcnt++;
			
			options.add(chkb);
		}

		ans.setBorder(BorderFactory.createEtchedBorder());
		return ans;
	}
	
	private boolean isCounterSetInCoach(int id) {
		for (CoachCounter coachCounter : currentCoachCounters) {
			if (coachCounter.getCounter().getId() == id) {
				return true;
			}
		}
		return false;
	}

	private boolean isCounterSetInCoach(String name) {
		for (CoachCounter coachCounter : currentCoachCounters) {
			if (coachCounter.getCounter().getTemplate().getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
}
package com.d.apps.scoach.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d.apps.scoach.CounterApp;
import com.d.apps.scoach.Utilities;
import com.d.apps.scoach.db.model.CoachInstance;
import com.d.apps.scoach.db.model.Counter;
import com.d.apps.scoach.db.model.Profile;
import com.d.apps.scoach.ui.managers.ManageCoachesIFrame;
import com.d.apps.scoach.ui.managers.ManageProfilesIFrame;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = -7859383450590563738L;

	private static final Logger LOG = LoggerFactory.getLogger(MainFrame.class);
	private static final int WINDOW_BATCH_SIZE = 3;
	private static final int WINDOW_PADDING = 25;

	private MainFrame _instance = null;
	
	//MODEL
	private Profile activeProfile = null;
	
	//SWING
	private JDesktopPane desktopPane = null;
	private ToolbarPanel actionPanel = null;
	
	public MainFrame() {
		super();
		
		setName(Utilities.NAME_MAINFRAME);
		initGrcs();

		activeProfileChanged(CounterApp.DBServices.getActiveProfile());
		_instance = this;
	}

	public Profile getActiveProfile() {	return activeProfile;	}
	public void activeProfileChanged(Profile p) {
		activeProfile = p;
		if (activeProfile != null) {
			updateProfileRelatedUI(activeProfile.getCoaches());
		} else {
			if (!isIFrameVisible(Utilities.NAME_PROFILESMANAGER)) {
				showIFrame(new ManageProfilesIFrame());
			}
		}
		setTitle("Counter Application - Active Profile : "+((activeProfile == null) ? "N/A" : activeProfile.getFirstName()));
		actionPanel.toggleActionBar(activeProfile != null);
	}

	//PRIVATE
	public void updateProfileRelatedUI(List<CoachInstance> activeCoaches) {
		createBasicMenu();
		actionPanel.cleanBar();
		
		JMenu coaches = new JMenu("Coaches");
		for (CoachInstance profileCoach : activeCoaches) {
			//TODO MENU ACTIONS
			JMenuItem coachitem = new JMenuItem(profileCoach.getName());
			coaches.add(coachitem);
			
			//TODO TOOLBAR OF COUNTERS
			for (Counter counter : profileCoach.getCounters()) {
				actionPanel.addActionButton(new ToolbarAction(counter, true));
			}
		}
		getJMenuBar().add(coaches);
		actionPanel.recreateBar();
	}
	
	private void initGrcs() {
		JPanel parent = (JPanel) getContentPane();
		parent.setLayout(new BorderLayout());
		
		desktopPane = new JDesktopPane();
		actionPanel = new ToolbarPanel();

		parent.add(desktopPane, BorderLayout.CENTER);
		parent.add(actionPanel, BorderLayout.SOUTH);
		
		createBasicMenu();
		
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)/2 - getWidth()/2, (Toolkit.getDefaultToolkit().getScreenSize().height)/2 - getHeight()/2);
		try {
			setIconImage(ImageIO.read(MainFrame.class.getClassLoader().getResourceAsStream("europa-flag.gif")));
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
	}

	private void createBasicMenu() {
		JMenu help = new JMenu("Help");
		JMenuItem about = new JMenuItem("About"); 
		help.add(about);
		
		JMenu file = new JMenu("File");
		JMenuItem profiles = new JMenuItem("Manage Profiles"); 
		JMenuItem coaches = new JMenuItem("Manage Coaches");
		
		file.add(profiles);
		file.add(coaches);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(file);
		menuBar.add(help);
		
		setJMenuBar(menuBar);
		
		//MENU LISTENERS
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showIFrame(new AboutIFrame());
			}
		});
		profiles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showIFrame(new ManageProfilesIFrame());
			}
		});
		coaches.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showIFrame(new ManageCoachesIFrame());
			}
		});
	}
	
	public boolean isIFrameVisible(String name) {
		for (JInternalFrame iframe : desktopPane.getAllFrames()) {
			if (iframe.getName().equals(name) && iframe.isVisible()) {
				return true;
			}
		}
		return false;
	}
	
	public void showIFrame(JInternalFrame frame) {
		int l = desktopPane.getAllFrames().length;
		int x = (int)(l/WINDOW_BATCH_SIZE)* WINDOW_PADDING;
		int y = (l % WINDOW_BATCH_SIZE) * WINDOW_PADDING;
		
		desktopPane.add(frame);
	    try {
	        frame.setSelected(true);
	        frame.setLocation(x, (int)y);
	    	frame.setVisible(true);
	    } catch (java.beans.PropertyVetoException e) {
	    	LOG.error(e.getMessage());
	    }
	}

		/* TOOLBAR AND TOOLBAR ACTIONS */
	class ToolbarPanel extends JToolBar {
		private static final long serialVersionUID = 1L;
		private ArrayList<AbstractAction> actionButtons = new ArrayList<AbstractAction>();
		
		public ToolbarPanel() {
			super();
		}
		
		public void toggleActionBar(boolean enable) {
			for (AbstractAction toolbarAction : actionButtons) {
				if (toolbarAction != null) {
					toolbarAction.setEnabled(enable);
				}
			}
		}
		
		public void addActionButton(ToolbarAction action) {
			actionButtons.add(action);
		}
		
		public void cleanBar() {
			removeAll();
			actionButtons.clear();
		}
		
		public void recreateBar () {
			for (AbstractAction toolbarAction : actionButtons) {
				if (toolbarAction != null) {
					add(toolbarAction);
				}
			}
		}
	}
	
	class ToolbarAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private Counter counter = null;
		
		public ToolbarAction(Counter counter, boolean enabled) {
			super(counter.getName());
			this.counter = counter;
	
	//		super(text, icon);
	//		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	//		putValue(SHORT_DESCRIPTION, description);
			setEnabled(enabled);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Double value = null;
			switch (counter.getType()) {
			case INPUT:
				System.out.println("Input counter clicked");
				InputDialog d = new InputDialog();
				Point p = _instance.getLocation();
				p.translate((int)p.getX()/2, (int)p.getY()/2);
				d.setLocation(p);
				value = d.getUserValue();
				
				if (value == null) {
					JOptionPane.showMessageDialog(_instance, "Counter data cannot be null or not numeric");
					return;
				}
				break;
			case STEP:
				value = counter.getStepValue();
				System.out.println("Step counter clicked");
				break;
			default:
				throw new RuntimeException("Unkown counter type :"+counter.getType());
			}
			counter = CounterApp.DBServices.addCounterData(counter, new Timestamp(Calendar.getInstance().getTimeInMillis()), value);
		}
	}
	
	class InputDialog extends JDialog {
		private static final long serialVersionUID = -1L;
		
		private JTextField valueText = new JTextField();
		
		public InputDialog() {
			super();
			setTitle("Input a value");
			setModal(true);
			
			add(valueText);
			
			setSize(200, 70);
		}
		
		public Double getUserValue() {
			setVisible(true);
			
			Double ans = null;
			try {
				ans = Double.parseDouble(valueText.getText());
			} catch (Exception e) {
			}
			return ans;
		}
	}
}

class AboutIFrame extends JInternalFrame {
	private static final long serialVersionUID = -1L;
	
	String version = null;
	
	public AboutIFrame() {
		super();
		version = CounterApp.appProperties.getProperty("app.version");
		
		initGrcs();
	}
	
	
	private void initGrcs() {
		JPanel parent = (JPanel) getContentPane();
		
		GridBagLayout layout = new GridBagLayout();
		parent.setLayout(layout);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 1 };
		gridBagLayout.rowHeights   = new int[] { 20, 20, 20 };
		gridBagLayout.columnWeights = new double[] { 1.0 };
		gridBagLayout.rowWeights    = new double[] { 0.0, 0.0, 1.0 };
		parent.setLayout(gridBagLayout);

		parent.add(new JLabel("Version"), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 2, 0, 2), 0, 0));
		parent.add(new JLabel(version), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 2, 0, 2), 0, 0));

		setName(Utilities.NAME_ABOUTDIALOG);
		setClosable(true);
		setTitle("About Counter");
		setSize(100, 100);
	}
}
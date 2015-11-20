package com.d.apps.scoach.ui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d.apps.scoach.CounterApp;
import com.d.apps.scoach.db.model.CoachInstance;
import com.d.apps.scoach.db.model.Profile;
import com.d.apps.scoach.ui.managers.ManageCoachesIFrame;
import com.d.apps.scoach.ui.managers.ManageProfilesIFrame;
import com.d.apps.scoach.util.Utilities;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = -7859383450590563738L;

	private static final Logger LOG = LoggerFactory.getLogger(MainFrame.class);
	private static final int WINDOW_BATCH_SIZE = 3;
	private static final int WINDOW_PADDING = 25;

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
	}

	//INTERFACE
	public Profile getActiveProfile() {	return activeProfile;	}
	public void actionInvoked(String actionName) {
	}
	
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
	private void updateProfileRelatedUI(List<CoachInstance> activeCoaches) {
		createBasicMenu();
		actionPanel.cleanBar();
		
		for (CoachInstance profileCoach : activeCoaches) {
			//SETUP MENU ETC
		}
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

		public ToolbarAction(String text, Icon icon, String description, char accelerator, boolean enabled) {
			super(text, icon);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			putValue(SHORT_DESCRIPTION, description);
			setEnabled(enabled);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			actionInvoked(getValue(NAME).toString());
		}
	}
}
package com.d.apps.scoach.ui;

import java.awt.BorderLayout;
import java.awt.Container;
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
import com.d.apps.scoach.ui.iframes.AboutIFrame;
import com.d.apps.scoach.ui.iframes.ManageCoachesIFrame;
import com.d.apps.scoach.ui.iframes.ManageCountersIFrame;
import com.d.apps.scoach.ui.iframes.ManageProfilesIFrame;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = -7859383450590563738L;

	private static final Logger LOG = LoggerFactory.getLogger(MainFrame.class);
	private Profile activeProfile = null;
	
	//SWING
	private JDesktopPane desktopPane = null;
	private ToolbarPanel actionPanel = null;
	
	public MainFrame() {
		initGrcs();
		activeProfileChanged();

		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)/2 - getWidth()/2, (Toolkit.getDefaultToolkit().getScreenSize().height)/2 - getHeight()/2);

		try {
			setIconImage(ImageIO.read(MainFrame.class.getClassLoader().getResourceAsStream("europa-flag.gif")));
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
	}

	//INTERFACE
	public Profile getActiveProfile() {
		return activeProfile;
	}
	
	public void actionInvoked(String actionName) {
	}
	
	public void activeProfileChanged(Profile p) {
		activeProfile = p;
		setTitle("Counter Application - Active Profile : "+activeProfile.getFirstName());
		updateProfileRelatedUI(activeProfile.getCoaches());
		actionPanel.toggleActionBar(activeProfile != null);
		
	}

	public void activeProfileChanged() {
		activeProfile = CounterApp.DBServices.getActiveProfile();
		if (activeProfile != null) {
			setTitle("Counter Application - Active Profile : "+activeProfile.getFirstName());
			updateProfileRelatedUI(activeProfile.getCoaches());
		} else {
			setTitle("Counter Application - Active Profile : N/A");
			showIFrame(new ManageProfilesIFrame());
		}
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
		Container parent = new JPanel();
		setContentPane(parent);

		parent.setLayout(new BorderLayout());
		
		desktopPane = new JDesktopPane();
		actionPanel = new ToolbarPanel();

		parent.add(desktopPane, BorderLayout.CENTER);
		parent.add(actionPanel, BorderLayout.SOUTH);
		createBasicMenu();
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
	
	public void showIFrame(JInternalFrame frame) {
		double l = desktopPane.getAllFrames().length;
		double y = l;
		int x = (int)(l/5.0)* 10;
		
		desktopPane.add(frame);
	    try {
	        frame.setSelected(true);
	        frame.setLocation(x, 10);
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
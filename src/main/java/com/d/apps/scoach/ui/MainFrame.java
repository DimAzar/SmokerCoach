package com.d.apps.scoach.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
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
import com.d.apps.scoach.db.model.Profile;
import com.d.apps.scoach.ui.iframes.AboutIFrame;
import com.d.apps.scoach.ui.iframes.ManageProfilesIFrame;
import com.d.apps.scoach.ui.iframes.SmokerCoachIFrame;
import com.d.apps.scoach.util.Utilities;

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

		setSize(800, 800);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)/2 - getWidth()/2, (Toolkit.getDefaultToolkit().getScreenSize().height)/2 - getHeight()/2);

		try {
			setIconImage(ImageIO.read(MainFrame.class.getClassLoader().getResourceAsStream("europa-flag.gif")));
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
	}

	//INTERFACE
	public void actionInvoked(String actionName) {
		if (actionName.equals("Smoked")) {
			int count = CounterApp.DBServices.incrementSmokedCount(activeProfile.getId());
			for (JInternalFrame frame : desktopPane.getAllFrames()) {
				if (frame instanceof SmokerCoachIFrame && frame.isVisible()) {
					((SmokerCoachIFrame)frame).setSmokeCount(count);
				}
			}
		}
	}
	
	public void activeProfileChanged() {
		activeProfile = CounterApp.DBServices.getActiveProfile();
		if (activeProfile != null) {
			setTitle("Counter Application - Active Profile : "+activeProfile.getFirstName());
			CounterApp.DBServices.getProfileCoaches(activeProfile.getId());
		} else {
			setTitle("Counter Application - Active Profile : N/A");
			showIFrame(new ManageProfilesIFrame());
		}
		actionPanel.toggleActionBar(activeProfile != null);
		
		for (JInternalFrame frame : desktopPane.getAllFrames()) {
			if (frame instanceof SmokerCoachIFrame && frame.isVisible()) {
				((SmokerCoachIFrame)frame).setSmokeCount(activeProfile.getSmokeCount(Utilities.createDateStringRep()));
			}
		}
	}

	//PRIVATE
	private void initGrcs() {
		Container parent = new JPanel();
		setContentPane(parent);

		parent.setLayout(new BorderLayout());
		
		desktopPane = new JDesktopPane();
		actionPanel = new ToolbarPanel();

		parent.add(desktopPane, BorderLayout.CENTER);
		parent.add(actionPanel, BorderLayout.SOUTH);
		
		createMenu();
	}

	private void createMenu() {
		JMenu help = new JMenu("Help");
		JMenuItem about = new JMenuItem("About"); 
		help.add(about);
		
		JMenu health = new JMenu("Health");
		JMenuItem smoker = new JMenuItem("Smoker"); 
		health.add(smoker);
		
		JMenu file = new JMenu("File");
		JMenuItem users = new JMenuItem("Manage Profiles"); 
		file.add(users);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(file);
		menuBar.add(health);
		menuBar.add(help);
		
		setJMenuBar(menuBar);
		
		//MENU LISTENERS
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showIFrame(new AboutIFrame());
			}
		});
		users.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showIFrame(new ManageProfilesIFrame());
			}
		});
		smoker.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SmokerCoachIFrame f = new SmokerCoachIFrame();
				f.setSmokeCount(activeProfile.getSmokeCount(Utilities.createDateStringRep()));
				showIFrame(f);
			}
		});
	}
	
	public void showIFrame(JInternalFrame frame) {
		desktopPane.add(frame);
	    try {
	        frame.setSelected(true);
	    	frame.setVisible(true);
	    } catch (java.beans.PropertyVetoException x) {
	    	LOG.error(x.getMessage());
	    }
	}

	/* TOOLBAR AND TOOLBAR ACTIONS */
	class ToolbarPanel extends JToolBar {
		private static final long serialVersionUID = 1L;
		private AbstractAction[] actionButtons = new AbstractAction[8];
		
		public ToolbarPanel() {
			super();
			
			actionButtons[0] = new ToolbarAction("Smoked", new ImageIcon(MainFrame.class.getClassLoader().getResource("images/cig.jpg")), "Increment smoked count", 's', false); 
			for (AbstractAction toolbarAction : actionButtons) {
				if (toolbarAction != null) {
					add(toolbarAction);
				}
			}
		}
		
		public void toggleActionBar(boolean enable) {
			for (AbstractAction toolbarAction : actionButtons) {
				if (toolbarAction != null) {
					toolbarAction.setEnabled(enable);
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
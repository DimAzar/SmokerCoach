package com.d.apps.scoach.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = -7859383450590563738L;

	private static final Logger LOG = LoggerFactory.getLogger(MainFrame.class);
	private Properties appProperties = null; 

	//SWING
	private JDesktopPane desktopPane = new JDesktopPane();
	private JPanel actionPanel = new ToolbarPanel();
	
	public MainFrame(Properties properties) {
		appProperties = properties;
		
		initGrcs();
		
		setSize(1024, 800);
		setTitle("Smoker Coach Application");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)/2 - getWidth()/2, (Toolkit.getDefaultToolkit().getScreenSize().height)/2 - getHeight()/2);

		try {
			setIconImage(ImageIO.read(MainFrame.class.getClassLoader().getResourceAsStream("europa-flag.gif")));
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
	}
	
	private void initGrcs() {
		Container parent = new JPanel();
		setContentPane(parent);

		parent.setLayout(new BorderLayout());
		
		parent.add(desktopPane, BorderLayout.CENTER);
		parent.add(actionPanel, BorderLayout.SOUTH);
		
		createMenu();
		createListeners();

	}

	private void createListeners() {
		//MENU
	}
	
	private void createMenu() {
		JMenu help = new JMenu("Help");
		JMenuItem about = new JMenuItem("About"); 
		help.add(about);
		
		JMenu file = new JMenu("File");
		JMenuItem users = new JMenuItem("Manage Profiles"); 
		file.add(users);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(file);
		menuBar.add(help);
		
		setJMenuBar(menuBar);
		
		//MENU LISTENERS
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addIFrame(new AboutIFrame(appProperties));
			}
		});
		users.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addIFrame(new ManageProfilesIFrame(appProperties));
			}
		});
	}
	
	private void addIFrame(JInternalFrame frame) {
		desktopPane.add(frame);
	    try {
	        frame.setSelected(true);
	    	frame.setVisible(true);
	    } catch (java.beans.PropertyVetoException x) {
	    	LOG.error(x.getMessage());
	    }
	}
}

class ToolbarPanel extends JPanel {
	private static final long serialVersionUID = -6013670095226879299L;

	private JButton smokeButt = new JButton("Just had a smoke");
	public ToolbarPanel() {
		super();
		
		add(smokeButt);
	}
}
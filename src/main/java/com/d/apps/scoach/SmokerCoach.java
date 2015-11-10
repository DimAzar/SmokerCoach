package com.d.apps.scoach;

import java.util.Properties;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d.apps.scoach.db.DBServices;
import com.d.apps.scoach.db.DBServicesImpl;
import com.d.apps.scoach.ui.MainFrame;

public class SmokerCoach {
	private static final Logger LOG = LoggerFactory.getLogger(SmokerCoach.class);
	private static final String propertiesName = "app.properties";
	
	private final MainFrame mainFrame; 
	private Properties appProperties;

	private DBServices dbservices = new DBServicesImpl();
	
	public SmokerCoach() {
		LOG.debug("Starting Coach ");
		appProperties = Utilities.loadApplicationProperties(propertiesName);
		mainFrame = new MainFrame(appProperties, dbservices);
		mainFrame.setVisible(true);
		LOG.debug("Coach started");
	}

	//MAIN
	public static void main(String[] args) {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		new SmokerCoach();
		LOG.debug("Coach stopped");
	}
}
package com.d.apps.scoach.clients.swing;

import java.util.Properties;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d.apps.scoach.clients.swing.ui.MainFrame;
import com.d.apps.scoach.server.services.DBServices;
import com.d.apps.scoach.server.services.DBServicesImpl;

public class CounterAppClient {
	private static final Logger LOG = LoggerFactory.getLogger(CounterAppClient.class);
	private static final String propertiesName = "app.properties";
	
	private final MainFrame mainFrame; 
	public static Properties appProperties;

	public static DBServices DBServices = new DBServicesImpl();
	
	public CounterAppClient() {
		LOG.debug("Starting Coach ");
		appProperties = Utilities.loadApplicationProperties(propertiesName);
		mainFrame = new MainFrame();
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
		new CounterAppClient();
		LOG.debug("Coach stopped");
	}
}
package com.d.apps.scoach;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utilities {
	private static final Logger LOG = LoggerFactory.getLogger(Utilities.class);
	
	public static final String NAME_MAINFRAME   = "Main Frame";
	public static final String NAME_PROFILEMANAGER = "Profile Manager";
	public static final String NAME_ABOUTDIALOG = "About Counter";
	public static final String NAME_COACHMANAGER = "Manage Coaches";
	public static final String NAME_PROFILESMANAGER= "Manage Profiles";
	public static final String NAME_PROFILECOACHES= "Profile Coaches";
	public static final String NAME_ADDCOUNTER = "Add Counter to Coach";
	public static Properties loadApplicationProperties(String propertiesName) {
		Properties ans = new Properties();
		InputStream input = null;

		try {
			input = CounterApp.class.getClassLoader().getResourceAsStream(propertiesName);
    		if (input==null) {
	            LOG.error("Sorry, unable to find " + input);
    		    return null;
    		}

			ans.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return ans;
	}

	public static String createDateStringRep() {
		String ans = String.format("%s/%s/%s", 
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
				Calendar.getInstance().get(Calendar.MONTH)+1,
				Calendar.getInstance().get(Calendar.YEAR));
		return ans;
	}
}
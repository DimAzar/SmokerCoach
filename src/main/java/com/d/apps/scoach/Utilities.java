package com.d.apps.scoach;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Properties;

import lombok.Getter;

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
	
	public enum CounterFunctionType {
	    STEP(1, "Step function"), INPUT(2, "User-Input function");

	    @Getter
	    private final int id;
	    @Getter
	    private final String description;
	    
	    private CounterFunctionType(int id, String description) {	
	    	this.id = id;	
	    	this.description = description;
    	}
	    
	    public static CounterFunctionType forId(int id) {
	        for (CounterFunctionType func : CounterFunctionType.values()) {
	            if (func.getId() == id) {
	                return func;
	            }
	        }
	        return null;
	    }
	    
	    public static CounterFunctionType getId(String descr) {
	        for (CounterFunctionType func : CounterFunctionType.values()) {
	            if (func.getDescription().equals(descr)) {
	                return func;
	            }
	        }
	        return null;
	    }
	}
}
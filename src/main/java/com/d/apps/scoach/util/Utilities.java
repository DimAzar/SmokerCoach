package com.d.apps.scoach.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d.apps.scoach.SmokerCoach;
import com.d.apps.scoach.db.model.CigaretteTrackEntry;
import com.d.apps.scoach.db.model.Profile;

public class Utilities {
	private static final Logger LOG = LoggerFactory.getLogger(Utilities.class);
	
	public static Properties loadApplicationProperties(String propertiesName) {
		Properties ans = new Properties();
		InputStream input = null;

		try {
			input = SmokerCoach.class.getClassLoader().getResourceAsStream(propertiesName);
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

	public static CigaretteTrackEntry getCalendarEntry (Profile p , String key) {
		for (CigaretteTrackEntry entry : p.getCigaretteTrack()) {
			if (entry.getDateString().equals(key)) {
				return entry;
			}
		}
		return null;
	}
}

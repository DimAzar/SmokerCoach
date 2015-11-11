package com.d.apps.scoach.services;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d.apps.scoach.db.model.CigaretteTrackEntry;
import com.d.apps.scoach.db.model.Profile;
import com.d.apps.scoach.db.selectors.CigaretteTrackSelector;
import com.d.apps.scoach.db.selectors.ProfileSelector;
import com.d.apps.scoach.services.interfaces.DBServices;
import com.d.apps.scoach.util.Utilities;

public class DBServicesImpl implements DBServices {
	private static final String PERSISTENCE_UNIT = "SmokerCoach";
	private static final Logger LOG = LoggerFactory.getLogger(DBServicesImpl.class);
	
	private EntityManagerFactory factory ;

	private ProfileSelector pselector = new ProfileSelector();
	private CigaretteTrackSelector cteselector = new CigaretteTrackSelector();
	
	public DBServicesImpl() {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
		
		pselector.setEntityManager(factory.createEntityManager());
		cteselector.setEntityManager(factory.createEntityManager());
		LOG.debug("DBServices started");
	}

	@Override
	public int incrementSmokedCount(int pid) {
		Profile p = findProfile(pid);
		String key = Utilities.createDateStringRep();
		
		CigaretteTrackEntry entry = Utilities.getCalendarEntry(p, key);
		if (entry == null) {
	    	entry = new CigaretteTrackEntry();
	    	entry.setProfile(p);
	    	entry.setDateString(key);
	    	entry.setCigaretteCount(1);
	    	
	    	p.addTrackEntry(entry);
	    	LOG.debug("First entry "+p.getFirstName()+" ->"+entry.getDateString()+":"+entry.getCigaretteCount());
		} else {
			entry.incrementCigaretteCount();
			LOG.debug("incremented "+p.getFirstName()+" ->"+entry.getDateString()+":"+entry.getCigaretteCount());
		}
    	pselector.updateProfile(p);
    	return entry.getCigaretteCount();
	}
	
	@Override
	public int getProfilesCount() {
		return pselector.getProfilesCount();
	}

	@Override
	public List<Profile> getProfiles() {
		return pselector.getProfiles();
	}

	@Override
	public Profile findProfile(int pid) {
		return pselector.findProfile(pid);
	}

	@Override
	public void setActiveProfile(int id) {
		pselector.setActiveProfile(id);
	}

	@Override
	public void deleteProfile(int id) {
		pselector.deleteProfile(id);
	}

	@Override
	public void deactivateAllProfiles() {
		pselector.deactivateAllProfiles();
	}

	@Override
	public Profile getActiveProfile() {
		return pselector.getActiveProfile();
	}

	@Override
	public void createProfile(String name) {
		pselector.createProfile(name);
	}

	@Override
	public void createProfile(String name, boolean active) {
		pselector.createProfile(name, active);
	}

	@Override
	public void updateProfile(Profile p) {
		pselector.updateProfile(p);
	}
	
}
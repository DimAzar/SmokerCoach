package com.d.apps.scoach.db;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d.apps.scoach.db.model.CigaretteTrackEntry;
import com.d.apps.scoach.db.model.Profile;
import com.d.apps.scoach.db.selectors.CigaretteTrackSelector;
import com.d.apps.scoach.db.selectors.ProfileSelector;

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
	public void incrementSmokedCount(int pid) {
		
		Profile p = findProfile(pid);
		List<CigaretteTrackEntry> entries = p.getCigaretteTrack();
		String key = createDateStringRep();
		
		entries = cteselector.getAllEntries();
		cteselector.createCigaretteTrackEntry(p.getId(), key);
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
	
	private static String createDateStringRep() {
		String ans = String.format("%s/%s/%s", 
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
				Calendar.getInstance().get(Calendar.MONTH)+1,
				Calendar.getInstance().get(Calendar.YEAR));
		return ans;
	}
}
package com.d.apps.scoach.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d.apps.scoach.db.model.CigaretteTrackEntry;
import com.d.apps.scoach.db.model.Profile;
import com.d.apps.scoach.db.model.ProfileCoach;
import com.d.apps.scoach.db.model.ProfileCoaches;
import com.d.apps.scoach.db.selectors.CigaretteTrackSelector;
import com.d.apps.scoach.db.selectors.ProfileCoachSelector;
import com.d.apps.scoach.db.selectors.ProfileCoachesSelector;
import com.d.apps.scoach.db.selectors.ProfileSelector;
import com.d.apps.scoach.services.interfaces.DBServices;
import com.d.apps.scoach.util.Utilities;

public class DBServicesImpl implements DBServices {
	private static final String PERSISTENCE_UNIT = "CounterApp";
	private static final Logger LOG = LoggerFactory.getLogger(DBServicesImpl.class);
	
	private EntityManagerFactory factory ;

	private ProfileSelector pselector = new ProfileSelector();
	private CigaretteTrackSelector cteselector = new CigaretteTrackSelector();
	private ProfileCoachesSelector coachesSelector = new ProfileCoachesSelector();
	private ProfileCoachSelector profileCoachSelector = new ProfileCoachSelector();
	
	public DBServicesImpl() {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
		
		pselector.setEntityManager(factory.createEntityManager());
		cteselector.setEntityManager(factory.createEntityManager());
		coachesSelector.setEntityManager(factory.createEntityManager());
		profileCoachSelector.setEntityManager(factory.createEntityManager());
		createDBLists();
		LOG.debug("DBServices started");
	}

	@Override
	public int incrementSmokedCount(int pid) {
		Profile p = findProfile(pid);
		String key = Utilities.createDateStringRep();
		
		CigaretteTrackEntry entry = Utilities.getCalendarEntry(p, key);
		if (entry == null) {
	    	entry = new CigaretteTrackEntry();
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
	public Profile createProfile(String name) {
		return createProfile(name, false);
	}

	@Override
	public Profile createProfile(String name, boolean active) {
		return pselector.createProfile(name, active);
	}

	@Override
	public void updateProfile(Profile p) {
		pselector.updateProfile(p);
	}
	
	private void createDBLists() {
		ArrayList<ProfileCoaches> coaches = new ArrayList<ProfileCoaches>();
		
		ProfileCoaches coach = new ProfileCoaches();
		coach.setName("Smoker Coach");
		coaches.add(coach);
		
		EntityManager em =  factory.createEntityManager();
		for (ProfileCoaches c : coaches) {
			em.getTransaction().begin();
			em.persist(c);
			em.getTransaction().commit();
		}
	}

	@Override
	public void enableCoach(String coachName, Profile p) {
		ProfileCoach coach = new ProfileCoach();
		coach.setDateActivated(new Date(Calendar.getInstance().getTimeInMillis()));
		coach.setCoachId(coachesSelector.getCoachIDByName(coachName));
		p.addCoach(coach);
		
	}

	@Override
	public List<ProfileCoach> getProfileCoaches(int pid) {
		return profileCoachSelector.getProfileCoaches(pid);
	}
}
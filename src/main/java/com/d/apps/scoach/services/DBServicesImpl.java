package com.d.apps.scoach.services;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d.apps.scoach.db.model.CoachInstance;
import com.d.apps.scoach.db.model.CoachTemplate;
import com.d.apps.scoach.db.model.CounterTemplate;
import com.d.apps.scoach.db.model.Profile;
import com.d.apps.scoach.db.model.ProfileCoach;
import com.d.apps.scoach.db.selectors.CoachTemplateSelector;
import com.d.apps.scoach.db.selectors.CounterTemplateSelector;
import com.d.apps.scoach.db.selectors.ProfileCoachSelector;
import com.d.apps.scoach.db.selectors.ProfileSelector;
import com.d.apps.scoach.services.interfaces.DBServices;

public class DBServicesImpl implements DBServices {
	private static final String PERSISTENCE_UNIT = "CounterApp";
	private static final Logger LOG = LoggerFactory.getLogger(DBServicesImpl.class);
	
	private EntityManagerFactory factory ;

	private ProfileSelector pselector = new ProfileSelector();
	private CoachTemplateSelector coachesSelector = new CoachTemplateSelector();
	private ProfileCoachSelector profileCoachSelector = new ProfileCoachSelector();
	private CounterTemplateSelector counterTemplateSelector = new CounterTemplateSelector();
	
	public DBServicesImpl() {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
		
		pselector.setEntityManager(factory.createEntityManager());
		coachesSelector.setEntityManager(factory.createEntityManager());
		profileCoachSelector.setEntityManager(factory.createEntityManager());
		counterTemplateSelector.setEntityManager(factory.createEntityManager());
		LOG.debug("DBServices started");
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
		pselector.deleteEntity(id, Profile.class);
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
	public Profile updateProfile(Profile p) {
		return (Profile) pselector.updateEntity(p);
	}
	
	@Override
	public List<CoachTemplate> getCoachTemplates() {
		return coachesSelector.getAllCoaches();
	}

	@Override
	public void deleteCoachTemplate(int cid) {
		coachesSelector.deleteEntity(cid, CoachTemplate.class);
	}

	@Override
	public CoachTemplate createCoachTemplate(String name) {
		coachesSelector.createCoach(name);
		return null;
	}

	@Override
	public CoachTemplate updateCoachTemplate(int cid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Profile enableCoach(String name, Profile p) {
		CoachTemplate template = coachesSelector.getCoachByName(name);
		
		CoachInstance instance = new CoachInstance();
		instance.setTemplate(template);
		instance = (CoachInstance) coachesSelector.createEntity(instance);
		
		ProfileCoach xpc = new ProfileCoach();
		xpc.setDateActivated(new Date(Calendar.getInstance().getTimeInMillis()));
		xpc.setCoach(instance);

		p.addCoach(xpc);
		
		profileCoachSelector.createEntity(xpc);
		pselector.updateEntity(p);
		return p;
	}

	@Override
	public Profile disableCoach(String name, Profile p) {
		return null;
	}

	@Override
	public List<CounterTemplate> getCounterTemplates() {
		return counterTemplateSelector.getAllCounters();
	}

	@Override
	public CounterTemplate createCounterTemplate(String name) {
		return counterTemplateSelector.createCounter(name);
	}

	@Override
	public CounterTemplate updateCounterTemplate(int cid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteCounterTemplate(int cid) {
		counterTemplateSelector.deleteEntity(cid, CounterTemplate.class);
	}
}
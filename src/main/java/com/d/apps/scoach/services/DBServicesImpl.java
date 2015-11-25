package com.d.apps.scoach.services;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d.apps.scoach.Utilities.CounterFunctionType;
import com.d.apps.scoach.Utilities.DataSumType;
import com.d.apps.scoach.db.model.CoachInstance;
import com.d.apps.scoach.db.model.CoachTemplate;
import com.d.apps.scoach.db.model.Counter;
import com.d.apps.scoach.db.model.Profile;
import com.d.apps.scoach.db.selectors.CoachInstanceSelector;
import com.d.apps.scoach.db.selectors.CoachTemplateSelector;
import com.d.apps.scoach.db.selectors.CounterDataSelector;
import com.d.apps.scoach.db.selectors.CounterSelector;
import com.d.apps.scoach.db.selectors.ProfileSelector;

public class DBServicesImpl implements DBServices {
	private static final String PERSISTENCE_UNIT = "CounterApp";
	private static final Logger LOG = LoggerFactory.getLogger(DBServicesImpl.class);
	
	private EntityManagerFactory factory ;

	private ProfileSelector pselector = new ProfileSelector();
	private CoachTemplateSelector coachesSelector = new CoachTemplateSelector();
	private CoachInstanceSelector coachesInstSelector = new CoachInstanceSelector();
	private CounterSelector counterSelector = new CounterSelector();
	private CounterDataSelector counterDataSelector = new CounterDataSelector();
	
	public DBServicesImpl() {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
		
		pselector.setEntityManager(factory.createEntityManager());
		coachesSelector.setEntityManager(factory.createEntityManager());
		coachesInstSelector.setEntityManager(factory.createEntityManager());
		counterSelector.setEntityManager(factory.createEntityManager());
		counterDataSelector.setEntityManager(factory.createEntityManager());
		
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
	public Profile setActiveProfile(int id) {
		return pselector.setActiveProfile(id);
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
		
		CoachInstance ci = new CoachInstance();
		ci.setTemplate(template);
		ci.setName(String.format("%s_%s Instance", template.getName(), p.getFirstName()));
		p.addCoach(ci);
		pselector.updateEntity(p);
		return p;
	}

	@Override
	public Profile disableCoach(String name, Profile p) {
		coachesSelector.deleteEntity(p.removeCoach(name), CoachInstance.class);
		pselector.updateEntity(p);
		return p;
	}

	@Override
	public CoachInstance findCoachInstance(int cid) {
		return coachesInstSelector.getCoachInstanceByID(cid); 
	}

	@Override
	public Counter createCounter(int coachId, String name,
			CounterFunctionType type, double stepValue) {
		Counter instance = new Counter();
		instance.setName(name);
		instance.setStepValue(stepValue);
		instance.setType(type);
		
		CoachInstance ci = coachesInstSelector.getCoachInstanceByID(coachId);
		ci.addCounter(instance);
		
		coachesInstSelector.updateEntity(instance);
		return null;
	}

	@Override
	public Counter addCounterData(Counter owner, Timestamp created, Double value) {
		return (Counter)counterSelector.addCounterData(owner, created, value);
	}
	
	@Override
	public List<Object[]> getCounterDataSummed(int cid, DataSumType type) {
		return counterDataSelector.getCounterDataSummed(cid, type);
	}
}
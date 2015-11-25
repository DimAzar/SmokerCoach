package com.d.apps.scoach.services;

import java.sql.Timestamp;
import java.util.List;

import com.d.apps.scoach.Utilities.CounterFunctionType;
import com.d.apps.scoach.Utilities.DataSumType;
import com.d.apps.scoach.db.model.CoachInstance;
import com.d.apps.scoach.db.model.CoachTemplate;
import com.d.apps.scoach.db.model.Counter;
import com.d.apps.scoach.db.model.Profile;

public interface DBServices {
	//PROFILES
    public Profile findProfile(int pid);
	public List<Profile> getProfiles();
    public Profile createProfile(String name, boolean active);
    public Profile updateProfile(Profile p) ;
    public void deleteProfile(int id);
    
	public int getProfilesCount();
    public Profile createProfile(String name);
    public Profile setActiveProfile(int id);
    public void deactivateAllProfiles();
    public Profile getActiveProfile();
    
    public List<CoachTemplate> getCoachTemplates();
    public CoachTemplate createCoachTemplate(String name);
    public CoachTemplate updateCoachTemplate(int cid);
    public void deleteCoachTemplate(int cid);
    
    public Profile enableCoach(String name, Profile p);
    public Profile disableCoach(String name, Profile p);
    
    public CoachInstance findCoachInstance(int cid);
    
    public Counter createCounter(int coachInstanceId, String name, CounterFunctionType type, double stepValue);
    public Counter addCounterData(Counter owner, Timestamp created, Double value);
    
    public List<Object[]> getCounterDataSummed(int cid, DataSumType type);
}

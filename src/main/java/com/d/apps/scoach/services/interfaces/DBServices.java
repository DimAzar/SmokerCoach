package com.d.apps.scoach.services.interfaces;

import java.util.List;

import com.d.apps.scoach.db.model.CoachTemplate;
import com.d.apps.scoach.db.model.CounterTemplate;
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
    public void setActiveProfile(int id);
    public void deactivateAllProfiles();
    public Profile getActiveProfile();
    
    public List<CoachTemplate> getCoachTemplates();
    public CoachTemplate createCoachTemplate(String name);
    public CoachTemplate updateCoachTemplate(int cid);
    public void deleteCoachTemplate(int cid);
    
    public Profile enableCoach(String name, Profile p);
    public Profile disableCoach(String name, Profile p);
    
    public List<CounterTemplate> getCounterTemplates();
    public CounterTemplate createCounterTemplate(String name);
    public CounterTemplate updateCounterTemplate(int cid);
    public void deleteCounterTemplate(int cid);
}

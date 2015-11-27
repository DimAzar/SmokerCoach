package com.d.apps.scoach.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.d.apps.scoach.Utilities.CounterFunctionType;
import com.d.apps.scoach.Utilities.DataSumType;
import com.d.apps.scoach.db.model.CoachGraph;
import com.d.apps.scoach.db.model.Coach;
import com.d.apps.scoach.db.model.Counter;
import com.d.apps.scoach.db.model.Profile;

public interface DBServices {
	//PROFILES
    public Profile findProfile(int pid);
	public List<Profile> getProfiles();
    public Profile createProfile(String name, boolean active);
    public Profile updateProfile(Profile p) ;
    public void deleteProfile(int id);
    
    public Profile setActiveProfile(int id);
    public void deactivateAllProfiles();
    public Profile getActiveProfile();
    
    public Profile createCoach(int profileId, String name);
    public Coach updateCoach(Coach instance);
    public Profile enableCoach(String name, Profile p);
    public Profile disableCoach(String name, Profile p);
    
    public Coach findCoachInstance(int cid);
    public CoachGraph findCoachGraph(int gid);
    
    public Counter createCounter(int coachInstanceId, String name, CounterFunctionType type, double stepValue);
    public Counter addCounterData(Counter owner, Timestamp created, Double value);
    
    public List<Object[]> getCounterDataSummed(int cid, DataSumType type);
    public List<Object[]> getCounterDataFlat(int cid);
    
    public void deleteGraph(int id);
    public Coach addGraph (int coachId, String graphName, ArrayList<Integer> counterIds);
    public CoachGraph updateGraph (CoachGraph graph);
}

package com.d.apps.scoach.server.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.d.apps.scoach.clients.swing.Utilities.ChartPlotType;
import com.d.apps.scoach.clients.swing.Utilities.CounterDimension;
import com.d.apps.scoach.clients.swing.Utilities.DataSumType;
import com.d.apps.scoach.clients.swing.Utilities.GraphAxisHigherFunctions;
import com.d.apps.scoach.clients.swing.Utilities.GraphDimensions;
import com.d.apps.scoach.server.db.model.Coach;
import com.d.apps.scoach.server.db.model.CoachGraph;
import com.d.apps.scoach.server.db.model.Counter;
import com.d.apps.scoach.server.db.model.Profile;

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
    
    public Coach updateCoach(Coach instance);
    
    public Coach findCoachInstance(int cid);
    
    public Counter addCounterData(int counterId, Timestamp created, Double x, Double y, Double z);
    
    public List<Object[]> getCounterDataSummed(int cid, DataSumType type);
    public List<Object[]> getCounterData(int cid, CounterDimension[] dataPart, GraphAxisHigherFunctions[] hfuncs);
    
    public void deleteGraph(int id);
    public Coach addGraph (int coachId, String graphName, ArrayList<Integer> counterIds, 
    			GraphDimensions graphDimension, ChartPlotType plotType, 
    			CounterDimension[] dataFetch, GraphAxisHigherFunctions[] hfuncs);
    public CoachGraph updateGraph (CoachGraph graph);
}

package com.d.apps.scoach.db;

import java.util.List;

import com.d.apps.scoach.db.model.Profile;

public interface DBServices {
	//PROFILES
    public Profile findProfile(int pid);
	public List<Profile> getProfiles();
    public void createProfile(String name, boolean active);
    public void updateProfile(Profile p);
    public void deleteProfile(int id);
    
	public int getProfilesCount();
    public void createProfile(String name);
    public void setActiveProfile(int id);
    public void deactivateAllProfiles();
    public Profile getActiveProfile();
}

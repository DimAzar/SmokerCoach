package com.d.apps.scoach.services.interfaces;

import java.util.List;

import com.d.apps.scoach.db.model.Profile;
import com.d.apps.scoach.db.model.ProfileCoach;

public interface DBServices {
	//PROFILES
    public Profile findProfile(int pid);
	public List<Profile> getProfiles();
    public Profile createProfile(String name, boolean active);
    public void updateProfile(Profile p);
    public void deleteProfile(int id);
    
	public int getProfilesCount();
    public Profile createProfile(String name);
    public void setActiveProfile(int id);
    public void deactivateAllProfiles();
    public Profile getActiveProfile();
    
    public int incrementSmokedCount(int pid);
    public void enableCoach(String coachName, Profile p);
    public List<ProfileCoach> getProfileCoaches(int pid);
}

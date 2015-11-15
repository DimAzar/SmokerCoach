package com.d.apps.scoach.db.selectors;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d.apps.scoach.db.model.Profile;
import com.d.apps.scoach.db.selectors.base.BaseSelector;

public class ProfileSelector extends BaseSelector {
	private static final Logger LOG = LoggerFactory.getLogger(ProfileSelector.class);
	
    public Profile createProfile(String name, boolean active) {
    	entityManager.getTransaction().begin();
    	
    	Profile u = new Profile();
    	u.setFirstName(name);
    	u.setActive(active);
    	entityManager.persist(u);
    	
    	entityManager.getTransaction().commit();
    	
    	return u;
    }

	public  int getProfilesCount() {
    	return entityManager.createNamedQuery("profile.getAllProfiles", Profile.class)
    				.getResultList().size();
	}

	public  List<Profile> getProfiles() {
    	return entityManager.createNamedQuery("profile.getAllProfiles", Profile.class)
    				.getResultList();
	}
    
    public  Profile findProfile(int pid) {
    	return entityManager
    				.find(Profile.class, pid);
    }
    
    public  void setActiveProfile(int id) {
    	List<Profile> profiles = entityManager.createNamedQuery("profile.getAllProfiles", Profile.class).getResultList(); 
    	for (Profile profile : profiles) {
			if (profile.getId() == id) {
				profile.setActive(true);
			} else {
				profile.setActive(false);
			}
			updateEntity(profile);
		}    	
    }
    
    public  void deactivateAllProfiles() {
    	List<Profile> profiles = entityManager.createNamedQuery("profile.getAllProfiles", Profile.class).getResultList();
    	for (Profile profile : profiles) {
			if (profile.isActive()) {
				profile.setActive(false);
				updateEntity(profile);
			}
		}    	
    }
    
    public  Profile getActiveProfile() {
    	Profile ans = null;

    	List<Profile> profiles = entityManager.createNamedQuery("profile.getAllProfiles", Profile.class).getResultList();
    	for (Profile profile : profiles) {
			if (profile.isActive()) {
				if (ans != null) {
					String msg = "Multiple profiles are active! "+ans.getId()+","+profile.getId();
					LOG.error(msg);
					throw new RuntimeException(msg);
				}
				ans = profile;
			}
		}
    	return ans;
    }
}
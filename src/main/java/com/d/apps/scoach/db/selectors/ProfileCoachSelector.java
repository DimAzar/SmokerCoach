package com.d.apps.scoach.db.selectors;

import java.util.List;

import com.d.apps.scoach.db.model.ProfileCoach;
import com.d.apps.scoach.db.selectors.base.BaseSelector;

public class ProfileCoachSelector extends BaseSelector {
	public  List<ProfileCoach> getProfileCoaches(int pid) {
    	return entityManager.createNamedQuery("profileCoach.getProfileCoaches", ProfileCoach.class)
    				.setParameter("pid", pid)
    				.getResultList();
	}
	public  List<ProfileCoach> getAllProfileCoaches(int pid) {
    	return entityManager.createNamedQuery("profileCoach.getAllProfileCoaches", ProfileCoach.class)
    				.getResultList();
	}
}
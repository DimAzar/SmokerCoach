package com.d.apps.scoach.db.selectors;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d.apps.scoach.db.model.ProfileCoach;
import com.d.apps.scoach.db.selectors.base.BaseSelector;

public class ProfileCoachSelector extends BaseSelector {
	private static final Logger LOG = LoggerFactory.getLogger(ProfileCoachSelector.class);
	
	public  List<ProfileCoach> getProfileCoaches(int pid) {
    	return entityManager.createNamedQuery("profileCoach.getProfileCoaches", ProfileCoach.class)
    				.setParameter("pid", pid)
    				.getResultList();
	}
}
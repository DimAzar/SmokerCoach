package com.d.apps.scoach.db.selectors;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d.apps.scoach.db.model.ProfileCoaches;
import com.d.apps.scoach.db.selectors.base.BaseSelector;

public class ProfileCoachesSelector extends BaseSelector {
	private static final Logger LOG = LoggerFactory.getLogger(ProfileCoachesSelector.class);
	
	public  List<ProfileCoaches> getAllCoaches() {
    	return entityManager.createNamedQuery("profileCoaches.getAllCoaches", ProfileCoaches.class)
    				.getResultList();
	}

	public int getCoachIDByName(String name) {
    	return entityManager.createNamedQuery("profileCoaches.getCoachByName", Integer.class)
				.setParameter("name", name)
				.getSingleResult();
		
	}
}
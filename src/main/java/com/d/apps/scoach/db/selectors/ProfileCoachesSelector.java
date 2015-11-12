package com.d.apps.scoach.db.selectors;

import java.util.List;

import com.d.apps.scoach.db.model.ProfileCoaches;
import com.d.apps.scoach.db.selectors.base.BaseSelector;

public class ProfileCoachesSelector extends BaseSelector {
	public  List<ProfileCoaches> getAllCoaches() {
    	return entityManager.createNamedQuery("profileCoaches.getAllCoaches", ProfileCoaches.class)
    				.getResultList();
	}

	public int getCoachIDByName(String name) {
    	return entityManager.createNamedQuery("profileCoaches.getCoachIDByName", Integer.class)
				.setParameter("name", name)
				.getSingleResult();
		
	}

	public ProfileCoaches getCoachByName(String name) {
    	return entityManager.createNamedQuery("profileCoaches.getCoachByName", ProfileCoaches.class)
				.setParameter("name", name)
				.getSingleResult();
		
	}
}
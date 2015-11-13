package com.d.apps.scoach.db.selectors;

import java.util.List;

import com.d.apps.scoach.db.model.coaches.Coaches;
import com.d.apps.scoach.db.selectors.base.BaseSelector;

public class CoachesSelector extends BaseSelector {
	public  List<Coaches> getAllCoaches() {
    	return entityManager.createNamedQuery("profileCoaches.getAllCoaches", Coaches.class)
    				.getResultList();
	}

	public int getCoachIDByName(String name) {
    	return entityManager.createNamedQuery("profileCoaches.getCoachIDByName", Integer.class)
				.setParameter("name", name)
				.getSingleResult();
		
	}

	public Coaches getCoachByName(String name) {
    	return entityManager.createNamedQuery("profileCoaches.getCoachByName", Coaches.class)
				.setParameter("name", name)
				.getSingleResult();
		
	}
}
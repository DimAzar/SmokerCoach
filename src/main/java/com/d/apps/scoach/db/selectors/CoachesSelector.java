package com.d.apps.scoach.db.selectors;

import java.util.List;

import com.d.apps.scoach.db.model.coaches.CoachDefinition;
import com.d.apps.scoach.db.selectors.base.BaseSelector;

public class CoachesSelector extends BaseSelector {
	public  List<CoachDefinition> getAllCoaches() {
    	return entityManager.createNamedQuery("profileCoaches.getAllCoaches", CoachDefinition.class)
    				.getResultList();
	}

	public int getCoachIDByName(String name) {
    	return entityManager.createNamedQuery("profileCoaches.getCoachIDByName", Integer.class)
				.setParameter("name", name)
				.getSingleResult();
		
	}

	public CoachDefinition getCoachByName(String name) {
    	return entityManager.createNamedQuery("profileCoaches.getCoachByName", CoachDefinition.class)
				.setParameter("name", name)
				.getSingleResult();
		
	}
}
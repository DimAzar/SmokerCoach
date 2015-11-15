package com.d.apps.scoach.db.selectors;

import java.util.List;

import com.d.apps.scoach.db.model.CoachTemplate;
import com.d.apps.scoach.db.selectors.base.BaseSelector;

public class CoachesSelector extends BaseSelector {
	public  List<CoachTemplate> getAllCoaches() {
    	return entityManager.createNamedQuery("coachTemplate.getAllCoaches", CoachTemplate.class)
    				.getResultList();
	}
}
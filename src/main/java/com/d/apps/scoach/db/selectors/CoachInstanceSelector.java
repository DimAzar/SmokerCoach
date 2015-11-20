package com.d.apps.scoach.db.selectors;

import java.util.List;

import com.d.apps.scoach.db.model.CoachInstance;
import com.d.apps.scoach.db.selectors.base.BaseSelector;

public class CoachInstanceSelector extends BaseSelector {
	public  List<CoachInstance> getAllCoachInstances() {
    	return entityManager.createNamedQuery("coachInstance.getAllCoachInstances", CoachInstance.class)
    				.getResultList();
	}
	
	public  CoachInstance getCoachInstanceByID(int cid) {
    	return entityManager.createNamedQuery("coachInstance.getCoachInstanceByID", CoachInstance.class)
    				.setParameter("cid", cid)
    				.getSingleResult();
	}
}
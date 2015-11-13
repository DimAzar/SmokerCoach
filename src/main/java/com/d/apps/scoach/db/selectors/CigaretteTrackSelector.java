package com.d.apps.scoach.db.selectors;

import java.util.List;

import com.d.apps.scoach.db.model.coaches.smoker.CigaretteTrackEntry;
import com.d.apps.scoach.db.selectors.base.BaseSelector;

public class CigaretteTrackSelector extends BaseSelector {
	public  List<CigaretteTrackEntry> getAllEntries() {
    	return entityManager.createNamedQuery("cTrackEntry.getAllEntries", CigaretteTrackEntry.class)
    				.getResultList();
	}

}
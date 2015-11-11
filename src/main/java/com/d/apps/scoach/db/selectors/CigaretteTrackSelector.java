package com.d.apps.scoach.db.selectors;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d.apps.scoach.db.model.CigaretteTrackEntry;
import com.d.apps.scoach.db.model.Profile;
import com.d.apps.scoach.db.selectors.base.BaseSelector;

public class CigaretteTrackSelector extends BaseSelector {
	private static final Logger LOG = LoggerFactory.getLogger(CigaretteTrackSelector.class);
	
	public  List<CigaretteTrackEntry> getAllEntries() {
    	return entityManager.createNamedQuery("cTrackEntry.getAllEntries", CigaretteTrackEntry.class)
    				.getResultList();
	}

}
package com.d.apps.scoach.db.selectors;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d.apps.scoach.db.model.CigaretteTrackEntry;
import com.d.apps.scoach.db.model.Profile;
import com.d.apps.scoach.db.selectors.base.BaseSelector;

public class CigaretteTrackSelector extends BaseSelector {
	private static final Logger LOG = LoggerFactory.getLogger(CigaretteTrackSelector.class);
	
    public  void createCigaretteTrackEntry(int pid, String date) {
    	entityManager.getTransaction().begin();

    	CigaretteTrackEntry entry = new CigaretteTrackEntry();
    	Profile p = entityManager.find(Profile.class, pid);
    	entry.setProfile(p);
    	p.addTrackEntry(entry);
    	
    	entityManager.persist(p);
    	entityManager.getTransaction().commit();
    }
    
	public  List<CigaretteTrackEntry> getAllEntries() {
    	return entityManager.createNamedQuery("cTrackEntry.getAllEntries", CigaretteTrackEntry.class)
    				.getResultList();
	}

}
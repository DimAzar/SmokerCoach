package com.d.apps.scoach.db;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.d.apps.scoach.db.model.Profile;

public class DBManager {
	private static final String PERSISTENCE_UNIT = "SmokerCoach";
	private static EntityManager em = null;
	
	static {
		 EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT );
		 em = factory.createEntityManager();
		 assertNotNull(em);
	}
	
    @SuppressWarnings("unchecked")
	public static int getProfilesCount() {
		Query q = em.createQuery("SELECT p FROM Profile p");
		List<Profile> userList = q.getResultList();
        return userList.size();
	}

    @SuppressWarnings("unchecked")
	public static List<Profile> getProfiles() {
    	Query q = em.createQuery("SELECT p FROM Profile p");
		return q.getResultList();
	}
    
    public static void createProfile(String name) {
    	createProfile(name, false);
    }

    public static void createProfile(String name, boolean active) {
    	em.getTransaction().begin();
    	
    	Profile u = new Profile();
    	u.setFirstName(name);
    	u.setActive(active);
    	em.persist(u);
    	
    	em.getTransaction().commit();
    }
}

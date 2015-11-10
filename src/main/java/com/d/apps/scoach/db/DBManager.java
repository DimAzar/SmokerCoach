package com.d.apps.scoach.db;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d.apps.scoach.db.model.Profile;

public class DBManager {
	private static final String PERSISTENCE_UNIT = "SmokerCoach";
	private static final Logger LOG = LoggerFactory.getLogger(DBManager.class);
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
    
    public static Profile findProfile(int pid) {
    	return em.find(Profile.class, pid);
    }
    
    @SuppressWarnings("unchecked")
    public static void setActiveProfile(int id) {
    	Query q = em.createQuery("SELECT p FROM Profile p");
    	for (Profile profile : (List<Profile>)q.getResultList()) {
			if (profile.getId() == id) {
				profile.setActive(true);
			} else {
				profile.setActive(false);
			}
			updateProfile(profile);
		}    	
    }
    
    public static void deleteProfile(int id) {
    	em.getTransaction().begin();
    	em.remove(em.find(Profile.class, id));
    	em.getTransaction().commit();
    }
    
    @SuppressWarnings("unchecked")
    public static void deactivateAllProfiles() {
    	Query q = em.createQuery("SELECT p FROM Profile p");
    	for (Profile profile : (List<Profile>)q.getResultList()) {
			if (profile.isActive()) {
				profile.setActive(false);
				updateProfile(profile);
			}
		}    	
    }
    
    @SuppressWarnings("unchecked")
    public static Profile getActiveProfile() {
    	Profile ans = null;

    	Query q = em.createQuery("SELECT p FROM Profile p");
    	for (Profile profile : (List<Profile>)q.getResultList()) {
			if (profile.isActive()) {
				if (ans != null) {
					String msg = "Multiple profiles are active! "+ans.getId()+","+profile.getId();
					LOG.error(msg);
					throw new RuntimeException(msg);
				}
				ans = profile;
			}
		}
    	return ans;
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

    public static void updateProfile(Profile p) {
    	em.getTransaction().begin();
    	em.merge(p);
    	em.getTransaction().commit();
    }
}

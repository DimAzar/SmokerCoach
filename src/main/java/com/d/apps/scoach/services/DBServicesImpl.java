package com.d.apps.scoach.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d.apps.scoach.Utilities.CounterFunctionType;
import com.d.apps.scoach.Utilities.DataSumType;
import com.d.apps.scoach.db.model.CoachGraph;
import com.d.apps.scoach.db.model.Coach;
import com.d.apps.scoach.db.model.Counter;
import com.d.apps.scoach.db.model.CounterData;
import com.d.apps.scoach.db.model.Profile;
import com.d.apps.scoach.db.model.base.DBEntity;

public class DBServicesImpl implements DBServices {
	private static final String PERSISTENCE_UNIT = "CounterApp";
	private static final Logger LOG = LoggerFactory.getLogger(DBServicesImpl.class);
	
	private EntityManagerFactory factory ;

	public DBServicesImpl() {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
		LOG.debug("DBServices started");
	}

	@Override
	public List<Profile> getProfiles() {
		List<Profile> ans = null;
		
		EntityManager entityManager = factory.createEntityManager();
		ans = entityManager.createNamedQuery("profile.getAllProfiles", Profile.class).getResultList();
		return ans; 
	}

	@Override
	public Profile findProfile(int pid) {
		Profile ans = null;
		
		EntityManager entityManager = factory.createEntityManager();
		ans = entityManager.find(Profile.class, pid);
		return ans; 
	}

	@Override
	public Profile setActiveProfile(int id) {
		EntityManager entityManager = factory.createEntityManager();
    	List<Profile> profiles = getProfiles();
    	Profile activeProfile = null;
    	
    	for (Profile profile : profiles) {
			if (profile.getId() == id) {
				profile.setActive(true);
				activeProfile = profile;
			} else {
				profile.setActive(false);
			}
			updateEntity(profile);
		}    	
    	entityManager.close();
    	return activeProfile;
	}

	@Override
	public void deleteProfile(int id) {
		deleteEntity(id, Profile.class);
	}

	@Override
	public void deleteGraph(int gid) {
		deleteEntity(gid, CoachGraph.class);
	}

	@Override
	public void deactivateAllProfiles() {
		EntityManager entityManager = factory.createEntityManager();
    	List<Profile> profiles = getProfiles();
    	for (Profile profile : profiles) {
			if (profile.isActive()) {
				profile.setActive(false);
				updateEntity(profile);
			}
		}    	
    	entityManager.close();
	}

	@Override
	public Profile getActiveProfile() {
    	Profile ans = null;
    	List<Profile> profiles = getProfiles();
    	for (Profile profile : profiles) {
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

	@Override
	public Profile createProfile(String name, boolean active) {
    	Profile u = new Profile();
    	u.setFirstName(name);
    	u.setActive(active);
    	
    	return (Profile) createEntity(u);
	}

	@Override
	public Profile createCoach(int profileId, String name) {
		Coach coach = new Coach();
		coach.setName(name);
		
		Profile p = findProfile(profileId);
		p.addCoach(coach);
		
    	return (Profile) updateProfile(p);
	}


	@Override
	public Profile updateProfile(Profile p) {
		return (Profile) updateEntity(p);
	}
	
	@Override
	public Coach updateCoach(Coach instance) {
		return (Coach)updateEntity(instance);
	}
	
	@Override
	public Profile enableCoach(String name, Profile p) {
		Coach ci = new Coach();
		ci.setName(p.getFirstName());
		
		p.addCoach(ci);
		updateEntity(p);
		return p;
	}

	@Override
	public Profile disableCoach(String name, Profile p) {
//		deleteEntity(p.removeCoach(name), Coach.class);
		updateEntity(p);
		return p;
	}

	@Override
	public Coach findCoachInstance(int cid) {
		Coach ans = null;
		EntityManager entityManager = factory.createEntityManager();
    	ans = entityManager.createNamedQuery("coachInstance.getCoachInstanceByID", Coach.class)
				.setParameter("cid", cid)
				.getSingleResult();
    	return ans; 
	}

	@Override
	public Profile createCounter(int profileId, String name,
			CounterFunctionType type, double stepValue) {
		Counter instance = new Counter();
		instance.setName(name);
		instance.setStepValue(stepValue);
		instance.setType(type);
		
		Profile p = findProfile(profileId);
		p.addCounter(instance);
		
    	return (Profile) updateProfile(p);
	}

	@Override
	public Counter addCounterData(Counter owner, Timestamp created, Double value) {
		Counter ans = null;
		EntityManager entityManager = factory.createEntityManager();
		CounterData datum = new CounterData();
		datum.setT(created);
		datum.setX(value);
		owner.addDatum(datum);

		createEntity(datum);
		ans = entityManager.find(Counter.class, owner.getId());
		entityManager.close();
		
		return ans;
	}
	
	private static final String generalSumQuery = "select CAST(t as date), sum(x) from counterdata where counter_id = $ID group by CAST(t as date)";
	private static final String generalQuery = "select t, x from counterdata where counter_id = $ID order by t ASC";

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getCounterDataSummed(int cid, DataSumType type) {
		List<Object[]> ans = null;
		EntityManager entityManager = factory.createEntityManager();
		String query = generalSumQuery; 
		
		query = query.replace("$ID", ""+cid);
		query = query.replace("$BREAK", type.name());

		ans = entityManager.createNativeQuery(query).getResultList();
		entityManager.close();
		return ans;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getCounterDataFlat(int cid) {
		List<Object[]> ans = null;
		EntityManager entityManager = factory.createEntityManager();
		String query = generalQuery; 
		
		query = query.replace("$ID", ""+cid);

		ans = entityManager.createNativeQuery(query).getResultList();
		entityManager.close();
		return ans;
	}

	@Override
	public CoachGraph findCoachGraph(int gid) {
		CoachGraph ans = null;
		
		EntityManager entityManager = factory.createEntityManager();
		ans = entityManager.find(CoachGraph.class, gid);
		entityManager.close();
		return ans; 
	}
	
	@Override
	public Coach addGraph (int coachId, String graphName, ArrayList<Integer> counterIds){
		CoachGraph graph = new CoachGraph();
		graph.setName(graphName);
		
		EntityManager entityManager = factory.createEntityManager();
		for (Integer cid : counterIds) {
			Counter cnt = entityManager.find(Counter.class, cid);
			graph.addGraphCounter(cnt);
		}
		entityManager.close();
		
		Coach coach = findCoachInstance(coachId);
		coach.addGraph(graph);
		
		updateEntity(coach);
		return null;
	}

	@Override
	public CoachGraph updateGraph(CoachGraph graph) {
		return (CoachGraph) updateEntity(graph);
	}
	
	//PRIVATE
    private  void deleteEntity(int id, Class<?> cls) {
    	EntityManager entityManager = factory.createEntityManager();
    	try{
	    	entityManager.getTransaction().begin();
	    	entityManager.remove(entityManager.find(cls, id));
	    	entityManager.getTransaction().commit();
	    } catch (Throwable t){
	    	if (entityManager.getTransaction().isActive()) {
	    		entityManager.getTransaction().rollback();
	    	}
	    } finally {
    		entityManager.close();
    	}
    }
    
    private DBEntity updateEntity(DBEntity e) {
    	EntityManager entityManager = factory.createEntityManager();
    	try {
    		entityManager.getTransaction().begin();
        	e = entityManager.merge(e);
        	entityManager.getTransaction().commit();
    	} catch (Throwable t){
	    	if (entityManager.getTransaction().isActive()) {
	    		entityManager.getTransaction().rollback();
	    	}
	    	e = null;
	    } finally {
    		entityManager.close();
    	}
    	return e;
    }
    
    private DBEntity createEntity(DBEntity e) {
    	EntityManager entityManager = factory.createEntityManager();
    	try{
	    	entityManager.getTransaction().begin();
	    	entityManager.persist(e);
	    	entityManager.getTransaction().commit();
	    } catch (Throwable t){
	    	if (entityManager.getTransaction().isActive()) {
	    		entityManager.getTransaction().rollback();
	    	}
	    	e = null;
	    } finally {
    		entityManager.close();
    	}
    	return e;
    }

}
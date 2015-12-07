package com.d.apps.scoach.server.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d.apps.scoach.clients.swing.Utilities.ChartPlotType;
import com.d.apps.scoach.clients.swing.Utilities.CounterDimension;
import com.d.apps.scoach.clients.swing.Utilities.DataSumType;
import com.d.apps.scoach.clients.swing.Utilities.GraphAxisHigherFunctions;
import com.d.apps.scoach.clients.swing.Utilities.GraphDimensions;
import com.d.apps.scoach.server.db.model.Coach;
import com.d.apps.scoach.server.db.model.CoachGraph;
import com.d.apps.scoach.server.db.model.Counter;
import com.d.apps.scoach.server.db.model.CounterData;
import com.d.apps.scoach.server.db.model.Profile;
import com.d.apps.scoach.server.db.model.base.DBEntity;

@Path("/service")
public class DBServicesImpl implements DBServices {
	private static final String PERSISTENCE_UNIT = "CounterApp";
	private static final Logger LOG = LoggerFactory.getLogger(DBServicesImpl.class);

	private static final String dateformatFULL = "yyyy-mm-dd hh24:MI:SS.FF";
	private static final String dateformatMINUTE = "yyyy-mm-dd hh24:MI\":00.00\"";
	private static final String dateformatHOUR = "yyyy-mm-dd hh24\":00:00.00\"";
	private static final String dateformatDAY = "yyyy-mm-dd\" 00:00:00.00\"";
	private static final String dateformatMONTH = "yyyy-mm\"-01 00:00:00.00\"";
	private static final String dateformatYEAR = "yyyy\"-01-01 00:00:00.00\"";

	private EntityManagerFactory factory ;

	public DBServicesImpl() {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
		LOG.debug("DBServices started");
	}

	@GET
	@Path("/profiles")
	@Produces(MediaType.APPLICATION_JSON)
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
	public Profile updateProfile(Profile p) {
		return (Profile) updateEntity(p);
	}
	
	@Override
	public Coach updateCoach(Coach instance) {
		return (Coach)updateEntity(instance);
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
	public Counter addCounterData(int counterId, Timestamp created, Double x, Double y, Double z) {
		Counter ans = null;
		EntityManager entityManager = factory.createEntityManager();
		Counter owner = entityManager.find(Counter.class, counterId);
		CounterData datum = new CounterData();
		datum.setT(created);
		datum.setX(x);
		datum.setY(y);
		datum.setZ(z);
		owner.addDatum(datum);

		createEntity(datum);
		ans = entityManager.find(Counter.class, owner.getId());
		entityManager.close();
		
		return ans;
	}
	
	private static final String generalhfUNCQuery = "select SUM(X), to_char(t, '$DATEFORMAT') from counterdata group by to_char(t, '$DATEFORMAT')";
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getCounterDataSummed(int cid, DataSumType type) {
		List<Object[]> ans = null;
		EntityManager entityManager = factory.createEntityManager();
		String query = generalhfUNCQuery; 
		
		query = query.replace("$ID", ""+cid);
//		query = query.replace("$DATEFORMAT", dateformat);
		
		ans = entityManager.createNativeQuery(query).getResultList();
		entityManager.close();
		return ans;
	}

	private static final String generalQuery = "select $DIMENSIONS from counterdata where counter_id = $CID $GROUPBY $ORDERBY";
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getCounterData(int cid, CounterDimension[] dataPart, GraphAxisHigherFunctions[] hfuncs) {
		List<Object[]> ans = null;
		EntityManager entityManager = factory.createEntityManager();
		String query = generalQuery; 
		
		query = query.replace("$CID", ""+cid);
		query = createSelectAndOrderPart(query, dataPart, hfuncs);
		ans = entityManager.createNativeQuery(query).getResultList();
		entityManager.close();
		return ans;
	}

	@Override
    public Coach addGraph (int coachId, String graphName, ArrayList<Integer> counterIds, 
			GraphDimensions graphDimension, ChartPlotType plotType, CounterDimension[] dataFetch, GraphAxisHigherFunctions[] hfuncs) {
		CoachGraph graph = new CoachGraph();
		graph.setName(graphName);
		graph.setGraphDimension(graphDimension);
		graph.setPlotType(plotType);
		graph.setXAxisDataFetch(dataFetch[0]);
		graph.setYAxisDataFetch(dataFetch[1]);
		graph.setZAxisDataFetch(dataFetch[2]);
		graph.setGraphXHFunc(hfuncs[0]);
		graph.setGraphYHFunc(hfuncs[1]);
		graph.setGraphZHFunc(hfuncs[2]);
		
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
    
    private String createSelectAndOrderPart(String query, CounterDimension[] fetchData, GraphAxisHigherFunctions[] hfuncs) {
    	StringBuffer select = new StringBuffer();
    	String orderby = "";
    	String groupBy = "";
    	
    	for (int i = 0; i < hfuncs.length; i++) {
    		CounterDimension dim = fetchData[i];
    		GraphAxisHigherFunctions hfunc = hfuncs[i];

    		if (dim == CounterDimension.NONE) {
    			continue;
    		}
    		
    		if (hfunc == GraphAxisHigherFunctions.NONE) {
        		select.append(dim.name()).append(",");
    		} else {
    			if (dim == CounterDimension.T) {
    				String dateformat = "";
    				switch (hfunc) {
						case HOUR:
							dateformat = dateformatHOUR;
							break;
						case DAY:
							dateformat = dateformatDAY;
							break;
						case MONTH:
							dateformat = dateformatMONTH;
							break;
						case YEAR:
							dateformat = dateformatYEAR;
							break;
						default:
							break;
					}
					String s = "to_char(t, \'$DATEFORMAT\'),";
					s = s.replace("$DATEFORMAT", dateformat);
					select.append(s);
					
					groupBy = "group by to_char(t, \'$DATEFORMAT\')";
					groupBy = groupBy.replace("$DATEFORMAT", dateformat);
					
					orderby = groupBy.replace("group by", "order by");
    			} else {
        			select.append(String.format("%s(%s),", hfunc.name(), dim.name()));
        		}
    		} 
    	}
		select.replace(select.length()-1, select.length(), " ");
    	return query.replace("$DIMENSIONS", select).replace("$GROUPBY", groupBy).replace("$ORDERBY", orderby);
    }
}
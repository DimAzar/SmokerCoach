package com.d.apps.scoach.db.selectors;

import java.util.List;

import com.d.apps.scoach.Utilities.DataSumType;
import com.d.apps.scoach.db.selectors.base.BaseSelector;

public class CounterDataSelector extends BaseSelector {
//	private static final Logger LOG = LoggerFactory.getLogger(CounterDataSelector.class);

	private static final String generalSumQuery = "select CAST(addeddate as date), sum(datum) from counterdata where counter_id = $ID group by CAST(addeddate as date)";
	private static final String generalQuery = "select addeddate, datum from counterdata where counter_id = $ID order by addeddate ASC";
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getCounterDataSummed(int cid, DataSumType type) {
		String query = generalSumQuery; 
		
		query = query.replace("$ID", ""+cid);
		query = query.replace("$BREAK", type.name());

		return entityManager.createNativeQuery(query).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getCounterDataFlat(int cid) {
		String query = generalQuery; 
		
		query = query.replace("$ID", ""+cid);

		return entityManager.createNativeQuery(query).getResultList();
	}
}
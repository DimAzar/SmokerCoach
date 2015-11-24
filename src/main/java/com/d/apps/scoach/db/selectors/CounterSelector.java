package com.d.apps.scoach.db.selectors;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d.apps.scoach.db.model.Counter;
import com.d.apps.scoach.db.model.CounterData;
import com.d.apps.scoach.db.model.base.DBEntity;
import com.d.apps.scoach.db.selectors.base.BaseSelector;

public class CounterSelector extends BaseSelector {
	private static final Logger LOG = LoggerFactory.getLogger(CounterSelector.class);
	
	public DBEntity addCounterData(Counter owner, Timestamp created, Double value) {
		CounterData datum = new CounterData();
		datum.setAddedDate(created);
		datum.setDatum(value);
		owner.addDatum(datum);

		createEntity(datum);
		return entityManager.find(Counter.class, owner.getId());
	}
}
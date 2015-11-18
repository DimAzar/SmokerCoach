package com.d.apps.scoach.test;

import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

import com.d.apps.scoach.db.model.Profile;

public class AppTests {
	 @Test
	 public void test2(){
		 EntityManagerFactory factory = Persistence.createEntityManagerFactory("CounterApp");
		 EntityManager theManager = factory.createEntityManager();
		 assertNotNull(theManager);

		 theManager.getTransaction().begin();
		 Profile person = new Profile();
		 person.setFirstName("ana");
		 theManager.persist(person);
		 System.out.println(person.getId());
		 theManager.getTransaction().commit();

		 Profile p = (Profile)theManager.find(Profile.class, 1);
		 System.out.println(person.getId());

		 assertNotNull(p);
    }
}
package com.revature.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionFactorySingleton {
	
	private static SessionFactory sessionFactory;
	
	public synchronized static SessionFactory getSessionFactory() {
		if(sessionFactory == null) {
			Configuration config = new Configuration();
			config.setProperty("hibernate.connection.username", "admin");
			config.setProperty("hibernate.connection.password", "somepass");
			config.configure("hibernate.cfg.xml");
			
			sessionFactory = config.buildSessionFactory();
		}
		
		return sessionFactory;
	}
}

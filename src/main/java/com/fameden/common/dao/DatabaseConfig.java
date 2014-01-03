package com.fameden.common.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fameden.common.exception.BreachingSingletonException;

public class DatabaseConfig {

	static Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory() throws Exception {
		if (sessionFactory == null) {
			try {
				sessionFactory = new Configuration().configure()
						.buildSessionFactory();
			} catch (Throwable ex) {
				logger.info("Exception in DatabaseConfig");
				logger.error(null, ex);
				throw new ExceptionInInitializerError(ex);
			}
		}

		return sessionFactory;
	}

	private DatabaseConfig() throws BreachingSingletonException {
		if (sessionFactory != null) {
			throw new BreachingSingletonException(DatabaseConfig.class.getName());
		}
	}

}

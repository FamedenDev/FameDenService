package com.fameden.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.fameden.common.constants.CommonConstants;
import com.fameden.housekeeping.constants.HouseKeepingConstants;

/**
 * Application Lifecycle Listener implementation class FamedenContextListener
 * 
 */
@WebListener
public class FamedenContextListener implements ServletContextListener {

	/**
	 * Default constructor.
	 */
	public FamedenContextListener() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent context) {
		System.setProperty(
				HouseKeepingConstants.PRIVATE_RSA_KEY,
				context.getServletContext().getRealPath(
						HouseKeepingConstants.PRIVATE_RSA_KEY_PATH));

		System.setProperty(
				HouseKeepingConstants.PUBLIC_RSA_KEY,
				context.getServletContext().getRealPath(
						HouseKeepingConstants.PUBLIC_RSA_KEY_PATH));

		System.setProperty(
				CommonConstants.EMAIL_TEMPLATES_PROPERTY,
				context.getServletContext().getRealPath(
						CommonConstants.EMAIL_TEMPLATES_PATH));

		System.setProperty(
				HouseKeepingConstants.TNC_FILE_PROPERTY_NAME,
				context.getServletContext().getRealPath(
						HouseKeepingConstants.TNC_FILE_PATH));
		
		System.setProperty(
				HouseKeepingConstants.ABOUT_US_FILE_PROPERTY_NAME,
				context.getServletContext().getRealPath(
						HouseKeepingConstants.ABOUT_US_FILE_PATH));
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

}

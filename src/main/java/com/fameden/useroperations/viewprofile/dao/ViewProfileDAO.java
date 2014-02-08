package com.fameden.useroperations.viewprofile.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fameden.bean.user.FamedenUserInfoBean;
import com.fameden.common.dao.DatabaseConfig;
import com.fameden.common.exception.BreachingSingletonException;

public class ViewProfileDAO {

	Logger logger = LoggerFactory.getLogger(ViewProfileDAO.class);

	private static ViewProfileDAO SINGLETON;

	public static ViewProfileDAO getInstance()
			throws BreachingSingletonException {
		if (SINGLETON == null)
			SINGLETON = new ViewProfileDAO();

		return SINGLETON;
	}

	private ViewProfileDAO() throws BreachingSingletonException {
		if (SINGLETON != null) {
			throw new BreachingSingletonException(
					ViewProfileDAO.class.getName());
		}
	}

	@SuppressWarnings("unchecked")
	public FamedenUserInfoBean getUserProfileInfo(int externalUserId)
			throws Exception {
		FamedenUserInfoBean userInfo = null;
		try {
			Session session = DatabaseConfig.getSessionFactory().openSession();

			session.beginTransaction();

			Criteria crit = session.createCriteria(FamedenUserInfoBean.class);

			crit.createAlias("famedenUserBean", "famedenUser");
			Criterion requestIDRestriction = Restrictions.eq(
					"famedenUser.famdenExternalUserId", externalUserId);
			crit.add(requestIDRestriction);
			List<FamedenUserInfoBean> famedenUserInfoList = ((List<FamedenUserInfoBean>) crit
					.list());

			if (famedenUserInfoList != null && famedenUserInfoList.size() > 0) {
				userInfo = famedenUserInfoList.get(0);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return userInfo;
	}
}

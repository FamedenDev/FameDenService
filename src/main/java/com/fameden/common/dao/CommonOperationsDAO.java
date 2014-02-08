package com.fameden.common.dao;

import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fameden.bean.request.FamedenRequestBean;
import com.fameden.bean.request.FamedenRequestDetailBean;
import com.fameden.bean.user.FamedenUserBean;
import com.fameden.bean.user.FamedenUserInfoBean;
import com.fameden.common.constants.CommonConstants;
import com.fameden.common.exception.BreachingSingletonException;
import com.fameden.useroperations.login.constants.LoginConstatns;
import com.fameden.useroperations.login.exception.LoginValidationException;

public class CommonOperationsDAO implements Serializable {
	private static final long serialVersionUID = 8562224930520844251L;

	Logger logger = LoggerFactory.getLogger(CommonOperationsDAO.class);

	private static CommonOperationsDAO SINGLETON;

	public static CommonOperationsDAO getInstance()
			throws BreachingSingletonException {
		if (SINGLETON == null)
			SINGLETON = new CommonOperationsDAO();

		return SINGLETON;
	}

	private CommonOperationsDAO() throws BreachingSingletonException {
		if (SINGLETON != null) {
			throw new BreachingSingletonException(
					CommonOperationsDAO.class.getName());
		}
	}

	public int insertRequest(FamedenRequestDetailBean frd) throws Exception {
		int requestId = 0;
		try {
			Session session = DatabaseConfig.getSessionFactory().openSession();

			session.beginTransaction();

			session.save(frd);

			session.getTransaction().commit();

			requestId = frd.getFamedenRequestBean().getRequestID();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return requestId;
	}

	public void updateRequestStatus(int requestId, String status)
			throws Exception {
		if (requestId > 0) {
			try {
				Session session = DatabaseConfig.getSessionFactory()
						.openSession();

				session.beginTransaction();

				FamedenRequestBean request = (FamedenRequestBean) session.get(
						FamedenRequestBean.class, new Integer(requestId));

				request.setRequestStatus(status);
				request.setRequestUpdateDate(new Date(Calendar.getInstance()
						.getTimeInMillis()));

				session.getTransaction().commit();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw e;
			}
		} 
	}

	@SuppressWarnings("unchecked")
	public FamedenRequestDetailBean getRequest(int requestId) throws Exception {
		FamedenRequestDetailBean frd = null;
		try {
			Session session = DatabaseConfig.getSessionFactory().openSession();

			session.beginTransaction();

			Criteria crit = session
					.createCriteria(FamedenRequestDetailBean.class);

			crit.createAlias("famedenRequestBean", "frd");
			Criterion requestIDRestriction = Restrictions.eq("frd.requestID",
					requestId);
			crit.add(requestIDRestriction);
			List<FamedenRequestDetailBean> famedenRequestDetailList = ((List<FamedenRequestDetailBean>) crit
					.list());

			if (famedenRequestDetailList != null
					&& famedenRequestDetailList.size() > 0) {
				frd = famedenRequestDetailList.get(0);
			}

			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return frd;
	}

	@SuppressWarnings("unchecked")
	public FamedenUserBean searchByEmailId(String emailId) throws Exception {
		FamedenUserBean user = null;
		try {
			Session session = DatabaseConfig.getSessionFactory().openSession();

			session.beginTransaction();

			Criteria crit = session.createCriteria(FamedenUserBean.class);
			Criterion emailAddressRestriction = Restrictions.eq("emailAddress",
					emailId);

			Criterion activeRestriction = Restrictions.eq("active",
					CommonConstants.ACTIVE);

			LogicalExpression andExp = Restrictions.and(
					emailAddressRestriction, activeRestriction);
			crit.add(andExp);

			List<FamedenUserBean> famedenUserList = ((List<FamedenUserBean>) crit
					.list());

			if (famedenUserList != null && famedenUserList.size() > 0) {
				user = famedenUserList.get(0);
			}else{
				throw new Exception(CommonConstants.USER_DO_NOT_EXIST);
			}

			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return user;
	}
	
	@SuppressWarnings("unchecked")
	public FamedenUserBean searchByExternalUserId(int externalUserId) throws Exception {
		FamedenUserBean user = null;
		try {
			Session session = DatabaseConfig.getSessionFactory().openSession();

			session.beginTransaction();

			Criteria crit = session.createCriteria(FamedenUserBean.class);
			Criterion emailAddressRestriction = Restrictions.eq("famdenExternalUserId",
					externalUserId);

			Criterion activeRestriction = Restrictions.eq("active",
					CommonConstants.ACTIVE);

			LogicalExpression andExp = Restrictions.and(
					emailAddressRestriction, activeRestriction);
			crit.add(andExp);

			List<FamedenUserBean> famedenUserList = ((List<FamedenUserBean>) crit
					.list());

			if (famedenUserList != null && famedenUserList.size() > 0) {
				user = famedenUserList.get(0);
			}else{
				throw new Exception(CommonConstants.USER_DO_NOT_EXIST);
			}

			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return user;
	}

	@SuppressWarnings("unchecked")
	public FamedenUserBean searchByEmailIdAndMobileNumber(String emailId,
			String mobileNumber) throws Exception {
		FamedenUserBean user = null;
		try {
			Session session = DatabaseConfig.getSessionFactory().openSession();

			session.beginTransaction();

			Criteria crit = session.createCriteria(FamedenUserBean.class);
			Criterion emailAddressRestriction = Restrictions.eq("emailAddress",
					emailId);

			Criterion mobileRestriction = Restrictions.eq("mobileNumber",
					mobileNumber);

			LogicalExpression orExp = Restrictions.or(emailAddressRestriction,
					mobileRestriction);

			Criterion activeRestriction = Restrictions.eq("active",
					CommonConstants.ACTIVE);
			LogicalExpression andExp = Restrictions.and(orExp,
					activeRestriction);

			crit.add(andExp);

			List<FamedenUserBean> famedenUserList = ((List<FamedenUserBean>) crit
					.list());

			if (famedenUserList != null && famedenUserList.size() > 0) {
				user = famedenUserList.get(0);
			}

			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return user;
	}

	@SuppressWarnings("unchecked")
	public FamedenUserInfoBean getVerifiedUserInfoByEmailId(String emailId)
			throws Exception {
		FamedenUserBean user = null;
		FamedenUserInfoBean userInfo = null;

		user = searchByEmailId(emailId);

		if (user != null) {
			if (user.getIsVerified() != null
					&& user.getIsVerified().equals(CommonConstants.ACTIVE)) {
				Session session = DatabaseConfig.getSessionFactory()
						.openSession();

				session.beginTransaction();

				Criteria crit = session
						.createCriteria(FamedenUserInfoBean.class);

				crit.createAlias("famedenUserBean", "famedenUser");
				Criterion requestIDRestriction = Restrictions.eq(
						"famedenUser.famdenExternalUserId",
						user.getFamdenExternalUserId());
				crit.add(requestIDRestriction);
				List<FamedenUserInfoBean> famedenUserInfoList = ((List<FamedenUserInfoBean>) crit
						.list());

				if (famedenUserInfoList != null
						&& famedenUserInfoList.size() > 0) {
					userInfo = famedenUserInfoList.get(0);
				} else {
					throw new LoginValidationException(
							LoginConstatns.USER_INFO_DO_NOT_EXIST);
				}

				session.getTransaction().commit();

			} else {
				throw new LoginValidationException(
						LoginConstatns.USER_IS_NOT_VERIFIED);
			}
		} else {
			throw new LoginValidationException(CommonConstants.USER_DO_NOT_EXIST);
		}
		return userInfo;
	}

}

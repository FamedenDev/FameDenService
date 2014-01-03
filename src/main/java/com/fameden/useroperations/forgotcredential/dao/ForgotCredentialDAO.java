package com.fameden.useroperations.forgotcredential.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fameden.bean.codeverification.FamedenCodeVerificationBean;
import com.fameden.bean.codeverification.FamedenCodeVerificationCompositePK;
import com.fameden.bean.user.FamedenUserBean;
import com.fameden.bean.user.FamedenUserIdsMapBean;
import com.fameden.bean.user.FamedenUserKeysBean;
import com.fameden.common.constants.CommonConstants;
import com.fameden.common.dao.CommonOperationsDAO;
import com.fameden.common.dao.DatabaseConfig;
import com.fameden.common.exception.BreachingSingletonException;
import com.fameden.useroperations.forgotcredential.constants.ForgotCredentialConstants;
import com.fameden.useroperations.forgotcredential.dto.ForgotCredentialDTO;
import com.fameden.useroperations.forgotcredential.exception.ForgotCredentialException;
import com.fameden.useroperations.login.exception.LoginValidationException;

public class ForgotCredentialDAO implements Serializable {

	private static final long serialVersionUID = -7582548557116976570L;
	private static ForgotCredentialDAO SINGLETON;
	private Logger logger = LoggerFactory.getLogger(ForgotCredentialDAO.class);

	public static ForgotCredentialDAO getInstance()
			throws BreachingSingletonException {
		if (SINGLETON == null)
			SINGLETON = new ForgotCredentialDAO();

		return SINGLETON;
	}

	private ForgotCredentialDAO() throws BreachingSingletonException {
		if (SINGLETON != null) {
			throw new BreachingSingletonException(
					ForgotCredentialDAO.class.getName());
		}
	}

	@SuppressWarnings("unchecked")
	public boolean insertVerificationCode(ForgotCredentialDTO dto)
			throws Exception {
		boolean result = false;
		Session session = null;
		try {
			if (dto.getRequestType().equals(
					ForgotCredentialConstants.FORGOT_PASSWORD_REQUEST_TYPE)) {
				session = DatabaseConfig.getSessionFactory().openSession();

				session.beginTransaction();

				CommonOperationsDAO commonDao = CommonOperationsDAO
						.getInstance();

				FamedenUserBean user = commonDao.getVerifiedUserInfoByEmailId(
						dto.getUserEmailAddress()).getFamedenUserBean();

				Criteria crit = session
						.createCriteria(FamedenCodeVerificationBean.class);

				Criterion verificationTypeRestriction = Restrictions.eq(
						"compositePK.verificationType", dto.getRequestType());

				Criterion userIdRestriction = Restrictions.eq(
						"compositePK.famedenUserBean.famdenExternalUserId",
						user.getFamdenExternalUserId());

				LogicalExpression andExp = Restrictions.and(userIdRestriction,
						verificationTypeRestriction);
				crit.add(andExp);

				List<FamedenCodeVerificationBean> famedenCodeVerificationList = ((List<FamedenCodeVerificationBean>) crit
						.list());

				if (famedenCodeVerificationList != null
						&& famedenCodeVerificationList.size() > 0) {
					FamedenCodeVerificationBean verification = famedenCodeVerificationList
							.get(0);
					verification.setCode(dto.getVerificationCode());
				} else {
					FamedenCodeVerificationBean codeVerificationBean = new FamedenCodeVerificationBean();
					FamedenCodeVerificationCompositePK compositePK = new FamedenCodeVerificationCompositePK();
					compositePK.setFamedenUserBean(user);
					codeVerificationBean.setCompositePK(compositePK);
					codeVerificationBean.setCode(dto.getVerificationCode());
					compositePK.setVerificationType(dto.getRequestType());

					session.save(codeVerificationBean);
				}

				session.getTransaction().commit();
				result = true;
			} else {
				throw new ForgotCredentialException(
						CommonConstants.UNSUPPORTED_REQUEST_TYPE);
			}
		} catch (LoginValidationException e) {
			throw new ForgotCredentialException(e.getMessage());
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
			logger.error(e.getMessage(), e);
			throw e;
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public FamedenCodeVerificationBean getVerificationCodeDetails(
			String emailAddress, String mobileNumber, String verificationCode,
			String verificationType) throws Exception {
		Session session = null;
		FamedenCodeVerificationBean verification = null;
		try {
			session = DatabaseConfig.getSessionFactory().openSession();

			session.beginTransaction();
			CommonOperationsDAO commonDao = CommonOperationsDAO.getInstance();
			FamedenUserBean user = commonDao.getVerifiedUserInfoByEmailId(
					emailAddress).getFamedenUserBean();

			Criteria crit = session
					.createCriteria(FamedenCodeVerificationBean.class);

			Criterion verificationTypeRestriction = Restrictions.eq(
					"compositePK.verificationType",
					ForgotCredentialConstants.FORGOT_PASSWORD_REQUEST_TYPE);

			Criterion userIdRestriction = Restrictions.eq(
					"compositePK.famedenUserBean.famdenExternalUserId",
					user.getFamdenExternalUserId());

			LogicalExpression andExp = Restrictions.and(userIdRestriction,
					verificationTypeRestriction);

			Criterion verificationCodeRestriction = Restrictions.eq("code",
					verificationCode);
			LogicalExpression finalAndExp = Restrictions.and(andExp,
					verificationCodeRestriction);

			crit.add(finalAndExp);

			List<FamedenCodeVerificationBean> famedenCodeVerificationList = ((List<FamedenCodeVerificationBean>) crit
					.list());

			if (famedenCodeVerificationList != null
					&& famedenCodeVerificationList.size() > 0) {
				verification = famedenCodeVerificationList.get(0);
			}
		} catch (LoginValidationException e) {
			throw new ForgotCredentialException(e.getMessage());
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
			logger.error(e.getMessage(), e);
			throw e;

		}
		return verification;
	}

	@SuppressWarnings("unchecked")
	public FamedenUserKeysBean upatePasswordByEmailId(String emailId,
			String password) throws Exception {
		FamedenUserKeysBean userKey = null;

		Session session;
		try {
			CommonOperationsDAO dao = CommonOperationsDAO.getInstance();
			FamedenUserBean user = dao.searchByEmailId(emailId);
			if (user != null) {
				session = DatabaseConfig.getSessionFactory().openSession();

				session.beginTransaction();

				Criteria crit = session
						.createCriteria(FamedenUserIdsMapBean.class);
				Criterion requestIDRestriction = Restrictions
						.eq("famedenUserMappingCompositePK.famedenUserBean.famdenExternalUserId",
								user.getFamdenExternalUserId());
				crit.add(requestIDRestriction);
				List<FamedenUserIdsMapBean> famedenUserIdsMapList = ((List<FamedenUserIdsMapBean>) crit
						.list());

				if (famedenUserIdsMapList != null
						&& famedenUserIdsMapList.size() > 0) {
					FamedenUserIdsMapBean compositePK = famedenUserIdsMapList
							.get(0);

					userKey = (FamedenUserKeysBean) session.get(
							FamedenUserKeysBean.class, new Integer(compositePK
									.getFamedenUserMappingCompositePK()
									.getFamedenUserKeys()
									.getFamdenInternalUserId()));

					userKey.setPassword(password);

				}
			} else {
				throw new ForgotCredentialException(
						CommonConstants.USER_DO_NOT_EXIST);
			}

			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			throw e;
		}
		return userKey;
	}
}

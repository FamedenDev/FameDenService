package com.fameden.useroperations.login.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fameden.bean.user.FamedenUserIdsMapBean;
import com.fameden.bean.user.FamedenUserInfoBean;
import com.fameden.bean.user.FamedenUserKeysBean;
import com.fameden.common.dao.CommonOperationsDAO;
import com.fameden.common.dao.DatabaseConfig;
import com.fameden.common.exception.BreachingSingletonException;
import com.fameden.useroperations.login.constants.LoginConstatns;
import com.fameden.useroperations.login.dto.LoginDTO;
import com.fameden.useroperations.login.exception.LoginValidationException;
import com.fameden.util.encryptdecrypt.SaltAlgorithmImpl;

public class LoginDAO {

	Logger logger = LoggerFactory.getLogger(LoginDAO.class);

	private static LoginDAO SINGLETON;

	private CommonOperationsDAO commonDAO = null;

	public static LoginDAO getInstance() throws BreachingSingletonException {
		if (SINGLETON == null)
			SINGLETON = new LoginDAO();

		return SINGLETON;
	}

	private LoginDAO() throws BreachingSingletonException {
		if (SINGLETON != null) {
			throw new BreachingSingletonException(LoginDAO.class.getName());
		}
	}

	public FamedenUserInfoBean authenticateUser(LoginDTO dto) throws Exception {
		FamedenUserInfoBean userInfo = null;
		int externalUserId = dto.getExternalUserId();
		String emailId = dto.getUserEmailAddress();
		String password = dto.getPassword();
		try {

			FamedenUserKeysBean userKey = getPasswordByUserId(externalUserId);
			if (userKey != null) {
				String actualPassword = userKey.getPassword();
				SaltAlgorithmImpl salt = SaltAlgorithmImpl.getInstance();

				boolean valid = salt.validateStrings(password, actualPassword);
				if (valid) {
					commonDAO = CommonOperationsDAO.getInstance();
					userInfo = commonDAO.getVerifiedUserInfoByEmailId(emailId);
				} else {
					throw new LoginValidationException(
							LoginConstatns.INCORRECT_PASSWORD);
				}
			} else {
				throw new LoginValidationException(
						LoginConstatns.USER_INFO_DO_NOT_EXIST);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return userInfo;
	}

	@SuppressWarnings("unchecked")
	private FamedenUserKeysBean getPasswordByUserId(int externalUserId) {
		FamedenUserKeysBean userKey = null;

		Session session;
		try {
			session = DatabaseConfig.getSessionFactory().openSession();

			session.beginTransaction();

			Criteria crit = session.createCriteria(FamedenUserIdsMapBean.class);
			Criterion requestIDRestriction = Restrictions.eq(
					"famedenUserMappingCompositePK.famedenUserBean.famdenExternalUserId", externalUserId);
			crit.add(requestIDRestriction);
			List<FamedenUserIdsMapBean> famedenUserIdsMapList = ((List<FamedenUserIdsMapBean>) crit
					.list());

			if (famedenUserIdsMapList != null
					&& famedenUserIdsMapList.size() > 0) {
				FamedenUserIdsMapBean compositePK = famedenUserIdsMapList
						.get(0);

				userKey = (FamedenUserKeysBean) session
						.get(FamedenUserKeysBean.class,
								new Integer(compositePK
										.getFamedenUserMappingCompositePK()
										.getFamedenUserKeys()
										.getFamdenInternalUserId()));

			}

			session.getTransaction().commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userKey;
	}

}

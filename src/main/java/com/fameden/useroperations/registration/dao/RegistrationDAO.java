package com.fameden.useroperations.registration.dao;

import java.sql.Date;
import java.util.Calendar;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fameden.bean.user.FamedenUserBean;
import com.fameden.bean.user.FamedenUserIdsMapBean;
import com.fameden.bean.user.FamedenUserInfoBean;
import com.fameden.bean.user.FamedenUserKeysBean;
import com.fameden.bean.user.FamedenUserMappingCompositePK;
import com.fameden.common.constants.CommonConstants;
import com.fameden.common.dao.CommonOperationsDAO;
import com.fameden.common.dao.DatabaseConfig;
import com.fameden.common.exception.BreachingSingletonException;
import com.fameden.useroperations.registration.dto.RegistrationDTO;

public class RegistrationDAO {

	Logger logger = LoggerFactory.getLogger(RegistrationDAO.class);

	private static RegistrationDAO SINGLETON;

	private CommonOperationsDAO commonDAO = null;

	public static RegistrationDAO getInstance()
			throws BreachingSingletonException {
		if (SINGLETON == null)
			SINGLETON = new RegistrationDAO();

		return SINGLETON;
	}

	private RegistrationDAO() throws BreachingSingletonException {
		if (SINGLETON != null) {
			throw new BreachingSingletonException(
					RegistrationDAO.class.getName());
		}
	}

	public boolean registerUser(RegistrationDTO dto) throws Exception {
		boolean result = false;
		Session session = null;
		FamedenUserInfoBean famdenUserInfo = null;
		try {
			famdenUserInfo = populateFamedenUserInfo(dto);
		} catch (Exception e) {
			throw e;
		}
		try {
			session = DatabaseConfig.getSessionFactory().openSession();

			session.beginTransaction();
			FamedenUserKeysBean famedenUserKeys = populateFamedenUserKeys(dto);

			FamedenUserIdsMapBean famedenUserIdsMap = null;
			FamedenUserMappingCompositePK famedenUserMappingCompositePK = new FamedenUserMappingCompositePK();
			famedenUserIdsMap = new FamedenUserIdsMapBean();

			famedenUserMappingCompositePK.setFamedenUser(famdenUserInfo
					.getFamedenUserBean());
			famedenUserMappingCompositePK.setFamedenUserKeys(famedenUserKeys);

			famedenUserIdsMap
					.setFamedenUserMappingCompositePK(famedenUserMappingCompositePK);
			session.save(famdenUserInfo);
			session.save(famedenUserKeys);
			session.save(famedenUserIdsMap);

			session.getTransaction().commit();
			result = true;
		} catch (Exception e) {
			if (session != null) {
				session.getTransaction().rollback();
			}
			logger.error(e.getMessage(), e);
			throw e;
		}

		return result;
	}

	public FamedenUserBean populateFamedenUser(RegistrationDTO dto)
			throws Exception {
		FamedenUserBean famedenUser = null;
		if (dto != null) {
			famedenUser = new FamedenUserBean();
			famedenUser.setActive(CommonConstants.ACTIVE);
			famedenUser.setIsVerified(CommonConstants.INACTIVE);
			famedenUser.setCreationDate(new Date(Calendar.getInstance()
					.getTimeInMillis()));
			commonDAO = CommonOperationsDAO.getInstance();
			famedenUser.setFamedenRequestBean(commonDAO.getRequest(
					dto.getRequestId()).getFamedenRequestBean());
			famedenUser.setEmailAddress(dto.getUserEmailAddress());

		}
		return famedenUser;
	}

	public FamedenUserKeysBean populateFamedenUserKeys(RegistrationDTO dto) {
		FamedenUserKeysBean famedenUserDetail = null;
		if (dto != null) {
			famedenUserDetail = new FamedenUserKeysBean();
			famedenUserDetail.setPassword(dto.getUserPassword());
		}
		return famedenUserDetail;

	}

	public FamedenUserInfoBean populateFamedenUserInfo(RegistrationDTO dto)
			throws Exception {
		FamedenUserInfoBean famedenUserInfo = null;
		if (dto != null) {
			famedenUserInfo = new FamedenUserInfoBean();
			famedenUserInfo.setFullName(dto.getUserFullName());

			famedenUserInfo.setFamedenUserBean(populateFamedenUser(dto));

		}
		return famedenUserInfo;
	}

}

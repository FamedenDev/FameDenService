package com.fameden.useroperations.emailverification.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fameden.bean.user.FamedenUserBean;
import com.fameden.common.constants.CommonConstants;
import com.fameden.common.dao.DatabaseConfig;
import com.fameden.common.exception.BreachingSingletonException;
import com.fameden.useroperations.emailverification.dto.EmailVerificationDTO;

public class EmailVerificationDAO implements Serializable {
	private static final long serialVersionUID = 8562224930520844251L;

	Logger logger = LoggerFactory.getLogger(EmailVerificationDAO.class);

	private static EmailVerificationDAO SINGLETON;

	public static EmailVerificationDAO getInstance()
			throws BreachingSingletonException {
		if (SINGLETON == null)
			SINGLETON = new EmailVerificationDAO();

		return SINGLETON;
	}

	private EmailVerificationDAO() throws BreachingSingletonException {
		if (SINGLETON != null) {
			throw new BreachingSingletonException(EmailVerificationDAO.class.getName());
		}
	}

	@SuppressWarnings("unchecked")
	public boolean verifyEmailAddress(EmailVerificationDTO dto)
			throws Exception {
		boolean isValid = false;
		try {
			Session session = DatabaseConfig.getSessionFactory().openSession();

			session.beginTransaction();

			Criteria crit = session.createCriteria(FamedenUserBean.class);

			Criterion activeRestriction = Restrictions.eq("active",
					CommonConstants.ACTIVE);
			crit.createAlias("famedenRequestBean", "frd");
			
			Criterion requestIdRestriction = Restrictions.eq("frd.requestID",
					dto.getRequestId());

			Criterion isVerifiedRestriction = Restrictions.eq("isVerified",
					CommonConstants.INACTIVE);

			LogicalExpression andExp = Restrictions.and(requestIdRestriction,
					activeRestriction);

			LogicalExpression andExp1 = Restrictions.and(isVerifiedRestriction,
					andExp);
			crit.add(andExp1);

			List<FamedenUserBean> famedenUserList = ((List<FamedenUserBean>) crit
					.list());

			if (famedenUserList != null && famedenUserList.size() > 0) {
				isValid = true;
				FamedenUserBean user = famedenUserList.get(0);
				user.setIsVerified(CommonConstants.ACTIVE);
			}

			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		return isValid;
	}

}

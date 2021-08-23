package com.revature.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.revature.dto.AddOrEditReimbursementDTO;
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementStatus;
import com.revature.model.ReimbursementType;
import com.revature.model.Users;
import com.revature.util.SessionFactorySingleton;

public class ReimbursementDAOImpl implements ReimbursementDAO {

	@Override
	public List<Reimbursement> getAllReimbursementsFromAllUsers() {
		List<Reimbursement> reimbursement = new ArrayList<>();
		SessionFactory sf = SessionFactorySingleton.getSessionFactory();
		Session session = sf.openSession();

		try {
			String hql = "Select r From Reimbursement r";
			reimbursement = session.createQuery(hql).getResultList();
			return reimbursement;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public List<Reimbursement> getAllReimbursementByUserId(int userId) {
		List<Reimbursement> reimbursement = new ArrayList<>();
		SessionFactory sf = SessionFactorySingleton.getSessionFactory();
		Session session = sf.openSession();
		// CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<Reimbursement> query = builder.createQuery(Reimbursement.class);
//		Root<Reimbursement> root = query.from(Reimbursement.class);
//		Join<Reimbursement, Users> reimb = root.join("author", JoinType.INNER);
//		query.select(root).where(builder.equal(root.get("author"), user));
//		UserRoles finanaceManager = session.createQuery(query).getSingleResult();
		try {
			String hql = "Select r FROM Reimbursement r Join r.author u WHERE u.id = :userid";
			reimbursement = session.createQuery(hql).setParameter("userid", userId).getResultList();
			return reimbursement;
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

	@Override
	public void deleteReimbursementById(int reimbursementId) throws PersistenceException {
		SessionFactory sf = SessionFactorySingleton.getSessionFactory();
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		try {
			String hql = "Delete FROM Reimbursement r where r.id = :reimbursementId";
			int recordUpdate = session.createQuery(hql).setParameter("reimbursementId", reimbursementId)
					.executeUpdate();
			tx.commit();
			if (recordUpdate != 1) {
				throw new PersistenceException("Fail to delete reimbursement.");
			}
		} catch (IllegalStateException e) {
			tx.rollback();
			throw new IllegalStateException(e.getMessage());
		} catch (PersistenceException e) {
			tx.rollback();
			throw new PersistenceException(e.getMessage());
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}


	@Override
	public Reimbursement editReimbursementById(int reimbursementId, AddOrEditReimbursementDTO reimbursementDto) throws PersistenceException {
		SessionFactory sf = SessionFactorySingleton.getSessionFactory();
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		try {
			String hql = "Select r FROM Reimbursement r where r.id = :reimbursementId";
			Reimbursement reimbursement = (Reimbursement) session.createQuery(hql)
					.setParameter("reimbursementId", reimbursementId).getSingleResult();
			String hql2 = "Select rt FROM ReimbursementType rt WHERE rt.type = :type";
			ReimbursementType reimbursementType = (ReimbursementType) session.createQuery(hql2)
					.setParameter("type", reimbursementDto.getType()).getSingleResult();
			reimbursement.setType(reimbursementType);
			reimbursement.setAmount(reimbursementDto.getAmount());
			reimbursement.setDescription(reimbursementDto.getDescription());
			Reimbursement newReimbursement = (Reimbursement) session.merge(reimbursement);
			tx.commit();
			return newReimbursement;
		} catch (PersistenceException e) {
			tx.rollback();
			throw new PersistenceException(e.getMessage());
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public Reimbursement getReimbursementById(int reimbursementId) {
		SessionFactory sf = SessionFactorySingleton.getSessionFactory();
		Session session = sf.openSession();
		try {
			String hql = "Select r FROM Reimbursement r WHERE r.id = :reimbursementId";
			Reimbursement reimbursement = (Reimbursement) session.createQuery(hql).setParameter("reimbursementId", reimbursementId).getSingleResult();
			return reimbursement;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	@Override
	public Reimbursement addReimbursement(int userId, AddOrEditReimbursementDTO reimbursementDto) throws PersistenceException {
		SessionFactory sf = SessionFactorySingleton.getSessionFactory();
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		try {
			String hql1 = "Select rt FROM ReimbursementType rt WHERE rt.type = :type";
			ReimbursementType reimbursementType = (ReimbursementType) session.createQuery(hql1)
					.setParameter("type", reimbursementDto.getType()).getSingleResult();
			String hql2 = "Select rs FROM ReimbursementStatus rs WHERE rs.status = 'pending'";
			ReimbursementStatus reimbursementStatus = (ReimbursementStatus) session.createQuery(hql2).getSingleResult();
			String hql3 = "Select u FROM Users u WHERE u.id = :userid";
			Users user = (Users) session.createQuery(hql3).setParameter("userid", userId).getSingleResult();
			Reimbursement reimbursement = new Reimbursement(reimbursementDto.getAmount(), null, reimbursementDto.getDescription(), null);
			reimbursement.setAuthor(user);
			reimbursement.setType(reimbursementType);
			reimbursement.setStatus(reimbursementStatus);
			session.persist(reimbursement);
			tx.commit();
			return reimbursement;
		} catch (PersistenceException e) {
			tx.rollback();
			throw new PersistenceException(e.getMessage());
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}

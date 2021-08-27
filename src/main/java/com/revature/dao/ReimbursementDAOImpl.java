package com.revature.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

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
			String hql = "From Reimbursement r ORDER BY r.status.status desc, r.submitted desc";
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
		try {
			String hql = "SELECT r FROM Reimbursement r Join r.author u WHERE u.id = :userid ORDER BY r.status.status desc, r.submitted desc";
			reimbursement = session.createQuery(hql).setParameter("userid", userId).getResultList();
			return reimbursement;
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

	@Override
	public void deleteReimbursementById(int reimbursementId) throws Exception {
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
			throw new PersistenceException("something wrong with hibernate");
		}catch(Exception e) {
			throw new Exception("Fail delete reimbursemet Dao");
			
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public Reimbursement editReimbursementById(int reimbursementId, AddOrEditReimbursementDTO reimbursementDto)
			throws Exception {
		SessionFactory sf = SessionFactorySingleton.getSessionFactory();
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		try {
			String hql = "FROM Reimbursement r where r.id = :reimbursementId";
			Reimbursement reimbursement = (Reimbursement) session.createQuery(hql)
					.setParameter("reimbursementId", reimbursementId).getSingleResult();
			String hql2 = "FROM ReimbursementType rt WHERE rt.type = :type";
			ReimbursementType reimbursementType = (ReimbursementType) session.createQuery(hql2)
					.setParameter("type", reimbursementDto.getType()).getSingleResult();
			reimbursement.setType(reimbursementType);
			reimbursement.setAmount(reimbursementDto.getAmount());
			reimbursement.setDescription(reimbursementDto.getDescription());
			session.persist(reimbursement);
			tx.commit();
			return reimbursement;
		} catch (PersistenceException e) {
			tx.rollback();
			throw new PersistenceException("Something wrong with hibernate");
		}catch(Exception e) {
			tx.rollback();
			throw new Exception("Fail edit reimbursemet Dao");
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
			String hql = "FROM Reimbursement r WHERE r.id = :reimbursementId";
			Reimbursement reimbursement = (Reimbursement) session.createQuery(hql)
					.setParameter("reimbursementId", reimbursementId).getSingleResult();
			return reimbursement;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public Reimbursement addReimbursement(int userId, AddOrEditReimbursementDTO reimbursementDto)
			throws Exception {
		SessionFactory sf = SessionFactorySingleton.getSessionFactory();
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		try {
			String hql1 = "FROM ReimbursementType rt WHERE rt.type = :type";
			ReimbursementType reimbursementType = (ReimbursementType) session.createQuery(hql1)
					.setParameter("type", reimbursementDto.getType()).getSingleResult();
			String hql2 = "FROM ReimbursementStatus rs WHERE rs.status = 'pending'";
			ReimbursementStatus reimbursementStatus = (ReimbursementStatus) session.createQuery(hql2).getSingleResult();
			String hql3 = "FROM Users u WHERE u.id = :userid";
			Users user = (Users) session.createQuery(hql3).setParameter("userid", userId).getSingleResult();
			Reimbursement reimbursement = new Reimbursement(reimbursementDto.getAmount(), null,
					reimbursementDto.getDescription(), null);
			reimbursement.setAuthor(user);
			reimbursement.setType(reimbursementType);
			reimbursement.setStatus(reimbursementStatus);
			session.persist(reimbursement);
			tx.commit();
			return reimbursement;
		} catch (PersistenceException e) {
			tx.rollback();
			throw new PersistenceException("something wrong with hibernate");
		} catch (Exception e) {
			tx.rollback();
			throw new Exception("Fail edit in Dao");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public Reimbursement editReimbursementStatusById(int reimbId, int userId, Timestamp resolvedTime, String status) throws Exception {
		SessionFactory sf = SessionFactorySingleton.getSessionFactory();
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		try {
			String hql = "FROM ReimbursementStatus rs WHERE rs.status = :status";
			ReimbursementStatus reimbursementStatus = (ReimbursementStatus) session.createQuery(hql)
					.setParameter("status", status).getSingleResult();
			String hql2 = "FROM Reimbursement r WHERE r.id = :reimbursementId";
			Reimbursement reimbursement = (Reimbursement) session.createQuery(hql2)
					.setParameter("reimbursementId", reimbId).getSingleResult();
			System.out.println(reimbursement);
			String hql3 = "FROM Users u WHERE u.id = :userId";
			Users user = (Users) session.createQuery(hql3).setParameter("userId", userId).getSingleResult();
			reimbursement.setResolver(user);
			reimbursement.setResolved(resolvedTime);
			reimbursement.setStatus(reimbursementStatus);
			session.persist(reimbursement);
			tx.commit();
			return reimbursement;
		}catch(PersistenceException e) {
			tx.rollback();
			throw new PersistenceException("something wrong with hibernate");
		} catch (Exception e) {
			tx.rollback();
			throw new Exception("Fail edit in Dao");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public List<Reimbursement> filterReimbursementByStatus(String status) {
		List<Reimbursement> reimbursement = new ArrayList<>();
		SessionFactory sf = SessionFactorySingleton.getSessionFactory();
		Session session = sf.openSession();
		try {
			String hql = "FROM Reimbursement r WHERE r.status.status = :status";
			reimbursement = session.createQuery(hql).setParameter("status", status).getResultList();
			return reimbursement;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public Reimbursement addRecieptFile(int reimbId, byte[] storeImageByte) throws Exception {
		SessionFactory sf = SessionFactorySingleton.getSessionFactory();
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		try {
			String hql = "FROM Reimbursement r WHERE r.id = :id";
			Reimbursement reimbursement = (Reimbursement) session.createQuery(hql).setParameter("id", reimbId)
					.getSingleResult();
			reimbursement.setRecieptImage(storeImageByte);
			session.persist(reimbursement);
			tx.commit();
			return reimbursement;
		}catch(PersistenceException e) {
			tx.rollback();
			throw new PersistenceException("something wrong with hibernate");
		} catch (Exception e) {
			tx.rollback();
			throw new Exception("Add image fail in DAO");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}

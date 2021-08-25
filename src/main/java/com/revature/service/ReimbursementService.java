package com.revature.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import com.revature.dao.ReimbursementDAO;
import com.revature.dao.ReimbursementDAOImpl;
import com.revature.dto.AddOrEditReimbursementDTO;
import com.revature.exception.BadParameterException;
import com.revature.exception.DatabaseException;
import com.revature.exception.ReimbursementNotFoundException;
import com.revature.model.Reimbursement;
import com.revature.model.Users;

public class ReimbursementService {

	private ReimbursementDAO reimbursementDao;

	public ReimbursementService() {
		this.reimbursementDao = new ReimbursementDAOImpl();
	}

	public List<Reimbursement> getAllReimbursementsFromAllUsers() throws ReimbursementNotFoundException {
		List<Reimbursement> reimbursement = new ArrayList<>();
		reimbursement = reimbursementDao.getAllReimbursementsFromAllUsers();
		if (reimbursement == null) {
			throw new ReimbursementNotFoundException("No reimbursement in the database");
		}
		return reimbursement;
	}

	public List<Reimbursement> getReimbursementByUserId(String userId)
			throws ReimbursementNotFoundException, BadParameterException {
		List<Reimbursement> reimbursement = new ArrayList<>();
		try {
			int uid = Integer.parseInt(userId);
			reimbursement = reimbursementDao.getAllReimbursementByUserId(uid);
			if (reimbursement == null) {
				throw new ReimbursementNotFoundException("The user has not submit any reimbursement");
			}
			return reimbursement;
		} catch (NumberFormatException e) {
			throw new BadParameterException("User id is not a valid integer.");
		}
	}

	public void deleteReimbursementById(String reimbursementId)
			throws PersistenceException, BadParameterException, ReimbursementNotFoundException {
		try {
			int reimbId = Integer.parseInt(reimbursementId);
			if (reimbursementDao.getReimbursementById(reimbId) != null) {
				reimbursementDao.deleteReimbursementById(reimbId);
			} else {
				throw new ReimbursementNotFoundException("The user don't have a reimbursement with " + reimbursementId);
			}
		} catch (NumberFormatException e) {
			throw new BadParameterException("Reimbursement id is not a valid integer.");
		} catch (PersistenceException e) {
			throw new PersistenceException(e.getMessage());
		}
	}

	public Reimbursement editReimbursementById(String reimbursementId, AddOrEditReimbursementDTO reimbursementDto)
			throws BadParameterException, ReimbursementNotFoundException {
		try {
			if (reimbursementDto.getAmount() != 0 && reimbursementDto.getType() != null) {
			int reimbId = Integer.parseInt(reimbursementId);
			if (reimbursementDao.getReimbursementById(reimbId) != null) {
				Reimbursement reimbursement = reimbursementDao.editReimbursementById(reimbId, reimbursementDto);
				System.out.print(reimbursement);
				return reimbursement;
			} else {
				throw new ReimbursementNotFoundException("The user don't have a reimbursement with " + reimbursementId);
			}
			}else {
				throw new BadParameterException("cannot have null value for amount and type");
			}
		} catch (NumberFormatException e) {
			throw new BadParameterException("Reimbursement id is not a valid integer.");
		} catch (PersistenceException e) {
			throw new PersistenceException(e.getMessage());
		}
	}

	public Reimbursement addReimbursement(String userId, AddOrEditReimbursementDTO reimbursementDto)
			throws BadParameterException, ReimbursementNotFoundException {
		try {
			if (reimbursementDto.getAmount() != 0 && reimbursementDto.getType() != null) {
				int uId = Integer.parseInt(userId);
				Reimbursement reimbursement = reimbursementDao.addReimbursement(uId, reimbursementDto);
				return reimbursement;
			} else {
				throw new BadParameterException("cannot have null value for amount and type");
			}
		} catch (NumberFormatException e) {
			throw new BadParameterException("Reimbursement id is not a valid integer.");
		} catch (PersistenceException e) {
			throw new PersistenceException(e.getMessage());
		}
	}

	public Reimbursement editReimbursementStatusById(String reimbursementId, String userId, String status) throws ReimbursementNotFoundException, BadParameterException {
		try {
			String statusString = status.replaceAll("^\"|\"$", "");
			int reimbId = Integer.parseInt(reimbursementId);
			int uId = Integer.parseInt(userId);
			if (reimbursementDao.getReimbursementById(reimbId) != null) {
				long now = System.currentTimeMillis();
				Timestamp resolvedTime = new Timestamp(now);
				Reimbursement reimbursement = reimbursementDao.editReimbursementStatusById(reimbId, uId, resolvedTime, statusString);
				return reimbursement;
			} else {
				throw new ReimbursementNotFoundException("There is no reimbursement with " + reimbursementId);
			}
		} catch (NumberFormatException e) {
			throw new BadParameterException("Reimbursement id is not a valid integer.");
		} catch (PersistenceException e) {
			throw new PersistenceException(e.getMessage());
		}
	}
	}



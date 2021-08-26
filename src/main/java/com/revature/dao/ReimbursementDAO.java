package com.revature.dao;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.PersistenceException;

import com.revature.dto.AddOrEditReimbursementDTO;
import com.revature.model.Reimbursement;
import com.revature.model.Users;

public interface ReimbursementDAO {

	public abstract List<Reimbursement> getAllReimbursementsFromAllUsers();
	
	public abstract List<Reimbursement> getAllReimbursementByUserId(int userId);
	
	public abstract void deleteReimbursementById(int reimbursementId) throws PersistenceException;
	
	public abstract Reimbursement editReimbursementById(int reimbursementId, AddOrEditReimbursementDTO reimbursementDto) throws PersistenceException;
	
	public abstract Reimbursement getReimbursementById(int reimbursementId) throws PersistenceException;

	
	public abstract Reimbursement addReimbursement(int userId, AddOrEditReimbursementDTO reimbursementDto) throws PersistenceException;

	public abstract Reimbursement editReimbursementStatusById(int reimbId, int userId, Timestamp resolvedTime, String status);

	public abstract List<Reimbursement> filterReimbursementByStatus(String status);
}

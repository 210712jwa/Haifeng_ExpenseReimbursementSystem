package com.revature.util;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.Timestamp;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementStatus;
import com.revature.model.ReimbursementType;
import com.revature.model.UserRoles;
import com.revature.model.Users;

public class PopulateDataInDatabase {
	public static void main(String[] args) throws Exception {
		populateUserRoles();
		populateReimbursementStatus();
		populateReimbursementTypes();
		addSampleUsers();
		addSampleReimbursement();

	}

	private static void populateUserRoles() {
		SessionFactory sf = SessionFactorySingleton.getSessionFactory();
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();

		UserRoles financeManager = new UserRoles("finance manager");
		UserRoles employee = new UserRoles("employee");

		session.persist(financeManager);
		session.persist(employee);

		tx.commit();
		session.close();
	}

	private static void populateReimbursementStatus() {
		SessionFactory sf = SessionFactorySingleton.getSessionFactory();
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();

		ReimbursementStatus pending = new ReimbursementStatus("pending");
		ReimbursementStatus approved = new ReimbursementStatus("approved");
		ReimbursementStatus denied = new ReimbursementStatus("denied");

		session.persist(pending);
		session.persist(approved);
		session.persist(denied);

		tx.commit();
		session.close();
	}

	private static void populateReimbursementTypes() {
		SessionFactory sf = SessionFactorySingleton.getSessionFactory();
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();

		ReimbursementType lodging = new ReimbursementType("lodging");
		ReimbursementType travel = new ReimbursementType("travel");
		ReimbursementType food = new ReimbursementType("food");
		ReimbursementType other = new ReimbursementType("other");
		session.persist(lodging);
		session.persist(travel);
		session.persist(food);
		session.persist(other);

		tx.commit();
		session.close();
	}

	private static void addSampleUsers() throws NoSuchAlgorithmException, NoSuchProviderException {
		SessionFactory sf = SessionFactorySingleton.getSessionFactory();
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		String realAdminPassword = "somePassword";
		PasswordHashing passAuthen = PasswordHashing.getInstance();
		String adminPassword = passAuthen.getSecurePassword(realAdminPassword);
		Users financeManagerUser1 = new Users("Haifeng", "Zhu", "haifeng.zhu@revature.net", "someUsername",
				adminPassword);
		UserRoles finanaceManager = (UserRoles) session.createQuery("FROM UserRoles ur WHERE ur.id = 1")
				.getSingleResult();
		financeManagerUser1.setUserRole(finanaceManager);
		String realEmployee1Password = "12321";
		String employee1Password = passAuthen.getSecurePassword(realEmployee1Password);
		Users employee1 = new Users("Hello", "Hey", "test@test.com", "test123", employee1Password);
		UserRoles employee = (UserRoles) session.createQuery("FROM UserRoles ur WHERE ur.id = 2").getSingleResult();
		employee1.setUserRole(employee);
		String realEmployee2Password = "banna2";
		String employee2Password = passAuthen.getSecurePassword(realEmployee2Password);
		Users employee2 = new Users("Apple", "Banana", "apba@fruit.com", "apple1", employee2Password);
		employee2.setUserRole(employee);

		session.persist(financeManagerUser1);
		session.persist(employee1);
		session.persist(employee2);

		tx.commit();
		session.close();
	}

	private static void addSampleReimbursement() {
		SessionFactory sf = SessionFactorySingleton.getSessionFactory();
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		Users admin = (Users) session.createQuery("FROM Users u WHERE u.username = 'someUsername'").getSingleResult();
		Users employee1 = (Users) session.createQuery("FROM Users u WHERE u.username = 'test123'").getSingleResult();
		Users employee2 = (Users) session.createQuery("FROM Users u WHERE u.username = 'apple1'").getSingleResult();
		ReimbursementStatus reimbStatus1 = (ReimbursementStatus) session
				.createQuery("FROM ReimbursementStatus rs WHERE rs.status='pending'").getSingleResult();
		ReimbursementStatus reimbStatus2 = (ReimbursementStatus) session
				.createQuery("FROM ReimbursementStatus rs WHERE rs.status='approved'").getSingleResult();
		ReimbursementStatus reimbStatus3 = (ReimbursementStatus) session
				.createQuery("FROM ReimbursementStatus rs WHERE rs.status='denied'").getSingleResult();
		ReimbursementType reimbType1 = (ReimbursementType) session
				.createQuery("FROM ReimbursementType rt WHERE rt.type='travel'").getSingleResult();
		ReimbursementType reimbType2 = (ReimbursementType) session
				.createQuery("FROM ReimbursementType rt WHERE rt.type='food'").getSingleResult();
		Timestamp resolveTimestamp1 = Timestamp.valueOf("2020-10-22 19:30:23.168");
		Timestamp resolveTimestamp2 = Timestamp.valueOf("2020-11-22 16:32:26.238");

		Reimbursement reimbursement1 = new Reimbursement(2000.23, null, "test1 reimbursment", null);
		Reimbursement reimbursement2 = new Reimbursement(123.45, resolveTimestamp1, "test2 reimbursment", null);
		Reimbursement reimbursement3 = new Reimbursement(123.45, resolveTimestamp2, "apple reimbursment", null);

		reimbursement1.setAuthor(employee1);
		reimbursement1.setStatus(reimbStatus1);
		reimbursement1.setType(reimbType1);
		reimbursement2.setAuthor(employee1);
		reimbursement2.setResolver(admin);
		reimbursement2.setStatus(reimbStatus2);
		reimbursement2.setType(reimbType2);
		reimbursement3.setAuthor(employee2);
		reimbursement3.setResolver(admin);
		reimbursement3.setStatus(reimbStatus3);
		reimbursement3.setType(reimbType2);
		session.persist(reimbursement1);
		session.persist(reimbursement2);
		session.persist(reimbursement3);
		tx.commit();
		session.close();

	}

}

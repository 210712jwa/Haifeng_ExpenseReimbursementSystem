package com.revature.dao;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.revature.dto.AddUserDTO;
import com.revature.dto.LoginDTO;
import com.revature.model.UserRoles;
import com.revature.model.Users;
import com.revature.util.PasswordHashing;
import com.revature.util.SessionFactorySingleton;

public class UserDAOImpl implements UserDAO {
	
	
	@Override
	public Users getUserByUsernameAndPassword(LoginDTO loginDto) {
		SessionFactory sf = SessionFactorySingleton.getSessionFactory();
		Session session = sf.openSession();
		System.out.println(loginDto.getPassword());
		try {
			Users user = (Users) session
					.createQuery("FROM Users u WHERE u.username = :username AND u.password = :password")
					.setParameter("username", loginDto.getUsername())
					.setParameter("password", loginDto.getPassword())
					.getSingleResult();
			return user;
		} catch (NoResultException e) {
			return null;
		} finally {
			session.close();
		}

	}

	@Override
	public Users getUserById(int id) {
		SessionFactory sf = SessionFactorySingleton.getSessionFactory();
		Session session = sf.openSession();

		try {
			Users user = (Users) session
					.createQuery("FROM Users u WHERE u.id = :userid")
					.setParameter("userid", id)
					.getSingleResult();
			return user;
		} catch (NoResultException e) {
			return null;
		} finally {
			session.close();
		}
	}

	@Override
	public Users addRegularUser(AddUserDTO userDto) throws Exception {
		SessionFactory sf = SessionFactorySingleton.getSessionFactory();
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		try {
			Users user = new Users(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), userDto.getUsername(), userDto.getPassword());
			String hql = "FROM UserRoles ur WHERE ur.id = 2";
			UserRoles userRoles = (UserRoles) session.createQuery(hql).getSingleResult();
			user.setUserRole(userRoles);
			session.persist(user);
			return user;
		}catch(Exception e) {
			tx.rollback();
			throw new Exception("fail to add user");
		}finally{
			session.close();
		}
		
	}

}

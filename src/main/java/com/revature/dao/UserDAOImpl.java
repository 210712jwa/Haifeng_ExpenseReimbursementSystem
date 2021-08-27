package com.revature.dao;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.revature.dto.LoginDTO;
import com.revature.model.Users;
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
					.setParameter("username", loginDto.getUsername()).setParameter("password", loginDto.getPassword())
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
			Users user = (Users) session.createQuery("FROM Users u WHERE u.id = :userid").setParameter("userid", id)
					.getSingleResult();
			return user;
		} catch (NoResultException e) {
			return null;
		} finally {
			session.close();
		}
	}

}

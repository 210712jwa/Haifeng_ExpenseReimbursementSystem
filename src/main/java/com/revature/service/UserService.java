package com.revature.service;

import com.revature.dao.UserDAO;
import com.revature.dao.UserDAOImpl;
import com.revature.model.UserRoles;
import com.revature.model.Users;

public class UserService {
	
private UserDAO userDao;
	
	public UserService() {
		this.userDao = new UserDAOImpl();
	}

	public Users getUserById(String userId) {
		int uid = Integer.parseInt(userId);
		Users user = userDao.getUserById(uid);
		return user;
	}
	
	public UserRoles getUserRole() {
		return null;
	}

}

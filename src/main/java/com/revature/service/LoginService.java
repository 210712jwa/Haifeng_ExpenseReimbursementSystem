package com.revature.service;

import com.revature.dao.UserDAO;
import com.revature.dao.UserDAOImpl;
import com.revature.dto.LoginDTO;
import com.revature.exception.BadParameterException;
import com.revature.exception.InvalidLoginException;
import com.revature.model.Users;

public class LoginService {
	
	private UserDAO userDao;
	
	public LoginService() {
		this.userDao = new UserDAOImpl();
	}

	public Users login(LoginDTO loginDto) throws BadParameterException, InvalidLoginException {
		if(loginDto.getUsername().equals("") && loginDto.getPassword().equals("")) {
			throw new BadParameterException("Username and password cannot be blank.");
		}
		if(loginDto.getUsername().equals("")) {
			throw new BadParameterException("Username cannot be blank.");
		}
		if(loginDto.getPassword().equals("")) {
			throw new BadParameterException("Password cannot be blank.");
		}
		Users user = userDao.getUserByUsernameAndPassword(loginDto);
		if(user == null) {
			throw new InvalidLoginException("You provide incorrect credential when attempting to log in");
		}
		return user;
	}
}

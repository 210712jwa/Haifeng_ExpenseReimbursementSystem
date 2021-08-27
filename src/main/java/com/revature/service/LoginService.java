package com.revature.service;

import com.revature.dao.UserDAO;
import com.revature.dao.UserDAOImpl;
import com.revature.dto.LoginDTO;
import com.revature.exception.BadParameterException;
import com.revature.exception.InvalidLoginException;
import com.revature.model.Users;
import com.revature.util.PasswordHashing;

public class LoginService {

	private UserDAO userDao;

	public LoginService() {
		this.userDao = new UserDAOImpl();
	}

	public LoginService(UserDAO mockUserObject) {
		this.userDao = mockUserObject;

	}

	public Users login(LoginDTO loginDto) throws Exception {
		if (loginDto.getUsername().equals("") && loginDto.getPassword().equals("")) {
			throw new BadParameterException("Username and password cannot be blank.");
		}
		if (loginDto.getUsername().equals("")) {
			throw new BadParameterException("Username cannot be blank.");
		}
		if (loginDto.getPassword().equals("")) {
			throw new BadParameterException("Password cannot be blank.");
		}
		try {
			PasswordHashing passAuthen = PasswordHashing.getInstance();
			String newPassword = passAuthen.getSecurePassword(loginDto.getPassword());
			loginDto.setPassword(newPassword);
			Users user = userDao.getUserByUsernameAndPassword(loginDto);
			if (user == null) {
				throw new InvalidLoginException("You provide incorrect credential when attempting to log in");
			}
			return user;
		} catch (Exception e) {
			throw new Exception("Login fail");
		}
	}
}

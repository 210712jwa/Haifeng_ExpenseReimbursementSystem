package com.revature.dao;

import com.revature.dto.LoginDTO;
import com.revature.model.Users;

public interface UserDAO {

	public abstract Users getUserByUsernameAndPassword(LoginDTO loginDto);

	public abstract Users getUserById(int id);

}

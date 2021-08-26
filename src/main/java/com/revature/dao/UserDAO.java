package com.revature.dao;

import com.revature.dto.AddUserDTO;
import com.revature.dto.LoginDTO;
import com.revature.model.Users;

public interface UserDAO {

	public abstract Users getUserByUsernameAndPassword(LoginDTO loginDto);
	
	public abstract Users getUserById(int id);
	
	public abstract Users addRegularUser(AddUserDTO userDto) throws Exception;
}

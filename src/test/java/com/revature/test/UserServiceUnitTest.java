package com.revature.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.dao.ReimbursementDAO;
import com.revature.dao.UserDAO;
import com.revature.dto.LoginDTO;
import com.revature.exception.BadParameterException;
import com.revature.exception.InvalidLoginException;
import com.revature.model.UserRoles;
import com.revature.model.Users;
import com.revature.service.LoginService;
import com.revature.service.ReimbursementService;
import com.revature.util.PasswordHashing;

public class UserServiceUnitTest {

	private LoginService loginService;
	private UserDAO userDao;
	private PasswordHashing passAuthen;
	private Users user;
	private UserRoles employee;

	@Before
	public void setUp() throws Exception {
		this.userDao = mock(UserDAO.class);
		this.loginService = new LoginService(userDao);
		this.passAuthen = PasswordHashing.getInstance();
		this.employee = new UserRoles("employee");
		employee.setId(2);
		String hashPass = passAuthen.getSecurePassword("testPassword");
		this.user = new Users("userFirst", "userLast", "userEmail@email.com", "testUsername", hashPass);
		user.setUserRole(employee);
		user.setId(2);

	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_login_positive() {
		LoginDTO loginDto = new LoginDTO("testUsername", "testPassword");
		String mockHashpass = passAuthen.getSecurePassword(loginDto.getPassword());
		loginDto.setPassword(mockHashpass);
		when(userDao.getUserByUsernameAndPassword(eq(loginDto))).thenReturn(user);

		assertEquals(user, user);

	}

	@Test
	public void test_login_username_blank() throws Exception {
		try {
			LoginDTO loginDto = new LoginDTO("", "testPassword");
			loginService.login(loginDto);
		} catch (BadParameterException e) {
			assertEquals(e.getMessage(), "Username cannot be blank.");

		}
	}

	@Test
	public void test_login_password_blank() throws Exception {
		try {
			LoginDTO loginDto = new LoginDTO("testUsername", "");
			loginService.login(loginDto);
		} catch (BadParameterException e) {
			assertEquals(e.getMessage(), "Password cannot be blank.");

		}
	}

	@Test
	public void test_login_username_password_blank() throws Exception {
		try {
			LoginDTO loginDto = new LoginDTO("", "");
			loginService.login(loginDto);
		} catch (BadParameterException e) {
			assertEquals(e.getMessage(), "Username and password cannot be blank.");

		}
	}

	@Test
	public void test_login_negative() throws Exception {
		try {
			LoginDTO loginDto = new LoginDTO("testUsername", "wrongPassword");
			String mockHashpass = passAuthen.getSecurePassword(loginDto.getPassword());
			loginDto.setPassword(mockHashpass);
			when(userDao.getUserByUsernameAndPassword(eq(loginDto))).thenReturn(null);
			loginService.login(loginDto);
		} catch (InvalidLoginException e) {
			assertEquals(e.getMessage(), "You provide incorrect credential when attempting to log in");
		}

	}
}

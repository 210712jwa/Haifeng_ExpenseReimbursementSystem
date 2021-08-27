package com.revature.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.dao.ReimbursementDAO;
import com.revature.dao.UserDAO;
import com.revature.service.LoginService;
import com.revature.service.ReimbursementService;

public class UserServiceUnitTest {
	
	private LoginService LoginService;
	private UserDAO userDao;
	

	@Before
	public void setUp() throws Exception {
		this.userDao = mock(UserDAO.class);
		this.LoginService = new LoginService(userDao);
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
		fail("Not yet implemented");
	}

}

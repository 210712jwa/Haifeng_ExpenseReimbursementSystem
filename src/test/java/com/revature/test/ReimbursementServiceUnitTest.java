package com.revature.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.dao.ReimbursementDAO;
import com.revature.model.Reimbursement;
import com.revature.service.ReimbursementService;

public class ReimbursementServiceUnitTest {
	
	private ReimbursementService reimbursementService;
	private ReimbursementDAO reimbursementDao;
	

	@Before
	public void setUp() throws Exception {
		this.reimbursementDao = mock(ReimbursementDAO.class);
		this.reimbursementService = new ReimbursementService(reimbursementDao);
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
	public void test() {
		fail("Not yet implemented");
	}

}

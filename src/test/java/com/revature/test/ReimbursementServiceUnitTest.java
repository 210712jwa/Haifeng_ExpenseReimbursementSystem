package com.revature.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.revature.dao.ReimbursementDAO;
import com.revature.dto.AddOrEditReimbursementDTO;
import com.revature.exception.BadParameterException;
import com.revature.exception.ReimbursementNotFoundException;
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementStatus;
import com.revature.model.ReimbursementType;
import com.revature.model.UserRoles;
import com.revature.model.Users;
import com.revature.service.ReimbursementService;

public class ReimbursementServiceUnitTest {

	private ReimbursementService reimbursementService;
	private ReimbursementDAO reimbursementDao;
	private static List<Reimbursement> reimbursementArrayExpected;
	private static List<Reimbursement> reimbursementArrayMock;
	private static UserRoles employee;
	private static UserRoles financeManager;
	private static ReimbursementStatus pending;
	private static ReimbursementStatus approved;
	private static ReimbursementStatus denied;
	private static ReimbursementType lodging;
	private static ReimbursementType travel;
	private static ReimbursementType food;
	private static ReimbursementType other;
	private static Users user;
	private static Users user2;
	private static Users admin;

	Timestamp resolveTimestamp1;
	Timestamp resolveTimestamp2;

	@Before
	public void setUp() throws Exception {
		this.reimbursementDao = mock(ReimbursementDAO.class);
		this.reimbursementService = new ReimbursementService(reimbursementDao);
		employee = new UserRoles("employee");
		financeManager = new UserRoles("finance manager");
		pending = new ReimbursementStatus("pending");
		pending.setId(1);
		approved = new ReimbursementStatus("approved");
		approved.setId(2);
		denied = new ReimbursementStatus("denied");
		approved.setId(3);
		lodging = new ReimbursementType("lodging");
		lodging.setId(1);
		travel = new ReimbursementType("travel");
		travel.setId(2);
		food = new ReimbursementType("food");
		food.setId(3);
		other = new ReimbursementType("other");
		other.setId(4);
		resolveTimestamp1 = Timestamp.valueOf("2020-10-22 19:30:23.168");
		resolveTimestamp2 = Timestamp.valueOf("2020-11-22 16:32:26.238");
		user = new Users("userFirst", "userLast", "userEmail@email.com", "userUsername", "userPassword");
		user.setUserRole(employee);
		user.setId(2);
		user2 = new Users("userFirst2", "userLast2", "userEmail@email.com2", "userUsername2", "userPassword2");
		user2.setUserRole(employee);
		user.setId(3);
		admin = new Users("adminFirst", "adminLast", "adminEmail@email.com", "adminUsername", "adminPassword");
		admin.setUserRole(financeManager);
		user.setId(1);
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		reimbursementArrayExpected = new ArrayList<>();
		reimbursementArrayMock = new ArrayList<>();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		reimbursementArrayExpected.clear();
		reimbursementArrayMock.clear();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_get_all_reimbursement_all_user_positive() throws ReimbursementNotFoundException {
		Reimbursement reimbursement1 = new Reimbursement(1234.56, resolveTimestamp1, "tasty food", null);
		reimbursement1.setAuthor(user);
		reimbursement1.setResolver(admin);
		reimbursement1.setType(food);
		reimbursement1.setStatus(approved);
		Reimbursement reimbursement2 = new Reimbursement(345, resolveTimestamp2, "best travel", null);
		reimbursement2.setAuthor(user2);
		reimbursement2.setResolver(admin);
		reimbursement2.setType(travel);
		reimbursement2.setStatus(denied);
		Reimbursement reimbursement3 = new Reimbursement(225, resolveTimestamp2, "best lodging", null);
		reimbursement3.setAuthor(user);
		reimbursement3.setType(lodging);
		reimbursement3.setStatus(pending);

		reimbursementArrayMock.add(reimbursement1);
		reimbursementArrayMock.add(reimbursement2);
		reimbursementArrayMock.add(reimbursement3);

		when(reimbursementDao.getAllReimbursementsFromAllUsers()).thenReturn(reimbursementArrayMock);

		List<Reimbursement> actual = reimbursementService.getAllReimbursementsFromAllUsers();
		reimbursementArrayExpected.add(reimbursement1);
		reimbursementArrayExpected.add(reimbursement2);
		reimbursementArrayExpected.add(reimbursement3);

		assertEquals(actual, reimbursementArrayExpected);
	}

	@Test(expected = ReimbursementNotFoundException.class)
	public void test_get_all_reimbursement_all_user_not_found() throws ReimbursementNotFoundException {
		when(reimbursementDao.getAllReimbursementsFromAllUsers()).thenReturn(null);
		reimbursementService.getAllReimbursementsFromAllUsers();
	}

	@Test
	public void test_get_all_reimbursement_by_user_positive()
			throws ReimbursementNotFoundException, BadParameterException {
		Reimbursement reimbursement1 = new Reimbursement(1234.56, resolveTimestamp1, "tasty food", null);
		reimbursement1.setAuthor(user);
		reimbursement1.setResolver(admin);
		reimbursement1.setType(food);
		reimbursement1.setStatus(approved);
		Reimbursement reimbursement2 = new Reimbursement(345, resolveTimestamp2, "best travel", null);
		reimbursement2.setAuthor(user);
		reimbursement2.setResolver(admin);
		reimbursement2.setType(travel);
		reimbursement2.setStatus(denied);
		Reimbursement reimbursement3 = new Reimbursement(225, resolveTimestamp2, "best lodging", null);
		reimbursement3.setAuthor(user);
		reimbursement3.setType(lodging);
		reimbursement3.setStatus(pending);

		reimbursementArrayMock.add(reimbursement1);
		reimbursementArrayMock.add(reimbursement2);
		reimbursementArrayMock.add(reimbursement3);

		when(reimbursementDao.getAllReimbursementByUserId(eq(10))).thenReturn(reimbursementArrayMock);

		List<Reimbursement> actual = reimbursementService.getAllReimbursementByUserId("10");
		reimbursementArrayExpected.add(reimbursement1);
		reimbursementArrayExpected.add(reimbursement2);
		reimbursementArrayExpected.add(reimbursement3);

		assertEquals(actual, reimbursementArrayExpected);

	}

	@Test
	public void test_get_all_reimbursement_by_user_id_null_id() throws ReimbursementNotFoundException {
		try {
			reimbursementService.getAllReimbursementByUserId("");
			fail();
		} catch (BadParameterException e) {
			assertEquals("User id is not a valid integer.", e.getMessage());
		}
	}

	@Test
	public void test_get_all_reimbursement_by_user_id_string_id() throws ReimbursementNotFoundException {
		try {
			reimbursementService.getAllReimbursementByUserId("w23");
			fail();
		} catch (BadParameterException e) {
			assertEquals("User id is not a valid integer.", e.getMessage());
		}
	}

	@Test(expected = ReimbursementNotFoundException.class)
	public void test_get_all_reimbursement_by_user_id_no_result()
			throws ReimbursementNotFoundException, BadParameterException {
		when(reimbursementDao.getAllReimbursementByUserId(eq(10))).thenReturn(null);
		reimbursementService.getAllReimbursementByUserId("10");
	}

	@Test
	public void test_delete_reimbursement_by_id_positive() throws Exception {
		Reimbursement reimbursement1 = new Reimbursement(1234.56, resolveTimestamp1, "tasty food", null);
		reimbursement1.setAuthor(user);
		reimbursement1.setResolver(admin);
		reimbursement1.setType(food);
		reimbursement1.setStatus(approved);
		when(reimbursementDao.getReimbursementById(eq(10))).thenReturn(reimbursement1);
		Mockito.doNothing().when(reimbursementDao).deleteReimbursementById(10);
		reimbursementService.deleteReimbursementById("10");
		Mockito.verify(reimbursementDao).deleteReimbursementById(10);
	}

	@Test
	public void test_delete_reimbursement_by_id_string() throws Exception {
		try {
			reimbursementService.deleteReimbursementById("fesf");
			fail();
		} catch (BadParameterException e) {
			assertEquals("Reimbursement id is not a valid integer.", e.getMessage());
		}
	}

	@Test
	public void test_delete_reimbursement_by_id_null() throws Exception {
		try {
			reimbursementService.deleteReimbursementById("");
			fail();
		} catch (BadParameterException e) {
			assertEquals("Reimbursement id is not a valid integer.", e.getMessage());
		}
	}
	
	@Test
	public void test_delete_reimbursement_by_id_not_exist() throws Exception {
		try {
			when(reimbursementDao.getReimbursementById(eq(10))).thenReturn(null);
			reimbursementService.deleteReimbursementById("10");
			fail();
		} catch (ReimbursementNotFoundException e) {
			assertEquals("The user don't have a reimbursement with 10", e.getMessage());
		}
	}

	@Test(expected = PersistenceException.class)
	public void test_delete_reimbursement_by_id_persist_excepetion() throws Exception {
		Reimbursement reimbursement1 = new Reimbursement(1234.56, resolveTimestamp1, "tasty food", null);
		reimbursement1.setAuthor(user);
		reimbursement1.setResolver(admin);
		reimbursement1.setType(food);
		reimbursement1.setStatus(approved);
		when(reimbursementDao.getReimbursementById(eq(10))).thenReturn(reimbursement1);
		Mockito.doThrow(new PersistenceException()).when(reimbursementDao).deleteReimbursementById(anyInt());
		reimbursementService.deleteReimbursementById("10");
		Mockito.verify(reimbursementDao).deleteReimbursementById(10);
	}
	
	
	@Test
	public void test_edit_reimbursement_by_id_positive() throws Exception {
		AddOrEditReimbursementDTO reimbDto = new AddOrEditReimbursementDTO(100.23, "hello", null, "lodging");
		Reimbursement reimbursement1 = new Reimbursement(2000, null, "hey", null);
		reimbursement1.setId(10);
		reimbursement1.setAuthor(user);
		reimbursement1.setResolver(admin);
		reimbursement1.setType(travel);
		reimbursement1.setStatus(approved);
		when(reimbursementDao.getReimbursementById(eq(10))).thenReturn(reimbursement1);
		
		Reimbursement newReimb = new Reimbursement(reimbDto.getAmount(), null, reimbDto.getDescription(), null);
		newReimb.setId(10);
		newReimb.setAuthor(user);
		newReimb.setType(lodging);
		
		when(reimbursementDao.editReimbursementById(eq(10),eq(reimbDto))).thenReturn(newReimb);
		Reimbursement actual = reimbursementService.editReimbursementById("10", reimbDto);
		
		Reimbursement expectReimb = new Reimbursement(100.23, null, "hello", null);
		expectReimb.setId(10);
		expectReimb.setAuthor(user);
		expectReimb.setType(lodging);

		assertEquals(actual, expectReimb);
	}
	
	@Test
	public void test_edit_reimbursement_by_id_null() throws Exception {
		try {
			AddOrEditReimbursementDTO reimbDto = new AddOrEditReimbursementDTO(100.23, "hello", null, "lodging");
			reimbursementService.editReimbursementById("", reimbDto);
			fail();
		}catch(BadParameterException e) {
			assertEquals(e.getMessage(), "Reimbursement id is not a valid integer.");
		}
	}
	
	@Test
	public void test_edit_reimbursement_by_id_string() throws Exception {
		try {
			AddOrEditReimbursementDTO reimbDto = new AddOrEditReimbursementDTO(100.23, "hello", null, "lodging");
			reimbursementService.editReimbursementById("ww", reimbDto);
			fail();
		}catch(BadParameterException e) {
			assertEquals(e.getMessage(), "Reimbursement id is not a valid integer.");
		}
	}
	
	@Test
	public void test_edit_reimbursement_by_id_amount_null() throws Exception {
		try {
			AddOrEditReimbursementDTO reimbDto = new AddOrEditReimbursementDTO(0, "hello", null, "lodging");
			reimbursementService.editReimbursementById("10", reimbDto);
			fail();
		}catch(BadParameterException e) {
			assertEquals(e.getMessage(), "cannot have null value for amount and type");
		}
	}
	
	@Test
	public void test_edit_reimbursement_by_id_type_null() throws Exception {
		try {
			AddOrEditReimbursementDTO reimbDto = new AddOrEditReimbursementDTO(102, "hello", null, "");
			reimbursementService.editReimbursementById("10", reimbDto);
			fail();
		}catch(BadParameterException e) {
			assertEquals(e.getMessage(), "cannot have null value for amount and type");
		}
	}
	
	@Test
	public void test_edit_reimbursement_by_id_not_exist() throws Exception {
		try {
			AddOrEditReimbursementDTO reimbDto = new AddOrEditReimbursementDTO(102, "hello", null, "travel");
			reimbursementService.editReimbursementById("10", reimbDto);
			fail();
		}catch(ReimbursementNotFoundException e) {
			assertEquals(e.getMessage(), "The user don't have a reimbursement with 10");
		}
	}
	
	@Test
	public void test_add_reimbursement_positive() throws Exception {
		AddOrEditReimbursementDTO reimbDto = new AddOrEditReimbursementDTO(100.23, "hello", null, "lodging");
		Reimbursement reimbursementMock = new Reimbursement();
		reimbursementMock.setAmount(reimbDto.getAmount());
		reimbursementMock.setDescription(reimbDto.getDescription());
		reimbursementMock.setType(lodging);
		when(reimbursementDao.addReimbursement(eq(10), reimbDto)).thenReturn(reimbursementMock);
		
		Reimbursement actual = reimbursementService.addReimbursement("10", reimbDto);
		Reimbursement expected = new Reimbursement();
		expected.setAmount(100.23);
		expected.setDescription("hello");
		expected.setType(lodging);
		
		assertEquals(actual, expected);
		
	}
	
	@Test
	public void test_add_reimbursement_type_null() throws Exception {
		try {
			AddOrEditReimbursementDTO reimbDto = new AddOrEditReimbursementDTO(102, "hello", null, "");
			reimbursementService.addReimbursement("10", reimbDto);
			fail();
		}catch(BadParameterException e) {
			assertEquals(e.getMessage(), "cannot have null value for amount and type");
		}
	}
	
	@Test
	public void test_add_reimbursement_id_amount_null() throws Exception {
		try {
			AddOrEditReimbursementDTO reimbDto = new AddOrEditReimbursementDTO(0, "hello", null, "travel");
			reimbursementService.addReimbursement("10", reimbDto);
			fail();
		}catch(BadParameterException e) {
			assertEquals(e.getMessage(), "cannot have null value for amount and type");
		}
	}
	
	@Test
	public void test_add_reimbursement_id_null() throws Exception {
		try {
			AddOrEditReimbursementDTO reimbDto = new AddOrEditReimbursementDTO(100, "hello", null, "travel");
			reimbursementService.addReimbursement("", reimbDto);
			fail();
		}catch(BadParameterException e) {
			assertEquals(e.getMessage(), "Reimbursement id is not a valid integer.");
		}
	}
	
	@Test
	public void test_add_reimbursement_id_string() throws Exception {
		try {
			AddOrEditReimbursementDTO reimbDto = new AddOrEditReimbursementDTO(100, "hello", null, "travel");
			reimbursementService.addReimbursement("waw", reimbDto);
			fail();
		}catch(BadParameterException e) {
			assertEquals(e.getMessage(), "Reimbursement id is not a valid integer.");
		}
	}
	
	@Test
	public void test_edit_reimbursement_status_by_id_positive() throws Exception {
			Reimbursement reimbursementMock = new Reimbursement(100, resolveTimestamp1 ,"hello", null);
			reimbursementMock.setId(10);
			reimbursementMock.setAuthor(user);
			reimbursementMock.setResolver(admin);
			reimbursementMock.setStatus(approved);
			
			when(reimbursementDao.getReimbursementById(eq(10))).thenReturn(reimbursementMock);
			when(reimbursementDao.editReimbursementStatusById(eq(10), eq(1), resolveTimestamp1, eq("approved"))).thenReturn(reimbursementMock);
		
			Reimbursement actual = reimbursementService.editReimbursementStatusById("10", "1", "approved");
			Reimbursement reimbursementExpected = new Reimbursement(100, resolveTimestamp1 ,"hello", null);
			reimbursementExpected.setId(10);
			reimbursementExpected.setAuthor(user);
			reimbursementExpected.setResolver(admin);
			reimbursementExpected.setStatus(approved);
			
			assertEquals(actual, reimbursementExpected);
	}
	
	@Test
	public void test_edit_reimbursement_status_id_string() throws Exception {
		try {
		
			reimbursementService.editReimbursementStatusById("dw", "1", "approved");
			fail();
		}catch(BadParameterException e) {
			assertEquals(e.getMessage(), "Reimbursement id or user id is not a valid integer.");
		}
	}
	
	@Test
	public void test_edit_reimbursement_status_id_null() throws Exception {
		try {
		
			reimbursementService.editReimbursementStatusById("", "1", "approved");
			fail();
		}catch(BadParameterException e) {
			assertEquals(e.getMessage(), "Reimbursement id or user id is not a valid integer.");
		}
	}
	
	@Test
	public void test_edit_reimbursement_status_user_id_null() throws Exception {
		try {
		
			reimbursementService.editReimbursementStatusById("10", "", "approved");
			fail();
		}catch(BadParameterException e) {
			assertEquals(e.getMessage(), "Reimbursement id or user id is not a valid integer.");
		}
	}
	
	@Test
	public void test_edit_reimbursement_status_user_id_string() throws Exception {
		try {
		
			reimbursementService.editReimbursementStatusById("10", "doa", "approved");
			fail();
		}catch(BadParameterException e) {
			assertEquals(e.getMessage(), "Reimbursement id or user id is not a valid integer.");
		}
	}
	
	@Test
	public void test_edit_reimbursement_not_exist() throws Exception {
		try {
			reimbursementService.editReimbursementStatusById("10", "2", "approved");
			fail();
		}catch(ReimbursementNotFoundException e) {
			assertEquals(e.getMessage(), "There is no reimbursement with id 10");
		}
	}
	
	@Test
	public void test_filter_reimbursement() throws ReimbursementNotFoundException {
		Reimbursement reimbursement1 = new Reimbursement(1234.56, resolveTimestamp1, "tasty food", null);
		reimbursement1.setAuthor(user);
		reimbursement1.setResolver(admin);
		reimbursement1.setType(food);
		reimbursement1.setStatus(denied);
		Reimbursement reimbursement2 = new Reimbursement(345, resolveTimestamp2, "best travel", null);
		reimbursement2.setAuthor(user2);
		reimbursement2.setResolver(admin);
		reimbursement2.setType(travel);
		reimbursement2.setStatus(denied);
		Reimbursement reimbursement3 = new Reimbursement(225, resolveTimestamp2, "best lodging", null);
		reimbursement3.setAuthor(user);
		reimbursement3.setType(lodging);
		reimbursement3.setStatus(denied);

		reimbursementArrayMock.add(reimbursement1);
		reimbursementArrayMock.add(reimbursement2);
		reimbursementArrayMock.add(reimbursement3);
		
		when(reimbursementDao.filterReimbursementByStatus(eq("denied"))).thenReturn(reimbursementArrayMock);

		List<Reimbursement> actual = reimbursementService.filterReimbursementByStatus("denied");
		reimbursementArrayExpected.add(reimbursement1);
		reimbursementArrayExpected.add(reimbursement2);
		reimbursementArrayExpected.add(reimbursement3);

		assertEquals(actual, reimbursementArrayExpected);
		
	}
	
	@Test
	public void test_filter_reimbursement_not_exist() {
		
		try {
			when(reimbursementDao.filterReimbursementByStatus(eq("denied"))).thenReturn(null);
			List<Reimbursement> actual = reimbursementService.filterReimbursementByStatus("denied");
		}catch(ReimbursementNotFoundException e) {
			assertEquals("No such reimbursement in the database", e.getMessage());
		}
		
	}
	
	

	

}

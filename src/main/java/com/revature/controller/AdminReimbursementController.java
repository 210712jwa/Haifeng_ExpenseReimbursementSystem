package com.revature.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import com.revature.service.ReimbursementService;
import com.revature.app.Application;
import com.revature.dto.MessageDTO;
import com.revature.model.Reimbursement;
import com.revature.model.Users;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class AdminReimbursementController implements Controller {

	private ReimbursementService reimbursementService;

	public AdminReimbursementController() {
		this.reimbursementService = new ReimbursementService();
	}

	private Handler getAllReimbursementFromAllUserHandler = (ctx) -> {
		HttpSession httpSession = ctx.req.getSession();
		Users user = (Users) httpSession.getAttribute("currentUser");
		if (user != null && user.getUserRole().getRole().equals("finance manager")) {
			if (ctx.queryParam("status") != null) {
				String status = ctx.queryParam("status");
				List<Reimbursement> reimbursement = reimbursementService.filterReimbursementByStatus(status);
				ctx.json(reimbursement);
				ctx.status(200);
			} else if (ctx.queryParam("status") == null) {
				List<Reimbursement> reimbursement = new ArrayList<>();
				reimbursement = reimbursementService.getAllReimbursementsFromAllUsers();
				ctx.json(reimbursement);
				ctx.status(200);
			} 
		} else if (user != null) {
			ctx.json(new MessageDTO("Unauthorized action, not a manager"));
			ctx.status(403);
		} else if (user == null) {
			ctx.json(new MessageDTO("Unauthorized action, please login"));
			ctx.status(401);
		}
	};

	private Handler decideUserReimbursemenStatus = (ctx) -> {
		HttpSession httpSession = ctx.req.getSession();
		Users user = (Users) httpSession.getAttribute("currentUser");
		if (user != null && user.getUserRole().getRole().equals("finance manager")) {
			String status = ctx.body();
			String reimbursementId = ctx.pathParam("reimbursementid");
			String userId = ctx.pathParam("userid");
			Reimbursement reimbursement = reimbursementService.editReimbursementStatusById(reimbursementId, userId,
					status);
			ctx.json(reimbursement);
			ctx.status(200);
		} else if (user == null) {
			ctx.json(new MessageDTO("Unauthorized action, please login"));
			ctx.status(401);
		}
	};

	private Handler filterReimbursementByStatus = (ctx) -> {
		HttpSession httpSession = ctx.req.getSession();
		Users user = (Users) httpSession.getAttribute("currentUser");
		if (user != null && user.getUserRole().getRole().equals("finance manager")) {
			String status = ctx.body();
			List<Reimbursement> reimbursement = reimbursementService.filterReimbursementByStatus(status);
			ctx.json(reimbursement);
			ctx.status(200);
		} else if (user == null) {
			ctx.json(new MessageDTO("Unauthorized action, please login"));
			ctx.status(401);
		}
	};

	@Override
	public void mapEndpoints(Javalin app) {
		app.get("/admin/:userid/reimbursement", getAllReimbursementFromAllUserHandler);
		app.put("/admin/:userid/reimbursement/:reimbursementid", decideUserReimbursemenStatus);
		app.get("/admin/:userid/reimbursement/status", filterReimbursementByStatus);
	}

}

package com.revature.controller;

import io.javalin.Javalin;
import io.javalin.http.Handler;
import io.javalin.http.UploadedFile;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.revature.model.Users;
import com.revature.service.ReimbursementService;
import com.revature.dto.AddOrEditReimbursementDTO;
import com.revature.dto.MessageDTO;
import com.revature.model.Reimbursement;

public class ReimbursementController implements Controller {

	private ReimbursementService reimbursementService;

	public ReimbursementController() {
		this.reimbursementService = new ReimbursementService();
	}

	private Handler getReimbursementByIdHandler = (ctx) -> {

		HttpSession httpSession = ctx.req.getSession();
		Users user = (Users) httpSession.getAttribute("currentUser");
		String userId = ctx.pathParam("userid");
		if (user != null) {
			String currentUserId = Integer.toString(user.getId());
			if (userId.equals(currentUserId) && user.getUserRole().getRole().equals("employee")) {
				List<Reimbursement> reimbursement = new ArrayList<>();
				reimbursement = reimbursementService.getReimbursementByUserId(userId);
				ctx.json(reimbursement);
				ctx.status(200);
			} else if (user != null && userId.equals(currentUserId)
					&& user.getUserRole().getRole().equals("finance manager")) {
				ctx.json(new MessageDTO("Nonauthorized action"));
				ctx.status(401);
			}
		} else if (user == null) {
			ctx.status(401);
			ctx.json(new MessageDTO("Please login"));
		}
	};

	private Handler deleteReimbursementByIdHandler = (ctx) -> {
		HttpSession httpSession = ctx.req.getSession();
		Users user = (Users) httpSession.getAttribute("currentUser");
		String userId = ctx.pathParam("userid");
		String reimbursementId = ctx.pathParam("reimbursementid");
		if (user != null) {
			String currentUserId = Integer.toString(user.getId());
			if (userId.equals(currentUserId) && user.getUserRole().getRole().equals("employee")) {
				reimbursementService.deleteReimbursementById(reimbursementId);
				ctx.status(200);
			} else {
				ctx.json(new MessageDTO("Nonauthorized action"));
				ctx.status(401);
			}
		} else {
			ctx.json(new MessageDTO("Please login"));
			ctx.status(401);
		}
	};

	private Handler editReimbursementByIdHandler = (ctx) -> {
		HttpSession httpSession = ctx.req.getSession();
		Users user = (Users) httpSession.getAttribute("currentUser");
		String userId = ctx.pathParam("userid");
		String reimbursementId = ctx.pathParam("reimbursementid");
		AddOrEditReimbursementDTO reimbursementDto = ctx.bodyAsClass(AddOrEditReimbursementDTO.class);
		if (user != null) {
			String currentUserId = Integer.toString(user.getId());
			if (userId.equals(currentUserId) && user.getUserRole().getRole().equals("employee")) {
				Reimbursement reimbursement = reimbursementService.editReimbursementById(reimbursementId,
						reimbursementDto);
				ctx.json(reimbursement);
				ctx.status(200);
			} else {
				ctx.json(new MessageDTO("Nonauthorized action"));
				ctx.status(401);
			}
		} else {
			ctx.json(new MessageDTO("Please login"));
			ctx.status(401);
		}
	};

	private Handler addReimbursementHandler = (ctx) -> {
		HttpSession httpSession = ctx.req.getSession();
		Users user = (Users) httpSession.getAttribute("currentUser");
		String userId = ctx.pathParam("userid");
		AddOrEditReimbursementDTO reimbursementDto = ctx.bodyAsClass(AddOrEditReimbursementDTO.class);
		if (user != null) {
			String currentUserId = Integer.toString(user.getId());
			if (userId.equals(currentUserId) && user.getUserRole().getRole().equals("employee")) {
				Reimbursement reimbursement = reimbursementService.addReimbursement(userId, reimbursementDto);
				ctx.json(reimbursement);
				ctx.status(200);
			} else {
				ctx.json(new MessageDTO("Nonauthorized action"));
				ctx.status(401);
			}
		} else {
			ctx.json(new MessageDTO("Please login"));
			ctx.status(401);
		}
	};

	private Handler addRecieptFileHandler = (ctx) -> {
		HttpSession httpSession = ctx.req.getSession();
		Users user = (Users) httpSession.getAttribute("currentUser");
		String userId = ctx.pathParam("userid");
		String reimbursementId = ctx.pathParam("reimbursementid");
		if (user != null) {
			String currentUserId = Integer.toString(user.getId());
			if (userId.equals(currentUserId) && user.getUserRole().getRole().equals("employee")) {
				UploadedFile fileInput = ctx.uploadedFile("reciept");
				if (fileInput.component1() != null) {
					Reimbursement reimbursement = reimbursementService.addRecieptFile(reimbursementId, fileInput);
					ctx.json(reimbursement);
					ctx.status(200);
				} else {
					ctx.status(202);
				}
			} else {
				ctx.status(401);
			}
		}
	};

	@Override
	public void mapEndpoints(Javalin app) {
		app.get("/user/:userid/reimbursement", getReimbursementByIdHandler);
		app.delete("/user/:userid/reimbursement/:reimbursementid", deleteReimbursementByIdHandler);
		app.put("/user/:userid/reimbursement/:reimbursementid", editReimbursementByIdHandler);
		app.post("/user/:userid/reimbursement", addReimbursementHandler);
		app.put("/user/:userid/reimbursement/:reimbursementid/reciept", addRecieptFileHandler);
	}

}

package com.revature.controller;

import javax.servlet.http.HttpSession;

import com.revature.dto.LoginDTO;
import com.revature.dto.MessageDTO;
import com.revature.model.Users;
import com.revature.service.LoginService;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class LoginController implements Controller {

	private LoginService loginService;
	
	public LoginController() {
		this.loginService = new LoginService();
	}
	
	private Handler loginHandler = (ctx) -> {
		LoginDTO loginDto = ctx.bodyAsClass(LoginDTO.class);
		
		Users user = loginService.login(loginDto);
		
		HttpSession httpSession = ctx.req.getSession();
		httpSession.setAttribute("currentUser", user);
		ctx.json(user);
		ctx.status(200);
	};
	
	private Handler currentUserHandler = (ctx) -> {
		HttpSession httpSession = ctx.req.getSession();
		if(httpSession.getAttribute("currentUser") == null) {
			ctx.json(new MessageDTO("User is currently not logged in"));
			ctx.status(401);
		}else {
			Users user = (Users) httpSession.getAttribute("currentUser");
			ctx.json(user);
			ctx.status(200);
		}
	};
	
	private Handler checkUserRoleHandler = (ctx) -> {
		HttpSession httpSession = ctx.req.getSession();
		if(httpSession.getAttribute("currentUser") == null) {
			ctx.json(new MessageDTO("User is currently not logged in"));
			ctx.status(401);
		}else {
			String userId = ctx.pathParam("userid");
			Users user = (Users) httpSession.getAttribute("currentUser");
			String currentUserId = Integer.toString(user.getId());
			if(userId.equals(currentUserId)) {
				ctx.json(user.getUserRole().getRole());
				ctx.status(200);
			}
		}
	};
	
	private Handler logoutHandler = (ctx) -> {
		HttpSession httpSession = ctx.req.getSession();
		if(httpSession.getAttribute("currentUser") == null) {
			ctx.json(new MessageDTO("User is currently not logged in"));
			ctx.status(400);
		}else {
			httpSession.removeAttribute("currentUser");
			httpSession.invalidate(); 
			ctx.status(200);
		}
	};
	
	
	@Override
	public void mapEndpoints(Javalin app) {
		app.post("/login", loginHandler);
		app.get("currentUser", currentUserHandler);
		app.post("/logout", logoutHandler);
		app.get("/login/:userid", checkUserRoleHandler);
	}

}

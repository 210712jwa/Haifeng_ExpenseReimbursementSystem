package com.revature.app;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.controller.AdminReimbursementController;
import com.revature.controller.Controller;
import com.revature.controller.ExceptionController;
import com.revature.controller.LoginController;
import com.revature.controller.ReimbursementController;

import io.javalin.Javalin;;

public class Application {
	private static Javalin app;
	private static Logger logger = LoggerFactory.getLogger(Application.class);
			
	public static void main(String[] args) {
		app = Javalin.create((config) ->{
			config.enableCorsForAllOrigins();
		});
		
		mapControllers(new LoginController(), new ReimbursementController(), new AdminReimbursementController(), new ExceptionController());
		
		app.before((ctx) -> {
			logger.info(ctx.method() + " request received to the " + ctx.path() + " endpoint");
		});
		
		app.start(7000);
	}
	
	public static void mapControllers(Controller ...controllers) {
		for (Controller c : controllers) {
			c.mapEndpoints(Application.app);
		}
	}

}

package com.revature.controller;

import javax.persistence.PersistenceException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.dto.MessageDTO;
import com.revature.exception.BadParameterException;
import com.revature.exception.DatabaseException;
import com.revature.exception.InvalidLoginException;

import io.javalin.Javalin;
import io.javalin.http.ExceptionHandler;

public class ExceptionController implements Controller {

	private Logger logger = LoggerFactory.getLogger(ExceptionController.class);

	private ExceptionHandler<BadParameterException> badParameterExceptionHandler = (e, ctx) -> {
		logger.info("Exception Occurred: Exception message is " + e.getMessage());

		ctx.status(400);
		ctx.json(new MessageDTO(e.getMessage()));
	};

	private ExceptionHandler<InvalidLoginException> invalidLoginExceptionHandler = (e, ctx) -> {
		logger.info("Exception Occurred: Exception message is " + e.getMessage());

		ctx.status(401);
		ctx.json(new MessageDTO(e.getMessage()));
	};


	private ExceptionHandler<PersistenceException> persistenceExceptionHandler = (e, ctx) ->{
		logger.info("Exception Occurred: Exception message is " + e.getMessage());

		ctx.status(500);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<IOException> IOExceptionHandler = (e, ctx) ->{
		logger.info("Exception Occurred: Exception message is " + e.getMessage());

		ctx.status(400);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<Exception> ExceptionHandler = (e, ctx) -> {
		logger.info("Exception Occurred: Exception message is " + e.getMessage());
		ctx.status(500);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	

	@Override
	public void mapEndpoints(Javalin app) {
		app.exception(BadParameterException.class, badParameterExceptionHandler);
		app.exception(InvalidLoginException.class, invalidLoginExceptionHandler);
		app.exception(IOException.class, IOExceptionHandler);
		app.exception(PersistenceException.class, persistenceExceptionHandler);
		app.exception(Exception.class, ExceptionHandler);
	}

}

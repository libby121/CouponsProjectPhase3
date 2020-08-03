package com.example.demo.web.webConfig;

import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.example.demo.facades.AdminFacade;
import com.example.demo.facades.CompanyFacade;
import com.example.demo.facades.CustomerFacade;
import com.example.demo.web.SessionInfo;

/**
 * AOP class. 
 * Since the same token-validation method should run before each method in each controller ,
 * instead of writing it as part of the controllers methods, it is defined separately, once for every controller
 * (every controller requires it's own separate aspect since a different facade-type will be consequently returned by the methods).
 * 
 * Before every controller method, i.e before the server returns a response to client, it will be checked:
 * 1. whether the session is not null( the sessions map contains the given token,
 *    i.e the user is indeed logged in at the moment).
 * 2. whether the given token and session belong to the right client type (administrator/company/customer).
 * 3. 30 minutes have not been passed since last access.
 * 
 * If all the above conditions are true than the controller method will run automatically. (written below by
 * the proceed() method, and the returned value will be the controller returned value,
 * (as written below- return obj).
 * Otherwise exceptions will be thrown from the aspect accordingly. I chose to throw only the 
 * 401 unauthorized exception here for making it easier in the client side reaction. 
 * 
 * The aspect timing that was chosen is @around as a kind of a wrapper to the
 * controller logic, since an exception might or might not be thrown instead of the returned value.
 * 
 * Aspect class is annotated with @component for spring first scanning. And with @Aspect for 
 * reflection to follow the code instructions.
 *  
 * @author ליבי
 *
 */
@Component
@Aspect
public class WebAspect {

	@Autowired
	Map<String, SessionInfo> sessions;

	
	@Around("execution(* com.example.demo.web.AdminController.*(..) )")
 
	public Object AdminAccessValidation(ProceedingJoinPoint PjPoint) throws Throwable {
		Object token=PjPoint.getArgs()[0];
		SessionInfo session = sessions.get(token);
		
  		if (session != null) {
 			if(!(session.getFacade() instanceof AdminFacade)) 
				 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("wrong client request");

 			if (System.currentTimeMillis() - session.getLastAccessed() > 1000 * 60 * 30) {
				sessions.remove(token);
				 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("no such token, unauthorized access");

			}
			session.setLastAccessed(System.currentTimeMillis());

			Object obj=PjPoint.proceed();

			return obj;
		
		}
 
		else 
 			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login is required");
		
		

	};
 
	@Around("execution(* com.example.demo.web.CompanyController..*(..) )")
	public Object CompanyAccessValidation(ProceedingJoinPoint PjPoint) throws Throwable {
	Object token=PjPoint.getArgs()[0];
	 
		
		
 		SessionInfo session = sessions.get(token);
		 
 		if (session != null) {
 
			if(!(session.getFacade() instanceof CompanyFacade)) 
				 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("wrong client request");

 			if (System.currentTimeMillis() - session.getLastAccessed() > 1000 * 60 * 30) {
				sessions.remove(token);
				 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("no such token, unauthorized access");

			}
			session.setLastAccessed(System.currentTimeMillis());

			Object obj=PjPoint.proceed();

			return obj;
		
		}
 
		else {
 
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login is  required");
		
		}

	};
	
	
	
	@Around("execution(* com.example.demo.web.CustomerController.*(..) )")

	public Object CustomerAccessValidation(ProceedingJoinPoint PjPoint) throws Throwable {
		Object token=PjPoint.getArgs()[0];

		SessionInfo session = sessions.get(token);
 		if (session != null) {
			if(!(session.getFacade() instanceof CustomerFacade)) 
				 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("wrong client request");

 			if (System.currentTimeMillis() - session.getLastAccessed() > 1000 * 60 * 30) {
				sessions.remove(token);
				 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("no such token, unauthorized access");

			}
			session.setLastAccessed(System.currentTimeMillis());

			Object obj=PjPoint.proceed();

			return obj;
		
		}
 
		else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login is required");

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}



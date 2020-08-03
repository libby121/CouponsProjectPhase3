package com.example.demo.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.demo.exceptions.loginException;
import com.example.demo.facades.AdminFacade;
import com.example.demo.facades.CompanyFacade;
import com.example.demo.facades.CustomerFacade;
import com.example.demo.facades.Facade;
/**
 * LoginManager is a singleton class used for managing the login process. For each ClientType case the required client facade is
 * instantiated by spring context according to the user and returned if the login succeeds. 
 * Marked as component for spring scanning. 
 * @author ליבי
 *
 */
@Service
public class LoginManager {

 
	@Autowired
	private ConfigurableApplicationContext ctx;

	public Facade login(String email, String password, ClientType type) throws loginException {//better writing?
		switch (type) {
		case Administrator:
			AdminFacade adminFacade =ctx.getBean(AdminFacade.class);
			if (adminFacade.login(email, password))
				return adminFacade;
			
			break;
		case Company:
			CompanyFacade companyFacade=ctx.getBean(CompanyFacade.class);
			if (companyFacade.login(email, password))
				return companyFacade;
		 break;
		case Customer:
			CustomerFacade customerFacade=ctx.getBean(CustomerFacade.class);
			if (customerFacade.login(email, password))
				return customerFacade;
			 break;
		default:
			throw new loginException();

		}
		throw new loginException();
	}

}

package com.example.demo.web;

 import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
 import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exceptions.loginException;
import com.example.demo.facades.Facade;
import com.example.demo.login.ClientType;
import com.example.demo.login.LoginManager;

/**
 * A successful login process provides the user with a unique generated token
 * which is stored as a new key in the sessions hashMap, accompanied with the
 * user facade value. The new token is returned to client after a successful login, and from now on with
 * every new request the user's token will be sent to server for approval
 * before providing a response.
 * 
 * The login method is annotated with @PostMapping (and not @GetMapping) for
 * security matters, this will confirm that the user's information is not
 * visible in the URL parameters.
 * 
 * @author ליבי
 *
 */
@RestController
@RequestMapping("/client")
@CrossOrigin(origins = "http://localhost:4200")
public  class LoginController { 
	@Autowired
	private LoginManager loginManager;
	 

	@Autowired
	private Map<String, SessionInfo> sessions;

	@PostMapping("/login/{email}/{password}/{type}")
	public ResponseEntity<?> login(@PathVariable String email, @PathVariable String password,
			@PathVariable ClientType type) {
	 
		try {
			Facade facade = loginManager.login(email, password, type);
			String token = UUID.randomUUID().toString();
		
			 
 			sessions.put(token,new SessionInfo(System.currentTimeMillis(), facade)); 
  
			return ResponseEntity.ok(token);
			
		} catch (loginException e) {
 
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());

		}
	}
	
	@PostMapping("/logout/{token}")
	public ResponseEntity<?>logOut(@PathVariable String token){
		sessions.remove(token);
		return ResponseEntity.ok("logOut process succeeded");
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

package com.example.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.demo.facades.Facade;
/**
 * SessionInfo is a wrapping class that is made up of details about the session(last accessed) plus the facade itself.
 * @author ליבי
 *
 */
public class SessionInfo {

	
 	private long lastAccessed;
	private Facade facade;
	public SessionInfo(long lastAccessed, Facade facade) {
		super();
		this.lastAccessed = lastAccessed;
		this.facade = facade;
	}
	public SessionInfo() {
		 
	}
	
	public long getLastAccessed() {
		return lastAccessed;
	}
	public void setLastAccessed(long lastAccessed) {
		this.lastAccessed = lastAccessed;
	}
	public Facade getFacade() {
		return facade;
	}
	@Override
	public String toString() {
		return "SessionInfo [lastAccessed=" + lastAccessed + ", facade=" + facade + "]";
	}
	

}

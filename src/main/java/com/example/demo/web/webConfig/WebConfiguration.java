package com.example.demo.web.webConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import com.example.demo.beans.Company;
import com.example.demo.exceptions.CompanyDoesNotExistException;
import com.example.demo.exceptions.unmodifiedCompanyNameException;
import com.example.demo.facades.AdminFacade;
import com.example.demo.facades.Facade;
import com.example.demo.web.SessionInfo;


/**
 * @Configuration annotates a configuration class.
 * @Bean-marks a method (that returns an object) to be instantiated and injected
 *             into the calling class on the first run. It's default scope is Singleton. 
 * 
 * WebConfiguration class consists of:
 *  
 * 
 *             Sessions hashMap- required for managing active users sessions,
 *             and tracking user's state while communicating with the server- on
 *             each request the same sessions hashMap will be returned, since
 *             the default scope value is singleton and this is a shared resource.
 * 
 *             SessionInfo is a wrapping class that is made up of details about
 *             the session.
 * 
 *             The sessions map will be called on every controller.
 * @author ליבי
 *
 */
@Configuration


 public class WebConfiguration implements WebMvcConfigurer{


	@Bean
	public Map<String, SessionInfo> sessions() {
		return new HashMap<String, SessionInfo>();
	}
	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
        .addResourceLocations("classpath:/static/")
        .resourceChain(true)
        .addResolver(new PathResourceResolver() {
            protected Resource getResource(String resourcePath, Resource location) throws IOException {
                Resource requestedResource = location.createRelative(resourcePath);
                return requestedResource.exists() && requestedResource.isReadable() ? requestedResource : new ClassPathResource("/static/index.html");
            }
        });
	}


	 



}






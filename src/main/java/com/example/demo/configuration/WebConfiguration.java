package com.example.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Configuration tag annotates a configuration class.
 * Bean tag-marks a method (that returns an object) to be instantiated and injected
 *             into the calling class on the first run. It's default scope is Singleton.
 * WebConfiguration class consists of:
 *             Sessions hashMap- required for managing active users sessions,
 *             and tracking user's state while communicating with the server-on
 *             each request the same sessions hashMap will be returned, since
 *             the default scope value is singleton and this is a shared resource.
 *             SessionInfo is a wrapping class that is made up of details about
 *             the session.
 *             The sessions map will be called on every controller.
 * @author ליבי
 *
 */

 public class WebConfiguration implements WebMvcConfigurer{

	

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

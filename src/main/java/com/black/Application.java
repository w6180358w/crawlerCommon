package com.black;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import com.black.web.base.utils.CommonUtil;

@SpringBootApplication
public class Application extends SpringBootServletInitializer{
	
	public static void main(String[] args) {
		ApplicationContext app = SpringApplication.run(Application.class, args); 
		CommonUtil.setApplicationContext(app);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		SpringApplicationBuilder config = builder.sources(Application.class);
        return config;
    }

	@Override
	protected WebApplicationContext run(SpringApplication application) {
		WebApplicationContext context = super.run(application);
		CommonUtil.setApplicationContext(context);
		return context;
	}
	
	
}

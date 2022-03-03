package com.online.bookstore.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PropertiesReader {

	@Autowired
	private Environment environment;

	public String get(String key) {
		return environment.getProperty(key);
	}

	public Integer getAsInt(String key) {
		return Integer.valueOf(environment.getProperty(key));
	}

}

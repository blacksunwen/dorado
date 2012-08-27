package com.bstek.dorado.core.resource;

import java.util.Locale;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-8-16
 */
public class LocaleFactory implements FactoryBean<Locale> {
	private String language;
	private String country;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Locale getObject() throws Exception {
		return new Locale(language, country);
	}

	public Class<Locale> getObjectType() {
		return Locale.class;
	}

	public boolean isSingleton() {
		return true;
	}

}

package com.bstek.dorado.sample.basic;

import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bstek.dorado.view.widget.form.RadioGroup;

@Component
public class TestI18n {
	private static final String DEFAULT_LOCALE = "zh_CN";
	
	@Resource(name = "dorado.localeResolver")
	private LocaleResolver localeResolver;

	public void onViewInit(RadioGroup radioGroupLocale) throws Exception {
		Locale resolveLocale = localeResolver.resolveLocale();
		radioGroupLocale.setValue((resolveLocale != null) ? resolveLocale
				.toString() : DEFAULT_LOCALE);
	}
}

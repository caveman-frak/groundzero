package uk.co.bluegecko.utility.validation;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringJUnitConfig
public class AbstractValidatorTest {

	@Autowired
	Clock clock;
	@Autowired
	MessageSource messageSource;
	@Autowired
	Validator validator;

	@NotNull
	protected Errors validate(Object target, String name, Validator... validators) {
		DataBinder binder = new DataBinder(target, name);
		binder.addValidators(ArrayUtils.add(validators, validator));
		binder.validate(target);
		return binder.getBindingResult();
	}

	@TestConfiguration
	static class Configuration {

		@Bean
		Clock clock() {
			return Clock.fixed(Instant.ofEpochSecond(961072210), ZoneOffset.UTC);
		}

		@Bean
		public MessageSource messageSource() {
			ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
			messageSource.setBasename("classpath:messages/validation-invoice");
			messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
			return messageSource;
		}

		@Bean
		public Validator localValidator() {
			LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
			bean.setValidationMessageSource(messageSource());
			return bean;
		}

	}
}
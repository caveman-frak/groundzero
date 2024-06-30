package uk.co.bluegecko.utility.validation;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import javax.money.Monetary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import uk.co.bluegecko.common.model.invoice.Invoice;

@SpringJUnitConfig
class InvoiceValidatorTest {

	@Autowired
	Clock clock;

	@Test
	void invoiceNegativeNumber() {
		Invoice invoice = Invoice.builder()
				.number(-1L)
				.date(clock).currency(Monetary.getCurrency("GBP"))
				.build();
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
			messageSource.setDefaultEncoding("UTF-8");
			return messageSource;
		}

		@Bean
		public LocalValidatorFactoryBean getValidator() {
			LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
			bean.setValidationMessageSource(messageSource());
			return bean;
		}

	}
}
package uk.co.bluegecko.csv.beanio.write;

import org.beanio.StreamFactory;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.csv.beanio.AbstractBeanIoCountryTest;

public class BeanIOCountryWriteTest extends AbstractBeanIoCountryTest {


	@Test
	void fromDataWithMapper() {
		StreamFactory factory = StreamFactory.newInstance();
		// load the mapping file
		factory.loadResource(MAPPING_FILE);
	}

}
package uk.co.bluegecko.parser.path;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
class PathVisitorDrawerTest extends AbstractPathTest {

	private PathVisitorDrawer visitor;

	@BeforeEach
	void setUp() {
		visitor = new PathVisitorDrawer();
	}

	@Test
	void parseMove() {
		assertThat(walkPathWith(visitor, "M10,10")).isNull();
	}

}
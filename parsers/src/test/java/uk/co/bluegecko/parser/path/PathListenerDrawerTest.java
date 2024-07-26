package uk.co.bluegecko.parser.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;

import java.awt.Graphics;
import java.awt.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
class PathListenerDrawerTest extends AbstractPathTest {

	@MockBean
	Graphics graphics;

	PathListenerDrawer listener;

	@BeforeEach
	void setUp() {
		listener = new PathListenerDrawer(graphics);
	}

	@Test
	void parseMove() {
		walkPathWith(listener, "M10,10");

		assertThat(listener.getPosition()).isEqualTo(new Point(0, 0));
		verifyNoInteractions(graphics);
	}

}
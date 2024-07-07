package uk.co.bluegecko.utility.geo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.utility.geo.ex.ArgumentOverflowException;

class DegreeMinuteSecondTest {

	private TestableDMS dms;

	@BeforeEach
	void setUp() {
		dms = new TestableDMS(180, 0, 0);
	}

	@Test
	void constructor() {
		TestableDMS dms = new TestableDMS(100, 20, 10.05);
		assertThat(dms.getDegrees()).describedAs("degrees").isEqualTo(100);
		assertThat(dms.getMinutes()).describedAs("minutes").isEqualTo(20);
		assertThat(dms.getSeconds()).describedAs("seconds").isEqualTo(10.05, within(0.0001));
	}

	@Test
	void decimal() {
		TestableDMS dms = new TestableDMS(100, 20, 10.05);
		assertThat(dms.decimal()).describedAs("decimal").isEqualTo(100.33612500, within(0.0000001));
	}

	@Test
	void radians() {
		TestableDMS dms = new TestableDMS(100, 20, 10.05);
		assertThat(dms.radians()).describedAs("radians").isEqualTo(1.7511957, within(0.0000001));
	}

	@Test
	void fromDecimalParts() {
		Number[] args = DegreeMinuteSecond.partsFromDecimal(100.33612500);
		TestableDMS dms = new TestableDMS((int) args[0], (int) args[1], (double) args[2]);
		assertThat(dms.getDegrees()).describedAs("degrees").isEqualTo(100);
		assertThat(dms.getMinutes()).describedAs("minutes").isEqualTo(20);
		assertThat(dms.getSeconds()).describedAs("seconds").isEqualTo(10.05, within(0.0001));
		assertThat(dms.decimal()).describedAs("decimal").isEqualTo(100.33612500, within(0.0000001));
	}

	@Test
	void fromDecimal() {
		TestableDMS dms = TestableDMS.fromDecimal(100.33612500);
		assertThat(dms.getDegrees()).describedAs("degrees").isEqualTo(100);
		assertThat(dms.getMinutes()).describedAs("minutes").isEqualTo(20);
		assertThat(dms.getSeconds()).describedAs("seconds").isEqualTo(10.05, within(0.0001));
		assertThat(dms.decimal()).describedAs("decimal").isEqualTo(100.33612500, within(0.0000001));
	}

	@Test
	void cardinal() {
		assertThat(new TestableDMS(90, 0, 0).isCardinal())
				.describedAs("90 0 0").isTrue();
		assertThat(new TestableDMS(135, 0, 0).isCardinal())
				.describedAs("135 0 0").isFalse();
		assertThat(new TestableDMS(90, 0, 0.05).isCardinal())
				.describedAs("90 0 0.05").isFalse();
	}

	@Test
	void compassPoint() {
		assertThat(new TestableDMS(90, 0, 0).compassPoint())
				.describedAs("90 0 0").isPresent().get().isEqualTo(Compass.E);
		assertThat(new TestableDMS(135, 0, 0).compassPoint())
				.describedAs("135 0 0").isPresent().get().isEqualTo(Compass.SE);
		assertThat(new TestableDMS(90, 0, 0.05).compassPoint())
				.describedAs("90 0 0.05").isNotPresent();
	}

	@Test
	void cardinalCompassPoint() {
		assertThat(new TestableDMS(90, 0, 0).compassPoint(Compass.cardinal()))
				.describedAs("90 0 0").isPresent().get().isEqualTo(Compass.E);
		assertThat(new TestableDMS(135, 0, 0).compassPoint(Compass.cardinal()))
				.describedAs("135 0 0").isNotPresent();
		assertThat(new TestableDMS(90, 0, 0.05).compassPoint(Compass.cardinal()))
				.describedAs("90 0 0.05").isNotPresent();
	}

	@Test
	void wrapWithinBounds() {
		assertThat(dms.wrap(0, 360, 90, true))
				.describedAs("with wrapping")
				.isEqualTo(90, within(0.0000001));
		assertThat(dms.wrap(0, 360, 90, false))
				.describedAs("without wrapping")
				.isEqualTo(90, within(0.0000001));
	}

	@Test
	void wrapUnderflow() {
		assertThat(dms.wrap(0, 360, -10, true))
				.isEqualTo(350);
	}

	@Test
	void wrapUnderflowAt180() {
		assertThat(dms.wrap(-180, 180, -190, true))
				.isEqualTo(170);
	}

	@Test
	void wrapUnderflowException() {
		assertThatException().isThrownBy(() -> dms.wrap(0, 360, -10, false))
				.isInstanceOf(ArgumentOverflowException.class)
				.withMessage("Decimal underflow beyond 0 with value -10.0");
	}

	@Test
	void wrapOverflow() {
		assertThat(dms.wrap(0, 360, 370, true))
				.isEqualTo(10);
	}

	@Test
	void wrapOverflowAt180() {
		assertThat(dms.wrap(-180, 180, 190, true))
				.isEqualTo(-170);
	}

	@Test
	void addDMS() {
		assertThat(dms.add(new TestableDMS(10, 20, 30)))
				.isEqualTo(new TestableDMS(190, 20, 30));
	}

	@Test
	void addDecimal() {
		assertThat(dms.add(10.341666667))
				.isEqualTo(new TestableDMS(190, 20, 30));
	}

	@Test
	void subtractDMS() {
		assertThat(dms.subtract(new TestableDMS(10, 20, 30)))
				.isEqualTo(new TestableDMS(169, 39, 30));
	}

	@Test
	void subtractDecimal() {
		assertThat(dms.subtract(10.341666667))
				.isEqualTo(new TestableDMS(169, 39, 30));
	}

	@Test
	void wrapOverflowException() {
		assertThatException().isThrownBy(() -> dms.wrap(0, 360, 370, false))
				.isInstanceOf(ArgumentOverflowException.class)
				.withMessage("Decimal overflow beyond 360 with value 370.0");
	}

	private static final class TestableDMS extends DegreeMinuteSecond<TestableDMS> {

		public TestableDMS(int degrees, int minutes, double seconds) {
			super(0, 360, degrees, minutes, seconds);
		}

		@Override
		protected TestableDMS create(int degrees, int minutes, double seconds) {
			return new TestableDMS(degrees, minutes, seconds);
		}

		public static TestableDMS fromDecimal(double decimal) {
			Number[] args = DegreeMinuteSecond.partsFromDecimal(decimal);
			return new TestableDMS((int) args[0], (int) args[1], (double) args[2]);
		}

	}

}
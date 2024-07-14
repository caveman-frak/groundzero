package uk.co.bluegecko.common.math;

import javax.measure.Quantity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class QuantityMath {

	public <T extends Quantity<T>> double abs(Quantity<T> qty) {
		return NumberMath.abs(qty.getValue());
	}

	public <T extends Quantity<T>> double acos(Quantity<T> qty) {
		return NumberMath.acos(qty.getValue());
	}

	public <T extends Quantity<T>> double asin(Quantity<T> qty) {
		return NumberMath.asin(qty.getValue());
	}

	public <T extends Quantity<T>> double atan(Quantity<T> qty) {
		return NumberMath.atan(qty.getValue());
	}

	public <T extends Quantity<T>> double atan2(Quantity<T> qty1, Quantity<T> qty2) {
		return NumberMath.atan2(qty1.getValue(), qty2.getValue());
	}

	public <T extends Quantity<T>> double cbrt(Quantity<T> qty) {
		return NumberMath.cbrt(qty.getValue());
	}

	public <T extends Quantity<T>> double cos(Quantity<T> qty) {
		return NumberMath.cos(qty.getValue());
	}

	public <T extends Quantity<T>> double cosh(Quantity<T> qty) {
		return NumberMath.cosh(qty.getValue());
	}

	public <T extends Quantity<T>> double exp(Quantity<T> qty) {
		return NumberMath.exp(qty.getValue());
	}

	public <T extends Quantity<T>> double expm1(Quantity<T> qty) {
		return NumberMath.expm1(qty.getValue());
	}

	public <T extends Quantity<T>> double hypot(Quantity<T> qty1, Quantity<T> qty2) {
		return NumberMath.hypot(qty1.getValue(), qty2.getValue());
	}

	public <T extends Quantity<T>> double log(Quantity<T> qty) {
		return NumberMath.log(qty.getValue());
	}

	public <T extends Quantity<T>> double log10(Quantity<T> qty) {
		return NumberMath.log10(qty.getValue());
	}

	public <T extends Quantity<T>> double log1p(Quantity<T> qty) {
		return NumberMath.log1p(qty.getValue());
	}

	public <T extends Quantity<T>> double pow(Quantity<T> qty1, Quantity<T> qty2) {
		return NumberMath.pow(qty1.getValue(), qty2.getValue());
	}

	public <T extends Quantity<T>> double max(Quantity<T> qty1, Quantity<T> qty2) {
		return NumberMath.max(qty1.getValue(), qty2.getValue());
	}

	public <T extends Quantity<T>> double min(Quantity<T> qty1, Quantity<T> qty2) {
		return NumberMath.min(qty1.getValue(), qty2.getValue());
	}

	public <T extends Quantity<T>> double sin(Quantity<T> qty) {
		return NumberMath.sin(qty.getValue());
	}

	public <T extends Quantity<T>> double sinh(Quantity<T> qty) {
		return NumberMath.sinh(qty.getValue());
	}

	public <T extends Quantity<T>> double sqrt(Quantity<T> qty) {
		return NumberMath.sqrt(qty.getValue());
	}

	public <T extends Quantity<T>> double tan(Quantity<T> qty) {
		return NumberMath.tan(qty.getValue());
	}

	public <T extends Quantity<T>> double tanh(Quantity<T> qty) {
		return NumberMath.tanh(qty.getValue());
	}


}
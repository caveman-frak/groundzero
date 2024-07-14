package uk.co.bluegecko.common.math;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NumberMath {

	public double abs(Number num) {
		return Math.abs(num.doubleValue());
	}

	public double acos(Number num) {
		return Math.acos(num.doubleValue());
	}

	public double asin(Number num) {
		return Math.asin(num.doubleValue());
	}

	public double atan(Number num) {
		return Math.atan(num.doubleValue());
	}

	public double atan2(Number num1, Number num2) {
		return Math.atan2(num1.doubleValue(), num2.doubleValue());
	}

	public double cbrt(Number num) {
		return Math.cbrt(num.doubleValue());
	}

	public double cos(Number num) {
		return Math.cos(num.doubleValue());
	}

	public double cosh(Number num) {
		return Math.cosh(num.doubleValue());
	}

	public double exp(Number num) {
		return Math.exp(num.doubleValue());
	}

	public double expm1(Number num) {
		return Math.expm1(num.doubleValue());
	}

	public double hypot(Number num1, Number num2) {
		return Math.hypot(num1.doubleValue(), num2.doubleValue());
	}

	public double log(Number num) {
		return Math.log(num.doubleValue());
	}

	public double log10(Number num) {
		return Math.log10(num.doubleValue());
	}

	public double log1p(Number num) {
		return Math.log1p(num.doubleValue());
	}

	public double pow(Number num1, Number num2) {
		return Math.pow(num1.doubleValue(), num2.doubleValue());
	}

	public double max(Number num1, Number num2) {
		return Math.max(num1.doubleValue(), num2.doubleValue());
	}

	public double min(Number num1, Number num2) {
		return Math.min(num1.doubleValue(), num2.doubleValue());
	}

	public double sin(Number num) {
		return Math.sin(num.doubleValue());
	}

	public double sinh(Number num) {
		return Math.sinh(num.doubleValue());
	}

	public double sqrt(Number num) {
		return Math.sqrt(num.doubleValue());
	}

	public double tan(Number num) {
		return Math.tan(num.doubleValue());
	}

	public double tanh(Number num) {
		return Math.tanh(num.doubleValue());
	}

}
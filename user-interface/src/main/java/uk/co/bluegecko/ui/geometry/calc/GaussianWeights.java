package uk.co.bluegecko.ui.geometry.calc;

/**
 * The Gaussian Weights and Abscissae are taken from tables at
 * <a href="https://pomax.github.io/bezierinfo/legendre-gauss.html">Legendre-Gauss quadrature</a> that includes
 * descriptions of how to calculate the values.
 */
public record GaussianWeights(
		double weight, double abscissae
) {

	public static GaussianWeights[] forAccuracy(Accuracy accuracy) {
		return switch (accuracy) {
			case MIN -> new GaussianWeights[]{
					new GaussianWeights(1.0, 0.5773502691896257),
					new GaussianWeights(1.0, 0.5773502691896257)};
			case POOR -> new GaussianWeights[]{
					new GaussianWeights(0.8888888888888888, 0.0),
					new GaussianWeights(0.5555555555555556, 0.7745966692414834),
					new GaussianWeights(0.5555555555555556, 0.7745966692414834)};
			case LOW -> new GaussianWeights[]{
					new GaussianWeights(0.6521451548625461, -0.3399810435848563),
					new GaussianWeights(0.6521451548625461, 0.3399810435848563),
					new GaussianWeights(0.3478548451374538, -0.8611363115940526),
					new GaussianWeights(0.3478548451374538, 0.8611363115940526)};
			case MEDIUM -> new GaussianWeights[]{
					new GaussianWeights(0.5688888888888889, 0.0000000000000000),
					new GaussianWeights(0.4786286704993665, -0.5384693101056831),
					new GaussianWeights(0.4786286704993665, 0.5384693101056831),
					new GaussianWeights(0.2369268850561891, -0.9061798459386640),
					new GaussianWeights(0.2369268850561891, 0.9061798459386640)};
			case HIGH -> new GaussianWeights[]{
					new GaussianWeights(0.3607615730481386, 0.6612093864662645),
					new GaussianWeights(0.3607615730481386, -0.6612093864662645),
					new GaussianWeights(0.4679139345726910, -0.2386191860831969),
					new GaussianWeights(0.4679139345726910, 0.2386191860831969),
					new GaussianWeights(0.1713244923791704, -0.9324695142031521),
					new GaussianWeights(0.1713244923791704, 0.9324695142031521)};
			case MAX -> new GaussianWeights[]{
					new GaussianWeights(0.4179591836734694, 0.0000000000000000),
					new GaussianWeights(0.3818300505051189, 0.4058451513773972),
					new GaussianWeights(0.3818300505051189, -0.4058451513773972),
					new GaussianWeights(0.2797053914892766, -0.7415311855993945),
					new GaussianWeights(0.2797053914892766, 0.7415311855993945),
					new GaussianWeights(0.1294849661688697, -0.9491079123427585),
					new GaussianWeights(0.1294849661688697, 0.9491079123427585)};
		};
	}

}
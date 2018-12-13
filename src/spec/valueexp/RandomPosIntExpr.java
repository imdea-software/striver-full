package spec.valueexp;

import java.util.Random;

public class RandomPosIntExpr extends INDValExpr<Double> {

	private	Random r = new Random();
	private double rangeMin = 1d;
	private double rangeMax = 6d;

	@Override
	public Double calculateValueAt(double nt) {
		double ran = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		return ran;
	}

	@Override
	public void unhookPointers() {
	}

}

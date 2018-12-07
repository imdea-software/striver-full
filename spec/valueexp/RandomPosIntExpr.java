package spec.valueexp;

import java.util.Random;

import adts.MaybeNotick;

public class RandomPosIntExpr implements IValExpr<Double> {

	private	Random r = new Random();
	private double rangeMin = 1d;
	private double rangeMax = 6d;

	@Override
	public MaybeNotick<Double> calculateValueAt(double nt) {
		double ran = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		return MaybeNotick.of(ran);
	}

}

package spec.valueexp;

import adts.MaybeNotick;

public class RandomNegIntExpr implements IValExpr<Double> {

	@Override
	public MaybeNotick<Double> calculateValueAt(double nt) {
		return MaybeNotick.of(-5d);
	}

}

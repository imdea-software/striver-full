package spec.valueexp.tauexp;

import adts.MaybeOutside;

public class TExpr extends ITauExp {

	@Override
	public MaybeOutside<Double> getT(double t) {
		return MaybeOutside.of(t);
	}

}

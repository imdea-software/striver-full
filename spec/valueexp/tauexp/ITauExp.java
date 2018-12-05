package spec.valueexp.tauexp;

import java.util.Optional;

import adts.MaybeOutside;
import spec.valueexp.IValExpr;

public abstract class ITauExp implements IValExpr<MaybeOutside<Double>> {
	
	public abstract MaybeOutside<Double> getT(double t);
	
	public Optional<MaybeOutside<Double>> calculateValueAt(double t) {
		return Optional.of(getT(t));
	}

}

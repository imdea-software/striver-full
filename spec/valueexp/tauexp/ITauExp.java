package spec.valueexp.tauexp;

import adts.MaybeNotick;
import adts.MaybeOutside;
import spec.valueexp.IValExpr;

public abstract class ITauExp implements IValExpr<MaybeOutside<Double>> {
	
	public abstract MaybeOutside<Double> getT(double t);
	
	public MaybeNotick<MaybeOutside<Double>> calculateValueAt(double t) {
		return MaybeNotick.of(getT(t));
	}

}

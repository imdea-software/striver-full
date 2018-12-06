package spec.valueexp.tauexp;

import adts.MaybeOutside;
import semop.Pointer;

public class PrevEqExp extends ITauExp {
	
	private GenericPrev<Double> genericprev;

	public PrevEqExp(Pointer p, ITauExp it) {
		this.genericprev = new GenericPrev<Double>(p, it, true, false);
	}

	@Override
	public MaybeOutside<Double> getT(double t) {
		return genericprev.getRes(t);
	}

}

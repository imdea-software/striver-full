package spec.valueexp.tauexp;

import adts.MaybeOutside;
import semop.Pointer;

public class PrevExp extends ITauExp {
	
	private GenericPrev<Double> genericprev;

	public PrevExp(Pointer p, ITauExp it) {
		this.genericprev = new GenericPrev<Double>(p, it, false, false);
	}

	@Override
	public MaybeOutside<Double> getT(double t) {
		return genericprev.getRes(t);
	}

}

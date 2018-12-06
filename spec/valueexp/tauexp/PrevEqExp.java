package spec.valueexp.tauexp;

import adts.MaybeOutside;
import semop.Pointer;

public class PrevEqExp extends ITauExp {
	
	private GenericPrev genericprev;

	public PrevEqExp(Pointer p, ITauExp it) {
		this.genericprev = new GenericPrev(p, it, true);
	}

	@Override
	public MaybeOutside<Double> getT(double t) {
		return genericprev.getT(t);
	}

}

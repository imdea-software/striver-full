package spec.valueexp.tauexp;

import adts.MaybeOutside;
import semop.Pointer;

public class PrevExp extends ITauExp {
	
	private GenericPrev genericprev;

	public PrevExp(Pointer p, ITauExp it) {
		this.genericprev = new GenericPrev(p, it, false);
	}

	@Override
	public MaybeOutside<Double> getT(double t) {
		return genericprev.getT(t);
	}

}

package spec.valueexp.tauexp;

import semop.Pointer;
import spec.valueexp.generics.GenericPrev;

public class PrevExp extends ITauExp {
	
	private GenericPrev<Double> genericprev;

	public PrevExp(Pointer p, ITauExp it) {
		this.genericprev = new GenericPrev<Double>(p, it, false, false);
	}

	@Override
	public Object getT(double t) {
		return genericprev.getRes(t);
	}

}

package spec.valueexp.tauexp;

import semop.Pointer;
import spec.valueexp.generics.GenericPrev;

public class PrevEqExp extends ITauExp {
	
	private GenericPrev<Double> genericprev;

	public PrevEqExp(Pointer p, ITauExp it) {
		this.genericprev = new GenericPrev<Double>(p, it, true, false);
	}

	@Override
	public Object getT(double t) {
		return genericprev.getRes(t);
	}

}

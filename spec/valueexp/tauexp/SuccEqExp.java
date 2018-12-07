package spec.valueexp.tauexp;

import adts.MaybeOutside;
import semop.Pointer;
import spec.valueexp.generics.GenericSucc;

public class SuccEqExp extends ITauExp {
	
	private GenericSucc<Double> genericsucc;

	public SuccEqExp(Pointer p, ITauExp it) {
		this.genericsucc = new GenericSucc<Double>(p, it, true, false);
	}

	@Override
	public MaybeOutside<Double> getT(double t) {
		return genericsucc.getRes(t);
	}

}

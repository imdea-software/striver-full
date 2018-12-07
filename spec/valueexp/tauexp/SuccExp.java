package spec.valueexp.tauexp;

import adts.MaybeOutside;
import semop.Pointer;
import spec.valueexp.generics.GenericSucc;

public class SuccExp extends ITauExp {
	
	private GenericSucc<Double> genericsucc;

	public SuccExp(Pointer p, ITauExp it) {
		this.genericsucc = new GenericSucc<Double>(p, it, false, false);
	}

	@Override
	public MaybeOutside<Double> getT(double t) {
		return genericsucc.getRes(t);
	}

}

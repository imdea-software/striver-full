package spec.valueexp.tauexp;

import semop.Pointer;
import spec.valueexp.generics.GenericSucc;

public class SuccExp extends ITauExp {
	
	private GenericSucc<Double> genericsucc;

	public SuccExp(Pointer p, ITauExp it) {
		this.genericsucc = new GenericSucc<Double>(p, it, false, false);
	}

	@Override
	public Object getT(double t) {
		return genericsucc.getRes(t);
	}

	@Override
	public void unhookPointers() {
		genericsucc.unhookPointers();
	}

}

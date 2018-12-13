package spec.valueexp;

import semop.Pointer;
import spec.valueexp.generics.GenericSucc;
import spec.valueexp.tauexp.ITauExp;

public class SuccValExp<T> extends INDValExpr<Object> {
	
	private GenericSucc<T> genericsucc;

	public SuccValExp(Pointer p, ITauExp it) {
		this.genericsucc = new GenericSucc<T>(p, it, false, true);
	}

	@Override
	public Object calculateValueAt(double t) {
		return genericsucc.getRes(t);
	}

	@Override
	public void unhookPointers() {
		genericsucc.unhookPointers();
	}

}

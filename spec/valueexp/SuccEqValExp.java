package spec.valueexp;

import semop.Pointer;
import spec.valueexp.generics.GenericSucc;
import spec.valueexp.tauexp.ITauExp;

public class SuccEqValExp<T> implements IValExpr<Object> {
	
	private GenericSucc<T> genericsucc;

	public SuccEqValExp(Pointer p, ITauExp it) {
		this.genericsucc = new GenericSucc<T>(p, it, true, true);
	}

	@Override
	public Object calculateValueAt(double t) {
		return genericsucc.getRes(t);
	}

}

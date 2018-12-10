package spec.valueexp;


import semop.Pointer;
import spec.valueexp.generics.GenericPrev;
import spec.valueexp.tauexp.ITauExp;

public class PrevEqValExp<T> implements IValExpr<Object> {
	
	private GenericPrev<T> genericprev;

	public PrevEqValExp(Pointer p, ITauExp it) {
		this.genericprev = new GenericPrev<T>(p, it, true, true);
	}

	@Override
	public Object calculateValueAt(double t) {
		return genericprev.getRes(t);
	}

}

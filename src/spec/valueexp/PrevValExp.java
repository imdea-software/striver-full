package spec.valueexp;


import semop.Pointer;
import spec.valueexp.generics.GenericPrev;
import spec.valueexp.tauexp.ITauExp;

public class PrevValExp<T> extends INDValExpr<Object> {
	
	private GenericPrev<T> genericprev;

	public PrevValExp(Pointer p, ITauExp it) {
		this.genericprev = new GenericPrev<T>(p, it, false, true);
	}

	@Override
	public Object calculateValueAt(double t) {
		return genericprev.getRes(t);
	}

	@Override
	public void unhookPointers() {
		genericprev.unhookPointers();
	}
}

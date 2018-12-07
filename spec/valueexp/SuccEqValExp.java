package spec.valueexp;

import adts.MaybeNotick;
import adts.MaybeOutside;
import semop.Pointer;
import spec.valueexp.generics.GenericSucc;
import spec.valueexp.tauexp.ITauExp;

public class SuccEqValExp<T> implements IValExpr<MaybeOutside<T>> {
	
	private GenericSucc<T> genericsucc;

	public SuccEqValExp(Pointer p, ITauExp it) {
		this.genericsucc = new GenericSucc<T>(p, it, true, true);
	}

	@Override
	public MaybeNotick<MaybeOutside<T>> calculateValueAt(double t) {
		return MaybeNotick.of(genericsucc.getRes(t));
	}

}

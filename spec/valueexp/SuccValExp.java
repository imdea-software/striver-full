package spec.valueexp;

import java.util.Optional;

import adts.MaybeOutside;
import semop.Pointer;
import spec.valueexp.generics.GenericSucc;
import spec.valueexp.tauexp.ITauExp;

public class SuccValExp<T> implements IValExpr<MaybeOutside<T>> {
	
	private GenericSucc<T> genericsucc;

	public SuccValExp(Pointer p, ITauExp it) {
		this.genericsucc = new GenericSucc<T>(p, it, false, true);
	}

	@Override
	public Optional<MaybeOutside<T>> calculateValueAt(double t) {
		return Optional.of(genericsucc.getRes(t));
	}

}

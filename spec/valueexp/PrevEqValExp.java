package spec.valueexp;

import java.util.Optional;

import adts.MaybeOutside;
import semop.Pointer;
import spec.valueexp.generics.GenericPrev;
import spec.valueexp.tauexp.ITauExp;

public class PrevEqValExp<T> implements IValExpr<MaybeOutside<T>> {
	
	private GenericPrev<T> genericprev;

	public PrevEqValExp(Pointer p, ITauExp it) {
		this.genericprev = new GenericPrev<T>(p, it, true, true);
	}

	@Override
	public Optional<MaybeOutside<T>> calculateValueAt(double t) {
		return Optional.of(genericprev.getRes(t));
	}

}

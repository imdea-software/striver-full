package spec.valueexp;

import java.util.Optional;

import adts.MaybeOutside;
import semop.Pointer;
import spec.valueexp.tauexp.GenericPrev;
import spec.valueexp.tauexp.ITauExp;

public class PrevValExp<T> implements IValExpr<MaybeOutside<T>> {
	
	private GenericPrev<T> genericprev;

	public PrevValExp(Pointer p, ITauExp it) {
		this.genericprev = new GenericPrev<T>(p, it, false, true);
	}

	@Override
	public Optional<MaybeOutside<T>> calculateValueAt(double t) {
		return Optional.of(genericprev.getRes(t));
	}

}

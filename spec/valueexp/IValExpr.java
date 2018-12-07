package spec.valueexp;

import java.util.Optional;

import adts.MaybeNotick;

public interface IValExpr<T> {

	MaybeNotick<T> calculateValueAt(double nt);

}

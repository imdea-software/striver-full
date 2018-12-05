package spec.valueexp;

import java.util.Optional;

public interface IValExpr<T> {

	Optional<T> calculateValueAt(double nt);

}

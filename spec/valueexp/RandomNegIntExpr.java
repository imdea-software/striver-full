package spec.valueexp;

import java.util.Optional;

public class RandomNegIntExpr implements IValExpr {

	@Override
	public Optional<Object> calculateValueAt(double nt) {
		return Optional.of(-5d);
	}

}

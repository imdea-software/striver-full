package spec.valueexp;

import java.util.Optional;

public class RandomPosIntExpr implements IValExpr {

	@Override
	public Optional<Object> calculateValueAt(double nt) {
		return Optional.of(5d);
	}

}

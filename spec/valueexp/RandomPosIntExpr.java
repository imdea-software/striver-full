package spec.valueexp;

import java.util.Optional;

public class RandomPosIntExpr implements IValExpr<Double> {

	@Override
	public Optional<Double> calculateValueAt(double nt) {
		return Optional.of(5d);
	}

}

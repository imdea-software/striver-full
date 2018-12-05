package spec.valueexp;

import java.util.Optional;

public class RandomNegIntExpr implements IValExpr<Double> {

	@Override
	public Optional<Double> calculateValueAt(double nt) {
		return Optional.of(-5d);
	}

}

package spec.valueexp;

import java.util.Optional;

public class RandomIntExpr implements IValExpr {

	@Override
	public Optional<Object> calculateValueAt(int nt) {
		return Optional.of(5);
	}

}

package spec.valueexp;

public class RandomNegIntExpr implements IValExpr<Double> {

	@Override
	public Double calculateValueAt(double nt) {
		return -5d;
	}

}

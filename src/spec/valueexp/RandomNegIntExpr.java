package spec.valueexp;

public class RandomNegIntExpr extends INDValExpr<Double> {

	@Override
	public Double calculateValueAt(double nt) {
		return -5d;
	}

	@Override
	public void unhookPointers() {
	}

}

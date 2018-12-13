package spec.valueexp;

public class CVValExpr<T> implements IValExpr<T>{

	@Override
	public T calculateValueAt(double nt, Object cv) {
		return (T) cv;
	}

	@Override
	public void unhookPointers() {
	}

}

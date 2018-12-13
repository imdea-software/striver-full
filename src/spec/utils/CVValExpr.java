package spec.utils;

import spec.valueexp.IValExpr;

public class CVValExpr implements IValExpr<Object>{

	@Override
	public Object calculateValueAt(double nt, Object cv) {
		return cv;
	}

	@Override
	public void unhookPointers() {
	}

}

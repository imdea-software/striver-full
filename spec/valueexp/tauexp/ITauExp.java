package spec.valueexp.tauexp;

import spec.valueexp.IValExpr;

public abstract class ITauExp implements IValExpr<Object> {
	
	public abstract Object getT(double t);
	
	public Object calculateValueAt(double t) {
		return getT(t);
	}

}

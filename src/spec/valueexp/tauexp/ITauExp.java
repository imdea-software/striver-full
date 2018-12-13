package spec.valueexp.tauexp;

import spec.valueexp.INDValExpr;

public abstract class ITauExp extends INDValExpr<Object> {
	
	public abstract Object getT(double t);
	
	public Object calculateValueAt(double t) {
		return getT(t);
	}

}

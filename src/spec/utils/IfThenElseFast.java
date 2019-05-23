package spec.utils;


import spec.valueexp.IValExpr;

public class IfThenElseFast<T> implements IValExpr<T>{

	public IfThenElseFast(IValExpr<Boolean> ifExpr, IValExpr<T> thenExpr, IValExpr<T> elseExpr) {
		this.ifExpr = ifExpr;
		this.thenExpr = thenExpr;
		this.elseExpr = elseExpr;
	}

	private IValExpr<Boolean> ifExpr;
	private IValExpr<T> thenExpr;
	private IValExpr<T> elseExpr;

	@Override
	public T calculateValueAt(double nt, Object cv) {
		Boolean ifres = ifExpr.calculateValueAt(nt, cv);
		if (ifres)
			return thenExpr.calculateValueAt(nt, cv);
		return elseExpr.calculateValueAt(nt, cv);
	}

	@Override
	public void unhookPointers() {
		ifExpr.unhookPointers();
		thenExpr.unhookPointers();
		elseExpr.unhookPointers();
	}

}

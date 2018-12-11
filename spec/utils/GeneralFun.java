package spec.utils;

import spec.valueexp.IValExpr;

public class GeneralFun<T> implements IValExpr<T> {

	public interface Fun<T> {
        T apply(Object... args);
	}

	private IValExpr<?>[] exprs;
	private Fun<T> fun;

	@Override
	public T calculateValueAt(double nt) {
		Object[] args = new Object[exprs.length];
		int i=0;
		for (IValExpr<?> expr : exprs) {
			args[i] = expr.calculateValueAt(nt);
			i++;
		}
		return fun.apply(args);
	}
	
	public GeneralFun (Fun<T> fun, IValExpr<?>...exprs) {
		this.exprs = exprs;
		this.fun = fun;
	}

	@Override
	public void unhookPointers() {
		for (IValExpr<?> e:exprs) {
			e.unhookPointers();
		}
		
	}

}

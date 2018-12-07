package spec.utils;

import java.util.Optional;

import adts.MaybeNotick;
import spec.valueexp.IValExpr;

public class GeneralFun<T> implements IValExpr<T> {

	public interface Fun<T> {
		MaybeNotick<T> apply(MaybeNotick<?>... args);
	}

	private IValExpr<?>[] exprs;
	private Fun<T> fun;

	@Override
	public MaybeNotick<T> calculateValueAt(double nt) {
		MaybeNotick<?>[] args = new MaybeNotick<?>[exprs.length];
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

}

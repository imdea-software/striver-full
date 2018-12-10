package spec.utils;


import adts.MaybeNotick;
import spec.utils.GeneralFun.Fun;
import spec.valueexp.IValExpr;

public class IfThenElse<T> implements Fun<T>{

	@Override
	public T apply(Object... args) {
		return (Boolean) args[0]?(T) args[1]:(T) args[2];
	}

}

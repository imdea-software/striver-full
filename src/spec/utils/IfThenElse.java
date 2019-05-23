package spec.utils;

import spec.utils.GeneralFun.Fun;

public class IfThenElse<T> implements Fun<T> {

	@Override
	public T apply(Object... args) {
		return (boolean) args[0]?(T)args[1]:(T)args[2];
	}

}

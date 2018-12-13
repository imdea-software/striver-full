package spec.utils;

import spec.utils.GeneralFun.Fun;

public class UnsafeAdd implements Fun<Double>{

	@Override
	public Double apply(Object... args) {
		Double a = (Double) args[0];
		Double b = (Double) args[1];
		return a+b;
	}

}

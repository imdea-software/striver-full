package spec.utils;

import spec.utils.GeneralFun.Fun;

public class UnsafeAddInt implements Fun<Integer>{

	@Override
	public Integer apply(Object... args) {
		Integer a = (Integer) args[0];
		Integer b = (Integer) args[1];
		return a+b;
	}

}

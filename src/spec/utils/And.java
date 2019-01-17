package spec.utils;

import spec.utils.GeneralFun.Fun;

public class And implements Fun<Boolean> {

	@Override
	public Boolean apply(Object... args) {
		return (Boolean) args[0] && (Boolean) args[1];
	}

}

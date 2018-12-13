package spec.utils;


import spec.utils.GeneralFun.Fun;

public class GtFun implements Fun<Boolean>{

	@Override
	public Boolean apply(Object... args) {
		return (Double) args[0] > (Double) args[1];
	}

}

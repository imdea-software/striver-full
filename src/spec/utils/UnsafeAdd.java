package spec.utils;

import adts.Constants;
import spec.utils.GeneralFun.Fun;

public class UnsafeAdd implements Fun<Double>{

	@Override
	public Double apply(Object... args) {
		if (args[0]==Constants.posoutside() || args[1] == Constants.posoutside())
			return Constants.INFTY;
		Double a = (Double) args[0];
		Double b = (Double) args[1];
		return a+b;
	}

}

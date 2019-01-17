package spec.utils;


import adts.Constants;
import spec.utils.GeneralFun.Fun;

public class GtFun implements Fun<Boolean>{

	@Override
	public Boolean apply(Object... args) {
		Object a = args[0];
		Object b = args[1];
		if (a == Constants.negoutside() || b == Constants.posoutside())
			return false;
		if (a == Constants.posoutside() || b == Constants.negoutside())
			return true;
		return (Double) a > (Double) b;
	}

}

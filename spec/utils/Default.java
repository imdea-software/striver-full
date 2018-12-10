package spec.utils;

import adts.Constants;
import spec.utils.GeneralFun.Fun;

public class Default<T> implements Fun<T>{

	private T defVal;

	@Override
	public T apply(Object... args) {
		Object arg = ((Object) args[0]);
		return Constants.getOutsideType(arg)!=Constants.OutsideType.inside?defVal:(T) arg;
	}
	
	public Default(T defV) {
		this.defVal = defV;
	}

}

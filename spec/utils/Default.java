package spec.utils;

import adts.MaybeNotick;
import adts.MaybeOutside;
import spec.utils.GeneralFun.Fun;

public class Default<T> implements Fun<T>{

	private T defVal;

	@Override
	public T apply(Object... args) {
		MaybeOutside<T> arg = ((MaybeOutside<T>) args[0]);
		return arg.getType()!=MaybeOutside.Valtype.inside?defVal:arg.get();
	}
	
	public Default(T defV) {
		this.defVal = defV;
	}

}

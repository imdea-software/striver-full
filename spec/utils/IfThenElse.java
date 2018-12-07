package spec.utils;


import adts.MaybeNotick;
import spec.utils.GeneralFun.Fun;
import spec.valueexp.IValExpr;

public class IfThenElse<T> implements Fun<T>{

	@Override
	public MaybeNotick<T> apply(MaybeNotick<?>... args) {
		return (Boolean) args[0].getValue()?(MaybeNotick<T>) args[1]:(MaybeNotick<T>) args[2];
	}

}

package spec.utils;


import adts.MaybeNotick;
import spec.utils.GeneralFun.Fun;

public class EqFun implements Fun<Boolean>{

	@Override
	public Boolean apply(Object... args) {
		return args[0].equals(args[1]);
	}

}

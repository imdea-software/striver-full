package spec.utils;


import adts.MaybeNotick;
import spec.utils.GeneralFun.Fun;

public class EqFun implements Fun<Boolean>{

	@Override
	public MaybeNotick<Boolean> apply(MaybeNotick<?>... args) {
		return MaybeNotick.of(args[0].equals(args[1]));
	}

}

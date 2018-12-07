package spec.utils;


import adts.MaybeNotick;
import spec.utils.GeneralFun.Fun;

public class Constant<T> implements Fun<T>{

	private MaybeNotick<T> constant;

	public Constant (MaybeNotick<T> c) {
		this.constant = c;
	}

	@Override
	public MaybeNotick<T> apply(MaybeNotick<?>... args) {
		return constant;
	}

}

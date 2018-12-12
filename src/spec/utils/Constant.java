package spec.utils;


import spec.utils.GeneralFun.Fun;

public class Constant<T> implements Fun<T>{

	private T constant;

	public Constant (T c) {
		this.constant = c;
	}

	@Override
	public T apply(Object... args) {
		return constant;
	}

}

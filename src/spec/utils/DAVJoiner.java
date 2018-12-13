package spec.utils;

import adts.DelayAndValue;
import spec.utils.GeneralFun.Fun;

public class DAVJoiner<T> implements Fun<DelayAndValue<T>> {

	@Override
	public DelayAndValue<T> apply(Object... args) {
		return new DelayAndValue<T>((double) args[0], (T) args[1]);
	}

}

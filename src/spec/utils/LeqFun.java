package spec.utils;

import spec.utils.GeneralFun.Fun;

public class LeqFun implements Fun<Boolean>{
	
	private GtFun gtfun = new GtFun();

	@Override
	public Boolean apply(Object... args) {
		return !gtfun.apply(args);
	}

}

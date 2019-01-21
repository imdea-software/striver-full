package spec.utils;


import spec.utils.GeneralFun.Fun;

public class DivisionFun implements Fun<Double>{

	@Override
	public Double apply(Object... args) {
		Object a = args[0];
		Object b = args[1];
		return (Double) a / (Double) b;
	}

}

package main;

public class Unit {
	private static final Unit instance = new Unit();

	private Unit() {}
	
	public static final Unit unit() {
		return instance;
	}

}

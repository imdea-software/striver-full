package adts;

public class Pair {

	private Object leftval;
	private Object rightval;

	public Pair(Object leftval, Object rightval) {
		this.leftval = leftval;
		this.rightval = rightval;
	}

	public Object left() {
		return leftval;
	}

}

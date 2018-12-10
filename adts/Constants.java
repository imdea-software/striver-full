package adts;

public class Constants {

	private static class NoTickT {
		private NoTickT() {}
		public static final NoTickT notick = new NoTickT();
	}

    public static Object notick() {
        return NoTickT.notick;
    }

	public static boolean isnotick(Object o) {
		return (o.getClass() == NoTickT.class);
	}

	public static String toString(Object o) {
		if (isnotick(o)) {
			return "notick";
		}
		return o.toString();
	}

	public static final double EPS = 0.5d;
	public static final double INFTY = Double.MAX_VALUE;

}

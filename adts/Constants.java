package adts;

public class Constants {

	public enum OutsideType {
		negoutside,
		posoutside,
		inside
	}
	
	public static final double INFTY = Double.MAX_VALUE;
	private static Object notick = new Object() {
		public String toString() {
			return "notick";
		}
	};
	private static Object positiveOutside = new Object() {
		public String toString() {
			return "+outside";
		}
	};
	private static Object negativeOutside = new Object() {
		public String toString() {
			return "-outside";
		}
	};

    public static Object notick() {
        return notick;
    }

    public static Object posoutside() {
        return positiveOutside;
    }

	public static Object negoutside() {
        return negativeOutside;
	}

	public static boolean isnotick(Object o) {
		return (o == notick);
	}

	public static OutsideType getOutsideType(Object o) {
		if (o == negativeOutside) {
			return OutsideType.negoutside;
		}
		if (o == positiveOutside) {
			return OutsideType.posoutside;
		}
		return OutsideType.inside;
	}

	private Constants() {}

}

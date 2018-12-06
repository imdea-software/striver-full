package adts;
public final class MaybeOutside<T> {
	public enum Valtype {
		negoutside,
		posoutside,
		inside
	}
	private final Valtype valtype;
    private final T value;
    private static final MaybeOutside<?> negoutside = new MaybeOutside(null, Valtype.negoutside);
    private static final MaybeOutside<?> posoutside = new MaybeOutside(null, Valtype.posoutside);

    public static <T> MaybeOutside<T> of(T value) {
        return new MaybeOutside<T>(value, Valtype.inside);
    }

    public static <T> MaybeOutside<T> negoutside() {
			return (MaybeOutside<T>) negoutside;
    }

    public static <T> MaybeOutside<T> posoutside() {
			return (MaybeOutside<T>) posoutside;
    }

    private MaybeOutside(T value, Valtype type) {
        this.value = value;
        this.valtype = type;
    }

    public T get() {
        return value;
    }
    
    public Valtype getType() {
    		return valtype;
    }
    
    public String toString () {
		switch (valtype) {
		case inside:
			return value.toString();
		case negoutside:
			return "-outside";
		case posoutside:
			return "+outside";
		}
		return null; // stupid compiler
    }

}
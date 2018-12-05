package adts;
public final class MaybeOutside<T> {
    public static <T> MaybeOutside<T> of(T value) {
        if (value == null) {
            return MaybeOutside.outside();
        }

        return new MaybeOutside<T>(value);
    }

    public static <T> MaybeOutside<T> outside() {
        return new MaybeOutside<T>(null);
    }

    private final T value;

    private MaybeOutside(T value) {
        this.value = value;
    }

    public boolean isPresent() {
        return value != null;
    }

    public T get() {
        return value;
    }
    
    public String toString () {
    		return isPresent()?value.toString():"outside";
    }
}
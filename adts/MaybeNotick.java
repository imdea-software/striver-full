package adts;

public class MaybeNotick<T> {
	
    public static <T> MaybeNotick<T> of(T value) {
        if (value == null) {
            return MaybeNotick.notick();
        }

        return new MaybeNotick<T>(value);
    }

    public static <T> MaybeNotick<T> notick() {
        return new MaybeNotick<T>(null);
    }

    private final T value;

    private MaybeNotick(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

	public boolean isnotick() {
		return this.value==null;
	}

	public String toString() {
		if (isnotick()) {
			return "notick";
		}
		return value.toString();
	}

}

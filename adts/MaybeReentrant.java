package adts;

public class MaybeReentrant<T> {
	
    public static <T> MaybeReentrant<T> of(StriverEvent<T> value) {
        if (value == null) {
            return MaybeReentrant.reentrantevent();
        }

        return new MaybeReentrant<T>(value);
    }

    public static <T> MaybeReentrant<T> reentrantevent() {
        return new MaybeReentrant<T>(null);
    }

    private final StriverEvent<T> value;

    private MaybeReentrant(StriverEvent<T> value) {
        this.value = value;
    }

    public StriverEvent<T> getEvent() {
        return value;
    }

	public boolean isreentrant() {
		return this.value==null;
	}
	
	public String toString() {
		if (isreentrant()) {
			return "reentrant";
		}
		return value.toString();
	}


}

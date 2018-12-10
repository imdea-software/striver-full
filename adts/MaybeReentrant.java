package adts;

public class MaybeReentrant {
	
    public static  MaybeReentrant of(StriverEvent value) {
        if (value == null) {
            return MaybeReentrant.reentrantevent();
        }

        return new MaybeReentrant(value);
    }

    public static  MaybeReentrant reentrantevent() {
        return new MaybeReentrant(null);
    }

    private final StriverEvent value;

    private MaybeReentrant(StriverEvent value) {
        this.value = value;
    }

    public StriverEvent getEvent() {
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

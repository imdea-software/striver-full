package adts;

import java.util.Optional;

public class StriverEvent<T> {
	
	public static final StriverEvent<?> posOutsideEv = new StriverEvent(Constants.INFTY, MaybeNotick.notick());
	private double ts;
	private MaybeNotick<T> value;

	public StriverEvent(double nt, MaybeNotick<T> object) {
		this.ts = nt;
		this.value = object;
	}

	public double getTS() {
		return ts;
	}
	
	public MaybeNotick<T> getValue() {
		return value;
	}
	
	public boolean isnotick() {
		return value.isnotick();
	}
	
	public String toString() {
		return "("+this.ts+", "+this.value+")";
	}

}

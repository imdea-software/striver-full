package adts;

import java.util.Optional;

public class StriverEvent {
	
	public static final StriverEvent outsideEv = new StriverEvent(Constants.INFTY, Optional.empty());
	private double ts;
	private Optional<Object> value;

	public StriverEvent(double nt, Optional<Object> object) {
		this.ts = nt;
		this.value = object;
	}

	public double getTS() {
		return ts;
	}
	
	public Optional<Object> getValue() {
		return value;
	}
	
	public boolean isnotick() {
		return !value.isPresent();
	}
	
	public String toString() {
		return "("+this.ts+", "+this.value+")";
	}

}

package adts;

import java.util.Optional;

public class StriverEvent {
	
	public static final StriverEvent outsideEv = new StriverEvent(Integer.MAX_VALUE, Optional.empty());
	private int ts;
	private Optional<Object> value;

	public StriverEvent(int ts, Optional<Object> object) {
		this.ts = ts;
		this.value = object;
	}

	public int getTS() {
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

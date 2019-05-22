package adts;

public class StriverEvent {
	
	public static final StriverEvent posOutsideEv = new StriverEvent(null,Constants.INFTY, Constants.notick());
	private double ts;
	private Object value;
	private String streamName;

	public StriverEvent(String streamName, double nt, Object object) {
		this.ts = nt;
		this.value = object;
		this.streamName = streamName;
	}

	public double getTS() {
		return ts;
	}
	
	public Object getValue() {
		return value;
	}
	
	public boolean isnotick() {
		return Constants.isnotick(value);
	}
	
	public String toString() {
		return "("+this.ts+", "+this.value+")";
	}

}

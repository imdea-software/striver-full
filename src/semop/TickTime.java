package semop;

public class TickTime {
	private double time;
	private Object carriedValue=null;
	
	public TickTime(double d) {
		this.time=d;
	}

	public TickTime(double d, Object value) {
		this.time=d;
		this.carriedValue = value;
	}
	
	public Object getCV() {
		return this.carriedValue;
	}

	public double getTS() {
		return time;
	}

}
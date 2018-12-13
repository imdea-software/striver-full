package semop;

public class TickTime {
	public double time;
	public boolean isnotick;
	private Object carriedValue=null;
	
	public TickTime(double d, boolean isnotick) {
		this.time = d;
		this.isnotick = isnotick;
	}

	public TickTime(double d, boolean b, Object value) {
		this.time=d;
		this.isnotick = b;
		this.carriedValue = value;
	}
	
	public Object getCV() {
		return this.carriedValue;
	}
	
}
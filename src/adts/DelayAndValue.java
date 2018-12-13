package adts;

public class DelayAndValue<T> {

	private T value;

	private double delay;

	public T getValue() {
		return this.value;
	}

	public double getDelay() {
		return this.delay;
	}

	public DelayAndValue(double delay, T value) {
		this.value = value;
		this.delay = delay;
	}
	
	public String toString() {
		return "(" + this.delay + ", " + this.value + ")";
	}

}

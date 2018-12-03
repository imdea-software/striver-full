package adts;

public class ExtEvent {
	public enum ExtEvType {
		reentrant,
		real
	}
	public static final ExtEvent reentrantevent = new ExtEvent();

	public static final ExtEvent outsideEv = new ExtEvent(StriverEvent.outsideEv);
	private ExtEvType myType=ExtEvType.real;
	private StriverEvent ev;
	
	private ExtEvent() {
		this.myType = ExtEvType.reentrant;
	}
	
	public ExtEvent(StriverEvent ev2) {
		this.ev = ev2;
	}

	public ExtEvType getType() {
		return myType;
	}

	public StriverEvent getEvent() {
		return ev;
	}
	
	public String toString() {
		return ev.toString();
	}

}

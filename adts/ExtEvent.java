package adts;

public class ExtEvent {
	private enum ExtEvType {
		reentrant,
		nonreentrant
	}
	public static final ExtEvent reentrantevent = new ExtEvent();

	public static final ExtEvent outsideEv = new ExtEvent(StriverEvent.outsideEv);
	private ExtEvType myType=ExtEvType.nonreentrant;
	private StriverEvent ev;
	
	private ExtEvent() {
		this.myType = ExtEvType.reentrant;
	}
	
	public ExtEvent(StriverEvent ev2) {
		this.ev = ev2;
	}

	public boolean isreentrant() {
		return myType == ExtEvType.reentrant;
	}

	public StriverEvent getEvent() {
		return ev;
	}
	
	public String toString() {
		return ev.toString();
	}

}

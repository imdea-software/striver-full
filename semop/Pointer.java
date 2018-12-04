package semop;

import adts.ExtEvent;

public class Pointer {
	private int myPos = -1;
	// init these:
	private Table t;
	private String myStreamId;
	
	public Pointer(Table t, String streamid) {
		this.t=t;
		this.myStreamId=streamid;
	}
	
	public ExtEvent pull() {
		if (myPos == Integer.MAX_VALUE) {
			return ExtEvent.outsideEv;
		}
		
		ExtEvent ev = t.getNext(myStreamId, myPos);
		if (ev.isreentrant()) {
			return ev;
		}
		myPos = ev.getEvent().getTS();
		return ev;
	}
	

}

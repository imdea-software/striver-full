package semop;

import adts.Constants;
import adts.MaybeReentrant;
import adts.StriverEvent;

public class Pointer {
	private double myPos = -1;
	// init these:
	private Table t;
	private String myStreamId;
	
	public Pointer(Table t, String streamid) {
		this.t=t;
		this.myStreamId=streamid;
	}
	
	public MaybeReentrant pull() {
		if (myPos == Constants.INFTY) {
			return MaybeReentrant.of(StriverEvent.posOutsideEv);
		}
		MaybeReentrant ev = t.getNext(myStreamId, myPos);
		if (!ev.isreentrant()) {
			myPos = ev.getEvent().getTS();
		}
		return ev;
	}

	public String getStreamId() {
		return this.myStreamId;
	}

	public void sendForward() {
		this.myPos = Constants.INFTY;
	}
	

}

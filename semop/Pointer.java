package semop;

import java.util.Iterator;

import adts.Constants;
import adts.MaybeReentrant;
import adts.StriverEvent;

public class Pointer {
	private double myPos = -1;
	// init these:
	private Table t;
	private String myStreamId;
	private Iterator<StriverEvent> myIterator;
	
	public Pointer(Table t, String streamid, Iterator<StriverEvent> iterator) {
		this.t=t;
		this.myStreamId=streamid;
		this.myIterator = iterator;
	}
	
	public MaybeReentrant pull() {
		if (myPos == Constants.INFTY) {
			return MaybeReentrant.of(StriverEvent.posOutsideEv);
		}
		MaybeReentrant ev = null;
		if (myIterator.hasNext()) {
			ev = MaybeReentrant.of(myIterator.next());
			if (ev.getEvent().getTS() <= myPos) {
				int i=0;
			}
		} else {
			ev = t.getNext(myStreamId);
			if (!ev.isreentrant())
				myIterator.next();
		}
		if (!ev.isreentrant()) {
			myPos = ev.getEvent().getTS();
		}
		return ev;
	}

	public String getStreamId() {
		return this.myStreamId;
	}

	public void sendForward() {
		// TODO unhook iterator
		this.myPos = Constants.INFTY;
	}
	

}

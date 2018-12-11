package semop;

import java.util.Iterator;

import adts.Constants;
import adts.MaybeReentrant;
import adts.StriverEvent;
import adts.GCList.GCIterator;

public class Pointer {
	private boolean outsidepos = false;
	// init these:
	private Table t;
	private String myStreamId;
	// public for debug:
	public GCIterator<StriverEvent> myIterator;
	
	public Pointer(Table t, String streamid, GCIterator<StriverEvent> iterator) {
		this.t=t;
		this.myStreamId=streamid;
		this.myIterator = iterator;
	}
	
	public MaybeReentrant pull() {
		if (outsidepos) {
			return MaybeReentrant.of(StriverEvent.posOutsideEv);
		}
		StriverEvent ev;
		if (!myIterator.hasNext()) {
			boolean isreentrant = t.getNext(myStreamId);
			if (isreentrant) {
				return MaybeReentrant.reentrantevent();
			}
		}
		ev = myIterator.next();
		if (ev.getTS() == Constants.INFTY)
			sendForward();
		return MaybeReentrant.of(ev);
	}

	public String getStreamId() {
		return this.myStreamId;
	}

	public void sendForward() {
		myIterator.unhook();
		this.outsidepos = true;
	}
	

}

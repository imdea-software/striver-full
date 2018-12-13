package semop;

import adts.Constants;
import adts.MaybeReentrant;
import adts.StriverEvent;
import adts.GCList.GCIterator;

public class Pointer {
	private MaybeReentrant quickreturn=null;
	// init these:
	private Table t;
	private String myStreamId;
	// public for debug:
	public GCIterator<StriverEvent> myIterator;
	// exists for debug:
	public double lastpos = -1d;
	
	public Pointer(Table t, String streamid, GCIterator<StriverEvent> iterator) {
		this.t=t;
		this.myStreamId=streamid;
		this.myIterator = iterator;
	}
	
	public MaybeReentrant pull() {
		if (quickreturn != null) {
			return quickreturn;
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
		this.lastpos = ev.getTS();
		return MaybeReentrant.of(ev);
	}

	public String getStreamId() {
		return this.myStreamId;
	}

	public void sendForward() {
		myIterator.unhook();
		this.quickreturn = MaybeReentrant.of(StriverEvent.posOutsideEv);
		this.lastpos = Constants.INFTY;
	}
	

}

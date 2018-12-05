package spec.tickexp;

import adts.ExtEvent;
import semop.Pointer;
import semop.TickTime;

public class AtTickExpr implements ITickExpr {

	private Integer sum=Integer.MAX_VALUE;
	// init this
	private Pointer mypointer;

	@Override
	public TickTime calculateNextTime() {
		ExtEvent ev = mypointer.pull();
		TickTime ret=null;
		int evts = ev.isreentrant()?Integer.MAX_VALUE:ev.getEvent().getTS();
		if (sum <= evts) {
			// alarm goes off
			ret = new TickTime(sum, false);
			sum = Integer.MAX_VALUE;
		}
		// maybe update alarm with pulled event
		if (evts < Integer.MAX_VALUE) {
			sum = evts + (Integer) ev.getEvent().getValue().get();
		}
		if (ret==null) { // Alarm didn't go off
			ret = new TickTime(evts, true);
		}
		return ret;
	}

	public AtTickExpr(Pointer p) {
		this.mypointer = p;
	}

}

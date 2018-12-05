package spec.tickexp;

import adts.Constants;
import adts.ExtEvent;
import semop.Pointer;
import semop.TickTime;

public class AtNegTickExpr implements ITickExpr {

	private double limit = 0;
	// init this
	private Pointer mypointer;

	@Override
	public TickTime calculateNextTime() {
		// WARNING: we assume that an event will be generated at some point
		ExtEvent ev = null;
		double evts;
		do {
			ev = mypointer.pull();
			assert (!ev.isreentrant()); // no positive cycles allowed
			evts = ev.getEvent().getTS();
		} while (evts != Constants.INFTY && ev.getEvent().isnotick());
		
		if (evts == Constants.INFTY) {
			return new TickTime(evts, true);
		}
		double sum = evts + (double) ev.getEvent().getValue().get();
		TickTime ret;
		if (sum>=limit) {
			ret = new TickTime(sum, false);
		} else {
			ret = new TickTime(evts, true);
		}
		limit = evts;
		return ret;
	}

	public AtNegTickExpr(Pointer p) {
		this.mypointer = p;
	}

}

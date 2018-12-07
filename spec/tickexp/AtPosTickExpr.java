package spec.tickexp;

import adts.Constants;
import adts.MaybeReentrant;
import semop.Pointer;
import semop.TickTime;

public class AtPosTickExpr implements ITickExpr {

	private double sum=Constants.INFTY;
	// init this
	private Pointer mypointer;

	@Override
	public TickTime calculateNextTime() {
		MaybeReentrant ev = mypointer.pull();
		TickTime ret=null;
		double evts = ev.isreentrant()?Constants.INFTY:ev.getEvent().getTS();
		if (sum <= evts) {
			// alarm goes off
			ret = new TickTime(sum, false);
			sum = Constants.INFTY;
		}
		// maybe update alarm with pulled event
		if (evts < Constants.INFTY && !ev.getEvent().isnotick()) {
			sum = evts + (double) ev.getEvent().getValue().getValue();
		}
		if (ret==null) { // Alarm didn't go off
			ret = new TickTime(evts, true);
		}
		return ret;
	}

	public AtPosTickExpr(Pointer p) {
		this.mypointer = p;
	}

}

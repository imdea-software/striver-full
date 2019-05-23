package spec.tickexp.nodelayTE;

import adts.Constants;
import adts.MaybeReentrant;
import adts.Unit;
import semop.Pointer;
import semop.TickTime;
import spec.tickexp.ITickExpr;

public class DelayPosTickExpr implements ITickExpr {

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
			ret = new TickTime(sum, Unit.unit());
			sum = Constants.INFTY;
		}
		// maybe update alarm with pulled event
		if (evts < Constants.INFTY && !ev.getEvent().isnotick()) {
			sum = evts + (double) ev.getEvent().getValue();
		}
		if (ret==null) { // Alarm didn't go off
			ret = new TickTime(evts, Constants.notick());
		}
		return ret;
	}

	public DelayPosTickExpr(Pointer p) {
		this.mypointer = p;
	}

	@Override
	public void unhookPointers() {
		mypointer.sendForward();
	}

}

package spec.tickexp;

import adts.ExtEvent;
import semop.Pointer;
import semop.TickTime;

public class AtTickExpr implements ITickExpr {

	private Integer sum=null;
	// init this
	private Pointer mypointer;

	@Override
	public TickTime calculateNextTime() {
		ExtEvent ev = mypointer.pull();
		int evts = Integer.MAX_VALUE;
		if (sum==null) {
			if (ev.isreentrant()) {
				return new TickTime(Integer.MAX_VALUE, true);
			}
			evts = ev.getEvent().getTS();
			if (ev.getEvent().isnotick()) {
				return new TickTime(evts, true);
			}
			sum = evts + (Integer) ev.getEvent().getValue().get();
			return calculateNextTime();
		}
		if (ev.isreentrant() || sum <= ev.getEvent().getTS()) {
			TickTime tt = new TickTime(sum, false);
			sum=null;
			return tt;
		}
		if (!ev.getEvent().isnotick()) {
			sum = ev.getEvent().getTS() + (Integer) ev.getEvent().getValue().get();
		}
		return new TickTime(evts, true);
	}

	public AtTickExpr(Pointer p) {
		this.mypointer = p;
	}

}

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
		if (sum==null) {
			if (ev.isreentrant()) {
				return new TickTime(Integer.MAX_VALUE, true);
			}
			if (ev.getEvent().isnotick()) {
				return new TickTime(ev.getEvent().getTS(), true);
			}
			updateSum(ev);
			return calculateNextTime();
		}
		if (ev.isreentrant() || sum <= ev.getEvent().getTS()) {
			TickTime tt = new TickTime(sum, false);
			sum=null;
			return tt;
		}
		if (!ev.getEvent().isnotick()) {
			updateSum(ev);
		}
		return new TickTime(ev.getEvent().getTS(), true);
	}

	private void updateSum(ExtEvent ev) {
		int evts = ev.getEvent().getTS();
		sum = evts + (Integer) ev.getEvent().getValue().get();
	}

	public AtTickExpr(Pointer p) {
		this.mypointer = p;
	}

}

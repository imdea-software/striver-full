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
		if (!ev.isreentrant()) {
			evts = ev.getEvent().getTS();
		}
		if (sum!=null && sum <= evts) {
			TickTime tt = new TickTime(sum, false);
			sum=null;
			return tt;
		}
		if (evts<Integer.MAX_VALUE && !ev.getEvent().isnotick()) {
			sum = ev.getEvent().getTS() + (Integer) ev.getEvent().getValue().get();
		}
		return new TickTime(evts, true);
	}

	public AtTickExpr(Pointer p) {
		this.mypointer = p;
	}

}

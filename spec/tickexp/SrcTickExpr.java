package spec.tickexp;

import adts.ExtEvent;
import semop.Pointer;
import semop.TickTime;

public class SrcTickExpr implements ITickExpr {
	
	// init this
	private Pointer mypointer;

	@Override
	public TickTime calculateNextTime() {
		//assert(mypointer.myPos <= lastpos);
		ExtEvent ev = mypointer.pull();
		assert !ev.isreentrant();
		double ts = ev.getEvent().getTS();
		return new TickTime(ts,!ev.getEvent().getValue().isPresent());
	}
	
	public SrcTickExpr(Pointer p) {
		this.mypointer = p;
	}

}

package spec.tickexp.nodelayTE;

import adts.MaybeReentrant;
import semop.Pointer;
import semop.TickTime;
import spec.tickexp.ITickExpr;

public class SrcTickExpr implements ITickExpr {
	
	// init this
	private Pointer mypointer;

	@Override
	public TickTime calculateNextTime() {
		//assert(mypointer.myPos <= lastpos);
		MaybeReentrant ev = mypointer.pull();
		assert !ev.isreentrant();
		double ts = ev.getEvent().getTS();
		return new TickTime(ts,ev.getEvent().getValue());
	}
	
	public SrcTickExpr(Pointer p) {
		this.mypointer = p;
	}

	@Override
	public void unhookPointers() {
		mypointer.sendForward();
	}

}

package spec.tickexp;

import adts.Constants;
import adts.DelayAndValue;
import adts.MaybeReentrant;
import semop.Pointer;
import semop.TickTime;

public class ShiftTickExpr implements ITickExpr {
	
	// init this
	private Pointer mypointer;
	private double c;

	@Override
	public TickTime calculateNextTime() {
		// FIXME: This implementation assumes a monotonically increasing t+fst(s(~t))
		MaybeReentrant ev = mypointer.pull();
		// FIXME: It could be reentrant
		assert !ev.isreentrant();
		double ts = ev.getEvent().getTS();
		if (Constants.isnotick(ev.getEvent().getValue())) {
			return new TickTime(ts,Constants.notick());
		}
		return new TickTime(ts + this.c, ev.getEvent().getValue());
	}
	
	public ShiftTickExpr(Pointer p, double c) {
		this.mypointer = p;
		this.c = c;
	}

	@Override
	public void unhookPointers() {
		mypointer.sendForward();
	}

}

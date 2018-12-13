package spec.tickexp;

import adts.Constants;
import adts.DelayAndValue;
import adts.MaybeReentrant;
import semop.Pointer;
import semop.TickTime;

public class DelayTickExpr implements ITickExpr {
	
	// init this
	private Pointer mypointer;

	@Override
	public TickTime calculateNextTime() {
		// FIXME: This implementation assumes a monotonically increasing t+fst(s(~t))
		MaybeReentrant ev = mypointer.pull();
		assert !ev.isreentrant();
		double ts = ev.getEvent().getTS();
		if (Constants.isnotick(ev.getEvent().getValue())) {
			return new TickTime(ts,true, null);
		}
		DelayAndValue dav = (DelayAndValue) ev.getEvent().getValue();
		return new TickTime(ts + dav.getDelay(), false, dav.getValue());
	}
	
	public DelayTickExpr(Pointer p) {
		this.mypointer = p;
	}

	@Override
	public void unhookPointers() {
		mypointer.sendForward();
	}

}

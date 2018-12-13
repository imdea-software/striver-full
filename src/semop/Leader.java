package semop;

import adts.Constants;
import adts.StriverEvent;
import spec.StriverSpec;
import spec.tickexp.ITickExpr;
import spec.valueexp.IValExpr;

public class Leader<T> implements ILeader<T> {
	
	// init these
	private ITickExpr myTickExpr;
	private IValExpr<T> myValExpr;
	
	public Leader(StriverSpec s) {
		this.myTickExpr = s.getTickExpr();
		this.myValExpr = s.getValExpr();
	}

	public StriverEvent getNext() {
		TickTime tickTime = myTickExpr.calculateNextTime();
		double nt = tickTime.time;
		if (nt == Constants.INFTY) {
			myTickExpr.unhookPointers();
			myValExpr.unhookPointers();
			return StriverEvent.posOutsideEv;
		}
		if (tickTime.isnotick) {
			return new StriverEvent(nt, Constants.notick());
		}
		Object val = myValExpr.calculateValueAt(nt,tickTime.getCV());
		return new StriverEvent(nt, val);
	}

}

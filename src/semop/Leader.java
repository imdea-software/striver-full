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
	private String name;
	
	public Leader(StriverSpec s, String name) {
		this.myTickExpr = s.getTickExpr();
		this.myValExpr = s.getValExpr();
		this.name=name;
	}

	public StriverEvent getNext() {
		TickTime tickTime = myTickExpr.calculateNextTime();
		double nt = tickTime.getTS();
		if (nt == Constants.INFTY) {
			myTickExpr.unhookPointers();
			myValExpr.unhookPointers();
			return StriverEvent.posOutsideEv;
		}
		if (Constants.isnotick(tickTime.getCV())) {
			return new StriverEvent(name, nt, Constants.notick());
		}
		Object val = myValExpr.calculateValueAt(nt,tickTime.getCV());
		return new StriverEvent(name, nt, val);
	}

	@Override
	public String getStreamName() {
		return name;
	}

}

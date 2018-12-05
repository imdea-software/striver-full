package semop;

import java.util.Optional;

import adts.Constants;
import adts.StriverEvent;
import spec.StriverSpec;
import spec.tickexp.ITickExpr;
import spec.valueexp.IValExpr;

public class Leader {
	
	// init these
	private ITickExpr myTickExpr;
	private IValExpr myValExpr;
	
	public Leader(StriverSpec s) {
		this.myTickExpr = s.getTickExpr();
		this.myValExpr = s.getValExpr();
	}

	public StriverEvent getNext() {
		TickTime tickTime = myTickExpr.calculateNextTime();
		double nt = tickTime.time;
		if (nt == Constants.INFTY) {
			return StriverEvent.outsideEv;
		}
		if (tickTime.isnotick) {
			return new StriverEvent(nt, Optional.empty());
		}
		Optional<Object> val = myValExpr.calculateValueAt(nt);
		return new StriverEvent(nt, val);
	}

}

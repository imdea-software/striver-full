package spec.tickexp;

import adts.Constants;
import semop.TickTime;

public class UnionTickExpr implements ITickExpr {
	
	private TickTime leftTickTime=null, rightTickTime=null;
	private double lastts = -1;
	//init these
	private ITickExpr leftExpr, rightExpr;

	@Override
	public TickTime calculateNextTime() {
		double retdouble=0;
		boolean retbool=true;
		leftTickTime = getNext(leftTickTime, leftExpr);
		rightTickTime = getNext(rightTickTime, rightExpr);
		double leftt = leftTickTime.time;
		double rightt = rightTickTime.time;
		if (leftt <= rightt) {
			retdouble = leftt;
			retbool = leftTickTime.isnotick;
			leftTickTime = null;
		}
		if (rightt <= leftt) {
			retdouble = rightt;
			retbool = retbool && rightTickTime.isnotick;
			rightTickTime = null;
		}
		if (rightt == Constants.INFTY) {
			rightTickTime = null;
		}
		if (leftt == Constants.INFTY) {
			leftTickTime = null;
		}
		assert (retdouble > lastts || lastts == Constants.INFTY);
		lastts = retdouble;
		return new TickTime(retdouble, retbool);
	}
	
	private TickTime getNext(TickTime tickTime, ITickExpr tickExpr) {
		if (tickTime == null) {
			tickTime = tickExpr.calculateNextTime();
			if (tickTime.time == lastts) {
				tickTime = tickExpr.calculateNextTime();
			}
		}
		return tickTime;
	}

	public UnionTickExpr(ITickExpr leftExpr, ITickExpr rightExpr) {
		this.leftExpr = leftExpr;
		this.rightExpr = rightExpr;
	}

	@Override
	public void unhookPointers() {
		leftExpr.unhookPointers();
		rightExpr.unhookPointers();
	}
}

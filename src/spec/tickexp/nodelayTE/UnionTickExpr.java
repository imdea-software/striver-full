package spec.tickexp.nodelayTE;

import adts.Constants;
import semop.TickTime;
import spec.tickexp.ITickExpr;

public class UnionTickExpr implements ITickExpr {
	
	private TickTime leftTickTime=null, rightTickTime=null;
	private double lastts = -1;
	//init these
	private ITickExpr leftExpr, rightExpr;

	@Override
	public TickTime calculateNextTime() {
		double retdouble=0;
		boolean retnotick=true;
		leftTickTime = getNext(leftTickTime, leftExpr);
		rightTickTime = getNext(rightTickTime, rightExpr);
		double leftt = leftTickTime.getTS();
		double rightt = rightTickTime.getTS();
		if (leftt <= rightt) {
			retdouble = leftt;
			retnotick = Constants.isnotick(leftTickTime.getCV());
			leftTickTime = null;
		}
		if (rightt <= leftt) {
			retdouble = rightt;
			retnotick = retnotick && Constants.isnotick(rightTickTime.getCV());
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
		return new TickTime(retdouble, retnotick?Constants.notick():null);
	}
	
	private TickTime getNext(TickTime tickTime, ITickExpr tickExpr) {
		if (tickTime == null || tickTime.getTS() == lastts) {
			tickTime = tickExpr.calculateNextTime();
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

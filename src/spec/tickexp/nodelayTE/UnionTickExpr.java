package spec.tickexp.nodelayTE;

import adts.Constants;
import adts.Pair;
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
		leftTickTime = getNext(leftTickTime, leftExpr);
		rightTickTime = getNext(rightTickTime, rightExpr);
		double leftt = leftTickTime.getTS();
		double rightt = rightTickTime.getTS();
		Object leftval=Constants.notick(), rightval=Constants.notick();
		if (leftt <= rightt) {
			retdouble = leftt;
			leftval = leftTickTime.getCV();
			leftTickTime = null;
		}
		if (rightt <= leftt) {
			retdouble = rightt;
			rightval = rightTickTime.getCV();
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
		Object retval = Constants.isnotick(rightval) && Constants.isnotick(leftval) ? Constants.notick() : new Pair(leftval, rightval);
		return new TickTime(retdouble, retval);
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

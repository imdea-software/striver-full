package spec.tickexp;

import semop.TickTime;

public class ConstExpr implements ITickExpr {
	// init this:
	private int myVal;

	public ConstExpr(int val) {
		myVal=val;
	}

	@Override
	public TickTime calculateNextTime(int lastpos) {
		if (myVal > lastpos) {
			return new TickTime(myVal, false);
		}
		return new TickTime(Integer.MAX_VALUE, false);
	}
	

}

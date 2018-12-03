package spec.tickexp;

import semop.TickTime;

public class ConstTickExpr implements ITickExpr {
	// init this:
	private int myVal;

	public ConstTickExpr(int val) {
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

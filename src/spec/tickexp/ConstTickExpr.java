package spec.tickexp;

import adts.Constants;
import semop.TickTime;

public class ConstTickExpr implements ITickExpr {
	private boolean given=false;
	// init this:
	private int myVal;

	public ConstTickExpr(int val) {
		myVal=val;
	}

	@Override
	public TickTime calculateNextTime() {
		if (!given) {
			given=true;
			return new TickTime(myVal, false);
		}
		return new TickTime(Constants.INFTY, false);
	}

	@Override
	public void unhookPointers() {
	}
	

}

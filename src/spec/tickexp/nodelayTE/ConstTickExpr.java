package spec.tickexp.nodelayTE;

import adts.Constants;
import semop.TickTime;

public class ConstTickExpr implements INDTickExpr {
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
			return new TickTime(myVal);
		}
		return new TickTime(Constants.INFTY);
	}

	@Override
	public void unhookPointers() {
	}
	

}

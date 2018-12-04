package spec.tickexp;

import semop.TickTime;

public class UnionTickExpr implements ITickExpr {
	
	private TickTime leftTickTime=null, rightTickTime=null;
	//init these
	private ITickExpr leftExpr, rightExpr;

	@Override
	public TickTime calculateNextTime() {
		int retint=0;
		boolean retbool=true;
		if (leftTickTime == null) {
			leftTickTime = leftExpr.calculateNextTime();
		}
		if (rightTickTime == null) {
			rightTickTime = rightExpr.calculateNextTime();
		}
		int leftt = leftTickTime.time;
		int rightt = rightTickTime.time;
		if (leftt <= rightt) {
			retint = leftt;
			retbool = leftTickTime.isnotick;
			leftTickTime = null;
		}
		if (rightt <= leftt) {
			retint = rightt;
			retbool = retbool && rightTickTime.isnotick;
			rightTickTime = null;
		}
		if (rightt == Integer.MAX_VALUE) {
			rightTickTime = null;
		}
		if (leftt == Integer.MAX_VALUE) {
			leftTickTime = null;
		}
		return new TickTime(retint, retbool);
	}
	
	public UnionTickExpr(ITickExpr leftExpr, ITickExpr rightExpr) {
		this.leftExpr = leftExpr;
		this.rightExpr = rightExpr;
	}
}

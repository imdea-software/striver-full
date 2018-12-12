package spec;

import spec.tickexp.ITickExpr;
import spec.valueexp.IValExpr;

public class StriverSpec {
	
	// init these
	private ITickExpr myTickExpr;
	private IValExpr myValExpr;
	
	public StriverSpec(ITickExpr te, IValExpr ve) {
		this.myTickExpr = te;
		this.myValExpr = ve;
	}

	public IValExpr getValExpr() {
		return myValExpr;
	}

	public ITickExpr getTickExpr() {
		return myTickExpr;
	}

}

package spec;

import spec.tickexp.ITickExpr;
import spec.valueexp.IValExpr;

public class StriverSpec {
	
	// init these
	private ITickExpr myTickExpr;
	private IValExpr myValExpr;
	private String streamid;
	
	public StriverSpec(ITickExpr te, IValExpr ve, String streamid) {
		this.myTickExpr = te;
		this.myValExpr = ve;
		this.streamid = streamid;
	}

	public IValExpr getValExpr() {
		return myValExpr;
	}

	public ITickExpr getTickExpr() {
		return myTickExpr;
	}

}

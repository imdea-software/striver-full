package spec.tickexp;

import adts.ExtEvent;
import semop.Pointer;
import semop.TickTime;

public class SrcTickExpr implements ITickExpr {
	
	// init this
	private Pointer mypointer;

	@Override
	public TickTime calculateNextTime(int lastpos) {
		//assert(mypointer.myPos <= lastpos);
		ExtEvent ev = mypointer.pull();
		assert ev.getType() != ExtEvent.ExtEvType.reentrant;
		int ts = ev.getEvent().getTS();
		assert ts > lastpos;
		return new TickTime(ts,!ev.getEvent().getValue().isPresent());
	}
	
	public SrcTickExpr(Pointer p) {
		this.mypointer = p;
	}

}

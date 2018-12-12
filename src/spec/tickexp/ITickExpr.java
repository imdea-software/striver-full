package spec.tickexp;

import semop.TickTime;

public interface ITickExpr {

	TickTime calculateNextTime();

	void unhookPointers();

}

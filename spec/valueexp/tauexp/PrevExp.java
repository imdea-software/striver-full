package spec.valueexp.tauexp;

import adts.ExtEvent;
import adts.MaybeOutside;
import semop.Pointer;

public class PrevExp extends ITauExp {
	
	private MaybeOutside<Double> lastret = MaybeOutside.outside();
	private Double headt=-1d;
	private boolean headisreal=false;
	// init these
	private Pointer myPointer;
	private ITauExp innertau;

	@Override
	public MaybeOutside<Double> getT(double t) {
		MaybeOutside<Double> mt = innertau.getT(t);
		if (!mt.isPresent()) {
			return mt;
		}
		t = mt.get();
		while (headt<t) {
			if (headisreal) {
				lastret = MaybeOutside.of(headt);
			}
			ExtEvent extev = myPointer.pull();
			if (extev.isreentrant()) {
				return lastret;
			}
			headt = extev.getEvent().getTS();
			headisreal = !extev.getEvent().isnotick();
		}
		return lastret;
	}
	
	public PrevExp(Pointer p, ITauExp it) {
		this.myPointer = p;
		this.innertau = it;
	}

}

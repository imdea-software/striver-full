package spec.valueexp.generics;

import adts.Constants;
import adts.MaybeReentrant;
import adts.StriverEvent;
import semop.Pointer;
import spec.valueexp.IValExpr;
import spec.valueexp.tauexp.ITauExp;
import spec.valueexp.tauexp.TExpr;

public class SuccBoundedExpr implements IValExpr<Double> {

	private double headt = -Double.MAX_VALUE;
	// init these
	private Pointer myPointer;
	private double bound;
	private Double firstbeyond=null;

	public SuccBoundedExpr(Pointer p, double bound) {
		this.myPointer = p;
		this.bound=bound;
	}

	public void unhookPointers() {
		this.myPointer.sendForward();
	}

	@Override
	public Double calculateValueAt(double t, Object cv) {
		if (headt == Constants.INFTY) {
			return headt;
		}
		int counter = 0;
		if (firstbeyond != null && firstbeyond>t+bound)
			return Constants.INFTY;
		firstbeyond=null;
		while (headt<=t)
		{
			counter++;
			MaybeReentrant ev = myPointer.pull();
			assert !ev.isreentrant();
			StriverEvent strevent = ev.getEvent();
			double evTS = strevent.getTS();
			if (evTS>t+bound&&!false) {
				firstbeyond = evTS;
				return Constants.INFTY;
			}
			if (!strevent.isnotick()) {
				headt = evTS;
			}
			if (strevent.getTS()==Constants.INFTY) {
				headt = Constants.INFTY;
				return Constants.INFTY;
			}
		}
		return headt;
	}
}

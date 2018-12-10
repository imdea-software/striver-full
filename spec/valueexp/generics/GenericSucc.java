package spec.valueexp.generics;

import adts.Constants;
import adts.MaybeReentrant;
import adts.StriverEvent;
import semop.Pointer;
import spec.valueexp.tauexp.ITauExp;

public class GenericSucc<T> {

	private double headt = -2;
	private T headv = null;
	// init these
	private Pointer myPointer;
	private ITauExp innertau;
	private boolean isEq=false;
	private IValExtractor<T> extractor;

	public Object getRes(double t) {
		if (headt == Constants.INFTY) {
			return Constants.posoutside();
		}
		Object mt = innertau.getT(t);
		switch (Constants.getOutsideType(mt)) {
			case inside:
				t = (double) mt;
				break;
			case negoutside:
				t = -1;
				break;
			case posoutside:
				myPointer.sendForward();
				headt = Constants.INFTY;
				return Constants.posoutside();
		}
		while (isEq?headt<t:headt<=t)
		{
			MaybeReentrant ev = myPointer.pull();
			assert !ev.isreentrant();
			StriverEvent strevent = ev.getEvent();
			if (!strevent.isnotick()) {
				headt = strevent.getTS();
				headv = extractor.extractValue(strevent).get();
			}
			if (strevent.getTS()==Constants.INFTY) {
				headt = Constants.INFTY;
				return Constants.posoutside();
			}
		}
		return headv;
	}

	public GenericSucc(Pointer p, ITauExp it, boolean iseq, boolean isval) {
		this.myPointer = p;
		this.innertau = it;
		this.isEq = iseq;
		this.extractor = isval?
				(IValExtractor<T>) IValExtractor.valueExtractor:
				(IValExtractor<T>) IValExtractor.timeExtractor;
	}
}

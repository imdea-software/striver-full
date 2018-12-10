package spec.valueexp.generics;

import adts.Constants;
import adts.MaybeOutside;
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

	public MaybeOutside<T> getRes(double t) {
		if (headt == Constants.INFTY) {
			return MaybeOutside.posoutside();
		}
		MaybeOutside<Double> mt = innertau.getT(t);
		switch (mt.getType()) {
			case inside:
				t = mt.get();
				break;
			case negoutside:
				t = -1;
				break;
			case posoutside:
				myPointer.sendForward();
				headt = Constants.INFTY;
				return MaybeOutside.posoutside();
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
				return MaybeOutside.posoutside();
			}
		}
		return MaybeOutside.of(headv);
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

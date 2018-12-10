package spec.valueexp.generics;

import java.util.Optional;

import adts.Constants;
import adts.MaybeReentrant;
import semop.Pointer;
import spec.valueexp.tauexp.ITauExp;

public class GenericPrev<T> {
	
	private Object lastret = Constants.negoutside();
	private Double headt=-1d;
	private Optional<T> headv = Optional.empty();
	// init these
	private Pointer myPointer;
	private ITauExp innertau;
	private boolean isEq;
	private IValExtractor<T> extractor;

	public Object getRes(double t) {
		Object mt = innertau.getT(t);
		switch (Constants.getOutsideType(mt)) {
			case inside:
				t = (double) mt;
				break;
			case negoutside:
				t = -1;
				break;
			case posoutside:
				t = Constants.INFTY;
		}
		while (headt<t) {
			if (headv.isPresent()) {
				lastret = headv.get();
			}
			MaybeReentrant extev = myPointer.pull();
			if (!isEq && extev.isreentrant()) {
				return lastret;
			}
			headt = extev.getEvent().getTS();
			headv = extractor.extractValue(extev.getEvent());
		}
		if (isEq && headt==t && headv.isPresent()) {
			lastret = headv.get();
		}
		return lastret;
	}
	
	public GenericPrev(Pointer p, ITauExp it, boolean iseq, boolean isval) {
		this.myPointer = p;
		this.innertau = it;
		this.isEq = iseq;
		this.extractor = isval?
				(IValExtractor<T>) IValExtractor.valueExtractor:
				(IValExtractor<T>) IValExtractor.timeExtractor;
	}

}

package spec.valueexp.tauexp;

import adts.ExtEvent;
import adts.MaybeOutside;
import adts.StriverEvent;
import semop.Pointer;

public class GenericPrev<T> {
	
	private interface IValExtractor<R> {
		public MaybeOutside<R> extractValue(StriverEvent striverEvent);
	}
	
	private MaybeOutside<T> lastret = MaybeOutside.outside();
	private Double headt=-1d;
	private MaybeOutside<T> headv = MaybeOutside.outside();
	// init these
	private Pointer myPointer;
	private ITauExp innertau;
	private boolean isEq;
	private IValExtractor<T> extractor;

	public MaybeOutside<T> getRes(double t) {
		MaybeOutside<Double> mt = innertau.getT(t);
		if (!mt.isPresent()) {
			return MaybeOutside.outside();
		}
		t = mt.get();
		while (headt<t) {
			if (headv.isPresent()) {
				lastret = headv;
			}
			ExtEvent extev = myPointer.pull();
			if (!isEq && extev.isreentrant()) {
				return lastret;
			}
			headt = extev.getEvent().getTS();
			headv = extractor.extractValue(extev.getEvent());
		}
		if (isEq && headt==t) {
			if (headv.isPresent()) {
				lastret = headv;
			}
		}
		return lastret;
	}
	
	public GenericPrev(Pointer p, ITauExp it, boolean iseq, boolean isval) {
		this.myPointer = p;
		this.innertau = it;
		this.isEq = iseq;
		this.extractor = isval?
			new IValExtractor<T>() {
				@Override
				public MaybeOutside<T> extractValue(StriverEvent ev) {
					if (ev.isnotick()) {
						return MaybeOutside.outside();
					}
					return MaybeOutside.of((T) ev.getValue().get());
				}
			}:
			new IValExtractor<T>() {
				@Override
				public MaybeOutside<T> extractValue(StriverEvent ev) {
					if (ev.isnotick()) {
						return MaybeOutside.outside();
					}
					return (MaybeOutside<T>) MaybeOutside.of(ev.getTS());
				}
			};
	}

}

package spec.valueexp;

import java.util.Optional;

import adts.Constants;
import adts.ExtEvent;
import adts.MaybeOutside;
import adts.StriverEvent;
import semop.Pointer;
import spec.valueexp.tauexp.ITauExp;

public class GenericPrev<T> {
	
	private interface IValExtractor<R> {
		public Optional<R> extractValue(StriverEvent striverEvent);
	}
	
	private MaybeOutside<T> lastret = MaybeOutside.negoutside();
	private Double headt=-1d;
	private Optional<T> headv = Optional.empty();
	// init these
	private Pointer myPointer;
	private ITauExp innertau;
	private boolean isEq;
	private IValExtractor<T> extractor;

	public MaybeOutside<T> getRes(double t) {
		MaybeOutside<Double> mt = innertau.getT(t);
		switch (mt.getType()) {
			case inside:
				t = mt.get();
				break;
			case negoutside:
				t = -1;
				break;
			case posoutside:
				t = Constants.INFTY;
		}
		while (headt<t) {
			if (headv.isPresent()) {
				lastret = MaybeOutside.of(headv.get());
			}
			ExtEvent extev = myPointer.pull();
			if (!isEq && extev.isreentrant()) {
				return lastret;
			}
			headt = extev.getEvent().getTS();
			headv = extractor.extractValue(extev.getEvent());
		}
		if (isEq && headt==t && headv.isPresent()) {
			lastret = MaybeOutside.of(headv.get());
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
				public Optional<T> extractValue(StriverEvent ev) {
					if (ev.isnotick()) {
						return Optional.empty();
					}
					return Optional.of((T) ev.getValue().get());
				}
			}:
			new IValExtractor<T>() {
				@Override
				public Optional<T> extractValue(StriverEvent ev) {
					if (ev.isnotick()) {
						return Optional.empty();
					}
					return (Optional<T>) Optional.of(ev.getTS());
				}
			};
	}

}

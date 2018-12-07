package spec.valueexp.generics;

import java.util.Optional;

import adts.StriverEvent;

public interface IValExtractor<R> {
	public Optional<R> extractValue(StriverEvent striverEvent);
	
	public static final IValExtractor<Double> timeExtractor =
			new IValExtractor<Double>() {
				@Override
				public Optional<Double> extractValue(StriverEvent ev) {
					if (ev.isnotick()) {
						return Optional.empty();
					}
					return Optional.of(ev.getTS());
				}
			};
	public static final IValExtractor<?> valueExtractor =
new IValExtractor() {
				@Override
				public Optional<?> extractValue(StriverEvent ev) {
					if (ev.isnotick()) {
						return Optional.empty();
					}
					return Optional.of(ev.getValue().getValue());
				}
			};
}

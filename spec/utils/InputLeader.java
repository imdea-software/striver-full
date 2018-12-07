package spec.utils;

import java.util.List;

import adts.StriverEvent;
import semop.ILeader;

public class InputLeader<T> implements ILeader<T> {
	
	// init these
	private List<StriverEvent<T>> values;

	public InputLeader(List<StriverEvent<T>> values) {
		this.values = values;
	}

	@Override
	public StriverEvent<T> getNext() {
		if (values.isEmpty()) {
			return (StriverEvent<T>) StriverEvent.posOutsideEv;
		}
		return values.remove(0);
	}

}

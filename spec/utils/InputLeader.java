package spec.utils;

import java.util.List;

import adts.StriverEvent;
import semop.ILeader;

public class InputLeader<T> implements ILeader<T> {
	
	// init these
	private List<StriverEvent> values;

	public InputLeader(List<StriverEvent> values) {
		this.values = values;
	}

	@Override
	public StriverEvent getNext() {
		if (values.isEmpty()) {
			return (StriverEvent) StriverEvent.posOutsideEv;
		}
		return values.remove(0);
	}

}

package spec.utils;

import java.util.List;

import adts.StriverEvent;
import semop.ILeader;

public class InputLeader<T> implements ILeader<T> {
	
	// init these
	private List<StriverEvent> values;
	private String name;

	public InputLeader(List<StriverEvent> values,String name) {
		this.values = values;
		this.name=name;
	}

	@Override
	public StriverEvent getNext() {
		if (values.isEmpty()) {
			return (StriverEvent) StriverEvent.posOutsideEv;
		}
		return values.remove(0);
	}

	@Override
	public String getStreamName() {
		return name;
	}

}

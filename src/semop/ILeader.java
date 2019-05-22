package semop;

import adts.StriverEvent;

public interface ILeader<T> {

	public StriverEvent getNext();

	public String getStreamName();

}
package semop;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import adts.MaybeReentrant;
import adts.StriverEvent;

public class Table {
	
	private HashSet<String> resolving = new HashSet<String>();
	// init these:
	private HashMap<String, LinkedList<StriverEvent>> theTable = new HashMap<>();;
	private HashMap<String, ILeader<?>> leaders = new HashMap<>();
	
	public MaybeReentrant getNext(String streamid, double myPos) {
		LinkedList<StriverEvent> themap = theTable.get(streamid);
		Iterator<StriverEvent> it = themap.iterator();
		while (it.hasNext()) {
			StriverEvent ev = it.next();
			if (ev.getTS() > myPos) {
				return MaybeReentrant.of(ev);
			}
		}
		// Calculate it:
		if (resolving.contains(streamid)) {
			return MaybeReentrant.reentrantevent();
		}
		resolving.add(streamid);
		StriverEvent ev = leaders.get(streamid).getNext();
		StriverEvent last = themap.peekLast();
		if (last != null && last.isnotick()) {
			themap.removeLast();
		}
		themap.add(ev);
		resolving.remove(streamid);
		return MaybeReentrant.of(ev);
	}
	
	public void setLeader(ILeader<?> inputLeader, String streamid) {
		theTable.put(streamid, new LinkedList<>());
		leaders.put(streamid, inputLeader);
	}

}

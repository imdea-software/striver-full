package semop;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import adts.MaybeReentrant;
import adts.StriverEvent;
import adts.GCList.GCLinkedList;

public class Table {
	
	private HashSet<String> resolving = new HashSet<String>();
	// init these:
	private HashMap<String, GCLinkedList<StriverEvent>> theTable = new HashMap<>();
	private HashMap<String, ILeader<?>> leaders = new HashMap<>();
	private HashMap<String, Iterator<StriverEvent>> iterators = new HashMap<>();
	
	public MaybeReentrant getNext(String streamid, double myPos) {
		GCLinkedList<StriverEvent> thelist = theTable.get(streamid);
		Iterator<StriverEvent> it = iterators.get(streamid);
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
		/*StriverEvent last = thelist.peekLast();
		if (last != null && last.isnotick()) {
			thelist.removeLast();
		}*/
		thelist.add(ev);
		resolving.remove(streamid);
		return MaybeReentrant.of(ev);
	}
	
	public void setLeader(ILeader<?> inputLeader, String streamid) {
		GCLinkedList<StriverEvent> thelist = new GCLinkedList<>();
		theTable.put(streamid, thelist);
		iterators.put(streamid, thelist.iterator());
		leaders.put(streamid, inputLeader);
	}

}

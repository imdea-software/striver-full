package semop;

import java.util.HashMap;
import java.util.HashSet;

import adts.MaybeReentrant;
import adts.StriverEvent;
import adts.GCList.GCIterator;
import adts.GCList.GCLinkedList;

public class Table {
	
	private HashSet<String> resolving = new HashSet<String>();
	// init these:
	private HashMap<String, GCLinkedList<StriverEvent>> theTable = new HashMap<>();
	private HashMap<String, ILeader<?>> leaders = new HashMap<>();
	
	public MaybeReentrant getNext(String streamid) {
		// Calculate it:
		if (resolving.contains(streamid)) {
			return MaybeReentrant.reentrantevent();
		}
		resolving.add(streamid);
		StriverEvent ev = leaders.get(streamid).getNext();
		GCLinkedList<StriverEvent> thelist = theTable.get(streamid);
		// TODO recover
		/*StriverEvent last = thelist.peekLast();
		if (last != null && last.isnotick()) {
			thelist.removeLast();
		}*/
		thelist.add(ev);
		resolving.remove(streamid);
		return MaybeReentrant.of(ev);
	}
	
	public void setLeader(ILeader<?> inputLeader, String streamid) {
		leaders.put(streamid, inputLeader);
	}

	public Pointer getPointer(String stream) {
		GCLinkedList<StriverEvent> thelist = theTable.get(stream);
		if (thelist == null) {
			thelist = new GCLinkedList<>();
			theTable.put(stream, thelist);
		}
		GCIterator<StriverEvent> iterator = thelist.iterator();
		return new Pointer(this, stream, iterator);
	}

}

package semop;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import adts.StriverEvent;
import adts.GCList.GCIterator;
import adts.GCList.GCLinkedList;

public class Table {
	
	private static Table instance = new Table();
	private HashSet<String> resolving = new HashSet<String>();
	// init these:
	private HashMap<String, GCLinkedList<StriverEvent>> theTable = new HashMap<>();
	private HashMap<String, ILeader<?>> leaders = new HashMap<>();
	// for debug:
	public HashSet<Pointer> pointers = new HashSet<>();
	
	public boolean getNext(String streamid) {
		// Calculate it:
		if (resolving.contains(streamid)) {
			return true;
		}
		resolving.add(streamid);
		StriverEvent ev = leaders.get(streamid).getNext();
		GCLinkedList<StriverEvent> thelist = theTable.get(streamid);
		StriverEvent last = thelist.peekLast();
		if (last != null && last.isnotick()) {
			thelist.removeLast();
		}
		thelist.add(ev);
		resolving.remove(streamid);
		return false;
	}
	
	public void setLeader(ILeader<?> inputLeader) {
		leaders.put(inputLeader.getStreamName(), inputLeader);
	}

	public Pointer getPointer(String stream) {
		String debugid = Thread.currentThread().getStackTrace()[2].getMethodName() + "::" + Thread.currentThread().getStackTrace()[2].getLineNumber();
		GCLinkedList<StriverEvent> thelist = theTable.get(stream);
		if (thelist == null) {
			thelist = new GCLinkedList<>();
			theTable.put(stream, thelist);
		}
		GCIterator<StriverEvent> iterator = thelist.iterator();
		Pointer ret = new Pointer(this, stream, iterator, debugid);
		pointers.add(ret);
		return ret;
	}
	
	private Table() {}

	public static Table getInstance() {
		return instance;
	}

	public void size() {
		for (Pointer kv:this.pointers) {
			System.out.println("position of "+ kv.getStreamId()+ " created at " + kv.myId+ ": " + kv.posintable);
		}
		
	}

}

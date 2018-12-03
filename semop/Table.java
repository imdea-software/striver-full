package semop;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import adts.ExtEvent;
import adts.StriverEvent;

public class Table {
	
	private HashSet<String> resolving = new HashSet<String>();
	// init these:
	private HashMap<String, LinkedList<StriverEvent>> theTable;
	private HashMap<String, Leader> leaders;
	
	public ExtEvent getNext(String streamid, int lastpos) {
		LinkedList<StriverEvent> themap = theTable.get(streamid);
		assert(themap != null);
		Iterator<StriverEvent> it = themap.iterator();
		while (it.hasNext()) {
			StriverEvent ev = it.next();
			if (ev.getTS() > lastpos) {
				return new ExtEvent(ev);
			}
		}
		// Calculate it:
		if (resolving.contains(streamid)) {
			return ExtEvent.reentrantevent;
		}
		resolving.add(streamid);
		StriverEvent ev = leaders.get(streamid).getNext(lastpos);
		themap.add(ev); // Check if it is notick?
		resolving.remove(streamid);
		return new ExtEvent(ev);
	}
	
	public void setLeaders(HashMap<String, Leader> leadersMap) {
		theTable = new HashMap<>();
		this.leaders = leadersMap;
		for (String s:leaders.keySet()) {
			theTable.put(s, new LinkedList<>());
		}
		
	}

}

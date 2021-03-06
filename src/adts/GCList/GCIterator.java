package adts.GCList;

import java.util.Iterator;

public class GCIterator<T> implements Iterator<T>{

	// public for debug:
	public Elem<T> pnext;

	public GCIterator(Elem<T> pnext) {
		this.pnext=pnext;
	}

	@Override
	public boolean hasNext() {
		return pnext.getNext() != null;
	}

	@Override
	public T next() {
		pnext = pnext.getNext();
		return pnext.getVal();
	}

	public void unhook() {
		pnext = null;
	}

}

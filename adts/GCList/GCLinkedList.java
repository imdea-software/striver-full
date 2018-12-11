package adts.GCList;

public class GCLinkedList<T> implements Iterable<T> /*implements List<T> maybe later..*/ {

	private Elem<T> slast = new Elem<T>(null);
	private Elem<T> oldlast = null;

	private Elem<T> getLast() {
		Elem<T> last = slast.getNext();
		if (last == null)
			last = slast;
		return last;
	}
	
	public GCIterator<T> iterator() {
		Elem<T> last = getLast();
		return new GCIterator<T>(last);
	}

	public void add(T e) {
		Elem<T> elem = new Elem<T>(e);
		if (oldlast !=null)
			oldlast.setNext(elem);
		oldlast = null;
		Elem<T> last = getLast();
		last.setNext(elem);
		slast = last;
	}

	public T peekLast() {
		Elem<T> last = getLast();
		return last.getVal();
	}

	public void removeLast() {
		Elem<T> last = getLast();
		oldlast = last;
		slast.setNext(null);
	}

}
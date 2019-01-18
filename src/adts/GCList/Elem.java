package adts.GCList;

public class Elem<T> {

	private T val=null;
	private Elem<T> next;

	public T getVal() {
		return this.val;
	}

	public Elem<T> getNext() {
		return this.next;
	}

	public Elem(T val) {
		this.val = val;
	}

	public void setNext(Elem<T> nxt) {
		this.next = nxt;
	}
}
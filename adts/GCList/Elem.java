package adts.GCList;

class Elem<T> {

	private T val=null;
	private Elem<T> next;
	private boolean real;

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

	public boolean isReal() {
		return real;
	}

	public void setReal(boolean real) {
		this.real = real;
	}
}
package semop;

public class PointerFactory {
	
	private Table table;

	public PointerFactory(Table t) {
		this.table = t;
	}

	public Pointer getPointer(String stream) {
		return new Pointer(table, stream);
	}

}

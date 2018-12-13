package spec.valueexp;

public interface IValExpr<T> {

	T calculateValueAt(double nt, Object cv);

	void unhookPointers();

}

package spec.valueexp;

public abstract class INDValExpr<T> implements IValExpr<T> {
	
	public T calculateValueAt(double nt, Object cv) {
		return this.calculateValueAt(nt);
		
	}
	public abstract T calculateValueAt(double nt);


}

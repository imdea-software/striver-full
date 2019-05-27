package spec.utils;

import adts.Constants;
import adts.Pair;
import semop.Leader;
import semop.Table;
import spec.StriverSpec;
import spec.tickexp.ITickExpr;
import spec.tickexp.ShiftTickExpr;
import spec.tickexp.nodelayTE.DelayPosTickExpr;
import spec.tickexp.nodelayTE.SrcTickExpr;
import spec.tickexp.nodelayTE.UnionTickExpr;
import spec.valueexp.CVValExpr;
import spec.valueexp.IValExpr;
import spec.valueexp.PrevEqValExp;
import spec.valueexp.tauexp.SuccExp;
import spec.valueexp.tauexp.TExpr;

public class BoundedSuccExpr implements IValExpr<Object> {
	
	private Table theTable = Table.getInstance();
	private double b;
	private IValExpr<Object> thefun;
	
	private static int id=0;

	public BoundedSuccExpr(String signame, double b) {
		if (b<=0)
			return;
		this.b=b;
		id++;
		// alarm
		String alarmname = signame + "_alarm_"+b+id;
		ITickExpr te = new SrcTickExpr(theTable.getPointer(signame));
		GeneralFun<Double> ve = new GeneralFun<Double>(new Constant<Double>(b));
		theTable.setLeader(new Leader<Double>(new StriverSpec(te, ve), alarmname));
		
		// hasticked
		String hastickedname = signame + "_hasticked_"+b+id;
		te = new UnionTickExpr(
				new SrcTickExpr(theTable.getPointer(signame)),
				new DelayPosTickExpr(theTable.getPointer(alarmname))
				);
		IValExpr<Boolean> vebool = new GeneralFun<Boolean>(
					new NEqFun(),
					new GeneralFun<Object>((objs -> ((Pair) objs[0]).left()),
							new CVValExpr<Pair>()),
					new GeneralFun<Object>(new Constant<Object>(Constants.notick())));
		theTable.setLeader(new Leader<Object>(new StriverSpec(te, vebool), hastickedname));

		// willtick
		String willtickname = signame + "_willtick_"+b+id;
		te = new ShiftTickExpr(theTable.getPointer(hastickedname), -b);
		vebool = new CVValExpr<Boolean>();
		theTable.setLeader(new Leader<Object>(new StriverSpec(te, vebool), willtickname));
		
		// thefun
		thefun = new IfThenElseFast<Object>(
					new GeneralFun<Boolean>(
						new Default<Boolean>(false),
						new PrevEqValExp<Boolean>(theTable.getPointer(willtickname), new TExpr())),
					new SuccExp(theTable.getPointer(signame), new TExpr()),
					new GeneralFun<Object>(new Constant<Object>(Constants.posoutside())));
					
	}

	@Override
	public void unhookPointers() {
		if (b<=0)
			return;
		thefun.unhookPointers();
	}

	@Override
	public Object calculateValueAt(double nt, Object cv) {
		if (b<=0)
			return Constants.posoutside();
		return thefun.calculateValueAt(nt, cv);
	}

}

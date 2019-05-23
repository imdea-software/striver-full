package spec.utils;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import adts.Constants;
import adts.Pair;
import semop.Leader;
import semop.Table;
import spec.StriverSpec;
import spec.tickexp.ITickExpr;
import spec.tickexp.ShiftTickExpr;
import spec.tickexp.nodelayTE.DelayNegTickExpr;
import spec.tickexp.nodelayTE.DelayPosTickExpr;
import spec.tickexp.nodelayTE.SrcTickExpr;
import spec.tickexp.nodelayTE.UnionTickExpr;
import spec.valueexp.CVValExpr;
import spec.valueexp.IValExpr;
import spec.valueexp.SuccEqValExp;
import spec.valueexp.SuccValExp;
import spec.valueexp.tauexp.SuccExp;
import spec.valueexp.tauexp.TExpr;

public class BoundedSuccExpr implements IValExpr<Object> {
	
	private Table theTable = Table.getInstance();
	private double d;
	private GeneralFun<Object> thefun;

	public BoundedSuccExpr(String signame, double d) {
		if (d<=0)
			return;
		this.d=d;
		// alarm
		String alarmname = signame + "_alarm"+d;
		ITickExpr te = new SrcTickExpr(theTable.getPointer(signame));
		GeneralFun<Double> ve = new GeneralFun<Double>(new Constant<Double>(d));
		theTable.setLeader(new Leader<Double>(new StriverSpec(te, ve), alarmname));
		
		// hasticked
		String hastickedname = signame + "_hasticked_"+d;
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
		String willtickname = signame + "_willtick_"+d;
		te = new ShiftTickExpr(theTable.getPointer(hastickedname), -d);
		vebool = new CVValExpr<Boolean>();
		theTable.setLeader(new Leader<Object>(new StriverSpec(te, vebool), willtickname));
		
		// thefun
		thefun = new GeneralFun<Object>(
					new IfThenElse<Object>(),
					new GeneralFun<Boolean>(
						new Default<Boolean>(false),
						new SuccEqValExp<Boolean>(theTable.getPointer(willtickname), new TExpr())),
					new SuccExp(theTable.getPointer(signame), new TExpr()),
					new GeneralFun<Object>(new Constant<Object>(Constants.posoutside())));
					
	}

	@Override
	public void unhookPointers() {
		if (d<=0)
			return;
		thefun.unhookPointers();
	}

	@Override
	public Object calculateValueAt(double nt, Object cv) {
		if (d<=0)
			return Constants.posoutside();
		return thefun.calculateValueAt(nt, cv);
	}

}

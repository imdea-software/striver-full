package main;

import java.util.Arrays;
import java.util.List;
import adts.Constants;
import adts.DelayAndValue;
import adts.StriverEvent;
import semop.ILeader;
import semop.Leader;
import semop.Pointer;
import semop.Table;
import spec.StriverSpec;
import spec.tickexp.DelayTickExpr;
import spec.tickexp.ITickExpr;
import spec.tickexp.nodelayTE.SrcTickExpr;
import spec.tickexp.nodelayTE.UnionTickExpr;
import spec.utils.And;
import spec.utils.Constant;
import spec.utils.DAVJoiner;
import spec.utils.Default;
import spec.utils.GeneralFun;
import spec.utils.GtFun;
import spec.utils.IfThenElse;
import spec.utils.LeqFun;
import spec.utils.UnsafeAdd;
import spec.valueexp.CVValExpr;
import spec.valueexp.IValExpr;
import spec.valueexp.PrevEqValExp;
import spec.valueexp.tauexp.SuccExp;
import spec.valueexp.tauexp.TExpr;

public class STLPoC {

	private static Table theTable = new Table();

    public static void main(String[] args) throws InterruptedException {
    	
		
		// inputs:
		/*theTable.setLeader(new ILeader<Boolean>() {
			int nxt = 0;
			@Override
			public StriverEvent getNext() {
				nxt+=3;
				return new StriverEvent(nxt, Math.random() < 0.5);
			}
			
		}, "phi");*/
		
		theTable.setLeader(new ILeader<Boolean>() {
			boolean given = false;
			@Override
			public StriverEvent getNext() {
				if (given) {
					return StriverEvent.posOutsideEv;
				}
				given = true;
				return new StriverEvent(0, true);
			}
			
		}, "phi");

		theTable.setLeader(new ILeader<Boolean>() {
			int nxt = 0;
			@Override
			public StriverEvent getNext() {
				nxt+=2;
				return new StriverEvent(nxt, Math.random() < 0.5);
			}
			
		}, "psi");
		
		// outputs:
		// phifalse:
		Pointer p = theTable.getPointer("phi");
		ITickExpr te = new SrcTickExpr(p);
		p = theTable.getPointer("phi");
		IValExpr<Boolean> vebool = new GeneralFun<Boolean>(new IfThenElse<>(),
				new PrevEqValExp<>(p, new TExpr()),
				new GeneralFun<Object>(new Constant<Object>(Constants.notick())),
				new GeneralFun<Object>(new Constant<Object>(false))
				);
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool)), "phiFalse");
		
		//psitrue:
		p = theTable.getPointer("psi");
		te = new SrcTickExpr(p);
		p = theTable.getPointer("psi");
		vebool = new GeneralFun<Boolean>(new IfThenElse<>(),
				new PrevEqValExp<>(p, new TExpr()),
				new GeneralFun<Object>(new Constant<Object>(true)),
				new GeneralFun<Object>(new Constant<Object>(Constants.notick()))
				);
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool)), "psiTrue");
		
		// delayed
		Double b = 2d;
		// phi x win
		p = theTable.getPointer("phi");
		te = new SrcTickExpr(p);
		p = theTable.getPointer("phi");
		IValExpr<DelayAndValue<Boolean>> vedv = new GeneralFun<DelayAndValue<Boolean>>(
				new DAVJoiner<Boolean>(),
				new GeneralFun<Double>(new Constant<Double>(-b)),
				new PrevEqValExp<Boolean>(p, new TExpr())
				);
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vedv)), "phixwin");

		// shiftphi
		p = theTable.getPointer("phixwin");
		te = new DelayTickExpr(p);
		vebool = new CVValExpr<>();
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool)), "shiftphi");

		// psi x win
		p = theTable.getPointer("psi");
		te = new SrcTickExpr(p);
		p = theTable.getPointer("psi");
		vedv = new GeneralFun<DelayAndValue<Boolean>>(
				new DAVJoiner<Boolean>(),
				new GeneralFun<Double>(new Constant<Double>(-b)),
				new PrevEqValExp<Boolean>(p, new TExpr())
				);
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vedv)), "psixwin");

		// shiftpsi
		p = theTable.getPointer("psixwin");
		te = new DelayTickExpr(p);
		vebool = new CVValExpr<Boolean>();
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool)), "shiftpsi");
		
		// until[0, win]
		SrcTickExpr phisrc = new SrcTickExpr(theTable.getPointer("phi"));
		SrcTickExpr psisrc = new SrcTickExpr(theTable.getPointer("psi"));
		SrcTickExpr shiftphisrc = new SrcTickExpr(theTable.getPointer("shiftphi"));
		SrcTickExpr shiftpsisrc = new SrcTickExpr(theTable.getPointer("shiftpsi"));
		te = new UnionTickExpr(new UnionTickExpr(phisrc, psisrc), new UnionTickExpr(shiftphisrc, shiftpsisrc));
		vebool = new GeneralFun<Boolean>(
					new And(),
					new GeneralFun<Boolean>(
						new LeqFun(),
						getMinPsi(),
						new GeneralFun<Double>(
							new UnsafeAdd(),
							new TExpr(),
							new GeneralFun<Double>(new Constant<>(b))
						)
						),
					new GeneralFun<Boolean>(
						new GtFun(),
						getMinNotPhi(),
						getMinPsi()
						)
				);
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool)), "until");
		
		// pointers
		/*Pointer phi = theTable.getPointer("phi");
		Pointer phitrue = theTable.getPointer("phiFalse");
		Pointer psi = theTable.getPointer("psi");
		Pointer psifalse = theTable.getPointer("psiTrue");
		Pointer phixwin = theTable.getPointer("phixwin");
		Pointer shiftphi = theTable.getPointer("shiftphi");
		Pointer shiftpsi = theTable.getPointer("shiftpsi");
		List<Pointer> pointers = Arrays.asList(phi, phitrue, psi, psifalse, phixwin, shiftphi, shiftpsi, until);*/
		Pointer until = theTable.getPointer("until");
		
		long lastReport = System.currentTimeMillis();
		while (true) {
			long now = System.currentTimeMillis();
			if (!true) {
				System.out.println(until.pull());
				if (now - lastReport > 4000) {
				  /* Total amount of free memory available to the JVM */
				  System.err.println("Free memory (bytes): " + Runtime.getRuntime().freeMemory());
				  Thread.sleep(1000);
				  lastReport = now;
				}
				continue;
			}
			if (true) {
				until.pull();
				if (now - lastReport > 4000) {
					/* Total amount of free memory available to the JVM */
					//System.out.println("Free memory (bytes): " + Runtime.getRuntime().freeMemory());
					for (Pointer pointer:theTable.pointers) {
						System.out.println("Pointer " + pointer.myId + " for stream " + pointer.getStreamId() + " next val: " +pointer.myIterator.pnext);
					}
					System.out.println("-----------------------");
					lastReport = now;
				}
				continue;
			}
			/*for (Pointer pointer:pointers) {
				System.out.println(pointer.getStreamId());
				StriverEvent ev = new StriverEvent(0,true);
				Object lastval = null;
				while (ev.getTS()<100) {
					ev = pointer.pull().getEvent();
					Object newValue = ev.getValue();
					if (!newValue.equals(lastval) && newValue != Constants.notick()) {
						System.out.println(ev.getTS() + " " + lastval);
						System.out.println(ev.getTS() + " " + newValue);
						lastval = newValue;
					}
				}
			}*/
			System.exit(0);
			System.out.println("-----------------------");
		}
    }

	private static IValExpr<?> getMinNotPhi() {
	    Pointer phi = theTable.getPointer("phi");
	    Pointer phifalse = theTable.getPointer("phiFalse");
		return new GeneralFun<Double>(
				new IfThenElse<>(),
				new GeneralFun<Boolean>(
					new Default<Boolean>(false),
					new PrevEqValExp<Boolean>(phi, new TExpr())),
				new SuccExp(phifalse, new TExpr()),
				new TExpr()
				);
	}

	private static IValExpr<?> getMinPsi() {
	    Pointer psi = theTable.getPointer("psi");
	    Pointer psitrue = theTable.getPointer("psiTrue");
		return new GeneralFun<Double>(
				new IfThenElse<>(),
				new GeneralFun<Boolean>(
					new Default<Boolean>(false),
					new PrevEqValExp<Boolean>(psi, new TExpr())),
				new TExpr(),
				new SuccExp(psitrue, new TExpr())
				);
	}

}

package main;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


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
import spec.tickexp.nodelayTE.INDTickExpr;
import spec.tickexp.nodelayTE.SrcTickExpr;
import spec.tickexp.nodelayTE.UnionTickExpr;
import spec.utils.Constant;
import spec.utils.DAVJoiner;
import spec.utils.Default;
import spec.utils.GeneralFun;
import spec.utils.GtFun;
import spec.utils.IfThenElse;
import spec.utils.InputLeader;
import spec.utils.UnsafeAdd;
import spec.valueexp.CVValExpr;
import spec.valueexp.IValExpr;
import spec.valueexp.PrevEqValExp;
import spec.valueexp.PrevValExp;
import spec.valueexp.SuccEqValExp;
import spec.valueexp.tauexp.SuccExp;
import spec.valueexp.tauexp.TExpr;

public class STLPoC {

    public static void main(String[] args) {
    	
		Table theTable = new Table();
		
		// inputs:
		theTable.setLeader(new ILeader<Boolean>() {
			int nxt = 0;
			@Override
			public StriverEvent getNext() {
				nxt+=3;
				return new StriverEvent(nxt, Math.random() < 0.5);
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
		// phitrue:
		Pointer p = theTable.getPointer("phi");
		ITickExpr te = new SrcTickExpr(p);
		p = theTable.getPointer("phi");
		IValExpr<Boolean> vebool = new GeneralFun<Boolean>(new IfThenElse<>(),
				new PrevEqValExp<>(p, new TExpr()),
				new GeneralFun<Object>(new Constant<Object>(true)),
				new GeneralFun<Object>(new Constant<Object>(Constants.notick()))
				);
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool)), "phitrue");
		
		//psifalse:
		p = theTable.getPointer("psi");
		te = new SrcTickExpr(p);
		p = theTable.getPointer("psi");
		vebool = new GeneralFun<Boolean>(new IfThenElse<>(),
				new PrevEqValExp<>(p, new TExpr()),
				new GeneralFun<Object>(new Constant<Object>(Constants.notick())),
				new GeneralFun<Object>(new Constant<Object>(false))
				);
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool)), "psifalse");
		
		// delayed
		Double win = 5d;
		// phi x win
		p = theTable.getPointer("phi");
		te = new SrcTickExpr(p);
		p = theTable.getPointer("phi");
		IValExpr<DelayAndValue<Boolean>> vedv = new GeneralFun<DelayAndValue<Boolean>>(
				new DAVJoiner<Boolean>(),
				new GeneralFun<Double>(new Constant<Double>(win)),
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
				new GeneralFun<Double>(new Constant<Double>(win)),
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
					new IfThenElse<>(),
					new GeneralFun<Boolean>(new Default<Boolean>(false), new PrevEqValExp<Boolean>(theTable.getPointer("phi"), new TExpr())),
					new GeneralFun<Boolean>(new Constant<Boolean>(true)),
					new GeneralFun<Boolean>(
						new IfThenElse<>(),
						new GeneralFun<Boolean>(new GtFun(), new SuccExp(theTable.getPointer("phitrue"), new TExpr()), new GeneralFun<Double>(new UnsafeAdd(), new GeneralFun<Double>(new Constant<Double>(win)), new TExpr())),
						new GeneralFun<Boolean>(new Constant<Boolean>(false)),
						new GeneralFun<Object>(
							new IfThenElse<>(),
							new GeneralFun<Boolean>(new Default<Boolean>(false), new PrevEqValExp<Boolean>(theTable.getPointer("psi"), new TExpr())),
							new GeneralFun<Boolean>(new GtFun(), new SuccExp(theTable.getPointer("psifalse"), new TExpr()), new SuccExp(theTable.getPointer("phitrue"), new TExpr())),
							new GeneralFun<Boolean>(new Constant<Boolean>(false))
						)
							
					)
				);
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool)), "until");
		
		// pointers
		Pointer phi = theTable.getPointer("phi");
		Pointer phitrue = theTable.getPointer("phitrue");
		Pointer psi = theTable.getPointer("psi");
		Pointer psifalse = theTable.getPointer("psifalse");
		Pointer phixwin = theTable.getPointer("phixwin");
		Pointer shiftphi = theTable.getPointer("shiftphi");
		Pointer shiftpsi = theTable.getPointer("shiftpsi");
		Pointer until = theTable.getPointer("until");
		List<Pointer> pointers = Arrays.asList(phi, phitrue, psi, psifalse, phixwin, shiftphi, shiftpsi, until);
		
		long lastReport = System.currentTimeMillis();
		while (true) {
			long now = System.currentTimeMillis();
			if (now - lastReport > 4000&&false) {
			  /* Total amount of free memory available to the JVM */
			  //System.out.println("Free memory (bytes): " + Runtime.getRuntime().freeMemory());
				for (Pointer pointer:theTable.pointers) {
					System.out.println("Pointer " + pointer.hashCode() + " for stream " +pointer.getStreamId() + " next val: " +pointer.myIterator.pnext);
				}
					System.out.println("-----------------------");
			  lastReport = now;
			}
			for (Pointer pointer:pointers) {
				System.out.println(pointer.getStreamId());
				StriverEvent ev = new StriverEvent(0,true);
				Object lastval = null;
				while (ev.getTS()<100) {
					ev = pointer.pull().getEvent();
					if (!ev.getValue().equals(lastval)) {
						System.out.println(ev.getTS() + " " + lastval);
						System.out.println(ev.getTS() + " " + ev.getValue());
					}
					lastval = ev.getValue();
				}
			}
			System.exit(0);
			System.out.println("-----------------------");
		}
    }

}

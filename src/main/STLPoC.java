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
import spec.tickexp.nodelayTE.SrcTickExpr;
import spec.utils.Constant;
import spec.utils.DAVJoiner;
import spec.utils.Default;
import spec.utils.GeneralFun;
import spec.utils.IfThenElse;
import spec.utils.InputLeader;
import spec.utils.UnsafeAdd;
import spec.valueexp.CVValExpr;
import spec.valueexp.IValExpr;
import spec.valueexp.PrevEqValExp;
import spec.valueexp.PrevValExp;
import spec.valueexp.SuccEqValExp;
import spec.valueexp.tauexp.TExpr;

public class STLPoC {

    public static void main(String[] args) {
    	
		Table theTable = new Table();
		
		// inputs:
		theTable.setLeader(new ILeader<Integer>() {
			int nxt = 0;
			@Override
			public StriverEvent getNext() {
				return new StriverEvent(nxt++, Math.random() < 0.5);
			}
			
		}, "phi");

		theTable.setLeader(new ILeader<Integer>() {
			int nxt = 0;
			@Override
			public StriverEvent getNext() {
				return new StriverEvent(nxt++, Math.random() < 0.5);
			}
			
		}, "psi");
		
		// outputs:
		// phitrue:
		Pointer p = theTable.getPointer("phi");
		ITickExpr te = new SrcTickExpr(p);
		p = theTable.getPointer("phi");
		IValExpr<Integer> veint = new GeneralFun<Integer>(new IfThenElse<>(),
				new PrevEqValExp<>(p, new TExpr()),
				new GeneralFun<Object>(new Constant<Object>(true)),
				new GeneralFun<Object>(new Constant<Object>(Constants.notick()))
				);
		theTable.setLeader(new Leader<Integer>(new StriverSpec(te, veint)), "phitrue");
		
		//psifalse:
		p = theTable.getPointer("psi");
		te = new SrcTickExpr(p);
		p = theTable.getPointer("psi");
		veint = new GeneralFun<Integer>(new IfThenElse<>(),
				new PrevEqValExp<>(p, new TExpr()),
				new GeneralFun<Object>(new Constant<Object>(Constants.notick())),
				new GeneralFun<Object>(new Constant<Object>(false))
				);
		theTable.setLeader(new Leader<Integer>(new StriverSpec(te, veint)), "psifalse");
		
		// delayed
		Double win = 5d;
		// phi x win
		p = theTable.getPointer("phi");
		te = new SrcTickExpr(p);
		p = theTable.getPointer("phi");
		IValExpr<DelayAndValue<Integer>> vedv = new GeneralFun<DelayAndValue<Integer>>(
				new DAVJoiner<Integer>(),
				new GeneralFun<Double>(new Constant<Double>(win)),
				new PrevEqValExp<Integer>(p, new TExpr())
				);
		theTable.setLeader(new Leader<Integer>(new StriverSpec(te, vedv)), "phixwin");

		// shiftphi
		p = theTable.getPointer("phixwin");
		te = new DelayTickExpr(p);
		veint = new CVValExpr<>();
		theTable.setLeader(new Leader<Integer>(new StriverSpec(te, veint)), "shiftphi");
		
		// pointers
		Pointer phi = theTable.getPointer("phi");
		Pointer phitrue = theTable.getPointer("phitrue");
		Pointer psi = theTable.getPointer("psi");
		Pointer psifalse = theTable.getPointer("psifalse");
		Pointer phixwin = theTable.getPointer("phixwin");
		Pointer shiftphi = theTable.getPointer("shiftphi");
		List<Pointer> pointers = Arrays.asList(phi, phitrue, psi, psifalse, phixwin, shiftphi);
		
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
				System.out.println(pointer.getStreamId() + " : " + pointer.pull());
			}
		}
    }

}

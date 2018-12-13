package main;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import adts.StriverEvent;
import semop.ILeader;
import semop.Leader;
import semop.Pointer;
import semop.Table;
import spec.StriverSpec;
import spec.tickexp.ITickExpr;
import spec.tickexp.nodelayTE.SrcTickExpr;
import spec.utils.Default;
import spec.utils.GeneralFun;
import spec.utils.InputLeader;
import spec.utils.UnsafeAdd;
import spec.utils.UnsafeAddInt;
import spec.valueexp.IValExpr;
import spec.valueexp.PrevEqValExp;
import spec.valueexp.PrevValExp;
import spec.valueexp.SuccEqValExp;
import spec.valueexp.tauexp.TExpr;

public class FutStriver {

    public static void main(String[] args) {
    	
    		/*GCLinkedList<Integer> thelist = new GCLinkedList<>();
    		GCIterator<Integer> iterator = thelist.iterator();
    		GCIterator<Integer> iterator2 = thelist.iterator();
    		thelist.add(5);
    		thelist.add(6);
    		thelist.add(7);
    		thelist.add(8);
    		thelist.add(9);
    		while (iterator2.hasNext()) {
    			System.out.println(iterator2.next());
    		}
    		thelist.removeLast();
    		thelist.add(7);
    		while (iterator.hasNext()) {
    			System.out.println(iterator.next());
    		}
    		
    		System.exit(0);*/
    	
    	
		Table theTable = new Table();
		
		// inputs:
		List<StriverEvent> values = new LinkedList<StriverEvent>(Arrays.asList(
				new StriverEvent(10, 10)
				));
		theTable.setLeader(new InputLeader<Integer>(values), "in1");

		values = new LinkedList<StriverEvent>(Arrays.asList(
				new StriverEvent(0, 0),
				new StriverEvent(2, 2),
				new StriverEvent(4, 4),
				new StriverEvent(6, 6),
				new StriverEvent(8, 8),
				new StriverEvent(10, 10)
				));
		//theTable.setLeader(new InputLeader<Integer>(values), "in2");
		theTable.setLeader(new ILeader<Integer>() {

			int nxt = 0;
			@Override
			public StriverEvent getNext() {
				return new StriverEvent(nxt, nxt++);
			}
			
		}, "in2");
		
		// outputs:
		// r:
		Pointer p = theTable.getPointer("in2");
		ITickExpr te = new SrcTickExpr(p);
		Pointer pri2 = theTable.getPointer("in2");
		Pointer prr = theTable.getPointer("r");
		IValExpr<Integer> veint = new GeneralFun<Integer>(new UnsafeAddInt(), 
				new GeneralFun<Integer>(new Default<Integer>(0), new PrevValExp<>(prr, new TExpr())),
				new PrevEqValExp<>(pri2, new TExpr())
				);
		theTable.setLeader(new Leader<Integer>(new StriverSpec(te, veint)), "r");
		
		// x:
		p = theTable.getPointer("in1");
		te = new SrcTickExpr(p);
		p = theTable.getPointer("r");
		veint = new PrevEqValExp<Integer>(p, new TExpr());
		theTable.setLeader(new Leader<Integer>(new StriverSpec(te, veint)), "x");
		
		// s:
		p = theTable.getPointer("r");
		te = new SrcTickExpr(p);
		Pointer psr = theTable.getPointer("r");
		Pointer psx = theTable.getPointer("r");
		veint = new GeneralFun<Integer>(new UnsafeAddInt(), 
				new PrevEqValExp<>(psr, new TExpr()),
				new SuccEqValExp<>(psx, new TExpr()));
		theTable.setLeader(new Leader<Integer>(new StriverSpec(te, veint)), "s");
		
		/*
         input int in1, in2
         output int x, r, s

         r.ticks = in2.ticks
         r.val t = r(<t|0) + in2(~t)

         x.ticks = in1.ticks
         x.val t = r(~t)

         s.ticks = r.ticks
         s.val t = r(~t) + x(t~)
   
         in2 = [(0,0), (2,2), (4,4), (6,6), (8,8),...]
         in1 = [(10, 10)]
         r = [(0, 0), (2,2), (4, 6), (6, 12), (8, 20), (10, 30),...]
		 x = [(10, 30)]
	 	 s = [(0, 30), (2,32), (4, 36), (6, 42), (8, 50), (10, 60),...]
		 */
		
		// pointers
		Pointer prout = theTable.getPointer("r");
		Pointer pxout = theTable.getPointer("x");
		Pointer psout = theTable.getPointer("s");
		List<Pointer> pointers = Arrays.asList(prout, pxout, psout);
		
		long lastReport = System.currentTimeMillis();
		while (true) {
			long now = System.currentTimeMillis();
			if (now - lastReport > 4000) {
			  /* Total amount of free memory available to the JVM */
			  //System.out.println("Free memory (bytes): " + Runtime.getRuntime().freeMemory());
				for (Pointer pointer:theTable.pointers) {
					System.out.println("Pointer " + pointer.hashCode() + " for stream " +pointer.getStreamId() + " next val: " +pointer.myIterator.pnext);
				}
					System.out.println("-----------------------");
			  lastReport = now;
			}
			for (Pointer pointer:pointers)
				pointer.pull();
		}
    }

}

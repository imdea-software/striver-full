package main;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import adts.StriverEvent;
import semop.Leader;
import semop.Pointer;
import semop.PointerFactory;
import semop.Table;
import spec.StriverSpec;
import spec.tickexp.ITickExpr;
import spec.tickexp.SrcTickExpr;
import spec.utils.Default;
import spec.utils.GeneralFun;
import spec.utils.InputLeader;
import spec.utils.UnsafeAdd;
import spec.valueexp.IValExpr;
import spec.valueexp.PrevEqValExp;
import spec.valueexp.PrevValExp;
import spec.valueexp.SuccEqValExp;
import spec.valueexp.tauexp.TExpr;

public class FutStriver {

    public static void main(String[] args) {
		Table theTable = new Table();
		PointerFactory pfactory = new PointerFactory(theTable);
		
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
		theTable.setLeader(new InputLeader<Integer>(values), "in2");
		
		// outputs:
		// r:
		Pointer p = pfactory.getPointer("in2");
		ITickExpr te = new SrcTickExpr(p);
		Pointer pri2 = pfactory.getPointer("in2");
		Pointer prr = pfactory.getPointer("r");
		IValExpr<Integer> veint = new GeneralFun<Integer>(new UnsafeAdd(), 
				new GeneralFun<Integer>(new Default<Integer>(0), new PrevValExp<>(prr, new TExpr())),
				new PrevEqValExp<>(pri2, new TExpr())
				);
		theTable.setLeader(new Leader<Integer>(new StriverSpec(te, veint)), "r");
		
		// x:
		p = pfactory.getPointer("in1");
		te = new SrcTickExpr(p);
		p = pfactory.getPointer("r");
		veint = new PrevEqValExp<Integer>(p, new TExpr());
		theTable.setLeader(new Leader<Integer>(new StriverSpec(te, veint)), "x");
		
		// s:
		p = pfactory.getPointer("r");
		te = new SrcTickExpr(p);
		Pointer psr = pfactory.getPointer("r");
		Pointer psx = pfactory.getPointer("x");
		veint = new GeneralFun<Integer>(new UnsafeAdd(), 
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
		Pointer prout = pfactory.getPointer("r");
		Pointer pxout = pfactory.getPointer("x");
		Pointer psout = pfactory.getPointer("s");
		List<Pointer> pointers = Arrays.asList(prout, pxout, psout);
		
			for (Pointer pointer:pointers)
		for (int i=0;i<7;i++)
				System.out.println(pointer.getStreamId() + " : "+pointer.pull());
    }

}

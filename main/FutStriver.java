package main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import adts.StriverEvent;
import semop.ILeader;
import semop.Leader;
import semop.Pointer;
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
		
		// inputs:
		List<StriverEvent> values = new LinkedList<StriverEvent>(Arrays.asList(
				new StriverEvent(10, 10)
				));
		InputLeader<Integer> i1leader = new InputLeader<Integer>(values);

		values = new LinkedList<StriverEvent>(Arrays.asList(
				new StriverEvent(0, 0),
				new StriverEvent(2, 2),
				new StriverEvent(4, 4),
				new StriverEvent(6, 6),
				new StriverEvent(8, 8),
				new StriverEvent(10, 10)
				));
		InputLeader<Integer> i2leader = new InputLeader<Integer>(values);
		
		// outputs:
		// r:
		Pointer pri21 = new Pointer(theTable, "in2");
		ITickExpr te = new SrcTickExpr(pri21);
		Pointer pri22 = new Pointer(theTable, "in2");
		Pointer prr = new Pointer(theTable, "r");
		IValExpr<Integer> veint = new GeneralFun<Integer>(new UnsafeAdd(), 
				new GeneralFun<Integer>(new Default<Integer>(0), new PrevValExp<>(prr, new TExpr())),
				new PrevEqValExp<>(pri22, new TExpr())
				);
		Leader<Integer> rleader = new Leader<Integer>(new StriverSpec(te, veint, "r"));
		
		// x:
		Pointer pxi1 = new Pointer(theTable, "in1");
		te = new SrcTickExpr(pxi1);
		Pointer pxr = new Pointer(theTable, "r");
		veint = new PrevEqValExp<Integer>(pxr, new TExpr());
		Leader<Integer> xleader = new Leader<Integer>(new StriverSpec(te, veint, "x"));
		
		// s:
		Pointer psr1 = new Pointer(theTable, "r");
		te = new SrcTickExpr(psr1);
		Pointer psr2 = new Pointer(theTable, "r");
		Pointer psx = new Pointer(theTable, "x");
		veint = new GeneralFun<Integer>(new UnsafeAdd(), 
				new PrevEqValExp<>(psr2, new TExpr()),
				new SuccEqValExp<>(psx, new TExpr()));
		Leader<Integer> sleader = new Leader<Integer>(new StriverSpec(te, veint, "s"));
		
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
		
		// table
		HashMap<String, ILeader> leadersMap = new HashMap<>();
		leadersMap.put("in1", i1leader);
		leadersMap.put("in2", i2leader);
		leadersMap.put("r", rleader);
		leadersMap.put("x", xleader);
		leadersMap.put("s", sleader);
		theTable.setLeaders(leadersMap);
		
		// pointers
		Pointer prout = new Pointer(theTable, "r");
		Pointer pxout = new Pointer(theTable, "x");
		Pointer psout = new Pointer(theTable, "s");
		Pointer pi1out = new Pointer(theTable, "in1");
		List<Pointer> pointers = Arrays.asList(/*pi1out, */prout, pxout, psout);
		
			for (Pointer p:pointers)
		for (int i=0;i<7;i++)
				System.out.println(p.getStreamId() + " : "+p.pull());
    }

}

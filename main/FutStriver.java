package main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import adts.MaybeNotick;
import adts.MaybeOutside;
import adts.StriverEvent;
import semop.ILeader;
import semop.Leader;
import semop.Pointer;
import semop.Table;
import spec.StriverSpec;
import spec.tickexp.ConstTickExpr;
import spec.tickexp.ITickExpr;
import spec.tickexp.SrcTickExpr;
import spec.utils.Default;
import spec.utils.GeneralFun;
import spec.utils.InputLeader;
import spec.utils.UnsafeAdd;
import spec.valueexp.IValExpr;
import spec.valueexp.PrevValExp;
import spec.valueexp.tauexp.TExpr;

public class FutStriver {

    public static void main(String[] args) {
		Table theTable = new Table();
		
		// inputs:
		List<StriverEvent<Integer>> values = new LinkedList<StriverEvent<Integer>>(Arrays.asList(
				new StriverEvent<Integer>(10, MaybeNotick.of(10))
				));
		InputLeader<Integer> i1leader = new InputLeader<Integer>(values);

		values = new LinkedList<StriverEvent<Integer>>(Arrays.asList(
				new StriverEvent<Integer>(0, MaybeNotick.of(0)),
				new StriverEvent<Integer>(2, MaybeNotick.of(2)),
				new StriverEvent<Integer>(4, MaybeNotick.of(4)),
				new StriverEvent<Integer>(6, MaybeNotick.of(6)),
				new StriverEvent<Integer>(8, MaybeNotick.of(8)),
				new StriverEvent<Integer>(10, MaybeNotick.of(10))
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
				new GeneralFun<Integer>(new Default<Integer>(0),new PrevValExp<>(pri22, new TExpr()))
				);
		Leader<Integer> rleader = new Leader<Integer>(new StriverSpec(te, veint, "r"));
		
		
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
		 */
		
		// table
		HashMap<String, ILeader> leadersMap = new HashMap<>();
		leadersMap.put("in1", i1leader);
		leadersMap.put("in2", i2leader);
		leadersMap.put("r", rleader);
		theTable.setLeaders(leadersMap);
		
		// pointers
		Pointer prout = new Pointer(theTable, "r");
		Pointer pi1out = new Pointer(theTable, "in1");
		List<Pointer> pointers = Arrays.asList(pi1out, prout);
		
			for (Pointer p:pointers)
		for (int i=0;i<10;i++)
				System.out.println(p.getStreamId() + " : "+p.pull());
    }

}

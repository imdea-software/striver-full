package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import adts.MaybeNotick;
import adts.MaybeOutside;
import adts.StriverEvent;
import semop.ILeader;
import semop.Leader;
import semop.Pointer;
import semop.Table;
import spec.StriverSpec;
import spec.tickexp.AtNegTickExpr;
import spec.tickexp.AtPosTickExpr;
import spec.tickexp.ConstTickExpr;
import spec.tickexp.ITickExpr;
import spec.tickexp.SrcTickExpr;
import spec.tickexp.UnionTickExpr;
import spec.utils.InputLeader;
import spec.valueexp.IValExpr;
import spec.valueexp.PrevEqValExp;
import spec.valueexp.PrevValExp;
import spec.valueexp.RandomPosIntExpr;
import spec.valueexp.SuccEqValExp;
import spec.valueexp.SuccValExp;
import spec.valueexp.tauexp.ITauExp;
import spec.valueexp.tauexp.PrevEqExp;
import spec.valueexp.tauexp.PrevExp;
import spec.valueexp.tauexp.TExpr;
import spec.valueexp.RandomNegIntExpr;

public class FutStriver {

    public static void main(String[] args) {
		Table theTable = new Table();
		
		// inputs:
		List<StriverEvent<Integer>> values = new LinkedList<StriverEvent<Integer>>(Arrays.asList(
				new StriverEvent<Integer>(0, MaybeNotick.of(2))
				));
		InputLeader<Integer> i1leader = new InputLeader<Integer>(values);
		
		
		// s def
        ITickExpr te = new ConstTickExpr(9);
		IValExpr<MaybeOutside<Double>> veout = new TExpr();
		StriverSpec s = new StriverSpec(te, veout, "s");
		ILeader ls = new Leader(s);
		
		// table
		HashMap<String, ILeader> leadersMap = new HashMap<>();
		leadersMap.put("s", ls);
		leadersMap.put("i1", i1leader);
		theTable.setLeaders(leadersMap);
		
		// pointers
		Pointer ps = new Pointer(theTable, "s");
		Pointer pr = new Pointer(theTable, "r");
		Pointer px = new Pointer(theTable, "x");
		Pointer prneg = new Pointer(theTable, "rneg");
		Pointer pprevx = new Pointer(theTable, "prevx");
		Pointer i1outpointer = new Pointer(theTable, "i1");
		List<Pointer> pointers = Arrays.asList(i1outpointer);
		
			for (Pointer p:pointers)
		for (int i=0;i<10;i++)
				System.out.println(p.getStreamId() + " : "+p.pull());
    }

}

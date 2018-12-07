package main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import adts.MaybeOutside;
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
import spec.valueexp.IValExpr;
import spec.valueexp.PrevEqValExp;
import spec.valueexp.PrevValExp;
import spec.valueexp.RandomPosIntExpr;
import spec.valueexp.SuccEqValExp;
import spec.valueexp.SuccValExp;
import spec.valueexp.tauexp.ITauExp;
import spec.valueexp.tauexp.PrevExp;
import spec.valueexp.tauexp.TExpr;
import spec.valueexp.RandomNegIntExpr;

public class FutStriver {

    public static void main(String[] args) {
		Table theTable = new Table();
		// s def
        ITickExpr te = new ConstTickExpr(9);
		IValExpr<MaybeOutside<Double>> veout = new TExpr();
		StriverSpec s = new StriverSpec(te, veout, "s");
		Leader ls = new Leader(s);

		// r def
		Pointer prs = new Pointer(theTable, "s");
        te = new SrcTickExpr(prs);
		IValExpr<Double> ve = new RandomPosIntExpr();
		StriverSpec r = new StriverSpec(te, ve, "r");
		Leader lr = new Leader(r);

		// rneg def
        te = new ConstTickExpr(9);
		ve = new RandomNegIntExpr();
		StriverSpec rneg = new StriverSpec(te, ve, "rneg");
		Leader lrneg = new Leader(rneg);

		// x def
		Pointer pxr = new Pointer(theTable, "r");
		Pointer pxrneg = new Pointer(theTable, "rneg");
		Pointer pxx = new Pointer(theTable, "x");
		ITickExpr tel = new ConstTickExpr(2);
		ITickExpr terr = new AtPosTickExpr(pxr);
		ITickExpr terxpos = new AtPosTickExpr(pxx);
		ITickExpr terrneg = new AtNegTickExpr(pxrneg);
        te = new UnionTickExpr(tel, terxpos);
		ve = new RandomPosIntExpr();
		StriverSpec x = new StriverSpec(te, ve, "x");
		Leader lx = new Leader(x);
		
		// prevx def
		Pointer pprevxx0 = new Pointer(theTable, "x");
        te = new SrcTickExpr(pprevxx0);
		Pointer pprevxx1 = new Pointer(theTable, "x");
		IValExpr<MaybeOutside<Double>> veT = new SuccValExp<Double>(pprevxx1, new TExpr());
		StriverSpec prevx = new StriverSpec(te, veT, "prevx");
		Leader lprevx = new Leader(prevx);
		
		// table
		HashMap<String, Leader> leadersMap = new HashMap<>();
		leadersMap.put("s", ls);
		leadersMap.put("r", lr);
		leadersMap.put("x", lx);
		leadersMap.put("rneg", lrneg);
		leadersMap.put("prevx", lprevx);
		theTable.setLeaders(leadersMap);
		
		// pointers
		Pointer ps = new Pointer(theTable, "s");
		Pointer pr = new Pointer(theTable, "r");
		Pointer px = new Pointer(theTable, "x");
		Pointer prneg = new Pointer(theTable, "rneg");
		Pointer pprevx = new Pointer(theTable, "prevx");
		List<Pointer> pointers = Arrays.asList(px, pprevx);
		
			for (Pointer p:pointers)
		for (int i=0;i<10;i++)
				System.out.println(p.getStreamId() + " : "+p.pull());
    }

}

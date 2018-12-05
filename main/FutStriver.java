package main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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
import spec.valueexp.RandomPosIntExpr;
import spec.valueexp.RandomNegIntExpr;

public class FutStriver {

    public static void main(String[] args) {
		Table theTable = new Table();
		// s def
        ITickExpr te = new ConstTickExpr(9);
		IValExpr ve = new RandomPosIntExpr();
		StriverSpec s = new StriverSpec(te, ve, "s");
		Leader ls = new Leader(s);

		// r def
		Pointer prs = new Pointer(theTable, "s");
        te = new SrcTickExpr(prs);
		ve = new RandomPosIntExpr();
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
		ITickExpr terxneg = new AtNegTickExpr(pxrneg);
        te = new UnionTickExpr(tel, terxneg);
		ve = new RandomPosIntExpr();
		StriverSpec x = new StriverSpec(te, ve, "x");
		Leader lx = new Leader(x);
		
		// table
		HashMap<String, Leader> leadersMap = new HashMap<>();
		leadersMap.put("s", ls);
		leadersMap.put("r", lr);
		leadersMap.put("x", lx);
		leadersMap.put("rneg", lrneg);
		theTable.setLeaders(leadersMap);
		
		// pointers
		Pointer ps = new Pointer(theTable, "s");
		Pointer pr = new Pointer(theTable, "r");
		Pointer px = new Pointer(theTable, "x");
		Pointer prneg = new Pointer(theTable, "rneg");
		List<Pointer> pointers = Arrays.asList(ps, pr, px, prneg);
		
			for (Pointer p:pointers)
		for (int i=0;i<10;i++)
				System.out.println(p.getStreamId() + " : "+p.pull());
    }

}

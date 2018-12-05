package main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import semop.Leader;
import semop.Pointer;
import semop.Table;
import spec.StriverSpec;
import spec.tickexp.AtTickExpr;
import spec.tickexp.ConstTickExpr;
import spec.tickexp.ITickExpr;
import spec.tickexp.SrcTickExpr;
import spec.tickexp.UnionTickExpr;
import spec.valueexp.IValExpr;
import spec.valueexp.RandomIntExpr;

public class FutStriver {

    public static void main(String[] args) {
		Table theTable = new Table();
		// s def
        ITickExpr te = new ConstTickExpr(9);
		IValExpr ve = new RandomIntExpr();
		StriverSpec s = new StriverSpec(te, ve, "s");
		Leader ls = new Leader(s);

		// r def
		Pointer prs = new Pointer(theTable, "s");
        te = new SrcTickExpr(prs);
		ve = new RandomIntExpr();
		StriverSpec r = new StriverSpec(te, ve, "r");
		Leader lr = new Leader(r);

		// x def
		Pointer pxr = new Pointer(theTable, "r");
		Pointer pxx = new Pointer(theTable, "x");
		ITickExpr tel = new ConstTickExpr(2);
		ITickExpr terr = new AtTickExpr(pxr);
		ITickExpr terx = new AtTickExpr(pxx);
        te = new UnionTickExpr(tel, terx);
		ve = new RandomIntExpr();
		StriverSpec x = new StriverSpec(te, ve, "x");
		Leader lx = new Leader(x);
		
		// table
		HashMap<String, Leader> leadersMap = new HashMap<>();
		leadersMap.put("s", ls);
		leadersMap.put("r", lr);
		leadersMap.put("x", lx);
		theTable.setLeaders(leadersMap);
		
		// pointers
		Pointer ps = new Pointer(theTable, "s");
		Pointer pr = new Pointer(theTable, "r");
		Pointer px = new Pointer(theTable, "x");
		List<Pointer> pointers = Arrays.asList(ps, pr, px);
		
			for (Pointer p:pointers)
		for (int i=0;i<10;i++)
				System.out.println(p.getStreamId() + " : "+p.pull());
    }

}

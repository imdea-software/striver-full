package main;

import java.util.HashMap;

import semop.Leader;
import semop.Pointer;
import semop.Table;
import spec.StriverSpec;
import spec.tickexp.ConstTickExpr;
import spec.tickexp.ITickExpr;
import spec.tickexp.SrcTickExpr;
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
		
		// table
		HashMap<String, Leader> leadersMap = new HashMap<>();
		leadersMap.put("s", ls);
		leadersMap.put("r", lr);
		theTable.setLeaders(leadersMap);
		
		
		Pointer p = new Pointer(theTable, "r");
		System.out.println(p.pull());
		System.out.println(p.pull());
    }

}

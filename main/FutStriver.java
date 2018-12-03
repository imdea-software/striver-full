package main;

import java.util.HashMap;

import semop.Leader;
import semop.Pointer;
import semop.Table;
import spec.StriverSpec;
import spec.tickexp.ConstExpr;
import spec.tickexp.ITickExpr;
import spec.valueexp.IValExpr;
import spec.valueexp.RandomIntExpr;

public class FutStriver {

    public static void main(String[] args) {
        ITickExpr te = new ConstExpr(9);
		IValExpr ve = new RandomIntExpr();
		StriverSpec s = new StriverSpec(te, ve, "s");
		Leader l = new Leader(s);
		HashMap<String, Leader> leadersMap = new HashMap<>();
		leadersMap.put("s", l);
		Table theTable = new Table(leadersMap);
		Pointer p = new Pointer(theTable, "s");
		System.out.println(p.pull());
    }

}

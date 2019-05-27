package main;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import adts.Constants;
import adts.StriverEvent;
import adts.Unit;
import semop.ILeader;
import semop.Leader;
import semop.Pointer;
import semop.Table;
import spec.StriverSpec;
import spec.tickexp.ITickExpr;
import spec.tickexp.ShiftTickExpr;
import spec.tickexp.nodelayTE.ConstTickExpr;
import spec.tickexp.nodelayTE.SrcTickExpr;
import spec.tickexp.nodelayTE.UnionTickExpr;
import spec.utils.And;
import spec.utils.BoundedSuccExpr;
import spec.utils.Constant;
import spec.utils.Default;
import spec.utils.GeneralFun;
import spec.utils.GtFun;
import spec.utils.IfThenElse;
import spec.utils.Implies;
import spec.utils.LeqFun;
import spec.utils.UnsafeAdd;
import spec.valueexp.CVValExpr;
import spec.valueexp.IValExpr;
import spec.valueexp.PrevEqValExp;
import spec.valueexp.SuccValExp;
import spec.valueexp.tauexp.TExpr;

public class PaperEmpirical {

	private static final Double MAX_SPEED = 1d;
	private static final Double OK_SPEED = 0.4d;
	private static Table theTable = Table.getInstance();
	private static int evs=0;
	private static Double a=0d;
	private static Double b=5d;

    public static void main(String[] args) throws InterruptedException, IOException {
    	
		Thread t = new Thread() {
			public long peakMB = 0L;
			public void run() {
				// report:
				while(true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					/* Total amount of free memory available to the JVM */
					System.gc();
					//Thread.sleep(100);
					// This might be constant because memory goes up during calculation..
					// We should use an external tool or a dedicated thread.
					long mb = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024;
					System.err.println("Peak memory: " + mb + " KB");
					if (mb>peakMB) {
						peakMB = mb;
					}
				}
			}
		};
		t.start();
		// input:
		theTable.setLeader(new ILeader<Boolean>() {
			double nxtTs = 0d;
			double nxtVal = 0;
			Random generator = new Random(5);
			@Override
			public StriverEvent getNext() {
				nxtTs+=3;
				nxtVal = nxtVal + generator.nextDouble()-0.5;
				evs++;
				return new StriverEvent("speed",nxtTs, nxtVal);
			}
			@Override
			public String getStreamName() {
				return "speed";
			}
			
		});

		// outputs:
		Pointer p;
		ITickExpr te;
		IValExpr<Boolean> vebool;
		
		// true: true
		te = new ConstTickExpr(0);
		GeneralFun<Boolean> ve = new GeneralFun<Boolean>(new Constant<Boolean>(true));
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, ve), "true"));

		//toofast: speed > max
		p = theTable.getPointer("speed");
		te = new SrcTickExpr(p);
		p = theTable.getPointer("speed");
		vebool = new GeneralFun<Boolean>(new GtFun(),
				new PrevEqValExp<>(p, new TExpr()),
				new GeneralFun<Object>(new Constant<Object>(MAX_SPEED))
				);
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool), "toofast"));

		//psi: speed < okspeed
		p = theTable.getPointer("speed");
		te = new SrcTickExpr(p);
		p = theTable.getPointer("speed");
		vebool = new GeneralFun<Boolean>(new GtFun(),
				new GeneralFun<Object>(new Constant<Object>(OK_SPEED)),
				new PrevEqValExp<>(p, new TExpr())
				);
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool), "speedok"));
		
		// deaccel: accel < 0
		p = theTable.getPointer("speed");
		te = new SrcTickExpr(p);
		vebool = new GeneralFun<Boolean>(new GtFun(),
				new PrevEqValExp<>(theTable.getPointer("speed"), new TExpr()),
				new SuccValExp<>(theTable.getPointer("speed"), new TExpr())
				);
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool), "deaccel"));
		
		//createUntil("true", "deaccel", -1d, 2d, "event_deaccel");
		
		//createUntil("true", "speedok", 0d, 5d,"to_ok_in_five");

		createUntil("deaccel", "speedok", "deaccels_to_ok_in_five");
		
		// deaccel: accel < 0
		te = new UnionTickExpr(
				new SrcTickExpr(theTable.getPointer("toofast")),
				new SrcTickExpr(theTable.getPointer("deaccels_to_ok_in_five"))
				);
		vebool = new GeneralFun<Boolean>(new Implies(),
				new GeneralFun<Boolean>(
    					new Default<Boolean>(false),
					new PrevEqValExp<>(theTable.getPointer("toofast"), new TExpr())),
				new GeneralFun<Boolean>(
    					new Default<Boolean>(true),
				new PrevEqValExp<>(theTable.getPointer("deaccels_to_ok_in_five"), new TExpr()))
				);
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool), "too_fast_implies_deaccels_to_ok_in_five"));
		

		List<Pointer> pointers = Arrays.asList(
				//theTable.getPointer("speed"),
				theTable.getPointer("too_fast_implies_deaccels_to_ok_in_five")
				);

		double limitTS = 0D;
		while (evs<100000000) {
			for (Pointer pointer:pointers) {
				StriverEvent ev = pointer.pull().getEvent();
				double evTS = ev.getTS();
				//System.out.println(pointer.getStreamId() + "[" + evTS + "] = " + ev.getValue());
				// Keep the pointers close to each other
				while (evTS<limitTS) {
					ev = pointer.pull().getEvent();
					evTS = ev.getTS();
					//System.out.println(pointer.getStreamId() + "[" + evTS + "] = " + ev.getValue());
				}
				limitTS = evTS;
			}
			if (evs%5000==0&&false) {
				/* Total amount of free memory available to the JVM */
				//System.out.println("Free memory (bytes): " + Runtime.getRuntime().freeMemory());
				for (Pointer pointer:theTable.pointers) {
					System.out.println("Pointer " + pointer.myId + " for stream " + pointer.getStreamId() + " next val: " +pointer.myIterator.pnext);
				}
				System.out.println("-----------------------");
			}
		}
		System.exit(0);
    }

    private static void createUntil(String phi, String psi, String out) {
			// shiftedpsia
			Pointer p = theTable.getPointer(psi);
			ITickExpr te = new ShiftTickExpr(p,-a);
			CVValExpr<Boolean> shiftedpsiave = new CVValExpr<>();
			String shiftedpsia = "shifted"+psi+a;
			theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, shiftedpsiave), shiftedpsia));

			// shiftedpsiaT
			p = theTable.getPointer(shiftedpsia);
			te = new SrcTickExpr(p);
			IValExpr<Unit> veUnit = new GeneralFun<Unit>(
					new IfThenElse<>(),
					new CVValExpr<>(),
					new GeneralFun<Unit>(new Constant<Unit>(Unit.unit())),
					new GeneralFun<Object>(new Constant<Object>(Constants.notick()))
					);
			String shiftedpsiaT = shiftedpsia+"T";
			theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, veUnit), shiftedpsiaT));

			// phiF
			p = theTable.getPointer(phi);
			te = new SrcTickExpr(p);
			veUnit = new GeneralFun<Unit>(
					new IfThenElse<>(),
					new CVValExpr<>(),
					new GeneralFun<Object>(new Constant<Object>(Constants.notick())),
					new GeneralFun<Unit>(new Constant<Unit>(Unit.unit()))
					);
			String phiF = phi+"F";
			theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, veUnit), phiF));

			// out
			te = new UnionTickExpr(
					new UnionTickExpr(
							new ShiftTickExpr(theTable.getPointer(psi), -a),
							new ShiftTickExpr(theTable.getPointer(psi), -b)),
					new UnionTickExpr(
							new SrcTickExpr(theTable.getPointer(phi)),
							new ShiftTickExpr(theTable.getPointer(phi), -b)));
			GeneralFun<Boolean> vebool = new GeneralFun<Boolean>(
					new And(),
					new GeneralFun<Boolean>(
							new LeqFun(),
							new GeneralFun<Double>(
									new UnsafeAdd(),
									getMinPsi(shiftedpsia, shiftedpsiaT),
									new GeneralFun<Double>(new Constant<>(a))
									),
							new GeneralFun<Double>(
									new UnsafeAdd(),
									new TExpr(),
									new GeneralFun<Double>(new Constant<>(b))
									)
							),
					new GeneralFun<Boolean>(
							new LeqFun(),
							new GeneralFun<Double>(
									new UnsafeAdd(),
									getMinPsi(shiftedpsia, shiftedpsiaT),
									new GeneralFun<Double>(new Constant<>(a))
									),
							getMinNotPhi(phi,phiF)
							)
					);
			theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool), out));
    }

    private static IValExpr<?> getMinNotPhi(String phi, String phiF) {
		Pointer phiPointer = theTable.getPointer(phi);
		return new GeneralFun<Double>(
				new IfThenElse<>(),
				new GeneralFun<Boolean>(
						new Default<Boolean>(false),
						new PrevEqValExp<Boolean>(phiPointer, new TExpr())),
				new BoundedSuccExpr(phiF, b),
				new TExpr()
				);
    }

    private static IValExpr<?> getMinPsi(String shiftedpsia, String shiftedpsiaT) {
    	Pointer psi = theTable.getPointer(shiftedpsia);
    	return new GeneralFun<Double>(
    			new IfThenElse<>(),
    			new GeneralFun<Boolean>(
    					new Default<Boolean>(false),
    					new PrevEqValExp<Boolean>(psi, new TExpr())),
    			new TExpr(),
    			new BoundedSuccExpr(shiftedpsiaT, b-a)
    			);
    }

}

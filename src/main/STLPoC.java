package main;

import java.util.Arrays;
import java.util.List;

import adts.Constants;
import adts.MaybeReentrant;
import adts.StriverEvent;
import semop.CsvLeader;
import semop.Leader;
import semop.Pointer;
import semop.Table;
import spec.StriverSpec;
import spec.tickexp.ShiftTickExpr;
import spec.tickexp.ITickExpr;
import spec.tickexp.nodelayTE.SrcTickExpr;
import spec.tickexp.nodelayTE.UnionTickExpr;
import spec.utils.And;
import spec.utils.Constant;
import spec.utils.Default;
import spec.utils.DivisionFun;
import spec.utils.GeneralFun;
import spec.utils.GtFun;
import spec.utils.IfThenElse;
import spec.utils.Implies;
import spec.utils.LeqFun;
import spec.utils.MinusFun;
import spec.utils.UnsafeAdd;
import spec.valueexp.CVValExpr;
import spec.valueexp.IValExpr;
import spec.valueexp.PrevEqValExp;
import spec.valueexp.PrevValExp;
import spec.valueexp.tauexp.PrevExp;
import spec.valueexp.tauexp.SuccExp;
import spec.valueexp.tauexp.TExpr;

public class STLPoC {

	private static final Double MAX_SPEED = 1d;
	private static final Double OK_SPEED = 0.4d;
	private static final Double b = 2d;
	private static Table theTable = new Table();

    public static void main(String[] args) throws InterruptedException {
    	
		
		// inputs:
		/*theTable.setLeader(new ILeader<Boolean>() {
			int nxt = 0;
			@Override
			public StriverEvent getNext() {
				nxt+=3;
				return new StriverEvent(nxt, Math.random() < 0.5);
			}
			
		}, "phi");*/
		
		/*theTable.setLeader(new ILeader<Boolean>() {
			boolean given = false;
			@Override
			public StriverEvent getNext() {
				if (given) {
					return StriverEvent.posOutsideEv;
				}
				given = true;
				return new StriverEvent(0, true);
			}
			
		}, "phi");*/

		CsvLeader csvleader = new CsvLeader("/Users/felipe.gorostiaga/eclipse-workspace/FutStriverJava/data.csv");
		theTable.setLeader(csvleader);
		
		// outputs:
		Pointer p;
		ITickExpr te;
		IValExpr<Boolean> vebool;
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
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool), "psi"));
		
		// acceleration: (speed(~t) - speed(<t)) / (t - speed<<t)
		p = theTable.getPointer("speed");
		te = new SrcTickExpr(p);
		IValExpr<Double> vedouble = new GeneralFun<Double>(new DivisionFun(),
				new GeneralFun<Double>(new MinusFun(),
					new PrevEqValExp<>(theTable.getPointer("speed"), new TExpr()),
					new GeneralFun<Double>(
						new Default<Double>(0d),
						new PrevValExp<Double>(theTable.getPointer("speed"), new TExpr()))
					),
				new GeneralFun<Double>(new MinusFun(),
					new TExpr(),
					new GeneralFun<Double>(
						new Default<Double>(0d),
						new PrevExp(theTable.getPointer("speed"), new TExpr()))
					));
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vedouble), "accel"));
		
		// phi: accel < 0
		p = theTable.getPointer("accel");
		te = new SrcTickExpr(p);
		p = theTable.getPointer("accel");
		vebool = new GeneralFun<Boolean>(new GtFun(),
				new GeneralFun<Object>(new Constant<Object>(0d)),
				new PrevEqValExp<>(p, new TExpr())
				);
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool), "phi"));
		
		
		// phifalse:
		p = theTable.getPointer("phi");
		te = new SrcTickExpr(p);
		p = theTable.getPointer("phi");
		vebool = new GeneralFun<Boolean>(new IfThenElse<>(),
				new PrevEqValExp<>(p, new TExpr()),
				new GeneralFun<Object>(new Constant<Object>(Constants.notick())),
				new GeneralFun<Object>(new Constant<Object>(false))
				);
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool), "phiFalse"));
		
		//psitrue:
		p = theTable.getPointer("psi");
		te = new SrcTickExpr(p);
		p = theTable.getPointer("psi");
		vebool = new GeneralFun<Boolean>(new IfThenElse<>(),
				new PrevEqValExp<>(p, new TExpr()),
				new GeneralFun<Object>(new Constant<Object>(true)),
				new GeneralFun<Object>(new Constant<Object>(Constants.notick()))
				);
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool), "psiTrue"));
		
		// shiftphi
		p = theTable.getPointer("phi");
		te = new ShiftTickExpr(p,-b);
		vebool = new CVValExpr<>();
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool), "shiftphi"));

		// shiftpsi
		p = theTable.getPointer("psi");
		te = new ShiftTickExpr(p,-b);
		vebool = new CVValExpr<Boolean>();
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool), "shiftpsi"));
		
		// until[0, win]
		SrcTickExpr phisrc = new SrcTickExpr(theTable.getPointer("phi"));
		SrcTickExpr psisrc = new SrcTickExpr(theTable.getPointer("psi"));
		SrcTickExpr shiftphisrc = new SrcTickExpr(theTable.getPointer("shiftphi"));
		SrcTickExpr shiftpsisrc = new SrcTickExpr(theTable.getPointer("shiftpsi"));
		te = new UnionTickExpr(new UnionTickExpr(phisrc, psisrc), new UnionTickExpr(shiftphisrc, shiftpsisrc));
		vebool = new GeneralFun<Boolean>(
					new And(),
					new GeneralFun<Boolean>(
						new LeqFun(),
						getMinPsi(),
						new GeneralFun<Double>(
							new UnsafeAdd(),
							new TExpr(),
							new GeneralFun<Double>(new Constant<>(b))
						)
						),
					new GeneralFun<Boolean>(
						new GtFun(),
						getMinNotPhi(),
						getMinPsi()
						)
				);
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool), "until"));
		
		// property: toofast -> deacceleration until slow enough
		te = new SrcTickExpr(theTable.getPointer("speed"));
		vebool = new GeneralFun<Boolean>(new Implies(),
				new PrevEqValExp<Boolean>(theTable.getPointer("toofast"), new TExpr()),
				new PrevEqValExp<Boolean>(theTable.getPointer("until"), new TExpr())
		);
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool), "property"));
		
		// pointers
		Pointer phi = theTable.getPointer("phi");
		Pointer phitrue = theTable.getPointer("phiFalse");
		Pointer psi = theTable.getPointer("psi");
		Pointer psifalse = theTable.getPointer("psiTrue");
		Pointer shiftphi = theTable.getPointer("shiftphi");
		Pointer shiftpsi = theTable.getPointer("shiftpsi");
		List<Pointer> pointers = Arrays.asList(phi, phitrue, psi, psifalse, shiftphi, shiftpsi, theTable.getPointer("property"));
		//Pointer speed = theTable.getPointer("speed");
		//Pointer accel = theTable.getPointer("accel");
		Pointer prop = theTable.getPointer("property");
		
		long lastReport = System.currentTimeMillis();
		while (true) {
			long now = System.currentTimeMillis();
			if (!true) {
				/*MaybeReentrant ev = speed.pull();
				System.out.println("speed");
				System.out.println(ev);
				ev = accel.pull();
				System.out.println("accel");
				System.out.println(ev);
				System.out.println("until");*/
				MaybeReentrant ev = prop.pull();
				//System.out.println(ev);
				if (csvleader.processedEvents()%50000 == 0) {
				  /* Total amount of free memory available to the JVM */
				  System.err.println("Processed events: " + csvleader.processedEvents());
				  System.err.println("Used memory: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024/1024 + " MB");
				  //Thread.sleep(1000);
				  lastReport = now;
				}
				if (ev.getEvent() == StriverEvent.posOutsideEv)
					System.exit(0);
				continue;
			}
			if (!true) {
				prop.pull();
				if (now - lastReport > 2000) {
					/* Total amount of free memory available to the JVM */
					//System.out.println("Free memory (bytes): " + Runtime.getRuntime().freeMemory());
					for (Pointer pointer:theTable.pointers) {
						System.out.println("Pointer " + pointer.myId + " for stream " + pointer.getStreamId() + " next val: " +pointer.myIterator.pnext);
					}
					System.out.println("-----------------------");
					lastReport = now;
				}
				continue;
			}
			for (Pointer pointer:pointers) {
				System.out.println(pointer.getStreamId());
				StriverEvent ev = new StriverEvent(null,0,true);
				Object lastval = null;
				while (ev.getTS()<100) {
					ev = pointer.pull().getEvent();
					Object newValue = ev.getValue();
					if (!newValue.equals(lastval) && newValue != Constants.notick()) {
						System.out.println(ev.getTS() + " " + lastval);
						System.out.println(ev.getTS() + " " + newValue);
						lastval = newValue;
					}
				}
			}
			System.exit(0);
			System.out.println("-----------------------");
		}
    }

	private static IValExpr<?> getMinNotPhi() {
	    Pointer phi = theTable.getPointer("phi");
	    Pointer phifalse = theTable.getPointer("phiFalse");
		return new GeneralFun<Double>(
				new IfThenElse<>(),
				new GeneralFun<Boolean>(
					new Default<Boolean>(false),
					new PrevEqValExp<Boolean>(phi, new TExpr())),
				new SuccExp(phifalse, new TExpr()),
				new TExpr()
				);
	}

	private static IValExpr<?> getMinPsi() {
	    Pointer psi = theTable.getPointer("psi");
	    Pointer psitrue = theTable.getPointer("psiTrue");
		return new GeneralFun<Double>(
				new IfThenElse<>(),
				new GeneralFun<Boolean>(
					new Default<Boolean>(false),
					new PrevEqValExp<Boolean>(psi, new TExpr())),
				new TExpr(),
				new SuccExp(psitrue, new TExpr())
				);
	}

}

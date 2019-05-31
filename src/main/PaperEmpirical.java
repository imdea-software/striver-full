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
import spec.tickexp.nodelayTE.SrcTickExpr;
import spec.tickexp.nodelayTE.UnionTickExpr;
import spec.utils.And;
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
import spec.valueexp.generics.SuccBoundedExpr;
import spec.valueexp.tauexp.SuccExp;
import spec.valueexp.tauexp.TExpr;

public class PaperEmpirical {

	private static final Double MAX_SPEED = 1d;
	private static final Double OK_SPEED = -1d;
	private static Table theTable = Table.getInstance();
	private static int evs=0;
	private static final Double a=0d;
	private static Double b=5d;
	private static boolean bounded;

	public static void main(String[] args) throws InterruptedException, IOException {
		if (args.length<3) {
			System.out.println("Not enough arguments");
			System.out.println("Arg 1: b (win size)");
			System.out.println("Arg 2: events per second (rate)");
			System.out.println("Arg 3: bounded/unbounded");
			System.exit(0);
		}
		b = Double.parseDouble(args[0]);
		double timediff = 1/Double.parseDouble(args[1]);
		bounded = "bounded".equals(args[2]);
		/*System.out.println("Win size: "+b);
		System.out.println("Evs per second: "+args[1]);
		System.out.println("Future bounded: "+bounded);*/
		// input:
		theTable.setLeader(new ILeader<Boolean>() {
			double nxtTs = 0d;
			double nxtVal = 0;
			int sgn = 1;
			Random generator = new Random(5);
			int counter = 0;
			double lastkb = 0d;
			@Override
			public StriverEvent getNext() {
				nxtTs+=timediff;
				double nxtOff = generator.nextDouble()-0.5+(0.3*sgn);
				sgn = nxtOff<0?-1:1;
				nxtVal = nxtVal + nxtOff;
				evs++;
				if (evs%250==0) {
					System.gc();
					try { Thread.sleep(100); } catch (InterruptedException e) {}
					long kb = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024;
					if (kb==lastkb) counter++; else counter=0;
					lastkb=kb;
					if (true||counter>5) {
						//System.out.println("Win size " + b + "; Rate: " + args[1] + "; Used memory: " + kb);
						System.exit(0);
					}
				}
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

		//toofast:
		/*
		 * ticks double toofast = speed.ticks
		 * define bool toofast = cv > MAX_SPEED
		 */
		p = theTable.getPointer("speed");
		te = new SrcTickExpr(p);
		vebool = new GeneralFun<Boolean>(new GtFun(),
				new CVValExpr<Double>(),
				//new PrevEqValExp<>(theTable.getPointer("speed"), new TExpr()),
				new GeneralFun<Object>(new Constant<Object>(MAX_SPEED))
				);
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool), "toofast"));

		//speedok:
		/*
		 * ticks dobule speedok = speed.ticks
		 * define bool speedok = OK_SPEED > cv
		 */
		p = theTable.getPointer("speed");
		te = new SrcTickExpr(p);
		vebool = new GeneralFun<Boolean>(new GtFun(),
				new GeneralFun<Object>(new Constant<Object>(OK_SPEED)),
				new CVValExpr<Double>()
				//new PrevEqValExp<>(theTable.getPointer("speed"), new TExpr())
				);
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool), "speedok"));

		// deaccellerating:
		/*
		 * ticks double deaccellerating = speed.ticks
		 * define double deaccellerating = cv > speed(>t)
		 */
		p = theTable.getPointer("speed");
		te = new SrcTickExpr(p);
		vebool = new GeneralFun<Boolean>(new GtFun(),
				new CVValExpr<Double>(),
				//new PrevEqValExp<>(theTable.getPointer("speed"), new TExpr()),
				new SuccValExp<>(theTable.getPointer("speed"), new TExpr())
				);
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool), "deaccellerating"));

		// deaccels_to_ok_in_win = deaccellerating U[a,b] speedok
		createUntil("deaccellerating", "speedok", "deaccels_to_ok_in_win");

		// too_fast_implies_deaccels_to_ok_in_window
		/*
		 * ticks (bool,bool) TFIDOIW = toofast.ticks U DTOIW.ticks
		 * define bool TFIDOIW = toofast(~t|false) -> DTOIW(~t|true)
		 */
		te = new UnionTickExpr(
				new SrcTickExpr(theTable.getPointer("toofast")),
				new SrcTickExpr(theTable.getPointer("deaccels_to_ok_in_win"))
				);
		vebool = new GeneralFun<Boolean>(new Implies(),
				new GeneralFun<Boolean>(
						new Default<Boolean>(false),
						new PrevEqValExp<>(theTable.getPointer("toofast"), new TExpr())),
				new GeneralFun<Boolean>(
						new Default<Boolean>(true),
						new PrevEqValExp<>(theTable.getPointer("deaccels_to_ok_in_win"), new TExpr()))
				);
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool), "too_fast_implies_deaccels_to_ok_in_win"));

		// interesting:
		te = new UnionTickExpr(
				new SrcTickExpr(theTable.getPointer("toofast")),
				new SrcTickExpr(theTable.getPointer("deaccels_to_ok_in_win"))
				);
		vebool = new GeneralFun<Boolean>(new And(),
				new GeneralFun<Boolean>(
						new Default<Boolean>(false),
						new PrevEqValExp<>(theTable.getPointer("toofast"), new TExpr())),
				new GeneralFun<Boolean>(
						new Default<Boolean>(true),
						new PrevEqValExp<>(theTable.getPointer("deaccels_to_ok_in_win"), new TExpr()))
				);
		theTable.setLeader(new Leader<Boolean>(new StriverSpec(te, vebool), "interesting"));

		List<Pointer> pointers = Arrays.asList(
				theTable.getPointer("speed"),
				theTable.getPointer("interesting"),
				theTable.getPointer("too_fast_implies_deaccels_to_ok_in_win")
				);

		double limitTS = 0D;
		while (true) {
			for (Pointer pointer:pointers) {
				StriverEvent ev = pointer.pull().getEvent();
				double evTS = ev.getTS();
				System.out.println(pointer.getStreamId() + "[" + evTS + "] = " + ev.getValue());
				// Keep the pointers close to each other
				while (evTS<limitTS) {
					ev = pointer.pull().getEvent();
					evTS = ev.getTS();
					System.out.println(pointer.getStreamId() + "[" + evTS + "] = " + ev.getValue());
				}
				limitTS = evTS;
			}
		}
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
		Pointer phiFpointer = theTable.getPointer(phiF);
		IValExpr<?> succExpr=bounded?
				new SuccBoundedExpr(phiFpointer, b):
					new SuccExp(phiFpointer, new TExpr());
				return new GeneralFun<Double>(
						new IfThenElse<>(),
						new GeneralFun<Boolean>(
								new Default<Boolean>(false),
								new PrevEqValExp<Boolean>(phiPointer, new TExpr())),
						succExpr,
						new TExpr()
						);
	}

	private static IValExpr<?> getMinPsi(String shiftedpsia, String shiftedpsiaT) {
		Pointer psi = theTable.getPointer(shiftedpsia);
		Pointer shiftedpsiaTpointer = theTable.getPointer(shiftedpsiaT);
		IValExpr<?> succExpr = bounded?
				new SuccBoundedExpr(shiftedpsiaTpointer, b-a):
					new SuccExp(shiftedpsiaTpointer, new TExpr());
				return new GeneralFun<Double>(
						new IfThenElse<>(),
						new GeneralFun<Boolean>(
								new Default<Boolean>(false),
								new PrevEqValExp<Boolean>(psi, new TExpr())),
						new TExpr(),
						succExpr
						);
	}

}

package eddie.wu.manual;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class SymmetryStatistic {
	private static final Logger log = Logger.getLogger(SymmetryStatistic.class);

	private int manualIndex;

	/**
	 * how many steps with symmetry. at least 1;
	 */
	private int stepsSym;
	/**
	 * total number of symmetry in the manual, at lease 8.
	 */
	private int syms;
	/**
	 * the last step with symmetry
	 */
	private int lastStepSym;

	public int getStepsSym() {
		return stepsSym;
	}

	public void setStepsSym(int stepsSym) {
		this.stepsSym = stepsSym;
	}

	public int getSyms() {
		return syms;
	}

	public void setSyms(int syms) {
		this.syms = syms;
	}

	public int getLastStepSym() {
		return lastStepSym;
	}

	public void setLastStepSym(int lastStepSym) {
		this.lastStepSym = lastStepSym;
	}

	public SymmetryStatistic(int manualIndex, List<StepSymmetry> stepSymmetris) {
		this.manualIndex = manualIndex;
		this.stepsSym = stepSymmetris.size();
		this.lastStepSym = stepSymmetris.get(stepSymmetris.size() - 1)
				.getStepIndex();
		for (StepSymmetry sym : stepSymmetris) {
			this.syms += sym.getSymmetry().getNumberOfSymmetry();
		}
	}

	public static void statistic(List<SymmetryStatistic> list) {
		// thi
		int maxStepSyms = 0;
		int maxStepIndex = 0;
		int maxSyms = 0;
		int maxSymIndex = 0;
		int maxLastStep = 0;
		int maxLastIndex = 0;
		for (SymmetryStatistic stat : list) {
			if (stat.stepsSym > maxStepSyms) {
				maxStepSyms = stat.stepsSym;
				maxStepIndex = stat.manualIndex;
				if (log.isEnabledFor(Level.WARN))
					log.warn("stepsSym = " + stat.stepsSym + ", index = "
							+ stat.manualIndex);
			}
			if (stat.syms > maxSyms) {
				maxSyms = stat.syms;
				maxSymIndex = stat.manualIndex;
				if (log.isEnabledFor(Level.WARN))
					log.warn("Sym = " + stat.syms + ", index = "
							+ stat.manualIndex);
			}
			if (stat.lastStepSym > maxLastStep) {
				maxLastStep = stat.lastStepSym;
				maxLastIndex = stat.manualIndex;
				if (log.isEnabledFor(Level.WARN))
					log.warn("laststep = " + stat.lastStepSym + ", index = "
							+ stat.manualIndex);
			}
		}
		if (log.isEnabledFor(Level.WARN))
			log.warn("maxStepSym = " + maxStepSyms + ", at go manual "
					+ maxStepIndex);
		if (log.isEnabledFor(Level.WARN))
			log.warn("maxSym = " + maxSyms + ", at go manual " + maxSymIndex);
		if (log.isEnabledFor(Level.WARN))
			log.warn("maxLastStep = " + maxLastStep + ", at go manual "
					+ maxLastIndex);
	}
}

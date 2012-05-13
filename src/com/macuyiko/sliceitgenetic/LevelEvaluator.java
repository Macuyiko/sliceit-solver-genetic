package com.macuyiko.sliceitgenetic;

import java.util.List;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

public class LevelEvaluator implements FitnessEvaluator<List<SplitLine>> {
	
	private Level targetLevel;

	public LevelEvaluator(Level targetLevel) {
		this.targetLevel = targetLevel;
		targetLevel.reset();
	}

	public double getFitness(List<SplitLine> candidate,
			List<? extends List<SplitLine>> population) {
		
		Level eval = targetLevel.clone();
		for (SplitLine sl : candidate)
			eval.applyLineSplit(sl.x1, sl.y1, sl.x2, sl.y2);
		return eval.getFitness();
	}

	public boolean isNatural() {
		return false;
	}

}

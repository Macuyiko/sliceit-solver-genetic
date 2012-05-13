package com.macuyiko.sliceitgenetic.mutations;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import com.macuyiko.sliceitgenetic.SplitLine;

public class RemoveSplitLineMutation implements EvolutionaryOperator<List<SplitLine>> {
	private final NumberGenerator<Probability> removePolygonProbability;

	public RemoveSplitLineMutation(NumberGenerator<Probability> removePolygonProbability) {
		this.removePolygonProbability = removePolygonProbability;
	}

	public RemoveSplitLineMutation(Probability removePolygonProbability) {
		this(new ConstantGenerator<Probability>(removePolygonProbability));
	}

	public List<List<SplitLine>> apply(List<List<SplitLine>> selectedCandidates, Random rng) {
		
		List<List<SplitLine>> mutatedCandidates = 
				new ArrayList<List<SplitLine>>(selectedCandidates.size());
		
		for (List<SplitLine> candidate : selectedCandidates) {
			if (candidate.size() > 1 && removePolygonProbability.nextValue().nextEvent(rng)) {
				List<SplitLine> newLines = new ArrayList<SplitLine>(candidate);
				newLines.remove(rng.nextInt(newLines.size()));
				mutatedCandidates.add(newLines);
			} else {
				mutatedCandidates.add(candidate);
			}
		}
		return mutatedCandidates;
	}
}

package com.macuyiko.sliceitgenetic.mutations;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import com.macuyiko.sliceitgenetic.SplitLine;
import com.macuyiko.sliceitgenetic.SplitLineFactory;

public class MoveSplitLineMutation implements EvolutionaryOperator<List<SplitLine>> {
	private final NumberGenerator<Probability> movePolygonProbability;
	private SplitLineFactory factory;

	public MoveSplitLineMutation(NumberGenerator<Probability> movePolygonProbability,
			SplitLineFactory factory) {
		this.factory = factory;
		this.movePolygonProbability = movePolygonProbability;
	}

	public MoveSplitLineMutation(Probability replacePolygonProbability,
			SplitLineFactory factory) {
		this(new ConstantGenerator<Probability>(replacePolygonProbability), factory);
	}

	public List<List<SplitLine>> apply(List<List<SplitLine>> selectedCandidates, Random rng) {
		List<List<SplitLine>> mutatedCandidates = new ArrayList<List<SplitLine>>(
				selectedCandidates.size());
		for (List<SplitLine> candidate : selectedCandidates) {
			if (movePolygonProbability.nextValue().nextEvent(rng)) {
				List<SplitLine> newPolygons = new ArrayList<SplitLine>(candidate);
				newPolygons.remove(rng.nextInt(newPolygons.size()));
				newPolygons.add(rng.nextInt(newPolygons.size() + 1), factory.createRandomLine(rng));
				mutatedCandidates.add(newPolygons);
			} else {
				mutatedCandidates.add(candidate);
			}
		}
		return mutatedCandidates;
	}
}

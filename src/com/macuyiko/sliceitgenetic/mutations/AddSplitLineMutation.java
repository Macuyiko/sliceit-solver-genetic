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

public class AddSplitLineMutation implements EvolutionaryOperator<List<SplitLine>> {
	private final NumberGenerator<Probability> addPolygonProbability;
	private final SplitLineFactory factory;
	private final int maxPolygons;

	public AddSplitLineMutation(
			NumberGenerator<Probability> addPolygonProbability,
			SplitLineFactory factory, int maxPolygons) {
		if (maxPolygons < 2) {
			throw new IllegalArgumentException("Max polygons must be > 1.");
		}
		this.addPolygonProbability = addPolygonProbability;
		this.factory = factory;
		this.maxPolygons = maxPolygons;
	}

	public AddSplitLineMutation(Probability addPolygonProbability,
			SplitLineFactory factory, int maxPolygons) {
		this(new ConstantGenerator<Probability>(addPolygonProbability),
				factory, maxPolygons);
	}

	public List<List<SplitLine>> apply(List<List<SplitLine>> selectedCandidates, Random rng) {
		List<List<SplitLine>> mutatedCandidates = new ArrayList<List<SplitLine>>(selectedCandidates.size());
		for (List<SplitLine> candidate : selectedCandidates) {
			if (candidate.size() < maxPolygons && addPolygonProbability.nextValue().nextEvent(rng)) {
				List<SplitLine> newPolygons = new ArrayList<SplitLine>(candidate);
				newPolygons.add(rng.nextInt(newPolygons.size() + 1), factory.createRandomLine(rng));
				mutatedCandidates.add(newPolygons);
			} else {
				mutatedCandidates.add(candidate);
			}
		}
		return mutatedCandidates;
	}

}

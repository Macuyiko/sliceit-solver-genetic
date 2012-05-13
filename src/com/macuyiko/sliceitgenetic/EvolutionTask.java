package com.macuyiko.sliceitgenetic;

import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JComponent;

import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.random.GaussianGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.maths.random.XORShiftRNG;
import org.uncommons.swing.SwingBackgroundTask;
import org.uncommons.watchmaker.framework.CachingFitnessEvaluator;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.SelectionStrategy;
import org.uncommons.watchmaker.framework.TerminationCondition;
import org.uncommons.watchmaker.framework.interactive.Renderer;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.operators.ListCrossover;
import org.uncommons.watchmaker.framework.operators.ListOperator;
import org.uncommons.watchmaker.framework.selection.TournamentSelection;
import org.uncommons.watchmaker.swing.ProbabilityParameterControl;
import org.uncommons.watchmaker.swing.evolutionmonitor.EvolutionMonitor;

import com.macuyiko.sliceitgenetic.mutations.AddSplitLineMutation;
import com.macuyiko.sliceitgenetic.mutations.AdjustSplitLineMutation;
import com.macuyiko.sliceitgenetic.mutations.MoveSplitLineMutation;
import com.macuyiko.sliceitgenetic.mutations.RemoveSplitLineMutation;

public class EvolutionTask extends SwingBackgroundTask<List<SplitLine>> {
	private MainFrame parent;
	private final int populationSize;
	private final int eliteCount;
	private final TerminationCondition[] terminationConditions;
	private EvolutionMonitor<List<SplitLine>> monitor;
	private SplitLineFactory factory;

	EvolutionTask(MainFrame parent, int populationSize,
			int eliteCount, TerminationCondition... terminationConditions) {
		this.parent = parent;
		this.populationSize = populationSize;
		this.eliteCount = eliteCount;
		this.terminationConditions = terminationConditions;
		
		Renderer<List<SplitLine>, JComponent> renderer = new LevelSwingRenderer(parent);
        monitor = new EvolutionMonitor<List<SplitLine>>(renderer, false);
	}

	protected List<SplitLine> performTask() throws Exception {
		ProbabilityParameterControl selectionControl = 
				new ProbabilityParameterControl(
						Probability.EVENS, Probability.ONE, 2, new Probability(0.7));

		Random rng = new XORShiftRNG();
		
		FitnessEvaluator<List<SplitLine>> evaluator = 
				new CachingFitnessEvaluator<List<SplitLine>>(
						new LevelEvaluator(parent.getLevel()));

		factory = 
				new SplitLineFactory(
					parent.getLevel().getOriginalPolygon().getBounds(), 
					parent.getLevel().getNrTargetAreas());

		EvolutionaryOperator<List<SplitLine>> pipeline = 
				createEvolutionPipeline(factory, parent.getLevel().getOriginalPolygon().getBounds(), rng);

		SelectionStrategy<Object> selection = 
				new TournamentSelection(selectionControl.getNumberGenerator());

		EvolutionEngine<List<SplitLine>> engine = 
				new GenerationalEvolutionEngine<List<SplitLine>>(
						factory, pipeline, evaluator, selection, rng);

		engine.addEvolutionObserver(monitor);
		new VisualizationFrame(monitor);

		return engine.evolve(populationSize, eliteCount, terminationConditions);
	}

	public EvolutionaryOperator<List<SplitLine>> createEvolutionPipeline(
			SplitLineFactory factory, Rectangle boundingBox, Random rng) {
		List<EvolutionaryOperator<List<SplitLine>>> operators = new LinkedList<EvolutionaryOperator<List<SplitLine>>>();

		ProbabilityParameterControl crossOverControl = new ProbabilityParameterControl(
				Probability.ZERO, Probability.ONE, 3, new Probability(0.50d));
		ProbabilityParameterControl moveLineControl = new ProbabilityParameterControl(
				Probability.ZERO, Probability.ONE, 3, new Probability(0.10d));
		ProbabilityParameterControl moveControl = new ProbabilityParameterControl(
				Probability.ZERO, Probability.ONE, 3, new Probability(0.03d));

		operators.add(new ListCrossover<SplitLine>(
				new ConstantGenerator<Integer>(2), 
				crossOverControl.getNumberGenerator()));
		operators.add(new MoveSplitLineMutation(moveControl.getNumberGenerator(), factory));
		operators.add(new RemoveSplitLineMutation(moveControl.getNumberGenerator()));
        operators.add(new AddSplitLineMutation(moveControl.getNumberGenerator(), factory, 50));
        
        operators.add(new ListOperator<SplitLine>(new AdjustSplitLineMutation(
				boundingBox, moveLineControl.getNumberGenerator(),
				new GaussianGenerator(0, 3, rng))));

		return new EvolutionPipeline<List<SplitLine>>(operators);
	}

	protected void postProcessing(List<SplitLine> result) {
		parent.unblockUI();
	}

	protected void onError(Throwable throwable) {
		try {
			throw throwable;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		parent.unblockUI();
	}
}

package com.macuyiko.sliceitgenetic.mutations;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.uncommons.maths.Maths;
import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import com.macuyiko.sliceitgenetic.SplitLine;

public class AdjustSplitLineMutation implements EvolutionaryOperator<SplitLine>
{
    private final NumberGenerator<? extends Number> changeAmount;
    private final Rectangle canvasSize;
    private final NumberGenerator<Probability> mutationProbability;
    
    public AdjustSplitLineMutation(Rectangle canvasSize,
            NumberGenerator<Probability> mutationProbability,
            NumberGenerator<? extends Number> changeAmount) {
    	this.mutationProbability = mutationProbability;
        this.canvasSize = canvasSize;
        this.changeAmount = changeAmount;
    }
    
    public AdjustSplitLineMutation(Rectangle canvasSize,
            Probability mutationProbability,
            NumberGenerator<? extends Number> changeAmount) {
    	this(canvasSize, new ConstantGenerator<Probability>(mutationProbability), changeAmount);
    }

    protected NumberGenerator<Probability> getMutationProbability() {
        return mutationProbability;
    }

    public List<SplitLine> apply(List<SplitLine> lines, Random rng) {
        List<SplitLine> newLines = new ArrayList<SplitLine>(lines.size());
        for (SplitLine line : lines) {
        	SplitLine newLine = mutateSplitLine(line, rng);
            newLines.add(newLine);
        }
        return newLines;
    }

    protected SplitLine mutateSplitLine(SplitLine oldLine, Random rng) {
        if (getMutationProbability().nextValue().nextEvent(rng)) {
        	SplitLine newLine = new SplitLine();
            int xDelta = (int) Math.round(changeAmount.nextValue().doubleValue());
            int yDelta = (int) Math.round(changeAmount.nextValue().doubleValue());
            int index = rng.nextInt(2);
            int oldx = index == 0 ? oldLine.x1 : oldLine.x2;
            int oldy = index == 0 ? oldLine.y1 : oldLine.y2;
            int newX = oldx + xDelta;
            int newY = oldy + yDelta;
            newX = Maths.restrictRange(newX, canvasSize.x-10, canvasSize.x + canvasSize.width + 20);
            newY = Maths.restrictRange(newY, canvasSize.y-10, canvasSize.y + canvasSize.height + 20);
            if (index == 0) {
            	newLine.x1 = newX;
            	newLine.y1 = newY;
            	newLine.x2 = oldLine.x2;
            	newLine.y2 = oldLine.y2;
            } else {
            	newLine.x1 = oldLine.x1;
            	newLine.y1 = oldLine.y1;
            	newLine.x2 = newX;
            	newLine.y2 = newY;
            }
            return newLine;
        }else{
            return oldLine;
        }
    }
}

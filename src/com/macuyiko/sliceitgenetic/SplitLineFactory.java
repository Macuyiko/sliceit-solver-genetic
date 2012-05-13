package com.macuyiko.sliceitgenetic;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

public class SplitLineFactory extends AbstractCandidateFactory<List<SplitLine>> {
	private final Rectangle boundingBox;
	private final int linesNr;

	public SplitLineFactory(Rectangle boundingBox, int number) {
		this.boundingBox = boundingBox;
		this.linesNr = number;
	}

	public List<SplitLine> generateRandomCandidate(Random rng) {
		List<SplitLine> lines = new ArrayList<SplitLine>(linesNr);
		for (int i = 0; i < linesNr; i++)
			lines.add(createRandomLine(rng));
		return lines;
	}

	public SplitLine createRandomLine(Random rng) {
		SplitLine line = new SplitLine();
		int dir1 = rng.nextInt(4);
		int dir2 = rng.nextInt(4);
		if (dir1 == 0) {
			line.x1 = boundingBox.x-5;
			line.y1 = boundingBox.y-10 + rng.nextInt(boundingBox.height+20);	
		} else if (dir1 == 1) {
			line.x1 = boundingBox.width+15;
			line.y1 = boundingBox.y-10 + rng.nextInt(boundingBox.height+20);	
		} else if (dir1 == 2) {
			line.x1 = boundingBox.x-10 + rng.nextInt(boundingBox.width+20);	
			line.y1 = boundingBox.y-5;
		} else {
			line.x1 = boundingBox.x-10 + rng.nextInt(boundingBox.width+20);	
			line.y1 = boundingBox.height+15;
		}
		if (dir2 == 0) {
			line.x2 = boundingBox.x-5;
			line.y2 = boundingBox.y-10 + rng.nextInt(boundingBox.height+20);	
		} else if (dir2 == 1) {
			line.x2 = boundingBox.width+15;
			line.y2 = boundingBox.y-10 + rng.nextInt(boundingBox.height+20);	
		} else if (dir2 == 2) {
			line.x2 = boundingBox.x-10 + rng.nextInt(boundingBox.width+20);	
			line.y2 = boundingBox.y-5;
		} else {
			line.x2 = boundingBox.x-10 + rng.nextInt(boundingBox.width+20);	
			line.y2 = boundingBox.height+15;
		}
		return line;
	}
}

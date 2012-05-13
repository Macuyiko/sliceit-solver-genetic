package com.macuyiko.sliceitgenetic;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

public class Level {
	private List<Polygon> polygons;
	private Polygon originalPolygon;
	private int nrTargetAreas;
	private int nrTargetStrokes;
	@SuppressWarnings("unused")
	private double targetArea;
	private double targetPercentage;
	private double countErrorMargin = 1D;
	private double diffErrorMargin = 3D;
	private double totalArea;
	private int strokes;
	
	public Level(Polygon p, int ta, int ts) {
		originalPolygon = p;
		reset();
		nrTargetAreas = ta;
		nrTargetStrokes = ts;
		totalArea = PolygonUtils.getArea(originalPolygon);
		targetArea = totalArea / (double) ta;
		targetPercentage = 100D / nrTargetAreas;
	}
	
	public boolean isPolygonCounts(Polygon p) {
		double area = PolygonUtils.getArea(p);
		double percentage = area / totalArea * 100;
		return (percentage > countErrorMargin);
	}
	
	public int getNrAreas() {
		int realAreas = 0;
		for (int i = 0; i < polygons.size(); i++) {
			Polygon p = polygons.get(i);
			if (!isPolygonCounts(p))
				continue;
			realAreas++;
		}
		return realAreas;
	}
	
	public boolean isGameOver() {
		return getNrAreas() >= nrTargetAreas 
				|| getNrStrokes() >= nrTargetStrokes;
	}
	
	public boolean isGameWon() {
		if (getNrAreas() != nrTargetAreas)
			return false;
		if (strokes != nrTargetStrokes)
			return false;
		for (int i = 0; i < polygons.size(); i++) {
			Polygon p = polygons.get(i);
			if (!isPolygonCounts(p))
				continue;
			double area = PolygonUtils.getArea(p);
			double percentage = area / totalArea * 100;
			double diff = Math.abs(percentage - targetPercentage);
			if (diff > diffErrorMargin)
				return false;
		}
		return true;
	}
	
	public void applyLineSplit(int x1, int y1, int x2, int y2) {
		if (!validLineSplit(x1, y1, x2, y2))
			return;
		List<Polygon> newPolygons = new ArrayList<Polygon>();
		for (Polygon p : polygons) {
			Point[] polygonIntersects = PolygonUtils.getIntersections(p, x1, y1, x2, y2);
			if (polygonIntersects.length == 2) {
				Polygon[] newPolys = PolygonUtils.splitPolygon(p, x1, y1, x2, y2);
				newPolygons.add(newPolys[0]);
				newPolygons.add(newPolys[1]);
			} else {
				newPolygons.add(p);
			}
		}
		strokes++;
		polygons = newPolygons;
	}
	
	public void reset() {
		polygons = new ArrayList<Polygon>();
		polygons.add(originalPolygon);
		strokes = 0;
	}

	public boolean validLineSplit(int x1, int y1, int x2, int y2) {
		boolean isSplitting = false;
		for (Polygon p : polygons) {
			Point[] polygonIntersects = PolygonUtils.getIntersections(p, x1, y1, x2, y2);
			if (polygonIntersects.length != 2 && polygonIntersects.length != 0)
				return false;
			if (polygonIntersects.length == 2)
				isSplitting = true;
		}
		return isSplitting;
	}

	public List<Polygon> getPolygons() {
		return polygons;
	}
	
	private void setPolygons(List<Polygon> polygons) {
		this.polygons = polygons;
	}

	public int getNrTargetAreas() {
		return nrTargetAreas;
	}

	public double getFitness() {
		double areaerr = 0D;
		for (int i = 0; i < polygons.size(); i++) {
			Polygon p = polygons.get(i);
			double area = PolygonUtils.getArea(p);
			double percentage = area / totalArea * 100;
			double diff = Math.abs(percentage - targetPercentage);
			areaerr += diff * diff;
		}
		double nraerr = Math.abs(polygons.size() - nrTargetAreas);
		double nrserr = Math.abs(strokes - nrTargetStrokes);
		return nraerr * 100 + nrserr * 100 + areaerr;
	}

	public Polygon getOriginalPolygon() {
		return originalPolygon;
	}
	
	public Level clone() {
		Level newLevel = new Level(originalPolygon, nrTargetAreas, nrTargetStrokes);
		newLevel.setPolygons(polygons);
		return newLevel;
	}
	
	public int getNrStrokes() {
		return strokes;
	}

	public int getNrTargetStrokes() {
		return nrTargetStrokes;
	}

}

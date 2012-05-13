package com.macuyiko.sliceitgenetic;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

public class PolygonUtils {
	public static Polygon createPolygon(int[] x, int[] y) {
		if (x.length != y.length)
			return null;
		Polygon p = new Polygon();
		for (int i = 0; i < x.length; i++) {
			p.addPoint(x[i], y[i]);
		}
		return p;
	}

	public static Polygon createPolygon(Point[] pts) {
		Polygon p = new Polygon();
		for (int i = 0; i < pts.length; i++) {
			p.addPoint(pts[i].x, pts[i].y);
		}
		return p;
	}
	
	public static double getArea(List<Polygon> polygons) {
		double ta = 0D;
		for (Polygon p : polygons) {
			double a = getArea(p);
			ta += a;
		}
		return ta;
	}
	
	public static double getArea(Polygon p) {
		double a = 0D;
		for (int i = 0; i < p.npoints; i++) {
			int j = i == p.npoints - 1 ? 0 : i + 1;
			double xi = (double) p.xpoints[i];
			double yj = (double) p.ypoints[j];
			double xj = (double) p.xpoints[j];
			double yi = (double) p.ypoints[i];

			a += (xi * yj - xj * yi);
		}
		a /= 2D;
		return a;
	}

	public static Polygon[] splitPolygon(Polygon p, int l1x1, int l1y1, int l1x2, int l1y2) {
		Polygon[] newPolygons = new Polygon[2];
		
		Polygon firstPolygon = new Polygon();
		Polygon secondPolygon = new Polygon();
		int firstEdge = -1;
		int secondEdge = -1;
		Point firstPoint = null;
		Point secondPoint = null;
		int edge = 0;
		while (true) {
			if (edge >= p.npoints) edge = 0;
			int i = edge;
			int j = i == p.npoints - 1 ? 0 : i + 1;
			int l2x1 = p.xpoints[i];
			int l2x2 = p.xpoints[j];
			int l2y1 = p.ypoints[i];
			int l2y2 = p.ypoints[j];
			Point intersect = getIntersection(l1x1, l1y1, l1x2, l1y2, l2x1, l2y1, l2x2, l2y2);
			if (intersect != null) {
				if (firstEdge == -1) {
					firstEdge = i;
					firstPoint = intersect;
					firstPolygon.addPoint(firstPoint.x, firstPoint.y);
					firstPolygon.addPoint(l2x2, l2y2);
				} else if (secondEdge == -1) {
					secondEdge = i;
					secondPoint = intersect;
					firstPolygon.addPoint(intersect.x, intersect.y);
					firstPolygon.addPoint(firstPoint.x, firstPoint.y);
					secondPolygon.addPoint(secondPoint.x, secondPoint.y);
					secondPolygon.addPoint(l2x2, l2y2);
				} else if (secondEdge > -1) {
					secondPolygon.addPoint(intersect.x, intersect.y);
					secondPolygon.addPoint(secondPoint.x, secondPoint.y);
					break;
				}
			} else {
				if (firstEdge > -1 && secondEdge == -1)
					firstPolygon.addPoint(l2x2, l2y2);
				else if (secondEdge > -1 && secondEdge > -1)
					secondPolygon.addPoint(l2x2, l2y2);
			}
			edge++;
		}
		
		newPolygons[0] = firstPolygon;
		newPolygons[1] = secondPolygon;
		
		return newPolygons;
	}

	public static Point[] getIntersections(Polygon p, int l1x1, int l1y1, int l1x2, int l1y2) {
		List<Point> intersections = new ArrayList<Point>();
		for (int i = 0; i < p.npoints; i++) {
			int j = i == p.npoints - 1 ? 0 : i + 1;
			int l2x1 = p.xpoints[i];
			int l2x2 = p.xpoints[j];
			int l2y1 = p.ypoints[i];
			int l2y2 = p.ypoints[j];

			Point intersect = getIntersection(l1x1, l1y1, l1x2, l1y2, l2x1, l2y1, l2x2, l2y2);
			if (intersect != null)
				intersections.add(intersect);
		}
		return intersections.toArray(new Point[] {});
	}

	public static Point[] getIntersections(List<Polygon> polygons, int x1, int y1, int x2, int y2) {
		List<Point> intersections = new ArrayList<Point>();
		for (Polygon p : polygons) {
			Point[] polygonIntersects = getIntersections(p, x1, y1, x2, y2);
			for (Point pt : polygonIntersects)
				intersections.add(pt);
		}
		return intersections.toArray(new Point[] {});
	}
	
	public static Point getIntersection(int l1x1, int l1y1, int l1x2, int l1y2, int l2x1, int l2y1, int l2x2, int l2y2) {
		double d = (l2y2 - l2y1) * (l1x2 - l1x1) - (l2x2 - l2x1) * (l1y2 - l1y1);
		double na = (l2x2 - l2x1) * (l1y1 - l2y1) - (l2y2 - l2y1) * (l1x1 - l2x1);
		double nb = (l1x2 - l1x1) * (l1y1 - l2y1) - (l1y2 - l1y1) * (l1x1 - l2x1);
		if (d == 0D)
			return null;

		double ua = na / d;
		double ub = nb / d;
		if (ua >= 0D && ua <= 1D && ub >= 0D && ub <= 1D) {
			Point intersect = new Point();
			intersect.x = (int) (l1x1 + (ua * (l1x2 - l1x1)));
			intersect.y = (int) (l1y1 + (ua * (l1y2 - l1y1)));
			return intersect;
		}
		
		return null;
	}
}

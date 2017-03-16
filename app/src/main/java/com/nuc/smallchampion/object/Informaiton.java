package com.nuc.smallchampion.object;

import java.util.List;
import java.util.ArrayList;

import android.graphics.Point;

public class Informaiton {

	private List<Point> points = new ArrayList<Point>();

	public Informaiton(Point p1, Point p2) {

		points.add(p1);
		points.add(p2);
	}

	public Informaiton(Point p1, Point p2, Point p3) {
		points.add(p1);
		points.add(p2);
		points.add(p3);
	}

	public Informaiton(Point p1, Point p2, Point p3, Point p4) {
		points.add(p1);
		points.add(p2);
		points.add(p3);
		points.add(p4);
	}

	public List<Point> getLinkPoints() {
		return points;
	}
}

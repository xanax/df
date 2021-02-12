package org.newdawn.slick.util.pathfinding;

import uk.co.gosseyn.xanax.domain.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Path {
	private List<Point> steps = new ArrayList<>();
	
	public int getLength() {
		return steps.size();
	}

	public Point getLastStep() {
		return getStep(steps.size() - 1);
	}
	public Point getStep(int index) {
		return steps.get(index);
	}
	
	public int getX(int index) {
		return getStep(index).getX();
	}

	public int getY(int index) {
		return getStep(index).getY();
	}

	public int getZ(int index) {
		return getStep(index).getZ();
	}

	public void appendStep(Point point) {
		steps.add(point);
	}

	public void prependStep(Point point) {
		steps.add(0, point);
	}

	public boolean contains(Point point) {
		return steps.contains(point);
	}
	public void reverse() {
		Collections.reverse(steps);
	}
}
package abz.chand.supermarketassistant.guide;

import org.opencv.core.Point;

public class ProcessIntersectionPoint {

	private final Point[] twoPoints;
	
	public ProcessIntersectionPoint() {
		twoPoints = new Point[2];
	}

	public Point[] findFurthestPoints(final Point[] intersectionPoints) {		
		int a = 0;
		int b = 0;

		int greatestMinDiff = 0;

		for (int i = 0; i < intersectionPoints.length; i++){
			for (int j = i+1; j < intersectionPoints.length; j++){
				Point iPoint = intersectionPoints[i];
				Point jPoint = intersectionPoints[j];
				if (isPointBlack(iPoint) && isPointBlack(jPoint)){
					int diff = Math.abs(i - j);
					int minDiff = Math.min(diff, 16-diff);
					if (minDiff > greatestMinDiff){
						a = i;
						b = j;
						greatestMinDiff = minDiff;
					}	
				}
			}
		}
	
		twoPoints[0] = intersectionPoints[a];
		twoPoints[1] = intersectionPoints[b];

		return twoPoints;
	}

	private boolean isPointBlack(Point point) {
		return point.x >= 0 && point.y >= 0;
	}

}

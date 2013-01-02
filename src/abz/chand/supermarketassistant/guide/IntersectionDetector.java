package abz.chand.supermarketassistant.guide;

import org.opencv.core.Mat;
import org.opencv.core.Point;

public class IntersectionDetector {

	private static int[] xInc = {0, 1, 1, 2, 1, 2, 1, 1};
	private static int[] yInc = {1, 2, 1, 1, 0, -1, -1, -2};

	private Point[] intersectionPoints;
	private ColourRanges colourRanges;
	private ProcessIntersectionPoint processIntersectionPoint;

	public IntersectionDetector() {
		intersectionPoints = new Point[16];
		colourRanges = new ColourRanges();
		processIntersectionPoint = new ProcessIntersectionPoint();
		init();
	}

	private void init() {
		for (int i = 0; i < 16; i++){
			intersectionPoints[i] = new Point(0,0);
		}
	}

	public Point[] getBlackEdgePoints(final Point point, final Mat mat) {
		setBlackIntersectionPoints(point, mat);
		return processIntersectionPoint.findFurthestPoints(intersectionPoints);
	}

	private void setBlackIntersectionPoints(final Point center, final Mat mat){
		int x = 0;
		int y = 0;

		for (int i = 0; i < 8; i++){
			x = xInc[i];
			y = yInc[i];
			setIntersectionPoint(center, mat, x, y, i);
		}

		
		for (int i = 8; i < 16; i++){
			x = -1 * xInc[i-8];
			y = -1 * yInc[i-8];
			setIntersectionPoint(center, mat, x, y, i);	
		}		
	}

	private void setIntersectionPoint(Point point, Mat mat, int incX, int incY, int index){
		int x = (int) point.x;
		int y = (int) point.y;

		int h; int s; int v;
		
		boolean isWhite = true;
		while(isWhite){
			x+=incX;
			y+=incY;
			double[] pixelData = mat.get(y, x);
			h = (int) pixelData[0];
			s = (int) pixelData[1];
			v = (int) pixelData[2];
			if (!colourRanges.isWhite(h, s, v)){
				if (colourRanges.isBlack(h, s, v)){
					intersectionPoints[index].x = x;
					intersectionPoints[index].y = y;
					return;
				}
				isWhite = false;
			}			
		}	
		intersectionPoints[index].x = -1;
		intersectionPoints[index].y = -1;		
	}	
}

package abz.chand.supermarketassistant.guide;

import org.opencv.core.Point;

public class IndexPoint {

	private int index;
	
	private Point point;

	public IndexPoint(int index, Point point) {	
		this.index = index;
		this.point = point;
	}
	
	public int getIndex(){
		return index;
	}
	
	public Point getPoint(){
		return point;
	}
	
}

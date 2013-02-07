package abz.chand.supermarketassistant.guide.freespace;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import android.util.Pair;

public interface Go {

	Pair<Point, Point> getDirection(Mat lines, Point start, Mat rgbMat);
	
}

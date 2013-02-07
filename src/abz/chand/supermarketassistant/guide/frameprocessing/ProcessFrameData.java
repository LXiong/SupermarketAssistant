package abz.chand.supermarketassistant.guide.frameprocessing;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import abz.chand.supermarketassistant.guide.frameprocessing.colour.ColourRanges;
import android.util.Pair;

public class ProcessFrameData {

	private ColourRanges colourRanges;
	private List<Integer> dataValues;
	private int[] valueCounter;

	public ProcessFrameData(){
		colourRanges = new ColourRanges();
		dataValues = new ArrayList<Integer>();
		valueCounter = new int[8];
		init();
	}

	private void init() {
		for (int i = 0; i < 8; i++){
			valueCounter[i] = 0;
		}
	}

	public List<Integer> getData(Mat mat, Mat grayMat, Point center, Mat rgbMat){
		dataValues.clear();
		Pair<Integer, Integer> west = getWestBorders(grayMat, center);
		Pair<Integer, Integer> east = getEastBorders(grayMat, center);
		Pair<Integer, Integer> south = getSouthBorders(grayMat, center);
		int north = getNorthBorders(grayMat, center);
		int southY = south.first + (south.second-south.first)/4;
		int northY = north + 3*(south.second-south.first)/4;
		
		System.out.println("MAZA: " + west.first + ", " + west.second);
		Scalar color2 = new Scalar(0, 0, 255);
		Core.line(rgbMat, new Point(west.first, center.y), new Point(west.second, center.y), color2);
		Core.line(rgbMat, new Point(east.first, center.y), new Point(east.second, center.y), color2);
		Core.line(rgbMat, new Point(center.x, south.first), new Point(center.x, south.second), color2);
		Core.line(rgbMat, new Point(center.x, north), new Point(center.x, north + (south.second - south.first)), color2);
		Core.line(rgbMat, new Point(center.x, northY), new Point(west.second, northY), color2);
		Core.line(rgbMat, new Point(center.x, northY), new Point(east.second, northY), color2);
		Core.line(rgbMat, new Point(center.x, southY), new Point(west.second, southY), color2);
		Core.line(rgbMat, new Point(center.x, southY), new Point(east.second, southY), color2);
		
		
		getWest(mat, (int) center.y, west);
		getEast(mat, (int) center.y, east);
		getSouth(mat, (int) center.x, south);
		getSouthWest(mat, southY, west); 
		getSouthEast(mat, southY, east);		
		getNorthWest(mat, northY, west); 
		getNorthEast(mat, northY, east);
		
		return dataValues;
	}
	

	private int getNorthBorders(Mat grayMat, Point center) {
		int x = (int) center.x;
		int y = (int) center.y;
		
		int a = y;		
		for (int i = a; i > 0; i--){
			double[] vals = grayMat.get(i, x);
			if (vals[0] != 0){
				a = i;
				break;
			}
		}
		
		return a;
	}

	private Pair<Integer, Integer> getSouthBorders(Mat grayMat, Point center) {
		int x = (int) center.x;
		int y = (int) center.y;
		
		int a = y;		
		for (int i = a; i < grayMat.rows() - 1; i++){
			double[] vals = grayMat.get(i, x);
			if (vals[0] != 0){
				a = i;
				break;
			}
		}
		
		for (int i = a; i < grayMat.rows() - 1; i++){
			double[] vals = grayMat.get(i, x);
			if (vals[0] == 0){
				a = i;
				break;
			}
		}
		
		int b = a;		
		
		for (int i = b; i < grayMat.rows() - 1; i++){
			double[] vals = grayMat.get(i, x);
			if (vals[0] != 0){
				b = i;
				break;
			}
		}
		
		return new Pair<Integer, Integer>(a, b);	
	}

	private Pair<Integer, Integer> getEastBorders(Mat grayMat, Point center) {
		int x = (int) center.x;
		int y = (int) center.y;
		
		int a = x;		
		for (int i = a; i < grayMat.cols() - 1; i++){
			double[] vals = grayMat.get(y, i);
			if (vals[0] != 0){
				a = i;
				break;
			}
		}
		
		for (int i = a; i < grayMat.cols() - 1; i++){
			double[] vals = grayMat.get(y, i);
			if (vals[0] == 0){
				a = i;
				break;
			}
		}
		
		int b = a;		
		
		for (int i = b; i < grayMat.cols() - 1; i++){
			double[] vals = grayMat.get(y, i);
			if (vals[0] != 0){
				b = i;
				break;
			}
		}
		
		return new Pair<Integer, Integer>(a, b);			
	}

	private Pair<Integer, Integer> getWestBorders(Mat grayMat, Point center) {
		int x = (int) center.x;
		int y = (int) center.y;
		
		int a = x;		
		for (int i = a; i > 0; i--){
			double[] vals = grayMat.get(y, i);
			if (vals[0] != 0){
				a = i;
				break;
			}
		}
		
		for (int i = a; i > 0; i--){
			double[] vals = grayMat.get(y, i);
			if (vals[0] == 0){
				a = i;
				break;
			}
		}
		
		int b = a;		
		
		for (int i = b; i > 0; i--){
			double[] vals = grayMat.get(y, i);
			if (vals[0] != 0){
				b = i;
				break;
			}
		}
		
		return new Pair<Integer, Integer>(a, b);
	}

	public int getWest(Mat mat, int y, Pair<Integer, Integer> west) {
		int offset = extractWestValue(mat, y, west);
		int data = findBestData();
		dataValues.add(data);
		
		return offset;
	}

	public int getSouth(Mat mat, int x, Pair<Integer, Integer> south) {
		int offset = extractSouthValue(mat, x, south);
		int data = findBestData();
		dataValues.add(data);		

		return offset;
	}

	
	public int getNorth(Mat mat, Point center) {
		int x = (int) center.x;
		int y = (int) center.y;

		String str = "";
		int data = 0;
		int offset = y;

		offset = extractNorthValue(mat, offset, x);
	
		//		offset = extractSouthValue(mat, offset, x);
		//		data = findBestData();
		//		str+=data;
		//		dataValues.add(data);	

		return offset;
	}
	
	
	public int getSouthWest(Mat mat, int southY, Pair<Integer, Integer> west) {
		return getWest(mat, southY, west);
	}

	public int getEast(Mat mat, int y, Pair<Integer, Integer> east) {
		int offset = extractEastValue(mat, y, east);
		int data = findBestData();
		dataValues.add(data);

		return offset;
	}

	public int getSouthEast(Mat mat, int southY, Pair<Integer, Integer> east) {
		return getEast(mat, southY, east);
	}

	public int getNorthWest(Mat mat, int northY, Pair<Integer, Integer> west) {
		return getWest(mat, northY, west);
	}
	
	public int getNorthEast(Mat mat, int northY, Pair<Integer, Integer> east) {
		return getEast(mat, northY, east);
	}
	
	private int extractWestValue(Mat mat, int y, Pair<Integer, Integer> west) {
		for (int i = west.second; i < west.first; i++){		
			double[] vals = mat.get(y, i);
			int val = colourRanges.getColorValue(vals);
			if (val >= 0 && val <= 7){
				valueCounter[val]+=1;
			}
		}	
		return y;
	}

	private int extractEastValue(Mat mat, int y, Pair<Integer, Integer> east) {
		for (int i = east.first; i < east.second; i++){		
			double[] vals = mat.get(y, i);
			int val = colourRanges.getColorValue(vals);
			if (val >= 0 && val <= 7){
				valueCounter[val]+=1;
			}
		}	
		return y;
	}

	private int extractSouthValue(Mat mat, int x, Pair<Integer, Integer> south) {
		for (int i = south.first; i < south.second; i++){
			double[] vals = mat.get(i, x);
			int val = colourRanges.getColorValue(vals);
			if (val >= 0 && val <= 7){
				valueCounter[val]+=1;
			}
		}	
		return x;
	}

	private int extractNorthValue(Mat mat, int offset, int x) {
		for (int i = offset; i > 0; i--){	
//			System.out.println("BAZA: " + mat.get(i,x)[1]);
			if (!colourRanges.isWhite(mat.get(i, x)) && !colourRanges.isWhite(mat.get(i-1, x))){
				offset = i;
				break;
			}
		}

//		for (int i = offset; i > 0; i--){
//			double[] vals = mat.get(i, x);
//			int val = colourRanges.getColorValue(vals);
//			if (val >= 0 && val <= 7){
//				valueCounter[val]+=1;
//			}
//			if (colourRanges.isWhite(vals) && colourRanges.isWhite(mat.get(i-1, x))){
//				offset = i-1;
//				break;
//			}
//		}	
		return offset;
	}

	private int findBestData(){
		int max = 0;
		int maxIndex = 0;  

//		for (int i = 0; i < valueCounter.length; i++){
		for (int i = 0; i < 3; i++){
			int count = valueCounter[i];
			if (count > max){
				max = count; 
				maxIndex = i;
			}
		}

		//DEBUGGING LOOP DELETE IF NOT NEEDED
		String red = "";
		for (int i = 0; i < valueCounter.length; i++){
			int count = valueCounter[i];
			red += i + "=" + count + ", ";
		}
//				System.out.println("AAAAAAHHHH: " + red);


		init();
		return maxIndex;
	}

}

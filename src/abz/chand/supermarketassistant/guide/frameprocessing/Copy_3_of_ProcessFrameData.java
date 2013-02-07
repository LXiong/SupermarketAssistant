package abz.chand.supermarketassistant.guide.frameprocessing;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import abz.chand.supermarketassistant.guide.frameprocessing.colour.ColourRanges;

public class Copy_3_of_ProcessFrameData {

	private ColourRanges colourRanges;
	private List<Integer> dataValues;
	private int[] valueCounter;

	public Copy_3_of_ProcessFrameData(){
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
		int westOffset = getWest(mat, center);
		int eastOffset = getEast(mat, center);
		int southOffset = getSouth(mat, center);
		int northOffset = getNorth(mat, center);

		Scalar color2 = new Scalar(0, 0, 255);
		System.out.println("GAZA: " + southOffset + ", " + northOffset);
	
		Core.line(rgbMat, new Point(center.x, northOffset), new Point(500, northOffset), color2);
		
		northOffset += (southOffset - northOffset) * 0.3;
		Core.line(rgbMat, new Point(center.x, southOffset), new Point(500, southOffset), color2);
		Core.line(rgbMat, new Point(center.x, northOffset), new Point(500, northOffset), color2);
		Core.line(rgbMat, new Point(center.x, center.y), new Point(center.x, 470), color2);
		
		getSouthWest(mat, center, southOffset, westOffset); 
		getSouthEast(mat, center, southOffset, eastOffset); 
		getNorthWest(mat, center, northOffset); 
		getNorthEast(mat, center, northOffset);
		return dataValues;
	}
	
	public List<Integer> getData(Mat mat, Point center, Mat rgbMat){
		dataValues.clear();
		int westOffset = getWest(mat, center);
		int eastOffset = getEast(mat, center);
		int southOffset = getSouth(mat, center);
		int northOffset = getNorth(mat, center);

		Scalar color2 = new Scalar(0, 0, 255);
		System.out.println("GAZA: " + southOffset + ", " + northOffset);
	
		Core.line(rgbMat, new Point(center.x, northOffset), new Point(500, northOffset), color2);
		
		northOffset += (southOffset - northOffset) * 0.3;
		Core.line(rgbMat, new Point(center.x, southOffset), new Point(500, southOffset), color2);
		Core.line(rgbMat, new Point(center.x, northOffset), new Point(500, northOffset), color2);
		Core.line(rgbMat, new Point(center.x, center.y), new Point(center.x, 470), color2);
		
		getSouthWest(mat, center, southOffset, westOffset); 
		getSouthEast(mat, center, southOffset, eastOffset); 
		getNorthWest(mat, center, northOffset); 
		getNorthEast(mat, center, northOffset);
		return dataValues;
	}

	public int getWest(Mat mat, Point center) {
		int x = (int) center.x;
		int y = (int) center.y;

		String str = "";

		int data = 0;
		int offset = x;
		offset = extractWestValue(mat, offset, y);
		data = findBestData();
		str+=data;
		dataValues.add(data);

		//		offset = extractWestValue(mat, offset, y);
		//		data = findBestData();
		//		str+=data;
		//		dataValues.add(data);	

		return offset;
	}

	public int getSouth(Mat mat, Point center) {
		int x = (int) center.x;
		int y = (int) center.y;

		String str = "";

		int data = 0;
		int offset = y;

		offset = extractSouthValue(mat, offset, x);
		data = findBestData();
		str+=data;
		dataValues.add(data);

		//		offset = extractSouthValue(mat, offset, x);
		//		data = findBestData();
		//		str+=data;
		//		dataValues.add(data);	

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
	
	
	public String getSouthWest(Mat mat, Point center, int southOffset, int westOffset) {
		int x = (int) center.x;
		int y = (int) center.y;

		String str = "";
		int data = 0;
		int offset = y;

//		offset = extractSouthValue(mat, offset, x);
//		data = findBestData();

		offset = extractWestValue(mat, westOffset, southOffset);
		data = findBestData();
		str+=data;
		dataValues.add(data);	

		return str;
	}

	public int getEast(Mat mat, Point center) {
		int x = (int) center.x;
		int y = (int) center.y;

		String str = "";
		int data = 0;
		int offset = x;
		offset = extractEastValue(mat, offset, y);
		data = findBestData();
		str+=data;
		dataValues.add(data);

		//		offset = extractEastValue(mat, offset, y);
		//		data = findBestData();
		//		str+=data;
		//		dataValues.add(data);	

		return offset;
	}

	public String getSouthEast(Mat mat, Point center, int southOffset, int eastOffset) {
		int x = (int) center.x;
		int y = (int) center.y;

		String str = "";
		int data = 0;
		int offset = y;

//		offset = extractSouthValue(mat, offset, x);
//		data = findBestData();

		offset = extractEastValue(mat, eastOffset, southOffset);
		data = findBestData();
		str+=data;
		dataValues.add(data);

		return str;
	}

	public String getNorthWest(Mat mat, Point center, int northOffset) {
		int x = (int) center.x;
		int y = (int) center.y;

		String str = "";
		int data = 0;
		int offset = y;

		offset = extractWestValue(mat, x, northOffset);
		data = findBestData();
		str+=data;
		dataValues.add(data);

		return str;
	}

	public String getNorthEast(Mat mat, Point center, int northOffset) {
		int x = (int) center.x;
		int y = (int) center.y;

		String str = "";
		int data = 0;
		int offset = y;

		offset = extractEastValue(mat, x, northOffset);
		data = findBestData();
		str+=data;
		dataValues.add(data);

		return str;
	}

	private int extractWestValue(Mat mat, int offset, int y) {
		for (int i = offset; i > 0; i--){			
			if (!colourRanges.isWhite(mat.get(y, i)) && !colourRanges.isWhite(mat.get(y, i-1))){
				offset = i;
				break;
			}
		}

		for (int i = offset; i > 0; i--){
			double[] vals = mat.get(y, i);
			int val = colourRanges.getColorValue(vals);
			if (val >= 0 && val <= 7){
				valueCounter[val]+=1;
			}
			if ((colourRanges.isWhite(vals) || colourRanges.isBlack(vals))  && (colourRanges.isWhite(mat.get(y, i-1)) || colourRanges.isBlack(mat.get(y, i-1)))){
//				offset = i-1;
				break;
			}
		}	
		return offset;
	}

	private int extractEastValue(Mat mat, int offset, int y) {
		for (int i = offset; i < mat.cols() - 1; i++){			
			if (!colourRanges.isWhite(mat.get(y, i)) && !colourRanges.isWhite(mat.get(y, i+1))){
				offset = i;
				break;
			}
		}

		for (int i = offset; i < mat.cols() - 1; i++){		
			double[] vals = mat.get(y, i);
			int val = colourRanges.getColorValue(vals);
			if (val >= 0 && val <= 7){
				valueCounter[val]+=1;
			}
			if ((colourRanges.isWhite(vals) || colourRanges.isBlack(vals))  && (colourRanges.isWhite(mat.get(y, i+1)) || colourRanges.isBlack(mat.get(y, i+1)))){
//				offset = i+1;
				break;
			}
		}	
		return offset;
	}

	private int extractSouthValue(Mat mat, int offset, int x) {
		for (int i = offset; i < mat.rows() - 1; i++){			
			if (!colourRanges.isWhite(mat.get(i, x)) && !colourRanges.isWhite(mat.get(i+1, x))){
				offset = i;
				break;
			}
		}

		for (int i = offset; i < mat.rows() - 1; i++){
			double[] vals = mat.get(i, x);
			int val = colourRanges.getColorValue(vals);
			if (val >= 0 && val <= 7){
				valueCounter[val]+=1;
			}
			if (colourRanges.isWhite(vals) && colourRanges.isWhite(mat.get(i+1, x))){
//				offset = i+1;
				offset += (i - offset)/2;
				break;
			}
		}	
		return offset;
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

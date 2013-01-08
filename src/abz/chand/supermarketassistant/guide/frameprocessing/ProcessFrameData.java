package abz.chand.supermarketassistant.guide.frameprocessing;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import abz.chand.supermarketassistant.guide.frameprocessing.colour.ColourRanges;

public class ProcessFrameData {

	private ColourRanges colourRanges;
	private List<Integer> dataValues;
	private int[] valueCounter;

	public ProcessFrameData(){
		colourRanges = new ColourRanges();
		dataValues = new ArrayList<Integer>();;
		valueCounter = new int[8];
		init();
	}

	private void init() {
		for (int i = 0; i < 8; i++){
			valueCounter[i] = 0;
		}
	}

	public List<Integer> getData(Mat mat, Point center){
		dataValues.clear();
		String ss = getWest(mat, center) + " : " + getEast(mat, center) + " : " + getSouth(mat, center) +
		 " : " + getSouthWest(mat, center) + " : " + getSouthEast(mat, center);
		return dataValues;
	}

	public String getWest(Mat mat, Point center) {
		int x = (int) center.x;
		int y = (int) center.y;

		String str = "";
		
		int data = 0;
		int offset = x;
		offset = extractWestValue(mat, offset, y);
		data = findBestData();
		str+=data;
		dataValues.add(data);
		
		offset = extractWestValue(mat, offset, y);
		data = findBestData();
		str+=data;
		dataValues.add(data);	
		
		return str;
	}
	
	public String getSouth(Mat mat, Point center) {
		int x = (int) center.x;
		int y = (int) center.y;

		String str = "";
		
		int data = 0;
		int offset = y;
		
		offset = extractSouthValue(mat, offset, x);
		data = findBestData();
		str+=data;
		dataValues.add(data);

		offset = extractSouthValue(mat, offset, x);
		data = findBestData();
		str+=data;
		dataValues.add(data);	
		
		return str;
	}
	
	public String getSouthWest(Mat mat, Point center) {
		int x = (int) center.x;
		int y = (int) center.y;

		String str = "";
		int data = 0;
		int offset = x;
		
		offset = extractWestValue(mat, offset, y);
		data = findBestData();
		
		offset = extractSouthValue(mat, y, offset-2);
		data = findBestData();
		str+=data;
		dataValues.add(data);	
		
		return str;
	}
	
	public String getEast(Mat mat, Point center) {
		int x = (int) center.x;
		int y = (int) center.y;

		String str = "";
		int data = 0;
		int offset = x;
		offset = extractEastValue(mat, offset, y);
		data = findBestData();
		str+=data;
		dataValues.add(data);
		
		offset = extractEastValue(mat, offset, y);
		data = findBestData();
		str+=data;
		dataValues.add(data);	
		
		return str;
	}
	
	public String getSouthEast(Mat mat, Point center) {
		int x = (int) center.x;
		int y = (int) center.y;

		String str = "";
		int data = 0;
		int offset = y;

		offset = extractSouthValue(mat, offset, x);
		data = findBestData();
		
		offset = extractEastValue(mat, x, offset+2);
		data = findBestData();
		str+=data;
		dataValues.add(data);
		
		return str;
	}
	
	public String getNorthWest(Mat mat, Point center) {
		int x = (int) center.x;
		int y = (int) center.y;

		String str = "";
		int data = 0;
		int offset = x;

		offset = extractWestValue(mat, offset, y);
		data = findBestData();
		
		offset = extractNorthValue(mat, y, offset-2);
		data = findBestData();
		str+=data;
		dataValues.add(data);
		
		return str;
	}
	
	public String getNorthEast(Mat mat, Point center) {
		int x = (int) center.x;
		int y = (int) center.y;

		String str = "";
		int data = 0;
		int offset = x;

		offset = extractEastValue(mat, offset, y);
		data = findBestData();
		
		offset = extractNorthValue(mat, y, offset+2);
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
			if (colourRanges.isWhite(vals) && colourRanges.isWhite(mat.get(y, i-1))){
				offset = i-1;
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
			if (colourRanges.isWhite(vals) && colourRanges.isWhite(mat.get(y, i+1))){
				offset = i+1;
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
				offset = i+1;
				break;
			}
		}	
		return offset;
	}
	
	private int extractNorthValue(Mat mat, int offset, int x) {
		for (int i = offset; i > 0; i--){	
			if (!colourRanges.isWhite(mat.get(i, x)) && !colourRanges.isWhite(mat.get(i-1, x))){
				offset = i;
				break;
			}
		}
		
		for (int i = offset; i > 0; i--){
			double[] vals = mat.get(i, x);
			int val = colourRanges.getColorValue(vals);
			if (val >= 0 && val <= 7){
				valueCounter[val]+=1;
			}
			if (colourRanges.isWhite(vals) && colourRanges.isWhite(mat.get(i-1, x))){
				offset = i-1;
				break;
			}
		}	
		return offset;
	}

	private int findBestData(){
		int max = 0;
		int maxIndex = 0; 
		for (int i = 0; i < valueCounter.length; i++){
			int count = valueCounter[i];
			if (count > max){
				max = count; 
				maxIndex = i;
			}
		}
		init();
		return maxIndex;
	}
	
}

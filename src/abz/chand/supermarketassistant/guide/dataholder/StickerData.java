package abz.chand.supermarketassistant.guide.dataholder;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;

public class StickerData {

	private static final int DATA_CHECKING_MOD = 8;
	private Point centerPoint;
	private double angleFromNorth;
	private List<Integer> dataValues;

	public StickerData() {
		setCenterPoint(new Point(0, 0));
		setAngleFromNorth(0);
		setDataValues(new ArrayList<Integer>());
	}

	public void setCenterPoint(Point centerPoint) {
		this.centerPoint = centerPoint;
	}

	public Point getCenterPoint() {
		return centerPoint;
	}

	public void setAngleFromNorth(double angleFromNorth) {
		this.angleFromNorth = angleFromNorth;
	}

	public double getAngleFromNorth() {
		return angleFromNorth;
	}

	public void setDataValues(List<Integer> dataValues) {
		this.dataValues = dataValues;
	}

	public List<Integer> getDataValues() {
		return dataValues;
	}

	@Override
	public String toString() {
		String str = "";
		if (dataValues == null){
			return str;
		}
		for (int i : dataValues){
			str += i + ",";
		}
		return str;
	}

	public boolean validDataValues(){
		for (int i = 0; i < dataValues.size(); i++){
			if (dataValues.get(i) > 2){
				return false;
			}
		}		
		return true;
	}

	public int getActualValue(){
		int m = 1;
		int value = 0;
		for (int i = 0; i < dataValues.size() - 2; i++){
			value += (m * dataValues.get(i));
			m *= 3;
		}	

		boolean correct = validateValue(value);

		if (correct){
			return value;
		} else {
			return -1;
		}
	}

	private boolean validateValue(int value) {
		int validateValue = 0;
		int size = dataValues.size(); 
		if (size >= 2){
			validateValue = dataValues.get(size-2) + (3 * dataValues.get(size-1));
		}
		return (value + validateValue) % DATA_CHECKING_MOD == 0;
	}

	public boolean equalDataValues(final StickerData stickerData) {		

		List<Integer> otherDataValues = stickerData.getDataValues();
		if (dataValues.size() != otherDataValues.size()){
			return false;
		}

		for (int i = 0; i < dataValues.size(); i++){
			if (dataValues.get(i) != otherDataValues.get(i)){
				return false;
			}
		}
		return true;
	}

}

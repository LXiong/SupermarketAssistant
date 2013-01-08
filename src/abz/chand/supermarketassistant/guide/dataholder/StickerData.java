package abz.chand.supermarketassistant.guide.dataholder;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;

public class StickerData {
	
	private Point centerPoint;
	private int angleFromNorth;
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

	public void setAngleFromNorth(int angleFromNorth) {
		this.angleFromNorth = angleFromNorth;
	}

	public int getAngleFromNorth() {
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

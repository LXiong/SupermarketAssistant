package abz.chand.supermarketassistant.guide.frameprocessing.colour;


public class ColourRanges {

	public int getColorValue(int h, int s, int v){
		if (isRed(h, s, v)){
			return 0;
		} else if (isBlue(h, s, v)){
			return 1;
		} else if (isGreen(h, s, v)){
			return 2;
		} else if (isYellow(h, s, v)){
			return 3;
		} else if (isPink(h, s, v)){
			return 4;
		} else if (isAqua(h, s, v)){
			return 5;
		} else if (isBlack(h, s, v)){
			return 6;	
		} else if (isWhite(h, s, v)){
			return 7;
		}
		return -1;
	}

	public boolean isBlack(int h, int s, int v){
		if (h >= 0 && h <= 179){
			if (s >= 0 && s <= 255){
				if (v>=0 && v <= 70){
					return true;	
				}
			}
		}		
		return false;
	}

	public boolean isWhite(int h, int s, int v){
		if (h >= 0 && h <= 179){
			if (s >= 0 && s <= 70){
				if (v>=0 && v <= 255){
					return true;	
				}
			}
		}		
		return false;
	}

	public boolean isYellow(int h, int s, int v){
		if (h >= 25 && h <= 40){
			if (s >= 80 && s <= 255){
				if (v>= 80 && v <= 255){
					return true;	
				}
			}
		}		
		return false;
	}

	public boolean isGreen(int h, int s, int v){
		if (h >= 45 && h <= 70){
			if (s >= 80 && s <= 255){
				if (v>=80 && v <= 255){
					return true;	
				}
			}
		}		
		return false;
	}

	public boolean isBlue(int h, int s, int v){
		if (h >= 100 && h <= 125){
			if (s >= 80 && s <= 255){
				if (v>=80 && v <= 255){
					return true;	
				}
			}
		}		
		return false;
	}

	public boolean isAqua(int h, int s, int v){
		if (h >= 75 && h <= 95){
			if (s >= 80 && s <= 255){
				if (v>=80 && v <= 255){
					return true;	
				}
			}
		}		
		return false;
	}

	public boolean isRed(int h, int s, int v){
		if ((h >= 0 && h <= 20) || (h >= 170 && h <= 179)){
			if (s >= 80 && s <= 255){
				if (v>=80 && v <= 255){				
					return true;	
				}
			}
		}		
		return false;
	}

	public boolean isPink(int h, int s, int v){
		if (h >= 130 && h <= 160){
			if (s >= 80 && s <= 255){
				if (v>=80 && v <= 255){		
					return true;	
				}
			}
		}		
		return false;
	}

	public boolean isWhite(double[] values) {
		return isWhite((int) values[0], (int) values[1], (int) values[2]);
	}

	public int getColorValue(double[] values) {
		return getColorValue((int) values[0], (int) values[1], (int) values[2]);
	}
}

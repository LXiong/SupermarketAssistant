package abz.chand.supermarketassistant.guide;


public class CorrectColourRanges {

	public int getColorValue(int h, int s, int v){
		if (isWhite(h, s, v)){
			return -1;
		} else if (isRed(h, s, v)){
			return 0;
		} else if (isBlue(h, s, v)){
			return 1;
		} else if (isGreen(h, s, v)){
			return 2;
		} else if (isYellow(h, s, v)){
			return 3;
		} else if (isPink(h, s, v)){
			return 4;
		} else if (isBlack(h, s, v)){
			return 5;
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
		if (h >= 80 && h <= 100){
			if (s >= 50 && s <= 255){
				if (v>=50 && v <= 255){
					return true;	
				}
			}
		}		
		return false;
	}
	
	public boolean isGreen(int h, int s, int v){
		if (h >= 50 && h <= 80){
			if (s >= 50 && s <= 255){
				if (v>=50 && v <= 255){
					return true;	
				}
			}
		}		
		return false;
	}
	
	public boolean isBlue(int h, int s, int v){
		if (h >= 0 && h <= 30){
			if (s >= 50 && s <= 255){
				if (v>=50 && v <= 255){
					return true;	
				}
			}
		}		
		return false;
	}

	public boolean isRed(int h, int s, int v){
		if (h >= 100 && h <= 130){
			if (s >= 50 && s <= 255){
				if (v>=50 && v <= 255){				
					return true;	
				}
			}
		}		
		return false;
	}
	
	public boolean isPink(int h, int s, int v){
		if (h >= 130 && h <= 160){
			if (s >= 50 && s <= 255){
				if (v>=50 && v <= 255){		
					System.out.println("Pink: " + h);
					return true;	
				}
			}
		}		
		return false;
	}
}
